package Task;

import java.util.ArrayList;

import Level.EnemyManager;
import Level.NPCManager;
import Template.EnemyTemplate;
import Template.NpcTemplate;
import entities.NPC;
import entities.NPC_Wizard1;
import entities.NightBorne;
import entities.Player;
import gamestates.Playing;
import main.Game;
import ui.Confirm;
import untilz.Talk;
import untilz.Text;

public class Task {

	public Task(NPC_Wizard1 npc) {

	}

//	protected static void getTask(Player player,) {
//		
//	}
	public static void TaskGet(Player player, final short npcsTemplateId) {
		switch (player.getCtaskId()) {
		case 0:
			Confirm.OpenComfirmUI(player, npcsTemplateId, Talk.getTask(0, 1),
					new String[] { Text.get(0, 0), Text.get(0, 1) });

			break;
		case 1:
			Confirm.OpenComfirmUI(player, npcsTemplateId, Talk.getTask(0, 1),
					new String[] { Text.get(0, 0), Text.get(0, 1) });
			break;
		case 2:
			Confirm.OpenComfirmUI(player, npcsTemplateId, Talk.getTask(0, 1),
					new String[] { Text.get(0, 0), Text.get(0, 1) });
			break;
		}
	}

	public static void FinishTask(Player player, final short npcsTemplateId) {
		switch (player.getCtaskId()) {
		case 0:
			Confirm.OpenComfirmUI(player, npcsTemplateId, Talk.getTask(1, 0), new String[] { Text.get(0, 2) });
			Confirm.setPrize(new int[] { 8, 9 },
					new int[] { (int) player.getHitbox().getX(), (int) player.getHitbox().getX() + 40 },
					new int[] { (int) player.getHitbox().getY() - (int) (5 * Game.SCALE),
							(int) player.getHitbox().getY() - (int) (5 * Game.SCALE) });
			player.applyExp(20);
			break;
		case 1:
			Confirm.OpenComfirmUI(player, npcsTemplateId, Talk.getTask(1, 0), new String[] { Text.get(0, 2) });
			Confirm.setPrize(new int[] { 9, 3 },
					new int[] { (int) player.getHitbox().getX(), (int) player.getHitbox().getX() + 40 },
					new int[] { (int) player.getHitbox().getY() - (int) (5 * Game.SCALE),
							(int) player.getHitbox().getY() - (int) (5 * Game.SCALE) });
			break;
		case 2:
			Confirm.OpenComfirmUI(player, npcsTemplateId, Talk.getTask(1, 0), new String[] { Text.get(0, 2) });
			Confirm.setPrize(new int[] { 4, 5 },
					new int[] { (int) player.getHitbox().getX(), (int) player.getHitbox().getX() + 40 },
					new int[] { (int) player.getHitbox().getY() - (int) (5 * Game.SCALE),
							(int) player.getHitbox().getY() - (int) (5 * Game.SCALE) });
			break;
		}
	}

	public static boolean isTaskNPC(final Player player, final short npcTemplateId) {
		if (player.getCtaskId() < Game.tasks.length) {
			try {
				return Game.tasks[player.getCtaskId()][player.getCtaskIndex() + 1] == npcTemplateId;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean isFinishTask(final Player player) {
		try {
			return Game.taskTemplates[player.getCtaskId()].subNames.length == player.getCtaskIndex() + 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isLastMission(final Player player) {
		try {
			return Game.taskTemplates[player.getCtaskId()].subNames.length - 1 == player.getCtaskIndex() + 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void upNextTask(final Player player, Playing playing) {
		ArrayList<NPC_Wizard1> npcWizard1 = playing.getNpcManager().getNpcWizard1s();
		player.setCtaskIndex((byte) (player.getCtaskIndex() + 1));
		if (Task.isLastMission(player)) {
			Confirm.setReceivePrize(true);
			player.setDoneTask(true);
			player.setDescriptionTask("Mission success, meet npc to receive prize");
		} else if (isFinishTask(player)) {
			nextTask(player);
			Confirm.setReceivePrize(false);
		}
		for (NpcTemplate npc : NPCManager.arrNpcTemplate) {
			if (Task.isTaskNPC(player, (short) npc.npcTemplateId)) {
				npcWizard1.get(npc.npcTemplateId).setHaveTask(true, player);
				NPC.setCurrNpcId(npc.npcTemplateId);
				break;
			} else {
				npcWizard1.get(npc.npcTemplateId).setHaveTask(false, player);
				NPC.setCurrNpcId(-1);
				break;
			}
		}
		for (EnemyTemplate e : EnemyManager.arrEnemyTemplate) {
			if (Task.isExtermination(player, playing, e)) {
				break;
			}
		}
	}

	public static void updateTask(final Player player, final short npcTemplateId) {
		if (!isTaskNPC(player, npcTemplateId)) {
		}
	}

	public static boolean isExtermination(final Player player, Playing playing, final EnemyTemplate enemyTemplate) {
		switch (enemyTemplate.mobTemplateId) {
		case 0: {
			if (player.getCtaskId() == 1 && player.getCtaskIndex() == 0
					|| player.getCtaskId() == 2 && player.getCtaskIndex() == 0
					|| player.getCtaskId() == 0 && player.getCtaskIndex() == 0) {
				NightBorne.setTaskExtermination(true);
				player.setDescriptionTask(Game.taskTemplates[player.getCtaskId()].subNames[player.getCtaskIndex() + 1]);
				player.setDoTask(true);
				return true;
			}
			break;
		}
		}
		return false;
	}

	public static void doTask(Player player, Playing playing) {

		switch (player.getCtaskId()) {
		case 0:
			if (NightBorne.getExtermination()) {
				isDoneExtermination(player, playing);
			}
			break;
		case 1:
			if (NightBorne.getExtermination()) {
				isDoneExtermination(player, playing);
			}
			break;
		case 2:
			if (NightBorne.getExtermination()) {
				isDoneExtermination(player, playing);
			}
			break;
		}
	}

	public static void isDoneExtermination(Player player, Playing playing) {
		player.setCtaskCount((short) NightBorne.getDeadCount());
		if (player.getCtaskCount() >= Game.taskTemplates[player.getCtaskId()].counts[player.getCtaskIndex() + 1]) {
			player.setDoTask(false);
			NightBorne.setDeadCount(0);
			upNextTask(player, playing);
		}
	}

	public static void nextTask(Player player) {
		player.setCtaskIndex((byte) -1);
		player.setCtaskId((byte) (player.getCtaskId() + 1));
	}
}
