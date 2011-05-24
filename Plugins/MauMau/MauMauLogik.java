package CardGames.Plugins.MauMau;

import java.awt.Point;

/*
 * Created on 16.01.2006
 */

/**
 * @author lordlormi
 */
public class MauMauLogik 
{
    
    //Variablen-----------------------------------------------------------------
    private int nSpielerKartenZuZiehen;
    private int nKIKartenZuZiehen;
    
    private MauMauMain MMMain;
    private String sGewünschteKarte;
    private boolean bKarteGezogen;
    private boolean bSpielerMessageKKZMDrawed;
    private boolean bSpielerKannSiebenVerlängern;
    private boolean bKIKannSiebenVerlängern;
    
    //Getters and Setters-------------------------------------------------------
    public int getNKIKartenZuZiehen() {
        return nKIKartenZuZiehen;
    }
    public void setNKIKartenZuZiehen(int kartenZuZiehen) {
        nKIKartenZuZiehen = kartenZuZiehen;
    }
    public int getNSpielerKartenZuZiehen() {
        return nSpielerKartenZuZiehen;
    }
    public void setNSpielerKartenZuZiehen(int spielerKartenZuZiehen) {
        nSpielerKartenZuZiehen = spielerKartenZuZiehen;
    }
    public void setSGewünschteKarte(String gewünschteKarte) {
        sGewünschteKarte = gewünschteKarte;
    }
    //Konstruktor---------------------------------------------------------------
    public MauMauLogik(MauMauMain mauMauMain)
    {
        MMMain = mauMauMain;
        setNKIKartenZuZiehen(0);
        setNSpielerKartenZuZiehen(0);
        sGewünschteKarte = null;
        bKarteGezogen = false;
        bSpielerKannSiebenVerlängern = false;
        bKIKannSiebenVerlängern = false;
    }
    
    //Funktionen----------------------------------------------------------------
    /**
     * Testet ob der Zug der Karte in parameter mmc 
     * auf die Karte letzte Karte auf dem Friedhof 
     * erlaubt ist.
     */
    public boolean isZugErlaubt(MauMauCard mmc, MauMauCard lastCardOnFriedhof) 
    {
        boolean b = false;
        
        //Bube auf Bube ist nicht erlaubt!
        if (mmc.getSCardTitle().equals("Bube") && lastCardOnFriedhof.getSCardTitle().equals("Bube"))
        {
            MMMain.getMmmWindow().showMessage("Bube auf Bube ist nicht erlaubt!");
        }
        //Bube auf etwas anderes ist natürlich Erlaubt.
        else if (mmc.getSCardTitle().equals("Bube") && !lastCardOnFriedhof.getSCardTitle().equals("Bube"))
        {
            b=true;
        }
        else if (mmc.getSCardTitle().equals(lastCardOnFriedhof.getSCardTitle()))
        {
            b = true;
        }
        else if (mmc.getSCardType().equals(lastCardOnFriedhof.getSCardType()))
        {
            b = true;
        }
        return b;
    }

    /**
     * 
     * Testet ob der Spieler einen gültigen Zug tätigen kann. 
     * 
     * @param cards
     * @return
     */
    public boolean isUserTurnAviable(MauMauCardDeck cards)
    {
        boolean b = false;
        MauMauCard mmc;
        
        for (int nAktCard = 0; nAktCard < cards.getAnzahlKartenSpieler(); nAktCard++)
        {
            mmc = cards.getSpielerCard(nAktCard);
            
            if (isZugErlaubt(mmc, cards.getLastCardOnFriedhof()))
            {
                b = true;
                break;
            }
                       
        }
        return b;
    }
    
    /**
     * 
     * 
     * Testet ob der Computer einen gültigen Zug tätigen kann. 
     * 
     * @param cards
     * @return
     */
    public boolean isKITurnAviable(MauMauCardDeck cards)
    {
        boolean b = false;
        MauMauCard mmc;
        
        for (int nAktCard = 0; nAktCard < cards.getAnzahlKartenKI(); nAktCard++)
        {
            mmc = cards.getKICard(nAktCard);
            if (isZugErlaubt(mmc, cards.getLastCardOnFriedhof()))
            {
                b = true;
                break;
            }
        }
        
        return b;
    }
    
