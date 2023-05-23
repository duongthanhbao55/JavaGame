package Skill;

import java.awt.Graphics;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import gamestates.Playing;
import main.Game;

import static untilz.Constants.Directions.*;
import static untilz.Constants.Skill.START_UP1_WIDTH;
import static untilz.Constants.Skill.START_UP1_HEIGHT;

public class SkillManager {
	private Playing playing;
	private ArrayList<Skill> skills = new ArrayList<>();
	private WaterSkill waterSkill;

	public SkillManager(Playing playing) {
		this.playing = playing;
		waterSkill = new WaterSkill((int) playing.getPlayer().getRealHitbox().getX(),
				(int) playing.getPlayer().getRealHitbox().getY(), START_UP1_WIDTH, START_UP1_HEIGHT);
		skills.add(waterSkill);
	}

	public void update(long currTime) {
		for (Skill s : skills) {
			s.update(currTime);
		}
		waterSkill.Attack(playing);
	}

	public void render(Graphics g, int xLvlOffset) {
		for (Skill s : skills) {
			s.render(g, xLvlOffset);
		}
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_U:
			if(playing.getPlayer().getCurrMana() >= 30) {
				waterSkill.setIsStartUp1(true);
				setUp();	
				playing.getPlayer().changeMana(-30);
			}	
			break;
		case KeyEvent.VK_I:
			if(playing.getPlayer().getCurrMana() >= 30) {
				waterSkill.setIsStartUp2(true);
				setUp();
				playing.getPlayer().changeMana(-30);
			}		
			break;
		case KeyEvent.VK_O:

			break;
		}
	}

	public void keyRelease(KeyEvent e) {
		switch (e.getKeyCode()) {

		case KeyEvent.VK_U:

			break;
		case KeyEvent.VK_I:

			break;
		case KeyEvent.VK_O:

			break;

		}
	}

	public void setUp() {
		if (playing.getPlayer().getFlipW() == 1) {
			waterSkill.setPos((int) (playing.getPlayer().getHitbox().getX() + playing.getPlayer().getHitbox().getWidth()
					+ 75 * Game.SCALE), (int) playing.getPlayer().getHitbox().getY(), RIGHT);
		}
			
		else if (playing.getPlayer().getFlipW() == -1) {
			waterSkill.setPos((int) (playing.getPlayer().getHitbox().getX() - 75 * Game.SCALE),
					(int) playing.getPlayer().getHitbox().getY(), LEFT);
		}
			
	}
}
