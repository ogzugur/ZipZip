package ogz;

import java.awt.Rectangle;
import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Box {
	public enum DIR {
		UP, DOWN
	}

	Random rand = new Random();
	public float x, y;
	public Image image;
	public DIR direction;
	public float speed;
	public boolean isActive;
	public int id;
	public Rectangle intersection_rectangle;

	public Box() {
		intersection_rectangle = new Rectangle(0, 0, 48, 48);
		speed = (float) (rand.nextFloat() / 5 + 0.05);
		x = rand.nextInt((8 * Main.SCREEN_WIDTH - 320) / 10) + Main.SCREEN_WIDTH / 10;
		if (rand.nextInt(2) == 0) {
			y = rand.nextInt(200) * (-1);
			direction = DIR.DOWN;
		} else {
			y = rand.nextInt(100) + Main.SCREEN_HEIGHT;
			direction = DIR.UP;
		}
	}

	public void play_sound() {

	}

	public void draw_box(Graphics g) {
		if (isActive)
			image.draw(x, y, intersection_rectangle.width, intersection_rectangle.height);
	}

	public void set_intersection_rectangle() {
		intersection_rectangle.x = (int) x;
		intersection_rectangle.y = (int) y;
	}

	public void move(int delta) {
		if (direction == DIR.DOWN) {
			y += delta * speed;
		} else if (direction == DIR.UP) {
			y -= delta * speed;
		}
		set_intersection_rectangle();
	}

}
