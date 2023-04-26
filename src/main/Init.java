package main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import Level.EnemyManager;
import Load.CacheDataLoader;
import Template.EnemyTemplate;
import Template.TaskTemplate;
import database.MySQL;

public class Init {
	protected static void init() {
		int i = 0;
		try {
			final MySQL mySQL = new MySQL(0);
			ResultSet res = mySQL.stat.executeQuery("SELECT * FROM `task`;");
			if (res.last()) {
				Game.tasks = new byte[res.getRow()][];
				Game.mapTasks = new byte[Game.tasks.length][];
				res.beforeFirst();
			}
			i = 0;
			while (res.next()) {
				final JSONArray jarrT = (JSONArray) JSONValue.parse(res.getString("tasks"));
				final JSONArray jarrM = (JSONArray) JSONValue.parse(res.getString("mapTasks"));
				Game.tasks[i] = new byte[jarrT.size()];
				Game.mapTasks[i] = new byte[jarrM.size()];
				for (int j = 0; j < Game.tasks[i].length; ++j) {
					Game.tasks[i][j] = Byte.parseByte(jarrT.get(j).toString());
					Game.mapTasks[i][j] = Byte.parseByte(jarrM.get(j).toString());
				}
				++i;
			}
			res.close();
			res = mySQL.stat.executeQuery("SELECT * FROM `tasktemplate`;");
			if (res.last()) {
				Game.taskTemplates = new TaskTemplate[res.getRow()];
				res.beforeFirst();
			}
			i = 0;
			while (res.next()) {
				final TaskTemplate taskTemplate = new TaskTemplate();
				taskTemplate.taskId = res.getShort("taskId");
				taskTemplate.name = res.getString("name");
				taskTemplate.detail = res.getString("detail");
				final JSONArray subNames = (JSONArray) JSONValue.parse(res.getString("subNames"));
				taskTemplate.subNames = new String[subNames.size()];
				taskTemplate.counts = new short[taskTemplate.subNames.length];
				for (byte k = 0; k < taskTemplate.subNames.length; ++k) {
					taskTemplate.subNames[k] = subNames.get((int) k).toString();
					taskTemplate.counts[k] = -1;
				}
				final JSONArray counts = (JSONArray) JSONValue.parse(res.getString("counts"));
				for (byte l = 0; l < counts.size(); ++l) {
					taskTemplate.counts[l] = Short.parseShort(counts.get((int) l).toString());
				}
				Game.taskTemplates[i] = taskTemplate;
				++i;
			}
			res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `mobtemplate`;");
            if (res.last()) {
                EnemyManager.arrEnemyTemplate = new EnemyTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final EnemyTemplate mobTemplate = new EnemyTemplate();
                mobTemplate.mobTemplateId = res.getShort("mobTemplateId");
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
	}
}
