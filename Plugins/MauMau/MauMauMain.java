package CardGames.Plugins.MauMau;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import CardGames.CardGames;
import CardGames.CardPanel;
import CardGames.ClipsLoader;
import CardGames.GamePlug;
import CardGames.ImagesLoader;

import com.sun.j3d.utils.timer.J3DTimer;

/*
 * Created on 30.12.2005
 */
/**
 * @author lordlormi
 */
public class MauMauMain implements GamePlug
{
    private final String IMS_FILE = "MauMauIms.txt";
    private final String IMS_DIRECTORY = "Plugins/MauMau/images/";
    private final String SNDS_FILE = "clipsInfo.txt";
    private final String SNDS_DIR = "Plugins/MauMau/sounds/";
    
    private BufferedImage bgImage, biAufgeben, biAnleitung, biAskType;
    
    private MauMauCardDeck Cards;
    private ImagesLoader imsLoader;
    private CardPanel pCP;
	private CardGames pCG;
	private MauMauLogik mmcLogik;
	private ClipsLoader clipsloader;
	private MauMauCard MMCinAnimation;
	private MauMauMessagesWindow mmmWindow;
	
	private Object[] Button1 = {"",Color.BLACK,new Font("SansSerif", Font.HANGING_BASELINE, 18)};
	private Object[] Button2 = {"",Color.BLACK,new Font("SansSerif", Font.HANGING_BASELINE, 18)};
	private Object[] ButtonYes = {"Ja",Color.BLACK,new Font("SansSerif", Font.HANGING_BASELINE, 18)};
	private Object[] ButtonNo = {"Nein",Color.BLACK,new Font("SansSerif", Font.HANGING_BASELINE, 18)};
	private Object[] ButtonAnleitung = {"Anleitung",Color.BLACK,new Font("SansSerif", Font.HANGING_BASELINE, 18)};
	private Object[] ButtonSchliessen = {"Schliessen",Color.BLACK,new Font("SansSerif", Font.HANGING_BASELINE, 18)};
	private Object[] ButtonKreuz = {"Kreuz",Color.BLACK,new Font("SansSerif", Font.HANGING_BASELINE, 18)};
	private Object[] ButtonPeek = {"Peek",Color.BLACK,new Font("SansSerif", Font.HANGING_BASELINE, 18)};
	private Object[] ButtonHerz = {"Herz",Color.BLACK,new Font("SansSerif", Font.HANGING_BASELINE, 18)};
	private Object[] ButtonKaro = {"Karo",Color.BLACK,new Font("SansSerif", Font.HANGING_BASELINE, 18)};
	
	private int timeSpentInGame,nGameSecs,nGameMins;
	private final int nCardDistance = 50;
    private long gameStartTime;
    
    private boolean bGameRunning;
    private boolean bKIWon;
    private boolean bUserWon;
    private boolean bAnimationInProgress;
    private boolean bStartPhase;
    private boolean bMoveSpielerCardToFriedhofAfterAnimation;
    private boolean bMoveKICardToFriedhofAfterAnimation;
    private boolean bShowAufgeben;
    private boolean bRePositioniereSpielerKarten;
    private boolean bRePositioniereComputerKarten;
    private boolean bInitilised = false;
    
    private boolean bExitPlug;
    private boolean bUsersTurn;
    private boolean bShowAnleitung;
    private boolean bShowAskType;
    
    private boolean bMouseOverButton1 = false;
    private boolean bMouseOverButton2 = false;
    private boolean bMouseOverButtonYes = false;
    private boolean bMouseOverButtonAnleitung = false;
    private boolean bMouseOverButtonSchliessen = false;
    private boolean bMouseOverButtonNo = false;
    private boolean bMouseOverButtonPeek = false;
    private boolean bMouseOverButtonKreuz = false;
    private boolean bMouseOverButtonHerz = false;
    private boolean bMouseOverButtonKaro = false;
    
    private StringBuffer sGameSecs = new StringBuffer();
    private StringBuffer sGameMins = new StringBuffer();
    
    private final Point KartenStapelPosition = new Point(410, 233); 
    private final Point KartenFriedhofPosition = new Point(510, 233);
    private final Point AnleitungPosition = new Point(100, 100);
    private final Point AskTypePosition = new Point(228, 152);
    private final Point SpielerKartenPosition = new Point(230,430);
    private final Point KIKartenPosition = new Point(230,30);
    
    
    //Getter und Setter------------------------------------------------------------

