/*
 * Created on 18.01.2006
 */
package CardGames.Plugins.MauMau;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Stack;

import com.sun.j3d.utils.timer.J3DTimer;

/**
 * @author lordlormi
 */
public class MauMauMessagesWindow 
{
    private final Point poMessageWindowPosition;
    private final Rectangle r;
    private Stack stMessages;
    private long nLastMessageDroped = 0;
   
    public MauMauMessagesWindow() 
    {
        super();
        nLastMessageDroped = J3DTimer.getValue();
        stMessages = new Stack();
        poMessageWindowPosition = new Point(20,210);
        r = new Rectangle(poMessageWindowPosition.x, poMessageWindowPosition.y, 20,20);
    }
    
    public void renderMessagesWindow(Graphics g)
    {
        
        int nDrawedStrings = 0;
        if (stMessages.size() > 0)
        {
			g.setColor(Color.WHITE);
			g.setFont(new Font("SansSerif", Font.BOLD, 11));
			
	        for (Enumeration el = stMessages.elements(); el.hasMoreElements();)
	        {
	            g.drawString((String)el.nextElement(), poMessageWindowPosition.x, poMessageWindowPosition.y + (nDrawedStrings * 15)); 
	            nDrawedStrings++;
	        }
        }
    }
    
    public void updateMessages()
    {
        //Entferne alte Messages
        long timeSpent = 
	          (int) ((J3DTimer.getValue() - nLastMessageDroped)/1000000000L);  // ns --> secs
        if (timeSpent > 5)
        {
            if (stMessages.size() > 0)
            {
               //Entferne letzte Nachricht
               dropLastMessage();
            }
        }
    }
    
    public void showMessage(String sMessage)
    {
        //Erstes Element?
        if (stMessages.empty())
            nLastMessageDroped = J3DTimer.getValue();
        //Zuviele Elemente zum Anzeigen?
        if (stMessages.size() > 11)
            dropLastMessage();
        //Neues Element Hibzufügen
        stMessages.push(sMessage);
    }
    
    private void dropLastMessage()
    {
        boolean bFirst = true;
        Stack stTempMessage = new Stack();
        
        for (Enumeration el = stMessages.elements(); el.hasMoreElements();)
	       {
	           if (!bFirst)
	           {
	               stTempMessage.push(el.nextElement());
	           }
	           else
	           {
	               el.nextElement();
	               bFirst = false;
	           }
	       }
        stMessages = stTempMessage;
        stTempMessage = null;
        nLastMessageDroped = J3DTimer.getValue();
    }
    
    public void clearMessages()
    {
        stMessages.clear();
    }
}
