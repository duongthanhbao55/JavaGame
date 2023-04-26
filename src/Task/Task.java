package Task;

import Template.EnemyTemplate;
import entities.Enemy;
import entities.NPC_Wizard1;
import entities.Player;
import main.Game;

public class Task {
	
	public Task(NPC_Wizard1 npc) {
		
	}
//	protected static void getTask(Player player,) {
//		
//	}
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
	public static void upNextTask(final Player player) {
		player.setCtaskIndex((byte) (player.getCtaskIndex() + 1));
		if(isFinishTask(player)) {
			player.setCtaskIndex((byte) -1);
			player.setCtaskId((byte) (player.getCtaskId() + 1));
		}
	}
	
	public static void updateTask(final Player player, final short npcTemplateId) {
		if(!isTaskNPC(player, npcTemplateId)) {
			
		}
	}
	public static boolean isExtermination(final Player player, final EnemyTemplate enemyTemplate) {
        switch (enemyTemplate.mobTemplateId) {
            case 0: {
                if (player.getCtaskId() == 1 && player.getCtaskIndex() == 1) {
                	System.out.println(enemyTemplate.name);
                	
                    return true;
                }
                break;
            }          
        }
        return false;
    }
}
