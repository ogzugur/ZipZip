package ogz;

import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Birds {
	Random rand = new Random();
	public Image image;
	public float ySpeed;
	public float JUMP_SPEED;
	public float GRAVITY;
	public float speed;
	public int direction;
	public float x, y;
	public boolean jumped;
	public int avatar_index;

	public Birds() throws SlickException {
		x = (float) rand.nextInt(880) + 200;
		y = 720.0f;
		this.avatar_index = rand.nextInt(6);
		image = new Image("res/images/bird_" + this.avatar_index + ".png", false, Image.FILTER_NEAREST);
		GRAVITY = 0.20f;
		JUMP_SPEED = 10.0f;
		direction = rand.nextInt(2) * 2 - 1;
		// ySpeed = 0.5f;
		speed = 0.20f;
		jumped = false;
	}

	public void jump(int delta) throws SlickException {
		if (!jumped)
			ySpeed = -JUMP_SPEED;
		jumped = true;
		move(delta);
	}

	public void draw(Graphics g) {
		if (direction == 1)
			image.draw(x, y, 60, 60);
		else if (direction == -1)
			image.getFlippedCopy(true, false).draw(x, y, 60, 60);
	}

	public void fall() {
		if (ySpeed < 20)
			ySpeed += GRAVITY;
		y = y + ySpeed;
	}

	public void move(int delta) {
		fall();
		x += delta * speed * direction;
	}

	public void init_starting_attributes() {

	}

}
