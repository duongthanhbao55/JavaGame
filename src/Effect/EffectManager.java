package Effect;

import java.awt.Graphics;
import java.util.ArrayList;

import gamestates.Playing;
import static untilz.Constants.VFX.HEAL_EFFECT_SIZE;;

public class EffectManager {
	private Playing playing;
	private ArrayList<BuffEffect> buffEffects = new ArrayList<>();
	private ArrayList<HitEffect> hitEffects = new ArrayList<>();
	
	
	public EffectManager(Playing playing) {
		this.playing = playing;
		int x = (int)playing.getPlayer().getHitbox().getX();
		int y = (int)playing.getPlayer().getHitbox().getY();
		buffEffects.add(new BuffEffect(x,y,HEAL_EFFECT_SIZE,HEAL_EFFECT_SIZE));
	}
	
	public void update(long currTime) {
		for(BuffEffect be : buffEffects) {
			be.x = (int)playing.getPlayer().getHitbox().getX();
			be.y = (int)playing.getPlayer().getHitbox().getY();
			be.update(currTime);
		}
		for(HitEffect he : hitEffects) {
			he.update(currTime);
		}
	}
	public void render(Graphics g,int xLvlOffset) {
		for(BuffEffect be : buffEffects) {
			be.render(g, xLvlOffset);
		}
		for(HitEffect he : hitEffects) {
			he.render(g, xLvlOffset);
		}
	}
	public ArrayList<HitEffect> getHitEffects(){
		return this.hitEffects;
	}
}
