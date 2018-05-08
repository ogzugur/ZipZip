package States;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import ogz.Main;
import ogz.Player;

public class PickingScreen extends BasicGameState {

	private Image locked_frame;
	private SpriteSheet sp_active_frame;
	private Animation anim_active_frame;
	private Image pick_bg;
	private int x = 100;
	private int y = 294;
	private int index = 0;
	private int id = 1;
	private int character_number = 6;
	private int player_number = character_number - 1;
	private Input input;
	private boolean close;

	public void compute_pos(int index) {
		x = (index % player_number) * 128 + (index % player_number + 1) * 100;
		y = (index / player_number * 128) + ((index / player_number) + 1) * 294;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		locked_frame = new Image("res/images/locked_frame.png", false, Image.FILTER_NEAREST);
		sp_active_frame = new SpriteSheet("res/images/sp_active_frame.png", 128, 128);
		anim_active_frame = new Animation(sp_active_frame, 75);
		pick_bg = new Image("res/images/pick_bg.png", false, Image.FILTER_NEAREST);
		close = false;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

		pick_bg.draw();
		for (Player player : Main.players) {
			draw_player_avatars(player, g);
		}
		if (id > 1)
			compute_pos(index + 1);

		if (id < character_number) {
			anim_active_frame.draw(x, y);
		}

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		input = gc.getInput();
		if (close) {
			gc.exit();
		}
		if (input.isKeyPressed(Input.KEY_ENTER) && Main.players.size() > 1) {
			input.clearKeyPressedRecord();
			sbg.getState(1).leave(gc, sbg);
			sbg.getState(2).init(gc, sbg);
			sbg.enterState(2, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
			SplashScreen.splash_theme.stop();
		}
	}

	private void draw_player_avatars(Player player, Graphics g) {
		index = player.id - 1;
		compute_pos(index);
		locked_frame.draw(x, y);
		player.image.draw(x + 32, y + 32, 64, 64);
	}

	@Override
	public void keyPressed(int key, char c) {
		if ((key >= 0) && (key <= 120) && (key != Input.KEY_ENTER) && (key != Input.KEY_ESCAPE)
				&& (key != Input.KEY_F11)) {
			if (!is_key_assigned(key) && (id < character_number)) {
				try {
					Main.players.add(new Player(id, key, find_avaliable_avatar_index(0)));
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				id++;
			} else {
				int id = find_button_owner(key);
				Main.players.get(id)
						.set_avatar_index(find_avaliable_avatar_index(Main.players.get(id).get_avatar_index() + 1));
			}
		}
		if (key == Input.KEY_ESCAPE) {
			close = true;
		}
		if (key == Input.KEY_F11) {
			Main.fullscreen = !Main.fullscreen;
		}
	}

	private int find_avaliable_avatar_index(int index) {
		int i;
		int counter = 0;
		for (i = index % character_number; i < character_number; i = (i + 1) % character_number) {
			boolean avaliable = true;
			counter = counter + 1;
			for (Player player : Main.players) {
				if (player.get_avatar_index() == i)
					avaliable = false;
			}
			if (avaliable || counter >= character_number)
				return i;
		}
		return 0;

	}

	private int find_button_owner(int key) {
		for (Player player : Main.players) {
			if (player.button == key)
				return player.id - 1;
		}
		return 0;
	}

	private boolean is_key_assigned(int key) {
		for (Player player : Main.players) {
			if (player.button == key)
				return true;
		}
		return false;
	}

	@Override
	public void keyReleased(int key, char c) {

	}

	public int getID() {
		return 1;
	}

}
