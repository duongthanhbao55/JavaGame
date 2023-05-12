package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import untilz.LoadSave;

public class PlayerStatus {
	private BufferedImage statusBg;
	private BufferedImage[] iconStatus;
	private Playing playing;
	private int[] statusInf;
	private int xBgPos, yBgPos;
	private int xIconPos, yIconPos;
	private int PlayerHealth, PlayerMana, PlayerDamage, PlayerDefend;
	private int PlayerLuck, PlayerGold;

	public PlayerStatus(Playing playing) {
		this.playing = playing;
		xBgPos = (int) (Game.TILES_SIZE * 18);
		yBgPos = (int) (Game.TILES_SIZE * 3);
		xIconPos = (int) (xBgPos + Game.TILES_SIZE * 1);
		yIconPos = (int) (yBgPos + Game.TILES_SIZE * 1);
		loadImgs();
	}

	private void loadStatus() {
		statusInf = new int[6];
		statusInf[0] = playing.getPlayer().getCurrHealth();
		statusInf[1] = playing.getPlayer().getMana();
		statusInf[2] = playing.getPlayer().getDamage();
		statusInf[3] = playing.getPlayer().getDef();
		statusInf[4] = 0;
		statusInf[5] = 0;
	}

	private void loadImgs() {
		statusBg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BG);
		iconStatus = new BufferedImage[6];
		iconStatus[0] = LoadSave.GetSpriteAtlas(LoadSave.HEALTH_ICON);
		iconStatus[1] = LoadSave.GetSpriteAtlas(LoadSave.MANA_ICON);
		iconStatus[2] = LoadSave.GetSpriteAtlas(LoadSave.DAMAGE_ICON);
		iconStatus[3] = LoadSave.GetSpriteAtlas(LoadSave.DEFEND_ICON);
		iconStatus[4] = LoadSave.GetSpriteAtlas(LoadSave.LUCK_ICON);
		iconStatus[5] = LoadSave.GetSpriteAtlas(LoadSave.GOLD_ICON);
	}

	public void update() {

	}

	public void render(Graphics g) {
		
		g.drawImage(statusBg, xBgPos, yBgPos, (int) (statusBg.getWidth() * Game.SCALE / 1.5f),
				(int) (statusBg.getHeight() * Game.SCALE / 1.5f), null);
		for (int i = 0; i < iconStatus.length; i++) {
			g.drawImage(iconStatus[i], xIconPos, (int) (yIconPos + i * Game.TILES_SIZE * 1.5), 16, 16, null);
			if (i == 0) {
				g.setColor(new Color(0, 200, 0));
			} else if (i == 1) {
				g.setColor(new Color(0, 0, 200));
			} else if (i == 2) {
				g.setColor(new Color(200, 0, 0));
			} else if (i == 3) {
				g.setColor(new Color(160, 160, 160));
			} else if (i == 4) {
				g.setColor(new Color(51, 255, 52));
			} else if (i == 5) {
				g.setColor(new Color(255, 255, 0));
			}
			g.drawString("" + statusInf[i], xIconPos + Game.TILES_SIZE * 2,
					(int) (yIconPos + 10 * Game.SCALE + i * Game.TILES_SIZE * 1.5));
		}

	}

	public int getPlayerHealth() {
		return PlayerHealth;
	}

	public void setPlayerHealth(int playerHealth) {
		PlayerHealth = playerHealth;
	}

	public int getPlayerMana() {
		return PlayerMana;
	}

	public void setPlayerMana(int playerMana) {
		PlayerMana = playerMana;
	}

	public int getPlayerDamage() {
		return PlayerDamage;
	}

	public void setPlayerDamage(int playerDamage) {
		PlayerDamage = playerDamage;
	}

	public int getPlayerDefend() {
		return PlayerDefend;
	}

	public void setPlayerDefend(int playerDefend) {
		PlayerDefend = playerDefend;
	}

	public int getPlayerLuck() {
		return PlayerLuck;
	}

	public void setPlayerLuck(int playerLuck) {
		PlayerLuck = playerLuck;
	}

	public int getPlayerGold() {
		return PlayerGold;
	}

	public void setPlayerGold(int playerGold) {
		PlayerGold = playerGold;
	}
}