    public boolean isBAnimationInProgress() {
        return bAnimationInProgress;
    }
    public boolean isBShowAskType() {
        return bShowAskType;
    }
    public void setBShowAskType(boolean showAskType) {
        bShowAskType = showAskType;
    }
    public void setBAnimationInProgress(boolean animationInProgress) {
        bAnimationInProgress = animationInProgress;
    }
    public MauMauMessagesWindow getMmmWindow() {
        return mmmWindow;
    }
    public boolean isBUsersTurn() {
        return bUsersTurn;
    }
    public void setBUsersTurn(boolean usersTurn) {
        bUsersTurn = usersTurn;
    }
    public void setBMoveKICardToFriedhofAfterAnimation(
            boolean moveKICardToFriedhofAfterAnimation) {
        bMoveKICardToFriedhofAfterAnimation = moveKICardToFriedhofAfterAnimation;
    }
    public void setBMoveSpielerCardToFriedhofAfterAnimation(
            boolean moveSpielerCardToFriedhofAfterAnimation) {
        bMoveSpielerCardToFriedhofAfterAnimation = moveSpielerCardToFriedhofAfterAnimation;
    }
    public void setBRePositioniereComputerKarten(
            boolean rePositioniereComputerKarten) {
        bRePositioniereComputerKarten = rePositioniereComputerKarten;
    }
    public void setBRePositioniereSpielerKarten(
            boolean rePositioniereSpielerKarten) {
        bRePositioniereSpielerKarten = rePositioniereSpielerKarten;
    }
    public MauMauCard getMMCinAnimation() {
        return MMCinAnimation;
    }
    public void setMMCinAnimation(MauMauCard cinAnimation) {
        MMCinAnimation = cinAnimation;
    }
    public Point getKartenFriedhofPosition() {
        return KartenFriedhofPosition;
    }
    public Point getKartenStapelPosition() {
        return KartenStapelPosition;
    }
    public ClipsLoader getClipsLoader()
    {
     return clipsloader;   
    }  
    public int getNCardDistance() {
        return nCardDistance;
    }
    //Funktionen-------------------------------------------------------------------
    
    public void Initialize(CardPanel CP, CardGames CG)
    {
        bInitilised = false;
        pCP = CP;
		pCG = CG;
		mmcLogik = new MauMauLogik(this);
		mmmWindow = new MauMauMessagesWindow();
        initImages();
        initSounds();
        Cards = new MauMauCardDeck();
        bGameRunning = false;
        bInitilised = true;
    }
    
    public void starteSpiel()
	    { 
	        gameStartTime = J3DTimer.getValue();
	        Cards = new MauMauCardDeck();
	        Cards.ResetCards();
	        bUserWon = false;
	        bKIWon = false;
	        mmcLogik = new MauMauLogik(this);
			mmmWindow = new MauMauMessagesWindow();
			bMoveKICardToFriedhofAfterAnimation = false;
			bMoveSpielerCardToFriedhofAfterAnimation = false;
	        bStartPhase  = true;
	        bGameRunning = true;
	       
	    }
   
    private void initSounds()
    {
        //initialise the loaders
	    clipsloader = new ClipsLoader(SNDS_FILE, SNDS_DIR); 
	    
    }
    
    private void initImages()
	  {
	    imsLoader = new ImagesLoader(IMS_FILE, IMS_DIRECTORY); 
	    bgImage = imsLoader.getImage("MM_Back");  
	    biAufgeben = imsLoader.getImage("aufgeben");  
	    biAnleitung = imsLoader.getImage("MM_Anleitung");
	    biAskType = imsLoader.getImage("asktype");
	  } // end of initImages()
    
