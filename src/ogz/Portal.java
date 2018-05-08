package ogz;

import java.awt.Rectangle;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;

public class Portal {
	Random rand = new Random();
	public boolean isActive;
	public int life_time;
	public SpriteSheet sp_portal_orange;
	public SpriteSheet sp_portal_blue;
	public Animation anim_portal_orange;
	public Animation anim_portal_blue;
	public float blue_x, blue_y, orange_x, orange_y;
	public Rectangle blue_intersection_rectangle = new Rectangle(0, 0, 12, 100);
	public Rectangle orange_intersection_rectangle = new Rectangle(0, 0, 12, 100);
	private Sound portal_sound;

	public Portal() {
		isActive = true;
		life_time = 7000;
		blue_x = rand.nextInt(8 * Main.SCREEN_WIDTH / 10 - 6 * 48) + Main.SCREEN_WIDTH / 10 + 3 * 48;
		blue_y = rand.nextInt(8 * Main.SCREEN_HEIGHT / 10 - 100) + Main.SCREEN_HEIGHT / 10;
		orange_x = rand.nextInt(8 * Main.SCREEN_WIDTH / 10 - 6 * 48) + Main.SCREEN_WIDTH / 10 + 3 * 48;
		orange_y = rand.nextInt(8 * Main.SCREEN_HEIGHT / 10 - 100) + Main.SCREEN_HEIGHT / 10;
		blue_intersection_rectangle.x = (int) blue_x;
		blue_intersection_rectangle.y = (int) blue_y;
		orange_intersection_rectangle.x = (int) orange_x;
		orange_intersection_rectangle.y = (int) orange_y;
		try {
			portal_sound = new Sound("res/sounds/portal_sound.wav");
			sp_portal_orange = new SpriteSheet("res/images/portal_orange.png", 12, 100);
			anim_portal_orange = new Animation(sp_portal_orange, 250);
			sp_portal_blue = new SpriteSheet("res/images/portal_blue.png", 12, 100);
			anim_portal_blue = new Animation(sp_portal_blue, 250);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void play_sound() {
		portal_sound.play(0.8f, 0.2f);
	}

	public void draw(Graphics g) {
		if (isActive) {
			anim_portal_orange.draw(orange_x, orange_y, blue_intersection_rectangle.width,
					blue_intersection_rectangle.height);
			anim_portal_blue.draw(blue_x, blue_y, blue_intersection_rectangle.width,
					blue_intersection_rectangle.height);
		}
	}

	public void apply_effect(Player player, int i) {
		if (i == 1) {
			if (player.direction == 1) {
				player.x = orange_x + blue_intersection_rectangle.width + 1;
				player.y = orange_y + 50;
			} else if (player.direction == -1) {
				player.x = orange_x - player.intersection_rectangle.width - 1;
				player.y = orange_y + 50;
			}
		} else if (i == 2) {
			if (player.direction == 1) {
				player.x = blue_x + blue_intersection_rectangle.width + 1;
				player.y = blue_y + 50;
			} else if (player.direction == -1) {
				player.x = blue_x - player.intersection_rectangle.width - 1;
				player.y = blue_y + 50;
			}
		}

	}

}
