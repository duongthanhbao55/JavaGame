package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import Level.NPCManager;
import Task.Task;
import database.ItemManager;
import entities.NPC_Wizard1;
import entities.Player;
import gamestates.Playing;
import main.Game;
import objects.Item;

import static untilz.Constants.UI.ConfirmButton.*;

public class Confirm {

	private static NormalButton[] normalButtons;
	private static String text;
	private static Player player;
	private static short npcTemplateId;
	private static boolean isShow = false;
	private static boolean isReceivePrize = false;
	private static int index = -1;
	private TextBox dialogueBox;
	private Playing playing;

	public Confirm(Playing playing) {
		dialogueBox = new TextBox(Game.TILES_SIZE * 15, Game.TILES_SIZE * 26, 2);
		this.playing = playing;
	}

	public static void OpenComfirmUI(Player player, final short npcTemplateId, String text, String[] Button) {
		Confirm.normalButtons = new NormalButton[Button.length];
		initButtons(Button);
		Confirm.text = text;
		Confirm.player = player;
		Confirm.npcTemplateId = npcTemplateId;
		Confirm.isShow = true;
	}

	public static void initButtons(String[] Button) {
		for (int i = 0; i < normalButtons.length; i++) {
			normalButtons[i] = new NormalButton(
					(int) (Game.GAME_WIDTH / 2 - ((int) ((normalButtons.length / 2) - i) * CONFIRM_BUTTON_SIZE * 5f)),
					(int) (Game.TILES_SIZE * 23), 0, 5f, (byte) i, Button[i]);
		}
	}

	public void render(Graphics g, int xLvlOffset) {
		if (Confirm.isShow) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			for (NormalButton nb : Confirm.normalButtons)
				nb.render(g);
			dialogueBox.render(g);
			g.setColor(new Color(0, 0, 0));
			int y = dialogueBox.getBounds().y + Game.TILES_SIZE * 2 - (int) (Game.SCALE * 4);
			int x = dialogueBox.getBounds().x + Game.TILES_SIZE * 2;
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			for (String line : Confirm.text.split("\n")) {
				g.drawString(line, x, y);
				y += 20;
			}
			
		}
	}

	public void update() {
		if (Confirm.isShow) {
			for (NormalButton nb : Confirm.normalButtons) {
				nb.getBounds().getX();
				nb.update();
			}
			dialogueBox.update();
			if (index == 1) {
				notShow();
				Task.upNextTask(player, playing);
				index = -1;
			}
		}
	}

	private boolean isIn(NormalButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void MouseMoved(MouseEvent e) {
		for (NormalButton nb : normalButtons)
			nb.setMouseOver(false);
		for (NormalButton nb : normalButtons)
			if (isIn(nb, e))
				nb.setMouseOver(true);
	}

	public void MouseRelease(MouseEvent e) {
		for (NormalButton nb : normalButtons)
			if (isIn(nb, e)) {
				if (nb.isMousePressed()) {
					nb.applyTask();
					if (nb.getState() == 0) {
						Task.upNextTask(player, playing);
						player.setDoneTask(false);
						notShow();
					}
					if (nb.getState() == 1) {
						notShow();
						Confirm.isReceivePrize = false;
					}
				}
			}
		for (NormalButton nb : normalButtons)
			nb.resetBools();
	}

	public void MousePressed(MouseEvent e) {
		for (NormalButton nb : normalButtons)
			if (isIn(nb, e))
				nb.setMousePressed(true);

	}

	public void notShow() {
		Confirm.isShow = false;
		NPCManager.getAllNpc().get(npcTemplateId).setContact(false);
		playing.getPlayer().setInteract(false);
	}

	public static boolean isShow() {
		return Confirm.isShow;
	}

	public static void setShow(boolean isShow) {
		Confirm.isShow = isShow;
	}

	public static int getButtonLenght() {
		return normalButtons.length;
	}

	public static int getIndex() {
		return Confirm.index;
	}

	public static void setIndex(int index) {
		Confirm.index = index;
	}

	public static NormalButton[] getNormalButton() {
		return Confirm.normalButtons;
	}

	public static void setReceivePrize(boolean isReceivePrize) {
		Confirm.isReceivePrize = isReceivePrize;
	}

	public static boolean isReceivePrize() {
		return Confirm.isReceivePrize;
	}
	public static void setPrize(int[] index, int[] xPos, int[] yPos) {
		for (int i = 0; i < index.length; i++) {
			ItemManager.addItem(new Item(xPos[i], yPos[i], 0, ItemManager.arrItemTemplate[index[i]]));
		}
	}
}
