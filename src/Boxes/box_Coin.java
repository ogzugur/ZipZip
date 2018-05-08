package Boxes;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;

import ogz.Box;
import ogz.Player;

public class box_Coin extends Box {
	public static int probability = 10;
	public SpriteSheet sp_Coin;
	public Animation anim_Coin;
	public Sound PickUpSound;

	public box_Coin() throws SlickException {
		super();
		isActive = true;
		PickUpSound = new Sound("res/sounds/coin.wav");
		sp_Coin = new SpriteSheet("res/images/Coins.png", 16, 16);
		anim_Coin = new Animation(sp_Coin, 100);
	}

	public void play_sound() {
		PickUpSound.play();
	}

	public void draw_box(Graphics g) {
		if (isActive)
			anim_Coin.draw(x, y, intersection_rectangle.width, intersection_rectangle.height);
	}

	public void apply_effect(Player player) {
		play_sound();
		player.addCoins();
		isActive = false;
	}

}