    /**
     * Wählt für den Computer eine Karte zum Ziehen aus.
     * 
     * @param cards
     * @return
     */
    public MauMauCard selectKICardToTurn(MauMauCardDeck cards)
    {
        MauMauCard mmc = null;
        MauMauCard mmcReturn = null;
        for (int nAktCard = 0; nAktCard < cards.getAnzahlKartenKI(); nAktCard++)
        {
            mmc = cards.getKICard(nAktCard);
            if (isZugErlaubt(mmc, cards.getLastCardOnFriedhof()))
            {
                mmcReturn = mmc;
                break;
            }
          
        }
        
        return mmcReturn;
    }

    public boolean hasKISieben(MauMauCardDeck cards) 
    {
        boolean b = false;
        MauMauCard mmc = null;
        for (int nAktCard = 0; nAktCard < cards.getAnzahlKartenKI(); nAktCard++)
        {
            mmc = cards.getKICard(nAktCard);
            if (mmc.getSCardTitle().equals("Sieben"))
            {
                b = true;
                break;
            }
        }
        return b;
    }

    public MauMauCard getKICardSieben(MauMauCardDeck cards) 
    {
        MauMauCard mmc = null;
        for (int nAktCard = 0; nAktCard < cards.getAnzahlKartenKI(); nAktCard++)
        {
            mmc = cards.getKICard(nAktCard);
            if (mmc.getSCardTitle().equals("Sieben"))
            {
                break;
            }
        }
        return mmc;
    }

    public boolean hasSpielerSieben(MauMauCardDeck cards) 
    {
        boolean b = false;
        MauMauCard mmc = null;
        for (int nAktCard = 0; nAktCard < cards.getAnzahlKartenSpieler(); nAktCard++)
        {
            mmc = cards.getSpielerCard(nAktCard);
            if (mmc.getSCardTitle().equals("Sieben"))
            {
                b = true;
                break;
            }
        }
        return b;
    }

    private void SpielerLegeSiebenOderZiehe(MauMauCardDeck Cards)
    {
        if (hasSpielerSieben(Cards) && bSpielerKannSiebenVerlängern)
        {
            if (MMMain.isMousePressed() != null)
			{
		        Point pMouse = MMMain.isMousePressed();
				    //Spielerkarte angeklickt?
					MauMauCard mmc = Cards.getSpielerCardAt(pMouse.x, pMouse.y, MMMain.getNCardDistance());
					if (mmc != null)
					{
					    if (mmc.getSCardTitle().equals("Sieben"))
					    {
					        MMMain.setBAnimationInProgress(true);   			  
					        mmc.setPoEndPosition(new Point(MMMain.getKartenFriedhofPosition()));    					    
					        //Karte vom Spieler- auf den Friedhofsstapel verschieben
					        MMMain.setBMoveSpielerCardToFriedhofAfterAnimation(true);
					        //Spieler Karten neu Positionieren (Lücke auffüllen)
					        MMMain.setBRePositioniereSpielerKarten(true);
					        nKIKartenZuZiehen += 2;
					        nKIKartenZuZiehen += nSpielerKartenZuZiehen;
					        nSpielerKartenZuZiehen = 0;
					        bSpielerKannSiebenVerlängern = false;
					        bKIKannSiebenVerlängern = true;
					        MMMain.getMmmWindow().showMessage("KI, ziehe " + nKIKartenZuZiehen + " Karten");
					        MMMain.setBUsersTurn(false);
					        MMMain.setMMCinAnimation(mmc);
					        bKarteGezogen = false;
					    }
					    else
					    {
					        MMMain.getMmmWindow().showMessage("Lege eine Sieben oder");
					        MMMain.getMmmWindow().showMessage("ziehe eine Karte!");
					    }
					}
					else 
					{
					    //KartenStapel Angeklickt?
					    if(MMMain.isKartenstapelClicked(pMouse))
					    {
					        MMMain.moveCardfromStapelToSpieler();
					        nSpielerKartenZuZiehen -= 1;
					        MMMain.getMmmWindow().showMessage("Spieler zieht Karte.");
					    }
					}
			}
        }
        else //Keine Sieben, dann Ziehen!
        {
            bSpielerKannSiebenVerlängern = false;
            if (MMMain.isMousePressed() != null)
			{
		        Point pMouse = MMMain.isMousePressed();
				//KartenStapel Angeklickt?
			    if(MMMain.isKartenstapelClicked(pMouse))
			    {
			        MMMain.moveCardfromStapelToSpieler();
			        nSpielerKartenZuZiehen -= 1;
			        MMMain.getMmmWindow().showMessage("Spieler zieht Karte.");
			        MMMain.setBUsersTurn(true);
			    }
			}
        }
    }
    
