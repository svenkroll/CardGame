/*
 * Created on 03.07.2005
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

package CardGames;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

public class CardGames extends JFrame
{
	//hold the JPanel
	public CardPanel CP;
	private static int DEFAULT_FPS = 40; 
	private DrawAbleObjects cardmenu;
	
	public static void main(String[] args) 
		{
		// switch on translucency acceleration in Windows
	    System.setProperty("sun.java2d.translaccel", "true"); 
	    // System.setProperty("sun.java2d.ddforcevram", "true"); 
	    // switch on hardware acceleration if using OpenGL with pbuffers
	    // System.setProperty("sun.java2d.opengl", "true"); 
	    long period = (long) 1000.0/DEFAULT_FPS;
	    //System.out.println("fps: " + DEFAULT_FPS + "; period: " + period + " ms");
	    CardGames app = new CardGames("Lordsofts Cardgames", period*1000000L);
	    
		} // end of main()
	
	public CardGames(String szTitle, long period) 
		{
		super(szTitle);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setBackground(Color.white);
	    this.CP = new CardPanel(period);
	    this.getContentPane().add(CP, BorderLayout.CENTER);
	    this.pack();
	    this.setResizable(false);  
	    this.setVisible(true);
	    this.setEnabled(true);
	    
	    //Start the game menu
	    newPhase(new CardMenuMain(CP,this));
		}
	
	public void newPhase(DrawAbleObjects newDAO)
	{
		cardmenu = null;
		cardmenu = newDAO;
		//register as Graphic Object
		cardmenu.register();
	}
	
	
	
	
}