    public void draw(Graphics dbg)
	{
        //Initialise std. Color
        	dbg.setColor(Color.black);
		
		//get Screensize
			Rectangle r = dbg.getClipBounds();
		
		//draw Backgroundimage
			dbg.drawImage(bgImage,0,0,r.width,r.height,null);//draw Backgroundimage
		
		//Zeichne Button-Texte
			//Button1
			dbg.setColor((Color)Button1[1]);
			dbg.setFont((Font)Button1[2]);
			dbg.drawString((String)Button1[0], 27, 435);
			//Button2
			dbg.setColor((Color)Button2[1]);
			dbg.setFont((Font)Button2[2]);
			dbg.drawString((String)Button2[0], 27, 469);
			//ButtonAnleitung
			dbg.setColor((Color)ButtonAnleitung[1]);
			dbg.setFont((Font)ButtonAnleitung[2]);
			dbg.drawString((String)ButtonAnleitung[0], 27, 503);
			
		//Zeichne Spiel-Zeit
			if (bGameRunning && !bShowAufgeben && !bShowAnleitung)
			{
			    //Zeichne Zeit
				String sTime = sGameMins + ":" + sGameSecs; 
			    dbg.setColor(Color.WHITE);    
				dbg.drawString(sTime, 111, 160);
			}
		//Zeichne Informationen (MessageWindow)
			if (!bShowAufgeben && !bShowAnleitung)
			{
			    mmmWindow.renderMessagesWindow(dbg);
			}
		//Zeichne Karten
			if (bGameRunning && !bShowAufgeben && !bShowAnleitung)
			{
			    //Zeichne Kartenstapel
			    if (Cards.getAnzahlKartenAufStapel() > 0)
			    {
			        dbg.drawImage(Cards.getCDBack(),KartenStapelPosition.x,KartenStapelPosition.y,Cards.getCDBack().getWidth(),Cards.getCDBack().getHeight(),null);
			    }
			    //Zeichne Karten auf Friedhof
			    	Cards.drawFriedhof(dbg);
			    //Zeichne Spieler Karten
			    	Cards.drawSpielerKarten(dbg);
			    //Zeichne KI Karten (nur Rückseiten!)
			    	Cards.drawKIKarten(dbg);
			}
		//Zeichne Anleitung
			if(bShowAnleitung)
			{
			    //Zeichne Background
			    dbg.drawImage(biAnleitung,AnleitungPosition.x,AnleitungPosition.y,biAnleitung.getWidth(),biAnleitung.getHeight(),null);
			    //Zeichne Button Schliesen
			    dbg.setColor((Color)ButtonSchliessen[1]);
				dbg.setFont((Font)ButtonSchliessen[2]);
				dbg.drawString((String)ButtonSchliessen[0], 562, 467);
			}
			if(bShowAskType)
			{
			    //Zeichne Background
			    dbg.drawImage(biAskType,AskTypePosition.x,AskTypePosition.y,biAskType.getWidth(),biAskType.getHeight(),null);
			    //Zeichne Buttons
			    dbg.setColor((Color)ButtonPeek[1]);
				dbg.setFont((Font)ButtonPeek[2]);
				dbg.drawString((String)ButtonPeek[0], 590, 225);
				
				dbg.setColor((Color)ButtonKreuz[1]);
				dbg.setFont((Font)ButtonKreuz[2]);
				dbg.drawString((String)ButtonKreuz[0], 590, 264);
				
				dbg.setColor((Color)ButtonHerz[1]);
				dbg.setFont((Font)ButtonHerz[2]);
				dbg.drawString((String)ButtonHerz[0], 590, 303);
				
				dbg.setColor((Color)ButtonKaro[1]);
				dbg.setFont((Font)ButtonKaro[2]);
				dbg.drawString((String)ButtonKaro[0], 590, 342);
			}
		//Zeichne Aufgeben Frage
			if(bShowAufgeben)
			{
			 //Frage ob Aufgegeben werden soll wird Eingeblendet
			 dbg.drawImage(biAufgeben,((int)r.getWidth() / 2) - ((int)biAufgeben.getWidth() / 2),((int)r.getHeight() / 2) - ((int)biAufgeben.getHeight() / 2),biAufgeben.getWidth(),biAufgeben.getHeight(),null);//draw Backgroundimage
			 
			 //	Aufgeben Aktiv, dann auch diese Buttons anzeigen
			 dbg.setColor((Color)ButtonYes[1]);
			 dbg.setFont((Font)ButtonYes[2]);
			 dbg.drawString((String)ButtonYes[0], 320, 350);
			 
			 dbg.setColor((Color)ButtonNo[1]);
			 dbg.setFont((Font)ButtonNo[2]);
			 dbg.drawString((String)ButtonNo[0], 450, 350);
			}
			
		
	}
    
    private void Aufgegeben()
    {
        bGameRunning = false;
        mmmWindow.clearMessages();
        bShowAufgeben = false;
    }
    
    private void ExitAndMainMenue()
    {
        bExitPlug = true;
    }
    
