package ogz;

import java.awt.Rectangle;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Ammo {
	public int direction;
	public Image image;
	public float x;
	public float y;
	public float speed = 0.5f;
	public Rectangle intersection_rectangle;

	public Ammo(Player player) throws SlickException {
		intersection_rectangle = new Rectangle(0, 0, 23, 5);
		if (player.direction == 1) {
			x = player.x + player.intersection_rectangle.width + 1;
		} else if (player.direction == -1) {
			x = player.x - intersection_rectangle.width - 1;
		}
		y = player.y;
		direction = player.direction;
		image = new Image("res/images/ammo.png");
	}

	public void move(int delta) {
		x += delta * speed * direction;
		set_intersection_rectangle();
	}

	public void set_intersection_rectangle() {
		intersection_rectangle.x = (int) x;
		intersection_rectangle.y = (int) y;
	}

	public void draw(Graphics g) {
		image.draw(x, y);
	}
}