    private void ComputerLegeSiebenOderZiehe(MauMauCardDeck Cards)
    {
        if (this.hasKISieben(Cards) && bKIKannSiebenVerlängern)
		{
            MMMain.setBAnimationInProgress(true);
            MauMauCard mmc = this.getKICardSieben(Cards);
	        
	        mmc.setBDrawFrontSide(true);
	        mmc.setPoEndPosition(new Point(MMMain.getKartenFriedhofPosition()));
	        MMMain.getMmmWindow().showMessage("Spieler am Zug");
            nSpielerKartenZuZiehen += 2;
            nSpielerKartenZuZiehen += nKIKartenZuZiehen;
            nKIKartenZuZiehen = 0;
            bKIKannSiebenVerlängern = false;
            bSpielerKannSiebenVerlängern = true;
            MMMain.getMmmWindow().showMessage(nSpielerKartenZuZiehen + " Karten Ziehen!");
	        MMMain.setBUsersTurn(true);
	        //Karte vom KI- auf den Friedhofsstapel verschieben
	        MMMain.setBMoveKICardToFriedhofAfterAnimation(true);
	        //Computer Karten neu Positionieren (Lücke auffüllen)
	        MMMain.setBRePositioniereComputerKarten(true);
	        bKarteGezogen = false;
	        MMMain.setMMCinAnimation(mmc);
		}
        else//Muss Ziehen
        {
            bKIKannSiebenVerlängern = false;
            //Karte vom Stapel zum Computer
	        MMMain.moveCardfromStapelToKI();
	        nKIKartenZuZiehen -= 1;
	        MMMain.getMmmWindow().showMessage("KI Zieht Karte.");
	        MMMain.setBUsersTurn(false);
        }
    }
    
    protected void ComputersTurn( MauMauCardDeck Cards) 
    {
        //Muss KI Karten Ziehen?
        if (nKIKartenZuZiehen > 0)
        {
            ComputerLegeSiebenOderZiehe(Cards);
        }
        //Muss Buben Wunsch von Spieler erfüllt werden?
        else if (sGewünschteKarte != null)
        {
            ComputerLegeSpielerwunschOderZiehe(Cards);
        }
        else //Alles OK, keine Ziehen 
        {
            //Ki-Zug möglich? 
    		if (isKITurnAviable(Cards))
    		{
    		    MauMauCard mmc = selectKICardToTurn(Cards);
    		    ComputerMakeTurn(mmc, Cards);
    		}
    		else
    		{
    		    //Karte Schon gezogen? Nur eine Karte Ziehen, dann nächster Spieler
    		    if (!bKarteGezogen)
    		    {		        		     
    		        //Karte vom Stapel zum Computer
    		        MMMain.moveCardfromStapelToKI();
    		        bKarteGezogen = true;
    		        MMMain.getMmmWindow().showMessage("KI Zieht Karte.");				        				 
    		    }
    		    else //KI Zug beendet, User ist dran
    		    {
    		        MMMain.setBUsersTurn(true);
    		        MMMain.getMmmWindow().showMessage("Spieler am Zug");
    		        bKarteGezogen = false;
    		    }
    		}
        }
              
	}
    
