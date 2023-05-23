package Level;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Effect.HitEffect;
import Map.PhysicalMap;
import Template.EnemyTemplate;
import Template.MobStatus;
import database.ItemManager;
import entities.NightBorne;
import entities.Player;
import gamestates.Playing;
import objects.Item;

import static untilz.Constants.VFX.*;
import static untilz.HelpMethods.*;

public class EnemyManager {

	public static EnemyTemplate[] arrEnemyTemplate;
	public static MobStatus[] arrMobStatus;
	private Playing playing;
	private ArrayList<NightBorne> NightBornes = new ArrayList<>();
	private ArrayList<HitEffect> hitEffect;
	boolean activeEffect = false;
	boolean isThrownAway = false;

	public EnemyManager(Playing playing) {
		this.playing = playing;
		initHitEffect();
	}

	public void initHitEffect() {
		hitEffect = new ArrayList<>();
	}

	public void loadEnimies(PhysicalMap physicalMap) {
		NightBornes = physicalMap.getNightBornes();
		for(int i = 0; i < NightBornes.size(); i++) {
			playing.getEffectManager().getHitEffects().add(new HitEffect(0, 0, HIT_EFFECT_WIDTH, HIT_EFFECT_HEIGHT));
		}
	}

	public void checkEnemyHited(Rectangle2D.Float attackBox, Player player) {
		int i = 0;
		for (NightBorne n : NightBornes) {
			if (n.isActive())
				if (attackBox.intersects(n.getHitbox())) {
					int x = getCollistionPointX(attackBox, n.getHitbox());
					int y = getCollistionPointY(attackBox, n.getHitbox());
					playing.getEffectManager().getHitEffects().get(i).setPos(x, y);
					playing.getEffectManager().getHitEffects().get(i).setActive(true);
					if(isThrownAway) {
						n.getHitbox().y -= 1;
					}
					n.hurt(player.getCurrDamage());
				}
			i++;
		}
		resetEffectBoolean();
		
	}

	public void update(long currTime, int[][] lvlData, Player player) {
		
		for (NightBorne n : NightBornes) {
			n.reSpawn(currTime);
		}
		for (NightBorne n : NightBornes) {
			if (n.isActive()) {
				n.update(currTime, lvlData, player);
			}
		}
		for (HitEffect he : hitEffect) {
				he.update(currTime);
			}
	}

	public void render(Graphics g, int xLvlOffset) {
		drawNightBornes(g, xLvlOffset);

	}

	private void drawNightBornes(Graphics g, int xLvlOffset) {
		for (NightBorne n : NightBornes) {
			if (n.isActive()) {
				n.render(g, xLvlOffset);
				
			}
				
		}
		for (HitEffect he : hitEffect) {
			he.render(g, xLvlOffset);
		}
	}

	public void resetAllEnemies() {
		for (NightBorne c : NightBornes)
			c.resetEnemy();
	}
	public void setThrownAway(boolean isThrownAway) {
		this.isThrownAway = isThrownAway;
	}
	public void resetEffectBoolean() {
		isThrownAway = false;
	}
}