    public boolean isPlugExiting()
    {
        return bExitPlug;
    }
    
    private void buttonPressed(Object[] objButton)
    {
        if (objButton[0].equals("Neues Spiel"))
		{
		    starteSpiel();
		}
        else if (objButton[0].equals("Hauptmenü"))
		{
            ExitAndMainMenue();
		}
        else if (objButton[0].equals("Aufgeben"))
		{
           bShowAufgeben = true;
		}
        else if (objButton[0].equals("Ja"))
		{
           Aufgegeben();
		}
        else if (objButton[0].equals("Nein"))
		{
           bShowAufgeben = false;
		}
        else if (objButton[0].equals("Anleitung"))
		{
           bShowAnleitung = true;
		}
        else if (objButton[0].equals("Schliessen"))
		{
           bShowAnleitung = false;
		}
        else if (objButton[0].equals("Karo"))
		{
           mmcLogik.setSGewünschteKarte("Karo");
           mmmWindow.showMessage("Spieler Wünscht Karo");
           bShowAskType = false;
		}
        else if (objButton[0].equals("Herz"))
		{
           mmcLogik.setSGewünschteKarte("Herz");
           mmmWindow.showMessage("Spieler Wünscht Herz");
           bShowAskType = false;
		}
        else if (objButton[0].equals("Peek"))
		{
           mmcLogik.setSGewünschteKarte("Peek");
           mmmWindow.showMessage("Spieler Wünscht Peek");
           bShowAskType = false;
		}
        else if (objButton[0].equals("Kreuz"))
		{
           mmcLogik.setSGewünschteKarte("Kreuz");
           mmmWindow.showMessage("Spieler Wünscht Kreuz");
           bShowAskType = false;
		}
    }
    
    private void renderGameTime()
    {
        timeSpentInGame = 
	          (int) ((J3DTimer.getValue() - gameStartTime)/1000000000L);  // ns --> secs
		nGameSecs = timeSpentInGame % 60;
		nGameMins = (int) timeSpentInGame / 60;
		sGameSecs.setLength(0);
		sGameMins.setLength(0);
		if (nGameSecs < 10)
		    sGameSecs.append(0);
		if (nGameMins < 10)
		    sGameMins.append(0);
		sGameMins.append(nGameMins);
		sGameSecs.append(nGameSecs);
    }
    
    private void renderButtonTexte()
    {
	    if (!bGameRunning)
	    {
	        //Zeichne Button1 "Spiel Starten"
		    Button1[0] = new String("Neues Spiel");
		    Button1[1] = Color.BLACK;
		    Button1[2] = new Font("SansSerif", Font.HANGING_BASELINE, 18);
		    
		    //Zeichne Button2 "Hauptmenü"
		    Button2[0] = new String("Hauptmenü");
		    Button2[1] = Color.BLACK;
		    Button2[2] = new Font("SansSerif", Font.HANGING_BASELINE, 18);
		    
		}
	    else
	    {
	        //Zeichne Button1 "NIX"
		    Button1[0] = new String("");
		    Button1[1] = Color.BLACK;
		    Button1[2] = new Font("SansSerif", Font.HANGING_BASELINE, 18);
		    
		    //Zeichne Button2 "Aufgeben"
		    Button2[0] = new String("Aufgeben");
		    Button2[1] = Color.BLACK;
		    Button2[2] = new Font("SansSerif", Font.HANGING_BASELINE, 18);
	    }
	    //Zeichne Button Anleitung
	    ButtonAnleitung[0] = new String("Anleitung");
	    ButtonAnleitung[1] = Color.BLACK;
	    ButtonAnleitung[2] = new Font("SansSerif", Font.HANGING_BASELINE, 18);
    }
    
