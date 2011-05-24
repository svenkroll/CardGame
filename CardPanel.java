package CardGames;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import com.sun.j3d.utils.timer.J3DTimer;

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
public class CardPanel extends JPanel implements Runnable
{
	private static final int PWIDTH = 800;     // size of this panel
	private static final int PHEIGHT = 600;
	private Thread animator;           // the thread that performs the animation
	private int iMousePosX, iMousePosY;
	private boolean running = false;
	private boolean bMousePressed = false;
	
	private long PERIOD;   
	private static final int NO_DELAYS_PER_YIELD = 16;
	  /* Number of frames with a delay of 0 ms before the animation thread yields
	     to other running threads. */
	private static int MAX_FRAME_SKIPS = 5;
	    // no. of frames that can be skipped in any one animation loop
	    // i.e the games state is updated but not rendered

    //off screen rendering
	private Graphics dbg; 
	
 	private Image dbImage = null;
 	
 	//holds the Objects to Render
 	private int daoSize;
	private DrawAbleObjects dao[];
//---------------------------------------------------------------------------------						
	public CardPanel(long period)
	{
		this.PERIOD = period; 
		setPreferredSize( new Dimension(PWIDTH, PHEIGHT) );
		setFocusable(true);
	    requestFocus();    // the JPanel now has focus, so receives key events
	    addMouseMotionListener( new MouseMotionListener() {
	        public void mouseMoved(MouseEvent e)
	        {
	        	iMousePosX = (e.getX());
	        	iMousePosY = (e.getY());
	        }  // handle mouse moves
	        public void mouseDragged(MouseEvent e)
	        { ; }  // handle dragges
	      });
	    addMouseListener( new MouseAdapter() {
	        public void mousePressed(MouseEvent e)
	        { 
	        	bMousePressed = true; 
	        }
	      });
	}
	
	public void addNotify()
	  // wait for the JPanel to be added to the JFrame before starting
	  { super.addNotify();   // creates the peer
	    startGame();         // start the thread
	  }
	
	private void startGame()
	  // initialise and start the thread 
	  { 
	    if (animator == null || !running) {
	      animator = new Thread(this);
		  animator.start();
	    }
	  } // end of startGame()
	
	public int addDao(DrawAbleObjects Obj)
	{
		if(dao == null)
		{
			daoSize = 0;
			dao = new DrawAbleObjects[100];
		}
		dao[daoSize] = Obj;
		daoSize++;
		return daoSize - 1;
	}
	public void remDao(DrawAbleObjects DAO)
	{
		//TODO now clear all, but must clear only the one
		//for(int tick = 0; tick < daoSize; tick++)
		//{
		//	if (dao[tick].equals(DAO))
		//		dao[tick]= null;
		//}
		dao = null;
		daoSize = 0;
	}
	public void run()
	  { 
		long gameStartTime, beforeTime, afterTime, timeDiff, sleepTime;
	    long overSleepTime = 0L;
	    int noDelays = 0;
	    long excess = 0L;

	    gameStartTime = J3DTimer.getValue();
	    beforeTime = gameStartTime;
		running = true;
		while (running)
		{
			//update the Objects
			Update();
			//render the Objects
			render();
			//draw the Screen
		    paintScreen();
		    
		    afterTime = J3DTimer.getValue();
		      timeDiff = afterTime - beforeTime;
		      sleepTime = (PERIOD - timeDiff) - overSleepTime;  

		      if (sleepTime > 0) {   // some time left in this cycle
		        try {
		          Thread.sleep(sleepTime/1000000L);  // nano -> ms
		        }
		        catch(InterruptedException ex){}
		        overSleepTime = (J3DTimer.getValue() - afterTime) - sleepTime;
		      }
		      else {    // sleepTime <= 0; the frame took longer than the period
		        excess -= sleepTime;  // store excess time value
		        overSleepTime = 0L;

		        if (++noDelays >= NO_DELAYS_PER_YIELD) {
		          Thread.yield();   // give another thread a chance to run
		          noDelays = 0;
		        }
		      }

		      beforeTime = J3DTimer.getValue();

		      /* If frame animation is taking too long, update the game state
		         without rendering it, to get the updates/sec nearer to
		         the required FPS. */
		      int skips = 0;
		      while((excess > PERIOD) && (skips < MAX_FRAME_SKIPS)) {
		        excess -= PERIOD;
		        //update the Objects
				Update(); // update but don't render
		        skips++;
		      }
		}
	  } // end of actionPerformed()

	private void Update()
	{
		if (dao != null)
		{
			for(int tick = 0; tick < daoSize; tick++)
			{
				dao[tick].update();
			}
		}
		bMousePressed = false;
	}
	private void paintScreen()
	  // use active rendering to put the buffered image on-screen
	  { 
	    Graphics g;
	    Graphics2D g2;
	    try 
	    {
	      g = this.getGraphics();
	      g2 = (Graphics2D)g;
	      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	              RenderingHints.VALUE_ANTIALIAS_ON);
	      
	      if ((g2 != null) && (dbImage != null))
	        g2.drawImage(dbImage, 0, 0, null);
	      g2.dispose();
	    }
	    catch (Exception e)
	    { System.out.println("Graphics context error: " + e);  }
	  } // end of paintScreen()
	
	private void render()
	  {
	    if (dbImage == null){
	      dbImage = createImage(PWIDTH, PHEIGHT);
	      if (dbImage == null) {
	        System.out.println("dbImage is null");
	        return;
	      }
	      else
	        dbg = dbImage.getGraphics();
	    }
	    Graphics2D g2 = (Graphics2D)dbg;
	    // clear the background
	    g2.setColor(Color.white);
	    g2.fillRect (0, 0, PWIDTH, PHEIGHT);
		g2.setColor(Color.black);
		g2.setClip(0,0,PWIDTH, PHEIGHT);
	    // draw graphic elements:
		if (dao != null)
		{
			for(int tick = 0; tick < daoSize; tick++)
			{
				dao[tick].draw(g2);
			}
		}
	  }  // end of gameRender()

	public int getIMousePosX() {
		return iMousePosX;
	}
	
	public int getIMousePosY() {
		return iMousePosY;
	}
	
	public boolean isBMousePressed() {
		return bMousePressed;
	}
	
}
