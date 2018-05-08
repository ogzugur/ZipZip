package States;

import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import Boxes.box_Coin;
import Boxes.box_Redirection;
import Boxes.box_Spike;
import Boxes.box_Surprise;
import ogz.Ammo;
import ogz.Box;
import ogz.Cloud;
import ogz.Main;
import ogz.Player;
import ogz.Portal;

public class Gameplay extends BasicGameState {
	private boolean close;
	TrueTypeFont font;
	TrueTypeFont font2;
	private Image background;
	private Image background_2;
	private Image filled_heart;
	private Image empty_heart;
	private Image press_to_jump;
	private Image winner;
	private SpriteSheet sp_starting_counter;
	private Animation anim_starting_counter;
	private Sound gameplay_music;
	private Sound laser_hit_sound;
	private Sound jump_sound;
	private Sound hit_sound;
	private Sound winner_sound;
	private boolean sound_played = false;
	private boolean game_started = false;
	private int red = 0;
	private int coin = 0;
	private int spike = 0;
	private int surp = 0;
	private int xSize;
	private int ySize;
	private int cell_x;
	private int cell_y;
	private int cloud_counter = 4000;
	private int starting_counter = 4000;
	private int box_counter_time = 3000;
	private int box_counter = box_counter_time;
	int[][] cell = new int[4][4];
	Random rand = new Random();
	private int time_between_rounds = 3000;
	public static final ArrayList<Cloud> clouds = new ArrayList<Cloud>();
	public static final ArrayList<Box> boxes = new ArrayList<Box>();
	public static final ArrayList<Portal> portals = new ArrayList<Portal>();

