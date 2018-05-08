package ogz;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import States.Gameplay;
import States.PickingScreen;
import States.SplashScreen;

public class Main extends StateBasedGame {
	public static final int splashscreen = 0;
	public static final int pickingscreen = 1;
	public static final int gameplay = 2;
	public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 720;
	public static final ArrayList<Player> players = new ArrayList<Player>();
	public static final ArrayList<Ammo> ammo = new ArrayList<Ammo>();
	public static boolean fullscreen;

	public Main(String title) {
		super("ZýpZýp");
		fullscreen = true;
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		if (container instanceof AppGameContainer) {
			// This is going to work because it's executed after the constructor
			AppGameContainer appContainer = (AppGameContainer) container;

			// can't set icons if game is in full screen
			if (!appContainer.isFullscreen()) {
				String[] icons = { "res/images/icon16.png", "res/images/icon32.png" };
				container.setIcons(icons);
			}
		}
		
		this.addState(new SplashScreen());
		this.addState(new PickingScreen());
		this.addState(new Gameplay(SCREEN_WIDTH, SCREEN_HEIGHT));

	}
	

	public static void main(String[] args) {
		try {
			AppGameContainer app;
			app = new AppGameContainer((Game) new Main("Jump Game"));
			app.setVSync(true);
			app.setTargetFrameRate(60);
			app.setShowFPS(false);
			app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, fullscreen);
			app.start();
		} catch (SlickException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	

}