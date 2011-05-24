package CardGames;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/*
 * Created on 07.07.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author SKroll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CardMenuSW extends DrawAbleObjects 
{
//	hold the single images
	private BufferedImage mi_swahlen, bgImage, mi_back, mi_textbg, li_Loading, li_LoadingPoint;
	
	private final static String IMS_FILE = "MenueSWIms.txt";
	private final String IMS_DIRECTORY = "images/";
    /* The file holding the 'o', 'n', 's', and 'g' image information, 
       extracted with an ImagesLoader object. */
	
	private ImagesLoader imsLoader;   // the image loader
	private CardPanel pCP;
	private CardGames pCG;
	boolean swahlenVisible = false;
	boolean backVisible = false;
	boolean textbgVisible = true;
	boolean bBackActiv = false;
	boolean bGameActiv = false;
	boolean bMOBack = false;
	boolean bMOGame = false;
	int iMOGame;
	boolean bMiBackShrink = false;
	
	private final int MI_SWAHLEN_POS_X = 50;
	private final int MI_SWAHLEN_POS_Y = 50;
	private final int MI_BACK_POS_X = 450;
	private final int MI_BACK_POS_Y = 450;
	private final int MI_TEXTBACK_POS_X = 120;
	private final int MI_TEXTBACK_POS_Y = 160;
	private final double MAX_RESIZE_FAKTOR_MI_BACK = 1.5;
	private boolean bExitAnimationEnded = false;
	private boolean bClsPainted = false;
	double miInfoResizeFaktor = 0.0;
	double miBackResizeFaktor = 0.0;
	double miTextbgResizeFaktor = 0.0;
	private File[] szPlugs;
	private String[][] szPlugDetails; //1 = Game Name, 2 = Author, 3 = Description, 4 = Startclass

	private boolean bSlowAnimation = false;
