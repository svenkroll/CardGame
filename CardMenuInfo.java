package CardGames;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

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
public class CardMenuInfo extends DrawAbleObjects
{
//	hold the single images
	private BufferedImage mi_info, bgImage, mi_back, mi_textbg;
	
	private final static String IMS_FILE = "MenueInfoIms.txt";
	private final String IMS_DIRECTORY = "images/";
    /* The file holding the 'o', 'n', 's', and 'g' image information, 
       extracted with an ImagesLoader object. */
	
	private ImagesLoader imsLoader;   // the image loader
	private CardPanel pCP;
	private CardGames pCG;
	boolean infoVisible = false;
	boolean backVisible = false;
	boolean textbgVisible = true;
	boolean bBackActiv = false;
	boolean bMOBack = false;
	boolean bMiBackShrink = false;
	
	private final int MI_INFO_POS_X = 50;
	private final int MI_INFO_POS_Y = 10;
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

	private boolean bSlowAnimation = false;
//---------------------------------------------------------------------------
	public CardMenuInfo(CardPanel CP, CardGames CG)//Need a cardPanel to set paintComponents
	{
		pCP = CP;
		pCG = CG;
		//load and initialise the images
	    imsLoader = new ImagesLoader(IMS_FILE, IMS_DIRECTORY); 
	    initImages();
	    infoVisible = true;
	    backVisible = true;
	}//end of Konstruktor
	
	public void register()
	{
		pCP.addDao(this);
	}
	private void initImages()
	  {
	    // initialize the 'o' image variables
	    mi_info = imsLoader.getImage("mi_info_u");
	    bgImage = imsLoader.getImage("mi_bgimage");
	    mi_back = imsLoader.getImage("mi_back");
	    mi_textbg = imsLoader.getImage("mi_textbg");
	  }  // end of initImages()
	
	//Called from the Renderer to get Objects for paint
	public void draw(Graphics dbg)
	{
		dbg.setColor(Color.black);
		
		//get Screensize
		Rectangle r = dbg.getClipBounds();
		//draw Backgroundimage
		dbg.drawImage(bgImage,0,0,r.width,r.height,null);//draw Backgroundimage
		if (!infoVisible && !textbgVisible && !backVisible && bExitAnimationEnded)
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
		if(infoVisible)
		{
			if(miInfoResizeFaktor == 1.0) 
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
		dbg.setColor(Color.yellow);
		dbg.setFont(new Font("SansSerif", Font.HANGING_BASELINE, 18));
		dbg.drawString("Visit http://www.Lordsoft.de", r.width - 250, r.height - 20);
		
	}// end of draw
	
	public void update()
	{
		//Only every 2nd Update, else too fast
		if (!bSlowAnimation && !pCP.isBMousePressed())
		{
			bSlowAnimation = true;
			return;
		}
		bSlowAnimation = false;
//		beenden
		if (bExitAnimationEnded && bClsPainted)
		{
			bBackActiv = false;
			pCP.remDao(this);
			pCG.newPhase(new CardMenuMain(pCP, pCG));
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
				infoVisible = false;
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
		}
	}//end of update
}
