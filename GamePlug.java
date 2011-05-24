package CardGames;
import java.awt.Graphics;

/*
 * Created on 30.12.2005
 */

/**
 * @author lordlormi
 */
public interface GamePlug {
    
    
    public boolean isPlugExiting();
    public void Initialize(CardPanel CP, CardGames CG);
    public void draw(Graphics dbg);
	public void update();
	public boolean Initialised();
	

}