	public Gameplay(int xSize, int ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// load font from a .ttf file
		try {
			InputStream inputStream = ResourceLoader.getResourceAsStream("res/fonts/zýp.ttf");

			// counter font
			Font awtFont1 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont1 = awtFont1.deriveFont(48.0f); // set font size
			font = new TrueTypeFont(awtFont1, true);
			awtFont1 = awtFont1.deriveFont(12.0f);
			font2 = new TrueTypeFont(awtFont1, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		winner = new Image("res/images/winner.png", false, Image.FILTER_NEAREST);
		sp_starting_counter = new SpriteSheet("res/images/timer.png", 64, 64);
		anim_starting_counter = new Animation(sp_starting_counter, 1000);
		press_to_jump = new Image("res/images/press_jump.png", false, Image.FILTER_NEAREST);
		filled_heart = new Image("res/images/filled_heart.png", false, Image.FILTER_NEAREST);
		empty_heart = new Image("res/images/empty_heart.png", false, Image.FILTER_NEAREST);
		background = new Image("res/images/bg_1.png", false, Image.FILTER_NEAREST);
		background_2 = new Image("res/images/bg_2.png", false, Image.FILTER_NEAREST);
		gameplay_music = new Sound("res/sounds/gameplay_theme.ogg");
		jump_sound = new Sound("res/sounds/jump.wav");
		laser_hit_sound = new Sound("res/sounds/laser_hit.wav");
		hit_sound = new Sound("res/sounds/hit.wav");
		winner_sound = new Sound("res/sounds/winner_sound.wav");
		close = false;
		for (Player player : Main.players) {
			do {
				cell_x = rand.nextInt(4);
				cell_y = rand.nextInt(4);
			} while (cell[cell_x][cell_y] == 1);
			cell[cell_x][cell_y] = 0;
			initialize_starting_positions(player);
		}

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		int prob_counter;
		if (close) {
			gc.exit();
		}
		if (!gameplay_music.playing()) {
			gameplay_music.loop(1.0f, 0.4f); // play gameplay theme
		}

		if (alive_players() != 1) {
			if (starting_counter >= 1000)
				starting_counter -= delta;
			if (starting_counter <= 1000)
				game_started = true;
			if (game_started) {
				int box_probability = rand.nextInt(100);
				if (box_counter > 0)
					box_counter -= delta;
				if (box_counter <= 0) {
					prob_counter = 0;
					box_counter = box_counter_time;

					if (box_probability >= prob_counter && box_probability < prob_counter + box_Coin.probability) {
						boxes.add(new box_Coin());
						coin++;
					}

					prob_counter += box_Coin.probability;
					if (box_probability >= prob_counter && box_probability < prob_counter + box_Spike.probability) {
						boxes.add(new box_Spike());
						spike++;
					}

					prob_counter += box_Spike.probability;
					if (box_probability >= prob_counter && box_probability < prob_counter + box_Surprise.probability) {
						boxes.add(new box_Surprise());
						surp++;
					}

					prob_counter += box_Surprise.probability;
					if (box_probability >= prob_counter
							&& box_probability < prob_counter + box_Redirection.probability) {
						boxes.add(new box_Redirection());
						red++;
					}

					prob_counter += box_Redirection.probability;
				}
				if (cloud_counter > 0) {
					cloud_counter -= delta;
				} else if (cloud_counter < 0) {
					cloud_counter = 4000;
					clouds.add(new Cloud());
				}
				update_clouds(delta);
				update_ammos(delta);
				update_players(delta);
				update_boxes(delta);
				checkIntersections();
				core_player_mechanics(delta);
			}
		} else {
			if (!sound_played) {
				portals.clear();
				boxes.clear();
				Main.ammo.clear();
				winner_sound.play(1.0f, 0.22f);
				sound_played = true;
			}
			if (time_between_rounds >= 1000)
				time_between_rounds -= delta;
			else if (time_between_rounds <= 1000)
				for (Player player : Main.players) {
					set_next_round(player);
				}
		}
	}

	private void update_clouds(int delta) {
		for (Cloud cloud : clouds) {
			cloud.move(delta);
		}

	}

	private void update_ammos(int delta) {
		for (Ammo ammo : Main.ammo) {
			ammo.move(delta);
		}

	}

	private void update_boxes(int delta) {
		for (Box box : boxes) {
			if (box.isActive) {
				box.move(delta);
				if (box instanceof box_Redirection) {
					((box_Redirection) box).update_boo(delta);
				}
			}
		}
		for (Portal portal : portals) {
			if (portal.life_time > 0)
				portal.life_time -= delta;
			else if (portal.life_time < 0) {
				portal.isActive = false;
			}

		}
	}

	private void update_players(int delta) {
		for (Player player : Main.players) {
			player.update_jump_timer(delta);
		}
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		background.draw(0, 0);
		draw_boxes(g);
		draw_clouds(g);
		background_2.draw(0, 0);
		draw_ammo(g);
		// draw_game_boundaries(g);
		if (!game_started) {
			anim_starting_counter.draw(xSize / 2 - 32, ySize / 2 - 32);
		}
		if (alive_players() != 1) {
			for (Player player : Main.players) {
				if (!player.is_dead) {
					player.draw(g);
					/* DEBUG RECTANGLES */
					// draw_debug_rectangles(g);
					/* DEBUG RECTANGLES */
					if (player.onGround) {
						int i = 0;
						for (i = 0; i < player.health; i++) {
							filled_heart.draw((player.x - 0) + i * 14, player.y - 30);
						}
						for (int j = 0; j < 3 - player.health; j++, i++) {
							empty_heart.draw((player.x - 0) + i * 14, player.y - 30);
						}

						player.display_death_timer(g, font2);
					}
					if (player.readyToRelaunch) {
						press_to_jump.draw(player.x - 6, player.y - 20, 1.2f);

					}
				}
			}
		} else {
			for (Player player : Main.players) {
				if (!player.is_dead) {
					player.image.draw(player.x, player.y, 48, 48);
					winner.draw(player.x - 6, player.y - 12);
				}
			}
		}
	}

	private void draw_clouds(Graphics g) {
		for (Cloud cloud : clouds) {
			cloud.draw();
		}

	}

	public void draw_ammo(Graphics g) {
		for (Ammo ammo : Main.ammo) {
			ammo.draw(g);
		}
	}

	private void draw_boxes(Graphics g) {
		for (Box box : boxes) {
			box.draw_box(g);
		}
		for (Portal portal : portals) {
			portal.draw(g);
		}
	}

	public void draw_debug_rectangles(Graphics g) {
		for (Player player : Main.players) {
			g.drawRect(player.intersection_rectangle.x, player.intersection_rectangle.y,
					player.intersection_rectangle.width, player.intersection_rectangle.height);
		}
		for (Box box : boxes) {
			g.drawRect(box.intersection_rectangle.x, box.intersection_rectangle.y, box.intersection_rectangle.width,
					box.intersection_rectangle.height);
		}

	}

	public void set_next_round(Player player) {
		sound_played = false;
		game_started = false;
		time_between_rounds = 3000;
		starting_counter = 4000;
		player.init_starting_attributes();
		do {
			cell_x = rand.nextInt(4);
			cell_y = rand.nextInt(4);
		} while (cell[cell_x][cell_y] == 1);
		cell[cell_x][cell_y] = 0;
		initialize_starting_positions(player);
	}

	private int alive_players() {
		int count = 0;
		for (Player player : Main.players) {
			if (!player.is_dead)
				count++;
		}
		return count;
	}

	public void core_player_mechanics(int delta) {
		for (Player player : Main.players) {
			if (!player.is_dead) {
				if (player.is_playing)
					player.move(delta);// regular moving along x axis
				else if (player.is_falling) {// fall until hit the
												// ground
					if (player.y + player.intersection_rectangle.height < 9 * ySize / 10)
						player.move(delta);
					else { // when hit the ground, assign player's
							// status ground
						player.onGround = true;
						player.is_falling = false;
					}
				} else if (player.onGround) {
					if (player.death_timer <= 1000) {
						player.readyToRelaunch = true;
						player.onGround = false;
					}
					player.death_timer = player.death_timer - delta;
				}
			}
		}
	}

	public void checkIntersections() {
		for (Player player : Main.players) {
			// PLAYER-WALL INTERSECTIONS
			if (player.is_playing) {
				if ((player.y < ySize / 10) || (player.y + player.intersection_rectangle.height > 9 * ySize / 10)) {
					laser_hit_sound.play(1.0f, 0.5f);
					player.hitWall();
				}
			}
			// PLAYER-PORTAL INTERSECTIONS
			for (Portal portal : portals) {
				if (portal.isActive) {
					if (player.intersection_rectangle.intersects(portal.blue_intersection_rectangle)) {
						portal.play_sound();
						portal.apply_effect(player, 1);
					} else if (player.intersection_rectangle.intersects(portal.orange_intersection_rectangle)) {
						portal.play_sound();
						portal.apply_effect(player, 2);
					}
				}

			}
			if (player.is_playing) {
				for (Ammo ammo : Main.ammo) {
					if (player.is_playing)
						if (player.intersection_rectangle.intersects(ammo.intersection_rectangle)) {
							hit_sound.play();
							player.hitWall();
						}

				}
			}

			// PLAYER-BOX INTERSECTIONS
			for (Box box : boxes) {
				if (player.binding_id != -1 && (boxes.size() > player.binding_id)) {
					if (!player.intersection_rectangle
							.intersects(boxes.get(player.binding_id).intersection_rectangle)) {
						player.binding_id = -1;
					}
				}
				if (player.intersection_rectangle.intersects(box.intersection_rectangle)
						&& !bind(Main.players.indexOf(player), boxes.indexOf(box))) {
					player.binding_id = boxes.indexOf(box);
					if (box.isActive && player.is_playing) {
						if (box instanceof box_Redirection) {
							box_Redirection redirection_box = (box_Redirection) box;
							redirection_box.apply_effect(player);
						} else if (box instanceof box_Coin) {
							box_Coin coin_box = (box_Coin) box;
							coin_box.apply_effect(player);
						} else if (box instanceof box_Spike) {
							box_Spike spike_box = (box_Spike) box;
							hit_sound.play();
							spike_box.apply_effect(player);
						} else if (box instanceof box_Surprise) {
							box_Surprise surprise_box = (box_Surprise) box;
							surprise_box.apply_effect(player);

						}
					}
				}
			}
			// PLAYER-PLAYER INTERSECTIONS
			if (player.is_playing) {
				for (Player player_other : Main.players) {
					if (player.intersection_rectangle.intersects(player_other.intersection_rectangle)
							&& player.id != player_other.id && player_other.is_playing) {
						if (player.y - player_other.y >= player.intersection_rectangle.height / 2) {
							hit_sound.play(1.0f, 0.5f);
							player_other.mini_jump();
							player.hitWall();
						}
					}
				}
			}
		}
	}

	private boolean bind(int indexOfplayer, int indexOfbox) {
		if (Main.players.get(indexOfplayer).binding_id == indexOfbox)
			return true;
		else
			return false;
	}

	public void keyPressed(int key, char c) {
		if (find_button_owner(key) != 0 && alive_players() != 1 && game_started) {
			if (Main.players.get(find_button_owner(key) - 1).is_playing) {
				jump_sound.play(0.6f, 0.3f);
				try {
					Main.players.get(find_button_owner(key) - 1).jump();
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(Main.players.get(0).JUMP_SPEED);
			} else if (Main.players.get(find_button_owner(key) - 1).readyToRelaunch) {
				Main.players.get(find_button_owner(key) - 1).y = 9 * ySize / 10
						- Main.players.get(find_button_owner(key) - 1).intersection_rectangle.height - 10;
				Main.players.get(find_button_owner(key) - 1).set_playing_attributes();
				try {
					Main.players.get(find_button_owner(key) - 1).jump();
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (key == Input.KEY_ENTER) {
			System.out.println("coin " + coin);
			System.out.println("re " + red);
			System.out.println("surp " + surp);
			System.out.println("spike" + spike);
		}
		if (key == Input.KEY_ESCAPE) {
			close = true;
		}
		if (key == Input.KEY_F11) {
			Main.fullscreen = !Main.fullscreen;
		}
	}

	private int find_button_owner(int key) {
		for (Player player : Main.players) {
			if (player.button == key)
				return player.id;
		}
		return 0;
	}

	private void initialize_starting_positions(Player player) {
		player.x = rand.nextInt((xSize / 10) + ((cell_x + 1) * 220) - (xSize / 10 + cell_x * 240)) + xSize / 10
				+ cell_x * 240;
		player.y = rand.nextInt((ySize / 10) + ((cell_y + 1) * 120) - (ySize / 10 + cell_y * 130)) + ySize / 10
				+ cell_y * 130;
		player.set_intersection_rectangle();
	}

	public int getID() {
		return 2;
	}
}