package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import entities.Player;
import gamestates.Gamestate;
import main.Game;
import untilz.LoadSave;

import static untilz.Constants.UI.PauseButton.*;

public class Confirm {

	private static NormalButton[] normalButtons;
	private static String text;
	private static Player player;
	private static short npcTemplateId;
	private static boolean isShow = true;
	private TextBox dialogueBox;

	public Confirm() {
		dialogueBox = new TextBox(Game.TILES_SIZE * 15, Game.TILES_SIZE * 26, 2);
	}

	public static void OpenComfirmUI(Player player, final short npcTemplateId, String text, String[] Button) {
		Confirm.normalButtons = new NormalButton[Button.length];
		initButtons();
		Confirm.text = text;
		Confirm.player = player;
		Confirm.npcTemplateId = npcTemplateId;
		isShow = true;
	}

	public static void initButtons() {
		for (int i = 0; i < normalButtons.length; i++) {
			normalButtons[i] = new NormalButton(Game.GAME_WIDTH / 2, (int) (195 * Game.SCALE), 0,2f, (byte) i);
		}
		// login = new LoginButton(Game.GAME_WIDTH / 2, (int) (195 * Game.SCALE), 0,
		// Gamestate.LOGIN);
	}

	public void render(Graphics g) {
		if(isShow) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			for(NormalButton nb : normalButtons)
				nb.render(g);
			dialogueBox.render(g);
		}
	}

	public void update() {
		if(isShow) {
			for(NormalButton nb : normalButtons) 
				nb.update();
			dialogueBox.update();
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
					// game.getLogin().SetUpComponent();
					// game.getLogin().addComponent();
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
	public boolean isShow() {
		return Confirm.isShow;
	}
}
