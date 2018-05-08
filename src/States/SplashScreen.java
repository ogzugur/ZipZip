package States;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import ogz.Birds;
import ogz.Main;

public class SplashScreen extends BasicGameState {
	private Image img_SplashRed;
	private Image img_SplashText;
	private SpriteSheet sp_SplashPressKey;
	private Animation anim_SplashPressKey;
	public static Sound splash_theme;
	private boolean skip = false;
	private int bird_counter;
	public static final ArrayList<Birds> birds = new ArrayList<Birds>();

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		splash_theme = new Sound("res/sounds/splash_theme.ogg");
		img_SplashRed = new Image("res/images/splash_red.png", false, Image.FILTER_NEAREST);
		img_SplashText = new Image("res/images/splash_text.png", false, Image.FILTER_NEAREST);
		sp_SplashPressKey = new SpriteSheet("res/images/splash_press_key.png", 575, 25);
		anim_SplashPressKey = new Animation(sp_SplashPressKey, 300);
		bird_counter = 500;

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		img_SplashRed.draw();
		draw_birds(g);
		img_SplashText.draw();
		anim_SplashPressKey.draw(355, 380);

	}

	private void draw_birds(Graphics g) {
		for (Birds bird : birds) {
			bird.draw(g);
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		bird_counter -= delta;
		add_bird();
		update_birds(delta);
		if (!splash_theme.playing())
			splash_theme.play();

		if (skip) {
			game.getState(0).leave(container, game);
			game.getState(1).init(container, game);
			game.enterState(1, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		}

	}

	private void add_bird() throws SlickException {
		if (bird_counter < 0) {
			birds.add(new Birds());
			bird_counter = 500;
		}
	}

	private void update_birds(int delta) throws SlickException {
		for (Birds bird : birds) {
			bird.jump(delta);
		}

	}

	public void keyPressed(int key, char c) {
		if ((key >= 0) && (key <= 120) && (key != Input.KEY_F11)) {
			skip = true;
		} else if (key == Input.KEY_F11)
			Main.fullscreen = !Main.fullscreen;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