    private String getCardTypeToWishForKI(MauMauCardDeck cards) 
    {
        MauMauCard mmc;
        int nAnzKaro = 0;
        int nAnzHerz = 0;
        int nAnzPeek = 0;
        int nAnzKreuz = 0;
        String s = null;
        
        //Wovon existieren die meisten Karten?
        for (int nAktCard = 0; nAktCard < cards.getAnzahlKartenKI(); nAktCard++)
        {
            mmc = cards.getKICard(nAktCard);
            
            if (mmc.getSCardType().equalsIgnoreCase("Karo"))
            {
                nAnzKaro++;
            }
            else if (mmc.getSCardType().equalsIgnoreCase("Herz"))
            {
                nAnzHerz++;
            }
            else if (mmc.getSCardType().equalsIgnoreCase("Kreuz"))
            {
                nAnzKreuz++;
            }
            else  //Peek
            {
                nAnzPeek++;
            }
                       
        }
        //Wähle Kartentyp von dem am meisten auf der Hand sind.
        if (nAnzKreuz >= nAnzKaro && nAnzKreuz >= nAnzHerz && nAnzKreuz >= nAnzPeek)
        {
            //Habe am meisten Kreuz
            s = "Kreuz";
        }
        if (nAnzKaro >= nAnzHerz && nAnzKaro >= nAnzPeek && s == null)
        {
            //Habe am meisten Karo
            s = "Karo";
        }
        if (nAnzPeek >= nAnzHerz && s == null)
        {
            //Habe am meisten Peek
            s = "Peek";
        }
        if (s == null)
        {
            //Habe am meisten Herz
            s = "Herz";
        }
        return s;
    }
    
    protected void UsersTurn(MauMauCardDeck Cards) 
    {
        //Muss Spieler Karten Ziehen?
        if (nSpielerKartenZuZiehen > 0)
        {
            SpielerLegeSiebenOderZiehe(Cards);
        }
        //Muss Buben Wunsch von Computer erfüllt werden?
        else if (sGewünschteKarte != null)
        {
            SpielerLegeComputerwunschOderZiehe(Cards);
        }
        else //Alles OK keine Karte ziehen
        {
	        //Spielerzug möglich? 
			if (isUserTurnAviable(Cards))
			{
	        //Mousepressed?
				if (MMMain.isMousePressed() != null)
				{
			        Point pMouse = MMMain.isMousePressed();
			        	//Stapel angeklickt?
			        	if (MMMain.isKartenstapelClicked(pMouse))
			        	{
			        	    MMMain.getMmmWindow().showMessage("Ziehen nicht erlaubt!");
			        	    MMMain.getClipsLoader().play("fehler",true);
			        	}
			        	else
			        	{
						    //Spielerkarte angeklickt?
							MauMauCard mmc = Cards.getSpielerCardAt(pMouse.x, pMouse.y, MMMain.getNCardDistance());
							if (mmc != null)
							{
							    //Zug erlaubt?
							    boolean b = isZugErlaubt(mmc, Cards.getLastCardOnFriedhof());
							    if (b)
							    {
							        //Zug Erlaubt!
							        UserMakeTurn(mmc);
							    }
							    else
							    {
							        MMMain.getMmmWindow().showMessage("Zug nicht erlaubt!");
							        MMMain.getClipsLoader().play("fehler",true);
							    }
							}
			        	}
				}
			}
			else
			{
			    //Karte Schon gezogen?
			    if (!bKarteGezogen)
			    {
				    //Nachricht kein Zug möglich, Karte Ziehen oder Aufgeben! HAHA
			        if (!bSpielerMessageKKZMDrawed)
			        {
			            MMMain.getMmmWindow().showMessage("Spieler, bitte Karte ziehen!");
			            bSpielerMessageKKZMDrawed = true;
			        }
			        if (MMMain.isMousePressed() != null)
					{
				        Point pMouse = MMMain.isMousePressed();
				        
					    if (MMMain.isKartenstapelClicked(pMouse))
					    {
					        //Karte vom Stapel zum Spieler
					        MMMain.moveCardfromStapelToSpieler();
					        bKarteGezogen = true;
					        MMMain.getMmmWindow().showMessage("Spieler Zieht Karte.");
					        bSpielerMessageKKZMDrawed = false;
					    }
					}
			    }
			    else //User Zug beendet, Computer ist dran
			    {
			        MMMain.setBUsersTurn(false);
			        MMMain.getMmmWindow().showMessage("Weiterer Zug umöglich,");
			        MMMain.getMmmWindow().showMessage("KI am Zug");
			        bKarteGezogen = false;
			    }
			}
        }
	}
    
