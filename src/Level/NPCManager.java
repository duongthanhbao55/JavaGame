package Level;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Task.Task;
import entities.NPC_Wizard1;
import entities.Player;
import gamestates.Playing;
import objects.Potion;

public class NPCManager {
	private ArrayList<NPC_Wizard1> npcWizard1s = new ArrayList<>();
	private Playing playing;

	public NPCManager(Playing playing) {
		this.playing = playing;
		npcWizard1s.add(new NPC_Wizard1(400, 580, 0));
	}

	public void update(long currTime, int[][] lvlData, Player player) {
		for (NPC_Wizard1 w : npcWizard1s) {
			w.update(currTime, lvlData, player);
		}
	}

	public void render(Graphics g, int xLvlOffset) {
		for (NPC_Wizard1 w : npcWizard1s) {
			w.render(g, xLvlOffset);
		}
	}

	public void drawDialogue(Graphics g) {
		for (NPC_Wizard1 w : npcWizard1s) {
			if (w.isContact()) {
				w.drawDialogueBox(g);
			}
				
		}
	}

	public void checkNPCTouched(Rectangle2D.Float hitbox,Player player) {
		for (NPC_Wizard1 w : npcWizard1s) {
			if (w.isActive()) {
				if (hitbox.intersects(w.getHitbox())) {
					w.setIndex(w.getIndex() + 1);
					w.setContact(true);
					playing.getPlayer().setInteract(true);
					if (w.getIndex() == w.getConversation().length) {
						playing.getPlayer().setInteract(false);
						w.setContact(false);
						w.setIndex(-1);
						w.setHaveTask(false,player);
						Task.upNextTask(player);
						for(int i = 0; i < EnemyManager.arrEnemyTemplate.length; i++) {
							Task.isExtermination(player, EnemyManager.arrEnemyTemplate[i]);
						}
					}
				}
			}
		}
	}


	public void resetNPC() {
		for (NPC_Wizard1 w : npcWizard1s)
			w.reset();
	}

	public ArrayList<NPC_Wizard1> getNpcWizard1s() {
		return npcWizard1s;
	}

	public void setNpcWizard1s(ArrayList<NPC_Wizard1> npcWizard1s) {
		this.npcWizard1s = npcWizard1s;
	}
	
}
