package ogz;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Cloud {
	private Image img_cloud;
	private int direction;
	private float x, y;
	private float speed;
	private Random rand = new Random();

	public Cloud() throws SlickException {
		img_cloud = new Image("res/images/clouds/cloud_" + rand.nextInt(6) + ".png", false, Image.FILTER_NEAREST);
		speed = (float) (rand.nextFloat() * 0.03 + 0.01);
		if (rand.nextInt(2) == 0) {
			x = -200;
			y = (float) rand.nextInt(80);
			direction = 1;
		} else {
			x = Main.SCREEN_WIDTH + 200;
			y = (float) rand.nextInt(80);
			direction = -1;
		}
		// x = 500;
		// y = 200;
	}

	public void draw() {
		img_cloud.draw(x, y);
	}

	public void move(int delta) {

		if (direction == 1) {
			x += delta * speed;
		} else if (direction == -1) {
			x -= delta * speed;
		}
	}
}