    private void ComputerLegeSpielerwunschOderZiehe(MauMauCardDeck cards) 
    {
       
        //Kann der Wunsch bedient werden?
        if (hasKICardType(cards, sGewünschteKarte))
        {
            MauMauCard mmc = selectKICardToTurn(cards, sGewünschteKarte);
            sGewünschteKarte = null;
            ComputerMakeTurn(mmc, cards);
        }
        else //Zeihe eine Karte und dann noch mal einen Versuch
        {
            if (!bKarteGezogen)
            {
		        //Karte vom Stapel zum Computer
		        MMMain.moveCardfromStapelToKI();
		        bKarteGezogen = true;
		        MMMain.getMmmWindow().showMessage("KI Zieht Karte.");
	 
            }
            else //Karte Schon gezogen und trotzdem kein Zug möglich, Computer ist dran
            {
                MMMain.setBUsersTurn(true);
                MMMain.getMmmWindow().showMessage("Kein " + sGewünschteKarte + " gezogen.");
                bKarteGezogen = false;
            }
            
        }
    }
    
    private MauMauCard selectKICardToTurn(MauMauCardDeck cards, String spielerWünscht) 
    {
        MauMauCard mmc = null;
        MauMauCard mmcReturn = null;
        for (int nAktCard = 0; nAktCard < cards.getAnzahlKartenKI(); nAktCard++)
        {
            mmc = cards.getKICard(nAktCard);
            if (mmc.getSCardType().equals(spielerWünscht) && isZugErlaubt(mmc, cards.getLastCardOnFriedhof()))
            {
                mmcReturn = mmc;
                break;
            }           
        }
        
        return mmcReturn;
    }
    
    private void SpielerLegeComputerwunschOderZiehe(MauMauCardDeck cards) 
    {
        
        //Kann der Wunsch bedient werden?
        if (hasSpielerCardType(cards, sGewünschteKarte))
        {
            //Mousepressed?
			if (MMMain.isMousePressed() != null)
			{
		        Point pMouse = MMMain.isMousePressed();
		        //Stapel angeklickt?
		        if (MMMain.isKartenstapelClicked(pMouse))
		        {
		            MMMain.getMmmWindow().showMessage("");
		            MMMain.getClipsLoader().play("fehler", false);
		        }
		        else
		        {
				    //Spielerkarte angeklickt?
					MauMauCard mmc = cards.getSpielerCardAt(pMouse.x, pMouse.y, MMMain.getNCardDistance());
					if (mmc != null)
					{
					    //Zug erlaubt?
					    if (mmc.getSCardType().equals(sGewünschteKarte))
					    {
					        sGewünschteKarte = null;
					        UserMakeTurn(mmc);
					    }
					    else
					    {
					        MMMain.getMmmWindow().showMessage("Zug nicht erlaubt!");
					        MMMain.getClipsLoader().play("fehler",true);
					    }
					}
		        }
			}
				  
        }
        else //Zeihe eine Karte und dann noch mal einen Versuch
        {
            if (!bKarteGezogen)
            {
                if (!bSpielerMessageKKZMDrawed)
		        {
                    MMMain.getMmmWindow().showMessage("Spieler hat kein " + sGewünschteKarte + ",");
		            MMMain.getMmmWindow().showMessage("bitte Karte ziehen!");
		            bSpielerMessageKKZMDrawed = true;
		        }
		        if (MMMain.isMousePressed() != null)
				{
			        Point pMouse = MMMain.isMousePressed();
			        
				    if (MMMain.isKartenstapelClicked(pMouse))
				    {
				        //Karte vom Stapel zum Spieler
				        MMMain.moveCardfromStapelToSpieler();
				        bKarteGezogen = true;
				        MMMain.getMmmWindow().showMessage("Spieler Zieht Karte.");
				        bSpielerMessageKKZMDrawed = false;
				    }
				}
            }
            else //Karte Schon gezogen und trotzdem kein Zug möglich, Computer ist dran
            {
                MMMain.setBUsersTurn(false);
                MMMain.getMmmWindow().showMessage("Kein " + sGewünschteKarte + " gezogen.");
                 
                bKarteGezogen = false;
            }
            
        }
    }
    