	public void update()
	{
	    //Spielzeit
	    	if (bGameRunning && !bShowAufgeben && !bShowAnleitung && !bShowAskType)
	    	    renderGameTime();
	    //Nachrichten
	    	if (!bShowAufgeben && !bShowAnleitung && !bShowAskType)
	    	    mmmWindow.updateMessages();	
	    //Button Texte
		    renderButtonTexte();			   
	    //Mouse over Button? Button Angeklickt?
		    checkButtonAktivitäten();
		 
        //Animation im Gange?
			if (bAnimationInProgress && !bShowAufgeben && !bShowAnleitung && !bShowAskType)
			    CardAnimations();
		//Startphase?
			if (bStartPhase && !bAnimationInProgress && !bShowAnleitung  && !bShowAskType)
			    KartenZumSpielstartVerteilen();
		//Spiel Beenden?
			else if (isThereAWinner() != null && bGameRunning && !bStartPhase)
			{
			    //Spiel beendet
			    bGameRunning = false;
			    String sWinner = isThereAWinner();
			    if (sWinner.equals("KI"))
			    {
			        bKIWon = true;
			        mmmWindow.showMessage("KI hat Gewonnen!");
			        
			    }
			    else
			    {
			        bUserWon = true;
			        mmmWindow.showMessage("Spieler hat Gewonnen!");
			        clipsloader.play("applause", false);
			    }
			}
	    //Karten neu Positionieren?
			else if (bRePositioniereSpielerKarten && !bAnimationInProgress && !bStartPhase)
			{
			    spielerKartenNeuPositionieren();
			}
			else if (bRePositioniereComputerKarten && !bAnimationInProgress && !bStartPhase)
			{
			    KIKartenNeuPositionieren();
			}
		//Einen weiteren Zug machen
			else if (bGameRunning && !bAnimationInProgress && !bStartPhase && !bShowAskType)
			{
			    //UsersTurn?
				if (bUsersTurn)
				    mmcLogik.UsersTurn(Cards);
				//Dann der Zug des Computers
				else
				    mmcLogik.ComputersTurn(Cards);
			}
	}
	
    private String isThereAWinner() 
    {   
        String sWinner = null;
        
        if (Cards.getAnzahlKartenSpieler() < 1)
        {
            //Spieler hat gewonnen
            sWinner = new String("Spieler");
        }
        else if (Cards.getAnzahlKartenKI() < 1)
        {
            //KI hat gewonnen
            sWinner = new String("KI");
        }
        return sWinner;
    }

    private void spielerKartenNeuPositionieren() 
    {
       MauMauCard mmc = Cards.rePositioniereSpielerKarten(SpielerKartenPosition, nCardDistance);
       if (mmc != null)
       {
           bAnimationInProgress = true;
           MMCinAnimation = mmc;
       }
       else //Fertig
       {
           bRePositioniereSpielerKarten = false;
       }
    }

    private void KIKartenNeuPositionieren() 
    {
       MauMauCard mmc = Cards.rePositioniereKIKarten(KIKartenPosition, nCardDistance);
       if (mmc != null)
       {
           bAnimationInProgress = true;
           MMCinAnimation = mmc;
       }
       else //Fertig
       {
           bRePositioniereComputerKarten = false;
       }
    }
    
    private void KartenZumSpielstartVerteilen()
	{
	    //Verteile Karten
        //6 Karten für den Spieler
        if(Cards.getAnzahlKartenSpieler() < 6)
        {
            moveCardfromStapelToSpieler();
        }
        //6 Karten für KI
        else if (Cards.getAnzahlKartenKI() < 6)
        {
            moveCardfromStapelToKI();
        }
        else if (Cards.getAnzahlKartenAufFriedhof() < 1)
        {
            moveCardfromStapelToFriedhof();
        }
        else
        {
            bStartPhase = false;
            //Zufälliger Spielbeginner
            Random r = new Random();
            int num = Math.abs(r.nextInt()) % 2;
            if (num == 1)
            {
                bUsersTurn = false;
                mmmWindow.showMessage("Computer beginnt!");
            }
            else
            {
                bUsersTurn = true;
                mmmWindow.showMessage("Spieler beginnt!");
            }
        }
	}
	