//---------------------------------------------------------------------------
	public CardMenuSW(CardPanel CP, CardGames CG)//Need a cardPanel to set paintComponents
	{
		pCP = CP;
		pCG = CG;
		//load and initialise the images
	    imsLoader = new ImagesLoader(IMS_FILE, IMS_DIRECTORY); 
	    initImages();
	    loadAviableGames();
	    swahlenVisible = true;
	    backVisible = true;
	}//end of Konstruktor
	
	public void register()
	{
		pCP.addDao(this);
	}
	private void initImages()
	  {
	    // initialize the 'o' image variables
	    mi_swahlen = imsLoader.getImage("mi_swahlen_u");
	    bgImage = imsLoader.getImage("mi_bgimage");
	    mi_back = imsLoader.getImage("mi_back");
	    mi_textbg = imsLoader.getImage("mi_swtextbg");
	    li_Loading = imsLoader.getImage("li_Loading");
	    li_LoadingPoint = imsLoader.getImage("li_LoadingPoint");
	  }  // end of initImages()
	
	//Called from the Renderer to get Objects for paint
	public void draw(Graphics g)
	{
	    Graphics2D dbg = (Graphics2D) g;
	    
		dbg.setColor(Color.black);
		
		//get Screensize
		Rectangle r = dbg.getClipBounds();
		//draw Backgroundimage
		dbg.drawImage(bgImage,0,0,r.width,r.height,null);//draw Backgroundimage
		if (!swahlenVisible && !textbgVisible && !backVisible && bExitAnimationEnded)
		{
			bClsPainted = true;
		}
		//draw Menue Elements
		if(textbgVisible)
		{
			if(miTextbgResizeFaktor == 1.0) 
				dbg.drawImage(mi_textbg,MI_TEXTBACK_POS_X,MI_TEXTBACK_POS_Y,null);
			else
			{//draw resized
				int destWidth = (int) (mi_textbg.getWidth() * miTextbgResizeFaktor);
			    int destHeight = (int) (mi_textbg.getHeight() * miTextbgResizeFaktor);

			    // adjust top-left (x,y) coord of resized image so it remains centered 
			    int destX = MI_TEXTBACK_POS_X + mi_textbg.getWidth()/2 - destWidth/2;
			    int destY = MI_TEXTBACK_POS_Y + mi_textbg.getHeight()/2 - destHeight/2;

				dbg.drawImage(mi_textbg,destX,destY,destWidth,destHeight,null);
			}
		}
		if(swahlenVisible)
		{
			if(miInfoResizeFaktor == 1.0) 
				dbg.drawImage(mi_swahlen,MI_SWAHLEN_POS_X,MI_SWAHLEN_POS_Y,null);
			else
			{//draw resized
				int destWidth = (int) (mi_swahlen.getWidth() * miInfoResizeFaktor);
			    int destHeight = (int) (mi_swahlen.getHeight() * miInfoResizeFaktor);

			    // adjust top-left (x,y) coord of resized image so it remains centered 
			    int destX = MI_SWAHLEN_POS_X + mi_swahlen.getWidth()/2 - destWidth/2;
			    int destY = MI_SWAHLEN_POS_Y + mi_swahlen.getHeight()/2 - destHeight/2;

				dbg.drawImage(mi_swahlen,destX,destY,destWidth,destHeight,null);
			}
		}
		if(backVisible)
		{
			if(miBackResizeFaktor == 1.0) 
				dbg.drawImage(mi_back,MI_BACK_POS_X,MI_BACK_POS_Y,null);
			else
			{//draw resized
				int destWidth = (int) (mi_back.getWidth() * miBackResizeFaktor);
			    int destHeight = (int) (mi_back.getHeight() * miBackResizeFaktor);

			    // adjust top-left (x,y) coord of resized image so it remains centered 
			    int destX = MI_BACK_POS_X + mi_back.getWidth()/2 - destWidth/2;
			    int destY = MI_BACK_POS_Y + mi_back.getHeight()/2 - destHeight/2;

				dbg.drawImage(mi_back,destX,destY,destWidth,destHeight,null);
			}
		}
		//Draw avialbleGameList
		if (miTextbgResizeFaktor >= 1.0) //1.000000002, so only circa. "==" dont work
		{
		    for(int iTick = 0; iTick < szPlugs.length ;iTick++)
		    {
		        dbg.setColor(Color.BLACK);
		        dbg.setFont(new Font("SansSerif", Font.BOLD, 24));
		        if (bMOGame)
		        {
		            if (iMOGame == iTick)
		            {
		                //Draw Descriptions
		                dbg.setColor(Color.YELLOW);
				        dbg.setFont(new Font("SansSerif", Font.BOLD, 16));
				        dbg.drawString("Author:", 550, 200);
				        dbg.setFont(new Font("SansSerif", Font.PLAIN, 14));
		                dbg.drawString(szPlugDetails[iTick][1], 550, 220);
		                dbg.setFont(new Font("SansSerif", Font.BOLD, 16));
		                
		                dbg.drawString("Beschreibung:", 550, 250);
		             
		                AttributedString as = new AttributedString(szPlugDetails[iTick][2]);
		                as.addAttribute(TextAttribute.FONT,new Font("SansSerif", Font.PLAIN, 14));
		                AttributedCharacterIterator aci = as.getIterator();
		                FontRenderContext frc = dbg.getFontRenderContext();
		                LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
		                int y = 270;
		                while (lbm.getPosition() < aci.getEndIndex())
		                {
		                    TextLayout textlayout = lbm.nextLayout(245);
		                    textlayout.draw(dbg,550,y);
		                    y += textlayout.getAscent();
		                }
		   
		                dbg.setColor(Color.BLUE);
				        dbg.setFont(new Font("SansSerif", Font.BOLD, 34));
				        
		            }
		        }
		        //
		        dbg.drawString(szPlugDetails[iTick][0], MI_TEXTBACK_POS_X + 40, MI_TEXTBACK_POS_Y + 20 +((iTick + 1) * 30));
		    }
		}
	}// end of draw
	/**
	 * Lädt eine Liste aller verfügbaren Spiele
	 *
	 */
	public void loadAviableGames()
	{
	    File PLUG_DIR = new File ("src/Cardgames/Plugins/");
	    //Fürs JAR File
	    //File PLUG_DIR = new File ("Cardgames/Plugins/");
	    szPlugs = PLUG_DIR.listFiles(
	            new FilenameFilter()
	            {
                    public boolean accept(File dir, String name) 
                    {
                        if (name.contains(".plg"))
                        return true;
                        else
                        return false;
                    }
	        
	            }
	    );
	    //Load Game Details
	    szPlugDetails = new String[szPlugs.length][4];
	    for(int iTick = 0; iTick < szPlugs.length ;iTick++)
	    {
	        String line;
	        try
	        {
	            BufferedReader b = new BufferedReader(new FileReader(szPlugs[iTick]));
	            while((line = b.readLine()) != null) 
	            {
	                if (line.length() == 0)  // blank line
	                    continue;
	                if (line.startsWith("//"))   // comment
	                    continue;
	                if (line.contains("[TITLE]"))
	                    szPlugDetails[iTick][0] = line.substring(line.indexOf("]") + 1, line.indexOf(";"));
	                if (line.contains("[AUTHOR]"))
	                    szPlugDetails[iTick][1] = line.substring(line.indexOf("]") + 1, line.indexOf(";")); 
	                if (line.contains("[DESCRIPTION]"))
	                    szPlugDetails[iTick][2] = line.substring(line.indexOf("]") + 1, line.indexOf(";")); 
	                if (line.contains("[StartClass]"))
	                    szPlugDetails[iTick][3] = line.substring(line.indexOf("]") + 1, line.indexOf(";")); 
	          	                
	            }
	        }
	        catch (Exception e)
	        {
	            System.out.println("Error Reading Game details :");
	        }
	    }
	}
	public void update()
	{
		//Only every 2nd Update, else to fast
		if (!bSlowAnimation && !pCP.isBMousePressed())
		{
			bSlowAnimation = true;
			return;
		}
		bSlowAnimation = false;
		//beenden
		if (bExitAnimationEnded && bClsPainted)
		{
			bBackActiv = false;
			pCP.remDao(this);
			if (bGameActiv)
			{
			    	String[] szGameDetails = new String[4];
			    	szGameDetails[0] =  szPlugDetails[iMOGame][0];
			    	szGameDetails[1] =  szPlugDetails[iMOGame][1];
			    	szGameDetails[2] =  szPlugDetails[iMOGame][2];
			    	szGameDetails[3] =  szPlugDetails[iMOGame][3];
			    	
			        pCG.newPhase(new CgCardDeck(pCP, pCG, szGameDetails,li_Loading, li_LoadingPoint, bgImage));
			}
			else
			{
			    pCG.newPhase(new CardMenuMain(pCP, pCG));
			}
			return;
		}
		if (bExitAnimationEnded && !bClsPainted)
		{
			return;
		}
		if(bBackActiv )
		{
			if (miInfoResizeFaktor > 0.0)
			{
				miInfoResizeFaktor = miInfoResizeFaktor - 0.1;
			}
			if (miTextbgResizeFaktor > 0.0)
			{
				miTextbgResizeFaktor = miTextbgResizeFaktor - 0.1;
			}
			if (miBackResizeFaktor > 0.0)
			{
				miBackResizeFaktor = miBackResizeFaktor - 0.1;
			}
			if (miTextbgResizeFaktor <= 0.0 && miBackResizeFaktor <= 0.0 && miInfoResizeFaktor <= 0.0)
			{
				swahlenVisible = false;
				backVisible = false;
				textbgVisible = false;
				bExitAnimationEnded = true;
				
			}
			return;
		}
		//Grow Animation
		if(miInfoResizeFaktor < 1.0)
		{
				miInfoResizeFaktor = miInfoResizeFaktor + 0.1;
				return;
		}
		if(miBackResizeFaktor < 0.5)
		{
				miBackResizeFaktor = miBackResizeFaktor + 0.1;
				return;
		}
		if(miTextbgResizeFaktor < 1.0)
		{
			miTextbgResizeFaktor = miTextbgResizeFaktor + 0.05;
			return;
		}
		//Mouse Position
		int IMouseX = pCP.getIMousePosX();
		int IMouseY = pCP.getIMousePosY();
		//Mouse over a menue Object? YES? Let it Bump
		bMOGame = false;
		//Mouse on the Game Choose Menue X ?
		if(IMouseX >= MI_TEXTBACK_POS_X && IMouseX <= (MI_TEXTBACK_POS_X + 400))
		{
		    //Over A Game (y-pos)?
		    for (int iTick = 0; iTick < szPlugs.length; iTick++)
		    {
		        if(IMouseY >= MI_TEXTBACK_POS_Y + (iTick * 40) && IMouseY <= (MI_TEXTBACK_POS_Y + ((1 + iTick) * 40)))
		        {
		              bMOGame = true;
		              iMOGame = iTick;	
		            
		        }
		    }
		        
		}
		bMOBack = false;
		if(IMouseX >= MI_BACK_POS_X && IMouseX <= (MI_BACK_POS_X + mi_back.getWidth()) && IMouseY >= MI_BACK_POS_Y && IMouseY <= (MI_BACK_POS_Y + mi_back.getHeight()))
		{	
			bMOBack = true;
			if(miBackResizeFaktor < MAX_RESIZE_FAKTOR_MI_BACK && !bMiBackShrink)
			{
				miBackResizeFaktor = miBackResizeFaktor + 0.1;
			}
			else
			{
				if(miBackResizeFaktor >= MAX_RESIZE_FAKTOR_MI_BACK && !bMiBackShrink)
				{
					miBackResizeFaktor = miBackResizeFaktor - 0.1;
					bMiBackShrink = true;
				}
				else
				{
					if(miBackResizeFaktor > 1.0 && bMiBackShrink)
					{
						miBackResizeFaktor = miBackResizeFaktor - 0.1;
					}
					else
					{
						bMiBackShrink = false;
						miBackResizeFaktor = miBackResizeFaktor + 0.1;
					}
				}
			}
		}
		else
		{
			if (miBackResizeFaktor > 0.5)
				miBackResizeFaktor = miBackResizeFaktor - 0.1;
		}
		//Mousepressed?
		if (pCP.isBMousePressed())
		{
			if (bMOBack)
			{
				bBackActiv = true;
			}
			if (bMOGame)
			{
			    bBackActiv = true;
			    bGameActiv = true;
			}
		}
	}//end of update
}

