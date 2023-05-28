package main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.ItemManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import Level.EnemyManager;
import Level.NPCManager;
import Load.CacheDataLoader;
import Template.EnemyTemplate;
import Template.NpcTemplate;
import Template.TaskTemplate;
import database.MySQL;

public class Init {
	protected static void init() {
		int i = 0;
		try {
			final MySQL mySQL = new MySQL(0);
			ResultSet res = mySQL.stat.executeQuery("SELECT * FROM `taskinfo`;");
			if (res.last()) {
				Game.tasks = new byte[res.getRow()][];
				Game.mapTasks = new byte[Game.tasks.length][];
				res.beforeFirst();
			}
			i = 0;
			while (res.next()) {
				final JSONArray jarrT = (JSONArray) JSONValue.parse(res.getString("npcRequire"));
				final JSONArray jarrM = (JSONArray) JSONValue.parse(res.getString("mapRequire"));
				Game.tasks[i] = new byte[jarrT.size()];
				Game.mapTasks[i] = new byte[jarrM.size()];
				for (int j = 0; j < Game.tasks[i].length; ++j) {
					Game.tasks[i][j] = Byte.parseByte(jarrT.get(j).toString());
					Game.mapTasks[i][j] = Byte.parseByte(jarrM.get(j).toString());
				}
				++i;
			}
			res.close();
			res = mySQL.stat.executeQuery("SELECT * FROM `task`;");
			if (res.last()) {
				Game.taskTemplates = new TaskTemplate[res.getRow()];
				res.beforeFirst();
			}
			i = 0;
			while (res.next()) {
				final TaskTemplate taskTemplate = new TaskTemplate();
				taskTemplate.taskId = res.getShort("task_id");
				taskTemplate.name = res.getString("task_name");
				taskTemplate.detail = res.getString("detail");
				final JSONArray subNames = (JSONArray) JSONValue.parse(res.getString("subnames"));
				taskTemplate.subNames = new String[subNames.size()];
				taskTemplate.counts = new short[taskTemplate.subNames.length];
				for (byte k = 0; k < taskTemplate.subNames.length; ++k) {
					taskTemplate.subNames[k] = subNames.get((int) k).toString();
					taskTemplate.counts[k] = -1;
				}
				final JSONArray counts = (JSONArray) JSONValue.parse(res.getString("parameters"));
				for (byte l = 0; l < counts.size(); ++l) {
					taskTemplate.counts[l] = Short.parseShort(counts.get((int) l).toString());
				}
				Game.taskTemplates[i] = taskTemplate;
				++i;
			}
			res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `mob`;");
            if (res.last()) {
                EnemyManager.arrEnemyTemplate = new EnemyTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final EnemyTemplate mobTemplate = new EnemyTemplate();
                mobTemplate.mobTemplateId = res.getShort("mob_id");
                mobTemplate.type = res.getByte("type");
                mobTemplate.name = res.getString("name");
                mobTemplate.hp = res.getInt("hp");
                mobTemplate.isBoss = res.getBoolean("isBoss");
                mobTemplate.rangeMove = res.getByte("rangeMove");
                mobTemplate.speed = res.getByte("speed");
                mobTemplate.isAttack = res.getBoolean("isAttack");
                EnemyManager.arrEnemyTemplate[i] = mobTemplate;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `npc`;");
            if (res.last()) {
                NPCManager.arrNpcTemplate = new NpcTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final NpcTemplate npcTemplate = new NpcTemplate();
                npcTemplate.npcTemplateId = res.getByte("npc_id");
                npcTemplate.name = res.getString("name");
                final JSONArray jarr = (JSONArray)JSONValue.parse(res.getString("menu"));
                npcTemplate.menu = new String[jarr.size()][];
                for (int j = 0; j < npcTemplate.menu.length; ++j) {
                    final JSONArray jarr2 = (JSONArray)jarr.get(j);
                    npcTemplate.menu[j] = new String[jarr2.size()];
                    for (int k2 = 0; k2 < npcTemplate.menu[j].length; ++k2) {
                        npcTemplate.menu[j][k2] = jarr2.get(k2).toString();
                    }
                }
                NPCManager.arrNpcTemplate[i] = npcTemplate;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `exp`;");
            if (res.last()) {
                Game.exps = new long[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                Game.exps[i] = res.getLong("exp");
                ++i;
            }
            res.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			CacheDataLoader.getInstance().readXML(CacheDataLoader.PLAYER_FRAME);
			CacheDataLoader.getInstance().LoadXMLAnim(CacheDataLoader.PLAYER_ANIMATION);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ItemManager.loadItemData();
	}
}
