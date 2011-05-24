package CardGames;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/*
 * Created on 06.07.2005
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
public class CardMenuMain extends DrawAbleObjects
{
	//hold the single images
	private BufferedImage mi_exit, mi_info, mi_swahlen, bgImage, mi_title;
	
	private final static String IMS_FILE = "MenueIms.txt";
	private final String IMS_DIRECTORY = "images/";
    /* The file holding the 'o', 'n', 's', and 'g' image information, 
       extracted with an ImagesLoader object. */
	
	private ImagesLoader imsLoader;   // the image loader
	private CardPanel pCP;
	private CardGames pCG;
	boolean infoVisible = false;
	boolean exitVisible = false;
	boolean swahlenVisible = false;
	boolean bStartPhase = true;
	private final int MI_EXIT_POS_X = 220;
	private final int MI_INFO_POS_X = 220;
	private final int MI_SWAHLEN_POS_X = 220;
	private final int MI_EXIT_POS_Y = 440;
	private final int MI_INFO_POS_Y = 330;
	private final int MI_SWAHLEN_POS_Y = 230;
	boolean bMOExit = false;
	boolean bMOInfo = false;
	boolean bMOSwahlen = false;
	boolean bMiExitShrink = false;
	boolean bMiInfoShrink = false;
	boolean bMiSwahlenShrink = false;
	double miExitResizeFaktor = 0.0;
	double miInfoResizeFaktor = 0.0;
	double miSwahlenResizeFaktor = 0.0;
	private final double MAX_RESIZE_FAKTOR = 2.0; 
	private boolean bSwahlenActiv = false;
	private boolean bInfoActiv = false;
	private boolean bExitActiv = false;
	private boolean bExitAnimationEnded = false;
	private boolean bClsPainted = false;
	
	private boolean bSlowAnimation = false;