	private void checkButtonAktivitäten()
	{
	    int IMouseX = pCP.getIMousePosX();
		int IMouseY = pCP.getIMousePosY();
		//Mouse over a menue Object? YES? Let Change View
		
		//reset Buttons
		bMouseOverButton1 = false;
		bMouseOverButton2 = false;
		bMouseOverButtonYes = false;
		bMouseOverButtonNo = false;
		bMouseOverButtonAnleitung = false;
		bMouseOverButtonSchliessen = false;
		bMouseOverButtonHerz = false;
		bMouseOverButtonKaro = false;
		bMouseOverButtonKreuz = false;
		bMouseOverButtonPeek = false;
		
		if (!bShowAufgeben && !bShowAnleitung && !bShowAskType)
		{
			//Over Button 1?
			if(IMouseX > 17 && IMouseX < 163 && IMouseY > 414 && IMouseY < 441)
			{
			    bMouseOverButton1 = true;
			    Button1[1] = Color.yellow;
			}
			//Over Button 2?
			else if(IMouseX > 17 && IMouseX < 144 && IMouseY > 447 && IMouseY < 475)
			{
			    bMouseOverButton2 = true;
			    Button2[1] = Color.yellow;
			}
			//Over Button Anleitung?
			else if(IMouseX > 17 && IMouseX < 144 && IMouseY > 483 && IMouseY < 511)
			{
			    bMouseOverButtonAnleitung = true;
			    ButtonAnleitung[1] = Color.yellow;
			}
		}
		else if (bShowAufgeben)//Aufgeben Abfrage Aktiv, Buttons überprüfen
		{
		    //Zurücksetzen
		    ButtonYes[1] = Color.black;
		    ButtonNo[1] = Color.black;
		    //Over Button Yes?
			if(IMouseX > 278 && IMouseX < 384 && IMouseY > 330 && IMouseY < 357)
			{
			    bMouseOverButtonYes = true;
			    ButtonYes[1] = Color.yellow;
			}
			//Over Button No?
			else if(IMouseX > 412 && IMouseX < 517 && IMouseY > 330 && IMouseY < 357)
			{
			    bMouseOverButtonNo = true;
			    ButtonNo[1] = Color.yellow;
			}
		}
		else if (bShowAnleitung)
		{
		    //Zurücksetzen
		    ButtonSchliessen[1] = Color.black;
		    //Over Button Schliessen?
			if(IMouseX > 554 && IMouseX < 665 && IMouseY > 445 && IMouseY < 474)
			{
			    bMouseOverButtonSchliessen = true;
			    ButtonSchliessen[1] = Color.yellow;
			}
			
		}
		else if (bShowAskType)
		{
		    //Zurücksetzen
		    ButtonHerz[1] = Color.black;
		    ButtonKaro[1] = Color.black;
		    ButtonPeek[1] = Color.black;
		    ButtonKreuz[1] = Color.black;
		    //Over a Button?
			if(IMouseX > 557 && IMouseX < 667 && IMouseY > 281 && IMouseY < 312)
			{
			    bMouseOverButtonHerz = true;
			    ButtonHerz[1] = Color.yellow;
			}
			if(IMouseX > 557 && IMouseX < 667 && IMouseY > 321 && IMouseY < 351)
			{
			    bMouseOverButtonKaro = true;
			    ButtonKaro[1] = Color.yellow;
			}
			if(IMouseX > 557 && IMouseX < 667 && IMouseY > 201 && IMouseY < 230)
			{
			    bMouseOverButtonPeek = true;
			    ButtonPeek[1] = Color.yellow;
			}
			if(IMouseX > 557 && IMouseX < 667 && IMouseY > 241 && IMouseY < 270)
			{
			    bMouseOverButtonKreuz = true;
			    ButtonKreuz[1] = Color.yellow;
			}
		}
		    
		//Mousepressed?
		if (pCP.isBMousePressed())
		{
		    
			if (bMouseOverButton1)
			{
			    buttonPressed(Button1);
			}
			if (bMouseOverButton2)
			{
			    buttonPressed(Button2);
			}
			if (bMouseOverButtonNo)
			{
			    buttonPressed(ButtonNo);
			}
			if (bMouseOverButtonYes)
			{
			    buttonPressed(ButtonYes);
			}
			if (bMouseOverButtonAnleitung)
			{
			    buttonPressed(ButtonAnleitung);
			}
			if (bMouseOverButtonSchliessen)
			{
			    buttonPressed(ButtonSchliessen);
			}
			if (bMouseOverButtonKaro)
			{
			    buttonPressed(ButtonKaro);
			}
			if (bMouseOverButtonHerz)
			{
			    buttonPressed(ButtonHerz);
			}
			if (bMouseOverButtonPeek)
			{
			    buttonPressed(ButtonPeek);
			}
			if (bMouseOverButtonKreuz)
			{
			    buttonPressed(ButtonKreuz);
			}
		}
	}
	
