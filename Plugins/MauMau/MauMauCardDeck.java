 package CardGames.Plugins.MauMau;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Random;
import java.util.Stack;

import CardGames.ImagesLoader;


/*
 * Created on 08.01.2006
 */

/**
 * @author lordlormi
 */
public class MauMauCardDeck 
{

    private final String IMS_FILE = "CardsIms.txt";
    private final String IMS_DIRECTORY = "Plugins/MauMau/images/";
    private BufferedImage CDBack;
    
    private MauMauCard[] Cards;
    
    private ImagesLoader imsLoader;
    private Stack KartenStapel;
    private Stack KartenAufFriedhof;
    private Stack KartenStapelSpieler;
    private Stack KartenStapelKI;
    
    public MauMauCardDeck() 
    {
        super();
        KartenStapel = new Stack();
        KartenAufFriedhof = new Stack();
        KartenStapelSpieler = new Stack();
        KartenStapelKI = new Stack();
        initCardDeck();
        // TODO Auto-generated constructor stub
    }
    
    private void initCardDeck()
    {
        Cards = new MauMauCard[32];
        imsLoader = new ImagesLoader(IMS_FILE, IMS_DIRECTORY); 
        
        for (int iTick = 1; iTick < 33; iTick++)
        {
            Cards[iTick - 1] = new MauMauCard();
            Cards[iTick - 1].setCardImage(imsLoader.getImage("" + iTick));
        }
        
        //Kartendeck zusammenstellen
      
        Cards[0].setCardTitle("Ass");
        Cards[0].setCardType("Kreuz");
        Cards[1].setCardTitle("Ass");
        Cards[1].setCardType("Peek");
        Cards[2].setCardTitle("Ass");
        Cards[2].setCardType("Herz");
        Cards[3].setCardTitle("Ass");
        Cards[3].setCardType("Karo");
        
        Cards[4].setCardTitle("König");
        Cards[4].setCardType("Kreuz");
        Cards[5].setCardTitle("König");
        Cards[5].setCardType("Peek");
        Cards[6].setCardTitle("König");
        Cards[6].setCardType("Herz");
        Cards[7].setCardTitle("König");
        Cards[7].setCardType("Karo");
        
        Cards[8].setCardTitle("Dame");
        Cards[8].setCardType("Kreuz");
        Cards[9].setCardTitle("Dame");
        Cards[9].setCardType("Peek");
        Cards[10].setCardTitle("Dame");
        Cards[10].setCardType("Herz");
        Cards[11].setCardTitle("Dame");
        Cards[11].setCardType("Karo");
        
        Cards[12].setCardTitle("Bube");
        Cards[12].setCardType("Kreuz");
        Cards[13].setCardTitle("Bube");
        Cards[13].setCardType("Peek");
        Cards[14].setCardTitle("Bube");
        Cards[14].setCardType("Herz");
        Cards[15].setCardTitle("Bube");
        Cards[15].setCardType("Karo");
        
        Cards[16].setCardTitle("Zehn");
        Cards[16].setCardType("Kreuz");
        Cards[17].setCardTitle("Zehn");
        Cards[17].setCardType("Peek");
        Cards[18].setCardTitle("Zehn");
        Cards[18].setCardType("Herz");
        Cards[19].setCardTitle("Zehn");
        Cards[19].setCardType("Karo");
        
        Cards[20].setCardTitle("Neun");
        Cards[20].setCardType("Kreuz");
        Cards[21].setCardTitle("Neun");
        Cards[21].setCardType("Peek");
        Cards[22].setCardTitle("Neun");
        Cards[22].setCardType("Herz");
        Cards[23].setCardTitle("Neun");
        Cards[23].setCardType("Karo");
    
        Cards[24].setCardTitle("Acht");
        Cards[24].setCardType("Kreuz");
        Cards[25].setCardTitle("Acht");
        Cards[25].setCardType("Peek");
        Cards[26].setCardTitle("Acht");
        Cards[26].setCardType("Herz");
        Cards[27].setCardTitle("Acht");
        Cards[27].setCardType("Karo");
        
        Cards[28].setCardTitle("Sieben");
        Cards[28].setCardType("Kreuz");
        Cards[29].setCardTitle("Sieben");
        Cards[29].setCardType("Peek");
        Cards[30].setCardTitle("Sieben");
        Cards[30].setCardType("Herz");
        Cards[31].setCardTitle("Sieben");
        Cards[31].setCardType("Karo");
        
        CDBack = imsLoader.getImage("b2fv");
    }
    
    public BufferedImage getCDBack() 
    {
        return CDBack;
    }
    
    public int getAnzahlKartenAufStapel()
    {
        return KartenStapel.size();
    }
    
