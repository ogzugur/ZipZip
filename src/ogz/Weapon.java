package ogz;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Weapon {
	public Image image;
	private boolean active;
	public int ammo;

	public Weapon() throws SlickException {
		image = new Image("res/images/threeshooter.png");
	}

	public void draw_weapon(Graphics g, float player_x, float player_y) {
		//image.draw(player_x + 40, player_y + 40);
	}

	public void fire(Player player) throws SlickException {
		if (ammo-- > 0) {
			Main.ammo.add(new Ammo(player));
		}
		

	}

	public void setActive(boolean active) {
		if (active) {
			ammo = 3;
		}
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

}
