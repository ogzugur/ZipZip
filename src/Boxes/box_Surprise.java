package Boxes;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import States.Gameplay;
import ogz.Box;
import ogz.Player;
import ogz.Portal;

public class box_Surprise extends Box {
	public static int probability = 34;
	Random rand = new Random();
	public Sound portal_openin_sound;
	public int surprise_chooser;

	public box_Surprise() {
		super();
		isActive = true;
		try {
			portal_openin_sound = new Sound("res/sounds/portal_opening_sound.wav");
			image = new Image("res/images/surprise_box.png", false, Image.FILTER_NEAREST);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void apply_effect(Player player) {
		surprise_chooser = rand.nextInt(100);
		if (surprise_chooser >= 0 && surprise_chooser < 50) {
			portal_openin_sound.play(1.0f, 0.3f);
			Gameplay.portals.add(new Portal());
			isActive = false;
		} else if (surprise_chooser >= 50 && surprise_chooser < 100) {
			player.weapon.setActive(true);
			isActive = false;
		}

	}
}
