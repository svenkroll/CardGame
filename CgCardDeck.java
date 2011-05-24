package CardGames;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.sun.j3d.utils.timer.J3DTimer;


/*
 * Created on 06.11.2005
 */
public class CgCardDeck extends DrawAbleObjects
{
    private Object o;
    Class c;
    private CardPanel pCP;
	private CardGames pCG;
	private boolean bLoading;
	private boolean bLoadingScreenPainted;
	private boolean bGameInitialised;
	private int nShowLoadPoints = 0;
	private BufferedImage biBackground, biLoading, biLoadPoint;
	private long nLastLoadScreenChange;
	private long nLoadScreenStartet;
	private final int nMinLoadScreenTime = 4;//Secs
	private final double nLoadScreenChangeEvery = 0.5;//Secs
	private String sGameClassName;
	
    public CgCardDeck (CardPanel CP, 
            			CardGames CG, 
            			String[] szGame, 
            			BufferedImage Loading, 
            			BufferedImage Point, 
            			BufferedImage Background)
    {
        
        bLoading = true;
        bGameInitialised = false;
        bLoadingScreenPainted = false;
        nLoadScreenStartet = J3DTimer.getValue();
        nLastLoadScreenChange = J3DTimer.getValue();
   	    biLoading = Loading;
   	    biLoadPoint = Point;
   	    biBackground = Background;
   	    pCP = CP;
		pCG = CG;
        sGameClassName = szGame[3];
    }
    
    private void initialiseGame()
    {
        try 
        {
            c = Class.forName(sGameClassName);
            o = c.newInstance();
            ((GamePlug)o).Initialize(pCP, pCG);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics dbg)
	{
        if (bLoading)
        {
            dbg.setColor(Color.black);
    		
    		//get Screensize
    		Rectangle r = dbg.getClipBounds();
    		//draw Backgroundimage
    		dbg.drawImage(biBackground,0,0,r.width,r.height,null);//draw Backgroundimage
    		//Draw Loading Image
    		dbg.drawImage(biLoading,60,r.height / 2 - 30,biLoading.getWidth(),biLoading.getHeight(),null);//draw Backgroundimage
    	    //Draw LoadingPoints
    	    if (nShowLoadPoints > 0)
    	        dbg.drawImage(biLoadPoint,380,r.height / 2 - 30,biLoadPoint.getWidth(),biLoadPoint.getHeight(),null);//draw Backgroundimage
    	    if (nShowLoadPoints > 1)
    	        dbg.drawImage(biLoadPoint,420,r.height / 2 - 30,biLoadPoint.getWidth(),biLoadPoint.getHeight(),null);//draw Backgroundimage    
    	    if (nShowLoadPoints > 2)
    	        dbg.drawImage(biLoadPoint,460,r.height / 2 - 30,biLoadPoint.getWidth(),biLoadPoint.getHeight(),null);//draw Backgroundimage
    	    //Erstes Zeichnen durch, starte nun initialisierung des Spieles
    	    bLoadingScreenPainted = true;
    	    
        }
        else
        {    
            ((GamePlug)o).draw(dbg);
        }
	}
    private void ExitAndMainMenue()
    {
        pCP.remDao(this);
        o = null;
        c = null;
        pCG.newPhase(new CardMenuMain(pCP, pCG));
    }
	
    public void update()
	{
        if (bLoading)
        {
            if (bLoadingScreenPainted  && !bGameInitialised)
    	    {
    	        bGameInitialised = true;
    	        initialiseGame();
    	    }
            int timeSpentInGame = 
    	          (int) ((J3DTimer.getValue() - nLoadScreenStartet)/1000000000L);
            //Loadimage Change?
            int timeSinceLastChange =
                (int) ((J3DTimer.getValue() - nLastLoadScreenChange)/1000000000L);
            if (timeSinceLastChange > nLoadScreenChangeEvery)
            {
                if (nShowLoadPoints == 3)
                {
                    nShowLoadPoints = 1;
                }
                else
                {
                    nShowLoadPoints++;
                }
                nLastLoadScreenChange = J3DTimer.getValue();
            }
            //Minimum anzeigezeit verstrichen?
            if (timeSpentInGame > nMinLoadScreenTime)
            {
	            //Spiel Initialisiert?
	            if (((GamePlug)o).Initialised())
	            {
	                bLoading = false;
	            }
            }   
        }
        else
        {
		    //Beenden?
		    if (((GamePlug)o).isPlugExiting())
		    {
		        ExitAndMainMenue();
		    }
		    else
		    {
		        ((GamePlug)o).update();
		    }
        }
	}
    
	public void register()
	{
	    pCP.addDao(this);
	}
}
