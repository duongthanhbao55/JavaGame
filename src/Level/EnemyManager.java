package Level;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Effect.HitEffect;
import Load.CacheDataLoader;
import Map.PhysicalMap;
import Template.EnemyTemplate;
import entities.Enemy;
import entities.NightBorne;
import entities.Player;
import gamestates.Playing;
import static untilz.Constants.VFX.*;
import static untilz.HelpMethods.*;


public class EnemyManager {
	
	public static EnemyTemplate[] arrEnemyTemplate;
	private Playing playing;
	private ArrayList<NightBorne> NightBornes = new ArrayList<>();
	private HitEffect hitEffect = null;
	boolean activeEffect = false;
	
	public EnemyManager(Playing playing) {
		this.playing = playing;
		initHitEffect();
	}
	public void initHitEffect() {
		hitEffect = new HitEffect(0,0,HIT_EFFECT_WIDTH,HIT_EFFECT_HEIGHT);
	}
	public void loadEnimies(PhysicalMap physicalMap) {
		NightBornes = physicalMap.getNightBornes();
	}
	
	public void checkEnemyHited(Rectangle2D.Float attackBox,Player player) {
		for(NightBorne n : NightBornes) {
			if(n.isActive())
				if(attackBox.intersects(n.getHitbox())) {
					activeEffect = true;
					int x = getCollistionPointX(attackBox, n.getHitbox());
					int y = getCollistionPointY(attackBox, n.getHitbox());
					hitEffect.setPos(x, y);
					n.hurt(player.getDamage());			
				}				
			}
	}
	
	public void update(long currTime, int[][] lvlData,Player player ) {
		boolean isAnyActive = false;
		for(NightBorne n : NightBornes)
			if(n.isActive()) {
				n.update(currTime,lvlData,player);			
				if(activeEffect)
					hitEffect.update(currTime);
				isAnyActive = true;
			}
		checkEffect();
		if(!isAnyActive) {
			playing.setLevelCompleted(true);
		}
	
	}
	public void render(Graphics g, int xLvlOffset) {
		drawNightBornes(g, xLvlOffset);
		if(activeEffect)
		hitEffect.render(g,xLvlOffset);
		
	}
	private void drawNightBornes(Graphics g, int xLvlOffset) {
		for(NightBorne n : NightBornes)
			if(n.isActive())
				n.render(g,xLvlOffset);
	}
	private void checkEffect() {
		if(hitEffect.getHitEffect().isLastFrame()) {
			activeEffect = false;
		}
	}
	public void resetAllEnemies() {
		for (NightBorne c : NightBornes)
			c.resetEnemy();
	}
	
}
	
