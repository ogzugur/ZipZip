package ogz;

import java.awt.Rectangle;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;

public class Player {
	Random rand = new Random();
	public Weapon weapon;
	private int coins = 0;
	public int binding_id = -1;
	public boolean is_playing;
	public boolean is_falling;
	public boolean onGround;
	public boolean is_dead;
	public boolean readyToRelaunch;
	public int death_timer;
	public int jump_timer;
	public int time_between_jump = 300;
	public float JUMP_SPEED;
	public float GRAVITY;
	public int direction = 1;
	public float speed;
	public float ySpeed;
	public int health;
	public int button;
	public float x, y;
	private int avatar_index;
	public SpriteSheet sp_death_timer;
	public Animation anim_death_timer;
	public Image image;
	public int id;
	public Rectangle intersection_rectangle = new Rectangle(0, 0, 48, 48);

	public Player(int id, int button, int avatar_index) throws SlickException {

		sp_death_timer = new SpriteSheet("res/images/d_timer.png", 16, 16);
		anim_death_timer = new Animation(sp_death_timer, 1000);
		weapon = new Weapon();
		weapon.setActive(false);
		init_starting_attributes();
		// if(id!=1){
		// GRAVITY=0.0f;
		// speed = 0.0f;
		// ySpeed = 0.0f;
		// }
		this.id = id;
		this.button = button;
		this.avatar_index = avatar_index;
		try {
			this.image = new Image("res/images/bird_" + this.avatar_index + ".png", false, Image.FILTER_NEAREST);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		;
	}

	public void draw(Graphics g) {
		if (direction == 1)
			image.draw(x, y, intersection_rectangle.height, intersection_rectangle.width);
		else if (direction == -1)
			image.getFlippedCopy(true, false).draw(x, y, intersection_rectangle.height, intersection_rectangle.width);
		weapon.draw_weapon(g, x, y);

	}

	public void update_jump_timer(int delta) {
		if (jump_timer < time_between_jump)
			jump_timer += delta;
		else if (jump_timer >= time_between_jump)
			JUMP_SPEED = 10.0f;
	}

	public void jump() throws SlickException {
		if (weapon.isActive()) {
			weapon.fire(this);
		}
		if (jump_timer <= time_between_jump) {
			JUMP_SPEED += 3.0f;
			jump_timer = 0;
		}

		else if (jump_timer > time_between_jump)
			jump_timer = time_between_jump;
		ySpeed = -JUMP_SPEED;
	}

	public void mini_jump() {
		ySpeed = -5.0f;
	}

	public void fall() {
		if (ySpeed < 20)
			ySpeed += GRAVITY;
		y = y + ySpeed;
	}

	public void move(int delta) {
		fall();
		if (x + intersection_rectangle.width >= 9 * Main.SCREEN_WIDTH / 10) {
			x = 9 * Main.SCREEN_WIDTH / 10 - intersection_rectangle.width;
			direction = -direction;
		}

		else if (x <= Main.SCREEN_WIDTH / 10) {
			x = Main.SCREEN_WIDTH / 10;
			direction = -direction;
		}
		x += delta * speed * direction;
		set_intersection_rectangle();
	}

	public void set_avatar_index(int avatar_index) {
		this.avatar_index = avatar_index;
		try {
			this.image = new Image("res/images/bird_" + this.avatar_index + ".png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public int get_avatar_index() {
		return avatar_index;
	}

	public void set_intersection_rectangle() {
		intersection_rectangle.x = (int) x;
		intersection_rectangle.y = (int) y;
	}

	public void hitWall() {
		if (health != 1) {
			health--;
			set_falling_attributes();
			// if(id!=1){
			// GRAVITY = 0.5f;
			// ySpeed = 0.5f;
			// }
		} else {
			is_dead = true;
			is_playing = false;
		}
	}

	public void set_falling_attributes() {
		ySpeed = 0.5f;
		speed = 0;
		is_falling = true;
		is_playing = false;
	}

	public void set_playing_attributes() {
		speed = 0.3f;
		readyToRelaunch = false;
		is_dead = false;
		is_playing = true;
		is_falling = false;
		onGround = false;
		death_timer = 4000;
		anim_death_timer = new Animation(sp_death_timer, 1000);
	}

	public void init_starting_attributes() {
		jump_timer = time_between_jump;
		direction = rand.nextInt(2) * 2 - 1;
		GRAVITY = 0.60f;
		JUMP_SPEED = 10.0f;
		ySpeed = 0.5f;
		speed = 0.3f;
		death_timer = 4000;
		anim_death_timer = new Animation(sp_death_timer, 1000);
		health = 3;
		readyToRelaunch = false;
		onGround = false;
		is_dead = false;
		is_falling = false;
		is_playing = true;
	}

	public void display_death_timer(Graphics g, TrueTypeFont font) {
		anim_death_timer.draw((2*x+48) / 2 - 8, y - 20);
	}

	public int getCoins() {
		return coins;
	}

	public void addCoins() {
		coins++;
		if (coins % 3 == 0 && health < 3)
			health++;
	}

}
