package objects;

import java.awt.Graphics;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Effect.BuffEffect;
import Map.PhysicalMap;
import entities.Player;

import static untilz.Constants.ObjectConstants.*;
import static untilz.HelpMethods.CanCannonSeePlayer;
import static untilz.Constants.Objecttiles.CANNON_BALL_DAMAGE;
import static untilz.HelpMethods.IsProjectileHittingLevel;  
import gamestates.Playing;
import main.Game;

public class ObjectManager {

	private Playing playing;
	private ArrayList<Potion> potions = new ArrayList<>();
	private ArrayList<GameContainer> containers = new ArrayList<>();
	private ArrayList<Spike> spikes = new ArrayList<>();
	private ArrayList<Cannon> cannons = new ArrayList<>();
	private ArrayList<Projectiles> cannonBalls = new ArrayList<>();
	private ArrayList<Inviroment> torchs = new ArrayList<>();

	public ObjectManager(Playing playing) {
		this.playing = playing;
	}

	public void update(long currTime, int[][] lvlData, Player player) {
		for (Potion p : potions) {
			if (p.isActive())
				p.update(currTime);
		}
		for (GameContainer gc : containers) {
			if (gc.active)
				gc.update(currTime);
		}
		for (Spike s : spikes) {
			if (s.active)
				s.update(currTime);
		}
		for(Inviroment i : torchs) {
			i.update(currTime);
		}
		updateCannons(currTime, lvlData, player);
		updateCannonBalls(lvlData, player);

	}
	
	private void updateCannonBalls(int[][] lvlData, Player player) {
		for (Projectiles p : cannonBalls) {
			if (p.isActive()) {
				p.updatePos();
				if (p.getHitbox().intersects(player.getHitbox())) {
					player.changeHealth(-CANNON_BALL_DAMAGE);
					p.setActive(false);
				}else if(IsProjectileHittingLevel(p, lvlData)) {
					p.setActive(false);
				}
			}
		}
	}

	public void updateCannons(long currTime, int[][] lvlData, Player player) {
		for (Cannon c : cannons) {
			if (!c.doAnimation && c.canShoot())// !doAnimation <==> isLastFrame
				if (c.getTileY() == player.getTileY())
					if (isPlayerInRanger(c, player))
						if (isPlayerInFrontOfCannon(c, player))
							if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY())) {
								c.setAnimation(true);
							}
			c.update(currTime);
			int animTick = (int) (currTime - c.getBeginTime());
			if (c.getCurrFrame() == 5 && animTick == 0) {
				shootCannon(c, currTime);
				c.setShoot(false);
			}

		}

	}

	private void shootCannon(Cannon c, long currTime) {

		int dir = 1;
		if (c.getObjType() == LEFT_CANNON) {
			dir = -1;
		}
		cannonBalls.add(new Projectiles((int) c.getHitbox().x, (int) c.getHitbox().y, dir));

	}

	private boolean isPlayerInFrontOfCannon(Cannon c, Player player) {
		if (c.getObjType() == LEFT_CANNON) {
			if (c.getHitbox().x > player.getHitbox().x) {
				return true;
			}
		} else if (c.getHitbox().x < player.getHitbox().x) {
			return true;
		}
		return false;
	}

	private boolean isPlayerInRanger(Cannon c, Player player) {
		int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
		return absValue <= Game.TILES_SIZE * 10;
	}

	public void render(Graphics g, int xLvlOffset) {
		drawTorchs(g,xLvlOffset);
		drawPotions(g, xLvlOffset);
		drawContainers(g, xLvlOffset);
		drawSpike(g, xLvlOffset);
		drawCannons(g, xLvlOffset);
		drawCannonBalls(g, xLvlOffset);
	}

	private void drawContainers(Graphics g, int xLvlOffset) {
		for (GameContainer gc : containers) {
			if (gc.active)
				gc.render(g, xLvlOffset);
		}
	}

	private void drawPotions(Graphics g, int xLvlOffset) {
		for (Potion p : potions) {
			if (p.active) {
				p.render(g, xLvlOffset);
			}

		}

	}

	private void drawCannons(Graphics g, int xLvlOffset) {
		for (Cannon c : cannons) {
			c.render(g, xLvlOffset);
		}
	}

	private void drawSpike(Graphics g, int xLvlOffset) {
		for (Spike s : spikes) {
			s.render(g, xLvlOffset);
		}
	}

	private void drawCannonBalls(Graphics g, int xLvlOffset) {
		for (Projectiles cb : cannonBalls) {
			if (cb.isActive())
				cb.render(g, xLvlOffset);
		}
	}
	private void drawTorchs(Graphics g, int xLvlOffset) {
		for(Inviroment i : torchs) {
			i.render(g, xLvlOffset);
		}
	}
	public void resetAllObject() {

		loadObject(playing.getLevelManager().getCurrLevel());

		for (Potion p : potions) {
			p.reset();
		}
		for (GameContainer gc : containers) {
			gc.reset();
		}
		for (Spike s : spikes) {
			s.reset();
		}
		for (Cannon c : cannons) {
			c.reset();
		}
	}

	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for (Potion p : potions) {
			if (p.isActive()) {
				if (hitbox.intersects(p.getHitbox())) {
					p.setActive(false);
					BuffEffect.setIsHeal(true);
					applyEffectToPlayer(p);
				}
			}
		}
	}

	public void checkSpikesTouched(Player p) {
		for (Spike s : spikes) {
			if (s.active) {
				if (s.getHitbox().intersects(p.getHitbox())) {
					p.changeHealth(-GetObjectDmg(SPIKE));
					s.active = false;
				}
			}
			if (!s.getHitbox().intersects(p.getHitbox())) {
				s.active = true;
			}

		}
	}

	public void applyEffectToPlayer(Potion p) {
		if (p.getObjType() == HEAL_POTION) {
			playing.getPlayer().changeHealth(HEAL_POTION_VALUE);
		} else {
			playing.getPlayer().changeMana(MANA_POTION_VALUE);
		}
	}

	public void checkObjectHit(Rectangle2D.Float attackBox) {
		for (GameContainer gc : containers) {
			if (gc.isActive() && !gc.doAnimation) {
				if (gc.getHitbox().intersects(attackBox)) {
					gc.setAnimation(true);

					int type = 0;
					if (gc.getObjType() == BARREL)
						type = 1;
					potions.add(new Potion((int) (gc.getHitbox().x),
							(int) (gc.getHitbox().y - gc.getHitbox().width / 3), type));
					return;
				}
			}
		}
	}

	public void loadObject(PhysicalMap physicalMap) {

		loadPotion(physicalMap);
		loadContainer(physicalMap);
		loadSpike(physicalMap);
		loadCannon(physicalMap);
		loadTorch(physicalMap);
		cannonBalls.clear();
	}

	public void loadPotion(PhysicalMap physicalMap) {
		this.potions = new ArrayList<>(physicalMap.getPotions());
	}

	public void loadContainer(PhysicalMap physicalMap) {
		this.containers = new ArrayList<>(physicalMap.getContainers());
	}

	public void loadSpike(PhysicalMap physicalMap) {
		this.spikes = new ArrayList<>(physicalMap.getSpikes());
	}

	public void loadCannon(PhysicalMap physicalMap) {
		this.cannons = new ArrayList<>(physicalMap.getCannons());
	}
	public void loadTorch(PhysicalMap physicalMap) {
		this.torchs = new ArrayList<>(physicalMap.getTorch());
	}

}
