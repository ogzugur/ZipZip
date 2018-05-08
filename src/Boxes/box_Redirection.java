package Boxes;

import java.awt.Rectangle;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import ogz.Box;
import ogz.Player;

public class box_Redirection extends Box {
	public static int probability = 28;
	public SpriteSheet sp_Redirect;
	public Image img_boo;
	public int counter_boo;
	public Animation anim_Redirect;
	private boolean boo = false;

	public box_Redirection() throws SlickException {
		super();
		intersection_rectangle = new Rectangle(0, 0, 48, 60);
		isActive = true;
		direction = DIR.UP;
		img_boo = new Image("res/images/boo.png");
		sp_Redirect = new SpriteSheet("res/images/Balonlar.png", 48, 80);
		anim_Redirect = new Animation(sp_Redirect, 500);
	}

	public void draw_box(Graphics g) {
		if (isActive) {
			if (boo) {
				img_boo.draw(x, y, intersection_rectangle.width, 80);
			} else {
				anim_Redirect.draw(x, y, intersection_rectangle.width, 80);
			}
			if (counter_boo > 400)
				boo = false;
		}
	}

	public void update_boo(int delta) {
		counter_boo += delta;
	}

	public void apply_effect(Player player) {
		boo = true;
		counter_boo = 0;
		player.direction = -player.direction;
	}
}
