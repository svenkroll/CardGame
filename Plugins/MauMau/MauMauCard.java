package CardGames.Plugins.MauMau;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/*
 * Created on 15.01.2006
 */

/**
 * @author lordlormi
 */
public class MauMauCard {

    private String sCardTitle;
    private String sCardType;
    private BufferedImage biImage;
    private Point poAktuellePosition, poEndPosition;
    private boolean bDrawFrontSide;
    
    public MauMauCard()
    {
        super();
        poAktuellePosition = new Point();
        bDrawFrontSide = true;
    }
    
    public boolean isBDrawFrontSide() {
        return bDrawFrontSide;
    }
    public void setBDrawFrontSide(boolean drawFrontSide) {
        bDrawFrontSide = drawFrontSide;
    }
    /**
     * @param image
     */
    public void setCardImage(BufferedImage image) 
    {
        // TODO Auto-generated method stub
        biImage = image;
    }

    /**
     * @param string
     */
    public void setCardTitle(String string) 
    {
        // TODO Auto-generated method stub
        sCardTitle = string;
    }

    /**
     * @param string
     */
    public void setCardType(String string) 
    {
        // TODO Auto-generated method stub
        sCardType = string;
    }

    public BufferedImage getBiImage() {
        return biImage;
    }
    public String getSCardTitle() {
        return sCardTitle;
    }
    public String getSCardType() {
        return sCardType;
    }
    public Point getPoAktuellePosition() {
        return poAktuellePosition;
    }
    public void setPoAktuellePosition(Point poAktuellePosition) 
    {
        this.poAktuellePosition = poAktuellePosition;        
    }
    
    public boolean containsPoint(int x, int y)
    {
        Rectangle r = new Rectangle((int)poAktuellePosition.getX(),(int)poAktuellePosition.getY(),biImage.getWidth(),biImage.getHeight());
        Rectangle2D r2D = (Rectangle2D)r;
        boolean b = r2D.contains(x,y);
        return b;
    }
    
    public boolean containsPoint(int x, int y, int nDistance)
    {
        Rectangle r = new Rectangle((int)poAktuellePosition.getX(),(int)poAktuellePosition.getY(),nDistance,biImage.getHeight());
        Rectangle2D r2D = (Rectangle2D)r;
        boolean b = r2D.contains(x,y);
        return b;
    }
    
    public Point getPoEndPosition() {
        return poEndPosition;
    }
    public void setPoEndPosition(Point point) 
    {
        poEndPosition = point;
    }
    
}