    public void ResetCards() 
    {
        KartenStapel.clear();
        BitSet b = new BitSet();
        Random r = new Random();
        int cnt = 0;
        while (cnt < 32) 
        {
            int num = Math.abs(r.nextInt()) % 32;
            if (!b.get(num)) 
            {                
                b.set(num);
                ++cnt;
                KartenStapel.push(Cards[num]);
        	}
        }
        KartenAufFriedhof.clear();
        KartenStapelKI.clear();
        KartenStapelSpieler.clear();
 
    }

    public int getAnzahlKartenSpieler() {
        // TODO Auto-generated method stub
        return KartenStapelSpieler.size();
    }

    public int getAnzahlKartenKI() 
    {
        return KartenStapelKI.size();
    }
    
    /**
     * Mischt alle verbrauchten Karten vom Friedhof zurück in den Stapel.
     *
     */
    private void moveCardsfromFriedhofToStapel()
    {
        MauMauCard mmcTempFreidhofCard = (MauMauCard)KartenAufFriedhof.pop();
        MauMauCard mmc;
        while(!KartenAufFriedhof.isEmpty())
        {
            mmc = (MauMauCard)KartenAufFriedhof.pop();
            mmc.setBDrawFrontSide(false);
            KartenStapel.push(mmc);
        }
        KartenAufFriedhof.push(mmcTempFreidhofCard);
    }
    
    public MauMauCard getCardFromStapel()
    {
        MauMauCard mmc = null;
        mmc = (MauMauCard)KartenStapel.pop();
        if (KartenStapel.isEmpty())
        {
            moveCardsfromFriedhofToStapel();
        }
        return mmc;
    }
    
    public void addCardToStapelSpieler(MauMauCard mmc)
    {
        KartenStapelSpieler.push(mmc);
    }

    public int getAnzahlKartenAufFriedhof() {
        // TODO Auto-generated method stub
        return KartenAufFriedhof.size();
    }

    public MauMauCard getLastCardOnFriedhof() {
        // TODO Auto-generated method stub
        return (MauMauCard)KartenAufFriedhof.peek();
    }
    
    public void drawSpielerKarten(Graphics g)
    {
        if (KartenStapelSpieler.size() > 0)
        {
	        for (Enumeration el = KartenStapelSpieler.elements(); el.hasMoreElements();)
	        {
	            MauMauCard mmc = (MauMauCard)el.nextElement();
	            g.drawImage(mmc.getBiImage(),mmc.getPoAktuellePosition().x,mmc.getPoAktuellePosition().y,mmc.getBiImage().getWidth(),mmc.getBiImage().getHeight(),null);
	        }
        }
    }

    public void addCardToStapelKI(MauMauCard mmc) 
    {
        KartenStapelKI.push(mmc);
        
    }

    public void drawKIKarten(Graphics g) 
    {
        if (KartenStapelKI.size() > 0)
        {
	        for (Enumeration el = KartenStapelKI.elements(); el.hasMoreElements();)
	        {
	            MauMauCard mmc = (MauMauCard)el.nextElement();
	            if (!mmc.isBDrawFrontSide())
	            {
	                //debug
	                //g.drawImage(mmc.getBiImage(),mmc.getPoAktuellePosition().x,mmc.getPoAktuellePosition().y,mmc.getBiImage().getWidth(),mmc.getBiImage().getHeight(),null);
	                g.drawImage(getCDBack(),mmc.getPoAktuellePosition().x,mmc.getPoAktuellePosition().y,mmc.getBiImage().getWidth(),mmc.getBiImage().getHeight(),null);
	            }
	            else
	            {
	                g.drawImage(mmc.getBiImage(),mmc.getPoAktuellePosition().x,mmc.getPoAktuellePosition().y,mmc.getBiImage().getWidth(),mmc.getBiImage().getHeight(),null);
	            }
	        }
        }
        
    }

    public void drawFriedhof(Graphics g) 
    {
        if (!KartenAufFriedhof.isEmpty())
        {
            MauMauCard mmc = (MauMauCard)KartenAufFriedhof.peek();
            if (!mmc.isBDrawFrontSide())
            {
                
                g.drawImage(getCDBack(),mmc.getPoAktuellePosition().x,mmc.getPoAktuellePosition().y,mmc.getBiImage().getWidth(),mmc.getBiImage().getHeight(),null);
            }
            else
            {
                g.drawImage(mmc.getBiImage(),mmc.getPoAktuellePosition().x,mmc.getPoAktuellePosition().y,mmc.getBiImage().getWidth(),mmc.getBiImage().getHeight(),null);
            }
        }
    }

    public void addCardToFriedhof(MauMauCard mmc) 
    {
        mmc.setBDrawFrontSide(true);
        KartenAufFriedhof.push(mmc);
    }

