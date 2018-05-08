package Boxes;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import ogz.Box;
import ogz.Player;

public class box_Spike extends Box {
	public static int probability = 28;
	public SpriteSheet sp_Spike;
	public Animation anim_Spike;
	public box_Spike() {
		super();
		isActive = true;
		try {
			sp_Spike = new SpriteSheet("res/images/Spike.png", 48, 48);
			anim_Spike = new Animation(sp_Spike, 500);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void draw_box(Graphics g){
		if (isActive)
			anim_Spike.draw(x, y, intersection_rectangle.width, intersection_rectangle.height);
	}
	
	public void apply_effect(Player player) {
		player.hitWall();
	}
}