    private void UserMakeTurn(MauMauCard mmc)
    {
        MMMain.setBAnimationInProgress(true);
        mmc.setPoEndPosition(new Point(MMMain.getKartenFriedhofPosition()));
       
        //Eine Acht gezogen? 
        if (mmc.getSCardTitle().equals("Acht"))
        {
            MMMain.getMmmWindow().showMessage("KI, Aussetzen!");
            
            MMMain.setBUsersTurn(true);
        }
        else if(mmc.getSCardTitle().equals("Bube"))
        {
            
            MMMain.setBShowAskType(true);
            MMMain.setBUsersTurn(false);
        }
        else if(mmc.getSCardTitle().equals("Sieben"))
        {
            nKIKartenZuZiehen += 2; 
            bKIKannSiebenVerlängern = true;
            MMMain.getMmmWindow().showMessage("KI " + nKIKartenZuZiehen + " Karten Ziehen!");
            MMMain.setBUsersTurn(false);
        }
        else
        {
            MMMain.getMmmWindow().showMessage("KI am Zug");
	        MMMain.setBUsersTurn(false);
        }
        
        //Karte vom Spieler- auf den Friedhofsstapel verschieben
        MMMain.setBMoveSpielerCardToFriedhofAfterAnimation(true);
        //Spieler Karten neu Positionieren (Lücke auffüllen)
        MMMain.setBRePositioniereSpielerKarten(true);
        MMMain.setMMCinAnimation(mmc);
        bKarteGezogen = false;
    }
        
    private boolean hasSpielerCardType(MauMauCardDeck cards, String sType)
    {
        boolean b = false;
        MauMauCard mmc = null;
        for (int nAktCard = 0; nAktCard < cards.getAnzahlKartenSpieler(); nAktCard++)
        {
            mmc = cards.getSpielerCard(nAktCard);
            if (mmc.getSCardType().equals(sType))
            {
                b = true;
                break;
            }
        }
        return b;
    }
    
    private boolean hasKICardType(MauMauCardDeck cards, String sType)
    {
        boolean b = false;
        MauMauCard mmc = null;
        for (int nAktCard = 0; nAktCard < cards.getAnzahlKartenKI(); nAktCard++)
        {
            mmc = cards.getKICard(nAktCard);
            if (mmc.getSCardType().equals(sType) && isZugErlaubt(mmc, cards.getLastCardOnFriedhof()))
            {
                b = true;
                break;
            }
        }
        return b;
    }
    
    private void ComputerMakeTurn(MauMauCard mmc, MauMauCardDeck cards)
    {
        MMMain.setBAnimationInProgress(true);
        mmc.setBDrawFrontSide(true);
        mmc.setPoEndPosition(new Point(MMMain.getKartenFriedhofPosition()));    	        
        if (mmc.getSCardTitle().equals("Acht"))
        {
            MMMain.getMmmWindow().showMessage("Spieler, Aussetzen!");
            MMMain.setBUsersTurn(false);
        }
        else if (mmc.getSCardTitle().equals("Sieben"))
        {
            MMMain.getMmmWindow().showMessage("Spieler am Zug");
            nSpielerKartenZuZiehen += 2;
            bSpielerKannSiebenVerlängern = true;
            MMMain.getMmmWindow().showMessage(nSpielerKartenZuZiehen + " Karten Ziehen!");
            MMMain.setBUsersTurn(true);
        }
        else if (mmc.getSCardTitle().equals("Bube"))
        {
            sGewünschteKarte = getCardTypeToWishForKI(cards);
            MMMain.getMmmWindow().showMessage("Computer wünscht " + sGewünschteKarte);
            MMMain.getMmmWindow().showMessage("Spieler am Zug");
            MMMain.setBUsersTurn(true);
        }
        else
        {
            MMMain.getMmmWindow().showMessage("Spieler am Zug");
            MMMain.setBUsersTurn(true);
        }
       
        //Karte vom KI- auf den Friedhofsstapel verschieben
        MMMain.setBMoveKICardToFriedhofAfterAnimation(true);
        //Computer Karten neu Positionieren (Lücke auffüllen)
        MMMain.setBRePositioniereComputerKarten(true);
        MMMain.setMMCinAnimation(mmc);
        bKarteGezogen = false;
    }
    
    
}
