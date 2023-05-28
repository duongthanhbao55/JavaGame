package Level;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Map.PhysicalMap;
import Task.Task;
import Template.EnemyTemplate;
import Template.NpcStatus;
import Template.NpcTemplate;
import entities.NPC;
import entities.NPC_Wizard1;
import entities.NightBorne;
import entities.Player;
import gamestates.Playing;
import objects.Potion;
import ui.Confirm;

public class NPCManager {
	public static NpcStatus[] arrNpcStatus;
	private ArrayList<NPC_Wizard1> npcWizard1s = new ArrayList<>();
	private static ArrayList<NPC> allNpc = new ArrayList<>();
	private Playing playing;
	public static NpcTemplate[] arrNpcTemplate;                    

	public NPCManager(Playing playing) {
		this.playing = playing;
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
				if (!w.getHaveTask()) {
					w.drawDialogueBox(g);
				}
			}

		}
	}

	public void checkNPCTouched(Rectangle2D.Float hitbox, Player player) {
		for (NPC_Wizard1 w : npcWizard1s) {			
			if (w.isActive()) {
				if (hitbox.intersects(w.getHitbox())) {		
					if (w.getHaveTask()) {
						if (!Confirm.isShow()) {
							if(Confirm.isReceivePrize()) {
								Task.FinishTask(player, (short) w.getNpcId());
							}else {
								Task.TaskGet(player, (short) w.getNpcId());
							}		
							w.setContact(false);
						}
						w.setContact(true);
						playing.getPlayer().setInteract(true);
					} else {
						w.setIndex(w.getIndex() + 1);
						w.setContact(true);
						playing.getPlayer().setInteract(true);
						if (w.getIndex() == w.getConversation().length) {
							playing.getPlayer().setInteract(false);
							w.setContact(false);
							w.setIndex(-1);
						}
					}

					if (Confirm.getNormalButton() != null)
						if (Confirm.getButtonLenght() == 0) {
							Confirm.setIndex(Confirm.getIndex() + 1);
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
	public void loadNpcs(PhysicalMap physicalMap) {
		this.npcWizard1s = physicalMap.getNpcs();
	}
	public static ArrayList<NPC> getAllNpc() {
		return NPCManager.allNpc;
	}

}