    public MauMauCard getSpielerCardAt(int mouseX, int mouseY, int nCardDistance) 
    {    
        MauMauCard mmcReturn = null;
        MauMauCard mmc;
        
        if (KartenStapelSpieler.size() > 0)
        {
	        for (Enumeration el = KartenStapelSpieler.elements(); el.hasMoreElements();)
	        {
	            mmc = (MauMauCard)el.nextElement();
	            if (el.hasMoreElements())
	            {
	                if(mmc.containsPoint(mouseX, mouseY, nCardDistance))
		            {
		                mmcReturn = mmc;
		                break;
		            } 
	            }
	            else
	            {
	                if(mmc.containsPoint(mouseX, mouseY))
		            {
		                mmcReturn = mmc;
		                break;
		            }  
	            }
	            
	            
	        }
        }
        return mmcReturn;
    }

    public void removeSpielerKarte(MauMauCard cinAnimation)
    {
        MauMauCard mmc;
        Stack stTempSpielerKarten = new Stack();
        
        for (Enumeration el = KartenStapelSpieler.elements(); el.hasMoreElements();)
        {
            mmc = (MauMauCard)el.nextElement();
            if (!mmc.equals(cinAnimation))
            {
                stTempSpielerKarten.push(mmc);
            }
        }
        KartenStapelSpieler = stTempSpielerKarten;
        stTempSpielerKarten = null;
        mmc = null;
    }

    public void removeKIKarte(MauMauCard cinAnimation)
    {
        MauMauCard mmc;
        Stack stTempKIKarten = new Stack();
        
        for (Enumeration el = KartenStapelKI.elements(); el.hasMoreElements();)
        {
            mmc = (MauMauCard)el.nextElement();
            if (!mmc.equals(cinAnimation))
            {
                stTempKIKarten.push(mmc);
            }
        }
        KartenStapelKI = stTempKIKarten;
        stTempKIKarten = null;
        mmc = null;
    }

    
    /**
     * 
     * Spieler Spielkarten auf dem Spielfeld neu Positionieren.
     * Wenn alle Karten in Position sind wird kein Objekt mehr zurück
     * gegeben.
     * 
     * @param cardStartPoint (Erster Karten Punkt oben-rechts)
     * @param cardDistance (x Abstand der einzelnen Karten)
     * @return
     */
    public MauMauCard rePositioniereSpielerKarten(Point poCardStartPoint, int cardDistance) 
    {
        MauMauCard mmc;
        MauMauCard mmcReturn = null;
        int nAktCardNumber = 0;
        Point poNewPosition;
        
        for (Enumeration el = KartenStapelSpieler.elements(); el.hasMoreElements();)
        {
            poNewPosition = new Point((poCardStartPoint.x + (nAktCardNumber * cardDistance)), poCardStartPoint.y);
            nAktCardNumber++;
            mmc = (MauMauCard)el.nextElement();
            if (!mmc.getPoAktuellePosition().equals(poNewPosition))
            {
                mmc.setPoEndPosition(poNewPosition);
                mmcReturn = mmc;
                break;
            }
            
        }
        
        return mmcReturn;
    }

    public MauMauCard rePositioniereKIKarten(Point poCardStartPoint, int cardDistance) 
    {
        MauMauCard mmc;
        MauMauCard mmcReturn = null;
        int nAktCardNumber = 0;
        Point poNewPosition;
        
        for (Enumeration el = KartenStapelKI.elements(); el.hasMoreElements();)
        {
            poNewPosition = new Point((poCardStartPoint.x + (nAktCardNumber * cardDistance)), poCardStartPoint.y);
            nAktCardNumber++;
            mmc = (MauMauCard)el.nextElement();
            if (!mmc.getPoAktuellePosition().equals(poNewPosition))
            {
                mmc.setPoEndPosition(poNewPosition);
                mmcReturn = mmc;
                break;
            }
            
        }
        
        return mmcReturn;
    }

    
    /**
     * @param aktCard
     * @return
     */
    public MauMauCard getSpielerCard(int nCard) 
    {	
        int nAktCard = 0;
        MauMauCard mmc = null;
        for (Enumeration el = KartenStapelSpieler.elements(); el.hasMoreElements();)
        {
            if (nAktCard == nCard)
            {
                mmc = (MauMauCard)el.nextElement();
                break;
            }
            else
            {
                el.nextElement();
                nAktCard++;
            }
            
        }
        return mmc;
    }

    /**
     * @param aktCard
     * @return
     */
    public MauMauCard getKICard(int nCard) 
    {
        int nAktCard = 0;
        MauMauCard mmc = null;
        for (Enumeration el = KartenStapelKI.elements(); el.hasMoreElements();)
        {
            if (nAktCard == nCard)
            {
                mmc = (MauMauCard)el.nextElement();
                break;
            }
            else
            {
                el.nextElement();
                nAktCard++;
            }
            
        }
        return mmc;
    }

}