//---------------------------------------------------------------------------
	public CardMenuMain(CardPanel CP, CardGames CG)//Need a cardPanel to set paintComponents
	{
		pCP = CP;
		pCG = CG;
		//load and initialise the images
	    imsLoader = new ImagesLoader(IMS_FILE, IMS_DIRECTORY); 
	    initImages();
	    infoVisible = true;
	    exitVisible = true;
	    swahlenVisible = true;
	}//end of Konstruktor
	
	public void register()
	{
		pCP.addDao(this);
	}
	private void initImages()
	  {
	    // initialize the 'o' image variables
	    mi_exit = imsLoader.getImage("mi_exit");
	    mi_info = imsLoader.getImage("mi_info");
	    mi_swahlen = imsLoader.getImage("mi_swahlen");
	    bgImage = imsLoader.getImage("mi_bgimage");
	    mi_title = imsLoader.getImage("mi_title");
	  }  // end of initImages()
	
	//Called from the Renderer to get Objects for paint
	public void draw(Graphics dbg)
	{
		dbg.setColor(Color.black);
		
		//get Screensize
		Rectangle r = dbg.getClipBounds();
		//draw Backgroundimage
		dbg.drawImage(bgImage,0,0,r.width,r.height,null);//draw Backgroundimage
		dbg.drawImage(mi_title,80,20,null); //draw Title Image
		if (!exitVisible && !infoVisible && !swahlenVisible && bExitAnimationEnded)
		{
			bClsPainted = true;
		}
		//draw Menue Elements
		if(swahlenVisible)
		{
			if(!bMOSwahlen && miSwahlenResizeFaktor == 1.0) 
				dbg.drawImage(mi_swahlen,MI_SWAHLEN_POS_X,MI_SWAHLEN_POS_Y,null);
			else
			{//draw resized
				int destWidth = (int) (mi_swahlen.getWidth() * miSwahlenResizeFaktor);
			    int destHeight = (int) (mi_swahlen.getHeight() * miSwahlenResizeFaktor);

			    // adjust top-left (x,y) coord of resized image so it remains centered 
			    int destX = MI_SWAHLEN_POS_X + mi_swahlen.getWidth()/2 - destWidth/2;
			    int destY = MI_SWAHLEN_POS_Y + mi_swahlen.getHeight()/2 - destHeight/2;

				dbg.drawImage(mi_swahlen,destX,destY,destWidth,destHeight,null);
			}
		}
		if(infoVisible)
		{
			if(!bMOInfo && miInfoResizeFaktor == 1.0) 
				dbg.drawImage(mi_info,MI_INFO_POS_X,MI_INFO_POS_Y,null);
			else
			{//draw resized
				int destWidth = (int) (mi_info.getWidth() * miInfoResizeFaktor);
			    int destHeight = (int) (mi_info.getHeight() * miInfoResizeFaktor);

			    // adjust top-left (x,y) coord of resized image so it remains centered 
			    int destX = MI_INFO_POS_X + mi_info.getWidth()/2 - destWidth/2;
			    int destY = MI_INFO_POS_Y + mi_info.getHeight()/2 - destHeight/2;

				dbg.drawImage(mi_info,destX,destY,destWidth,destHeight,null);
			}
		}
		if(exitVisible)
		{
			if(!bMOExit && miExitResizeFaktor == 1.0) 
				dbg.drawImage(mi_exit,MI_EXIT_POS_X,MI_EXIT_POS_Y,null);
			else
			{//draw resized
				int destWidth = (int) (mi_exit.getWidth() * miExitResizeFaktor);
			    int destHeight = (int) (mi_exit.getHeight() * miExitResizeFaktor);

			    // adjust top-left (x,y) coord of resized image so it remains centered 
			    int destX = MI_EXIT_POS_X + mi_exit.getWidth()/2 - destWidth/2;
			    int destY = MI_EXIT_POS_Y + mi_exit.getHeight()/2 - destHeight/2;

				dbg.drawImage(mi_exit,destX,destY,destWidth,destHeight,null);
			}
				
		}
		if (!bStartPhase)
		{
			dbg.setColor(Color.yellow);
			dbg.setFont(new Font("SansSerif", Font.HANGING_BASELINE, 18));
			dbg.drawString("Visit http://www.Lordsoft.de", r.width - 250, r.height - 20);
		}
		
	}// end of draw
	
	public void update()
	{
		//Only every 2nd Update, else too fast. ut not when mouse pressed!
		if (!bSlowAnimation && !pCP.isBMousePressed())
		{
			bSlowAnimation = true;
			return;
		}
		bSlowAnimation = false;
		//beenden
		if (bExitAnimationEnded && bClsPainted)
		{
			if(bSwahlenActiv)
			{
				bSwahlenActiv = false;
				pCP.remDao(this);
				pCG.newPhase(new CardMenuSW(pCP, pCG));
			}
			else if(bInfoActiv) 
			{
				bInfoActiv = false;
				pCP.remDao(this);
				pCG.newPhase(new CardMenuInfo(pCP, pCG));
			}
			else if(bExitActiv)
			{
				System.exit(0);
			}
			return;
		}
		if (bExitAnimationEnded && !bClsPainted)
		{
			return;
		}
		//Shrink for exit
		if(bSwahlenActiv || bInfoActiv || bExitActiv)
		{
			if (miInfoResizeFaktor > 0.0)
			{
				miInfoResizeFaktor = miInfoResizeFaktor - 0.1;
			}
			if (miExitResizeFaktor > 0.0)
			{
				miExitResizeFaktor =  miExitResizeFaktor - 0.1;
			}
			if (miSwahlenResizeFaktor > 0.0)
			{
				miSwahlenResizeFaktor = miSwahlenResizeFaktor - 0.1;
			}
			if (miSwahlenResizeFaktor <= 0.0 && miExitResizeFaktor <= 0.0 && miInfoResizeFaktor <= 0.0)
			{
				exitVisible = false;
				infoVisible = false;
				swahlenVisible = false;
				bExitAnimationEnded = true;
			}
			return;
		}
//		Grow Animation
		if (bStartPhase)
		{
			if(miSwahlenResizeFaktor < 1.0)
			{
					miSwahlenResizeFaktor = miSwahlenResizeFaktor + 0.1;
					
			}
			if(miInfoResizeFaktor < 1.0)
			{
					miInfoResizeFaktor = miInfoResizeFaktor + 0.1;
					
			}
			if(miExitResizeFaktor < 1.0)
			{
					miExitResizeFaktor = miExitResizeFaktor + 0.1;
					
			}
			if(miExitResizeFaktor >= 1.0 && miInfoResizeFaktor >= 1.0 && miSwahlenResizeFaktor >= 1.0)
			{
				bStartPhase = false;
			}
			return;
		}	
		//Mouse Position
		int IMouseX = pCP.getIMousePosX();
		int IMouseY = pCP.getIMousePosY();
		//Mouse over a menue Object? YES? Let it Bump
		bMOExit = false;
		if(IMouseX >= MI_EXIT_POS_X && IMouseX <= (MI_EXIT_POS_X + mi_exit.getWidth()) && IMouseY >= MI_EXIT_POS_Y && IMouseY <= (MI_EXIT_POS_Y + mi_exit.getHeight()))
		{	
			bMOExit = true;
			if(miExitResizeFaktor < MAX_RESIZE_FAKTOR && !bMiExitShrink)
			{
				miExitResizeFaktor = miExitResizeFaktor + 0.1;
			}
			else
			{
				if(miExitResizeFaktor >= MAX_RESIZE_FAKTOR && !bMiExitShrink)
				{
					miExitResizeFaktor = miExitResizeFaktor - 0.1;
					bMiExitShrink = true;
				}
				else
				{
					if(miExitResizeFaktor > 1.5 && bMiExitShrink)
					{
						miExitResizeFaktor = miExitResizeFaktor - 0.1;
					}
					else
					{
						bMiExitShrink = false;
						miExitResizeFaktor = miExitResizeFaktor + 0.1;
					}
				}
			}
		}
		else
		{
			if (miExitResizeFaktor > 1.0)
				miExitResizeFaktor = miExitResizeFaktor - 0.1;
		}
		bMOInfo = false;
		if(IMouseX >= MI_INFO_POS_X && IMouseX <= (MI_INFO_POS_X + mi_exit.getWidth()) && IMouseY >= MI_INFO_POS_Y && IMouseY <= (MI_INFO_POS_Y + mi_exit.getHeight()))
		{	
			bMOInfo = true;
			if(miInfoResizeFaktor < MAX_RESIZE_FAKTOR && !bMiInfoShrink)
			{
				miInfoResizeFaktor = miInfoResizeFaktor + 0.1;
			}
			else
			{
				if(miInfoResizeFaktor >= MAX_RESIZE_FAKTOR && !bMiInfoShrink)
				{
					miInfoResizeFaktor = miInfoResizeFaktor - 0.1;
					bMiInfoShrink = true;
				}
				else
				{
					if(miInfoResizeFaktor > 1.5 && bMiInfoShrink)
					{
						miInfoResizeFaktor = miInfoResizeFaktor - 0.1;
					}
					else
					{
						bMiInfoShrink = false;
						miInfoResizeFaktor = miInfoResizeFaktor + 0.1;
					}
				}
			}
		}
		else
		{
			if (miInfoResizeFaktor > 1.0)
				miInfoResizeFaktor = miInfoResizeFaktor - 0.1;
		}
		bMOSwahlen = false;
		if(IMouseX >= MI_SWAHLEN_POS_X && IMouseX <= (MI_SWAHLEN_POS_X + mi_exit.getWidth()) && IMouseY >= MI_SWAHLEN_POS_Y && IMouseY <= (MI_SWAHLEN_POS_Y + mi_exit.getHeight()))
		{	
			bMOSwahlen = true;
			if(miSwahlenResizeFaktor < MAX_RESIZE_FAKTOR && !bMiSwahlenShrink)
			{
				miSwahlenResizeFaktor = miSwahlenResizeFaktor + 0.1;
			}
			else
			{
				if(miSwahlenResizeFaktor >= MAX_RESIZE_FAKTOR && !bMiSwahlenShrink)
				{
					miSwahlenResizeFaktor = miSwahlenResizeFaktor - 0.1;
					bMiSwahlenShrink = true;
				}
				else
				{
					if(miSwahlenResizeFaktor > 1.5 && bMiSwahlenShrink)
					{
						miSwahlenResizeFaktor = miSwahlenResizeFaktor - 0.1;
					}
					else
					{
						bMiSwahlenShrink = false;
						miSwahlenResizeFaktor = miSwahlenResizeFaktor + 0.1;
					}
				}
			}
		}
		else
		{
			if (miSwahlenResizeFaktor > 1.0)
				miSwahlenResizeFaktor = miSwahlenResizeFaktor - 0.1;
		}
		//Mousepressed?
		if (pCP.isBMousePressed())
		{
			if (bMOExit)
			{
				bExitActiv = true;
			}
			if (bMOInfo)
			{
				bInfoActiv = true;
			}
			if (bMOSwahlen)
			{
				bSwahlenActiv = true;
			}
		}
	}//end of update
	
}//end Class