	private void CardAnimations()
	{
	    Point poAktPos = MMCinAnimation.getPoAktuellePosition();
	    Point poEndPos = MMCinAnimation.getPoEndPosition();
	    if (poAktPos.equals(poEndPos))
	    {
	        bAnimationInProgress = false;
	        if (bMoveSpielerCardToFriedhofAfterAnimation)
	        {
	            Cards.removeSpielerKarte(MMCinAnimation);
	            Cards.addCardToFriedhof(MMCinAnimation);
	            bMoveSpielerCardToFriedhofAfterAnimation = false;
	        }
	        else if (bMoveKICardToFriedhofAfterAnimation)
	        {
	            Cards.removeKIKarte(MMCinAnimation);
	            Cards.addCardToFriedhof(MMCinAnimation);
	            bMoveKICardToFriedhofAfterAnimation = false;
	        }
	        
	        MMCinAnimation = null;
	    }
	    else
	    {
	        if (poAktPos.x < poEndPos.x)
		    {
	            if ((poEndPos.x - poAktPos.x) >= 8)
	            {
	                poAktPos.x += 8;
	            }
	            else
	            {
	                poAktPos.x += 1;
	            }
	           
		    }
	        else if(poAktPos.x > poEndPos.x)
	        {
	            if ((poAktPos.x - poEndPos.x) >= 8)
	            {
	                poAktPos.x -= 8;
	            }
	            else
	            {
	                poAktPos.x -= 1;
	            }
	        }
	        if (poAktPos.y < poEndPos.y)
		    {
	            if ((poEndPos.y - poAktPos.y) >= 8)
	            {
	                poAktPos.y += 8;
	            }
	            else
	            {
	                poAktPos.y += 1;
	            }
		    }
	        else if(poAktPos.y > poEndPos.y)
	        {
	            if ((poAktPos.y - poEndPos.y) >= 8)
	            {
	                poAktPos.y -= 8;
	            }
	            else
	            {
	                poAktPos.y -= 1;
	            }
	        }
	        MMCinAnimation.setPoAktuellePosition(poAktPos);
	    }
	}
	
    protected void moveCardfromStapelToFriedhof() 
    {
        MauMauCard mmc = Cards.getCardFromStapel();
        mmc.setPoAktuellePosition(new Point(KartenStapelPosition));
        mmc.setPoEndPosition(new Point(KartenFriedhofPosition));
        bAnimationInProgress = true;
        MMCinAnimation = mmc;
        Cards.addCardToFriedhof(mmc);
    }
 
    protected void moveCardfromStapelToKI() 
    {
        MauMauCard mmc = Cards.getCardFromStapel();
        int nCardAnzahl = Cards.getAnzahlKartenKI();
        mmc.setBDrawFrontSide(false);
        mmc.setPoAktuellePosition(new Point(KartenStapelPosition));
        Point poEnd = new Point();
        poEnd.x = KIKartenPosition.x + (nCardAnzahl * nCardDistance);
        poEnd.y = KIKartenPosition.y;
        mmc.setPoEndPosition(poEnd);
        bAnimationInProgress = true;
        MMCinAnimation = mmc;
        Cards.addCardToStapelKI(mmc);
    }

    protected void moveCardfromStapelToSpieler()
    {
       MauMauCard mmc = Cards.getCardFromStapel();
       mmc.setPoAktuellePosition(new Point(KartenStapelPosition));
       int nCardAnzahl = Cards.getAnzahlKartenSpieler();
       Point poEnd = new Point();
       poEnd.x = SpielerKartenPosition.x + (nCardAnzahl * nCardDistance);
       poEnd.y = SpielerKartenPosition.y;
       mmc.setPoEndPosition(poEnd);
       MMCinAnimation = mmc;
       bAnimationInProgress = true;
       Cards.addCardToStapelSpieler(mmc);
       
    }
    
    protected boolean isKartenstapelClicked(Point p)
    {
        boolean b = false;
        Rectangle r = new Rectangle(KartenStapelPosition.x, KartenStapelPosition.y, Cards.getCDBack().getWidth(),Cards.getCDBack().getHeight());
	    Rectangle2D r2D = (Rectangle2D) r;
	    if (r2D.contains(p.x, p.y))
	    {
	        b = true;
	    }
        return b;
    }

    protected Point isMousePressed()
    {
        Point p = null;
        if (pCP.isBMousePressed())
        {
            p = new Point(pCP.getIMousePosX(), pCP.getIMousePosY());
        }
        return p;
    }
    public boolean Initialised() 
    {
        return bInitilised;
    }
}
