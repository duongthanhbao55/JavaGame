package gamestates;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import main.Game;
import ui.MenuButton;
import untilz.LoadSave;

public class Menu extends State implements Statemethods {

	// VARIABLE
	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage backgroundImg;
	private ImageIcon gifIcon;
	Image scaledGif;
    private int gifX, gifY;
	private int menuX, menuY, menuWidth, menuHeight;

	// CONSTRUCTOR
	public Menu(Game game) {
		super(game);
		loadBottons();
		loadBackGround();
	}

	private void loadBackGround() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		gifIcon = new ImageIcon(getClass().getClassLoader().getResource(LoadSave.BACKGROUND_SCENE));
        gifX = 0;
        gifY = 0;
        scaledGif = gifIcon.getImage().getScaledInstance(Game.GAME_WIDTH, Game.GAME_HEIGHT, Image.SCALE_DEFAULT);
		menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2 - 300;
		menuY = (int) (45 * Game.SCALE);
	}

	private void loadBottons() {
		buttons[0] = new MenuButton(Game.GAME_WIDTH / 2 - 300, (int) (150 * Game.SCALE), 0, Gamestate.PLAYING);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 2 - 300, (int) (220 * Game.SCALE), 1, Gamestate.OPTIONS);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 2 - 300, (int) (290 * Game.SCALE), 2, Gamestate.QUIT);
	}

	// RENDER
	@Override
	public void render(Graphics g) {
		g.drawImage(scaledGif, gifX, gifY, null);
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

		for (MenuButton mb : buttons)
			mb.render(g);
	}

	// UPDATE
	@Override
	public void update(long currTime) {
		for (MenuButton mb : buttons)
			mb.update();

	}

	// FUNCTION
	private void resetButtons() {
		for (MenuButton mb : buttons)
			mb.resetBools();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons)
			if (isIn(e, mb)) {// isIn is extends function
				mb.setMousePressed(true);
				break;
			}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons)
			if (isIn(e, mb)) {
				if (mb.isMousePressed()) {
					mb.applyGamestate();// apply only when Pressed before and Release after if pressed in button but
				if(mb.getState() == Gamestate.PLAYING) {
					game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
				}
					
					// when release mouse position is out side bound of button, we didn't apply that
										// state
					break;
				}
			}
		resetButtons();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons)
			mb.setMouseOver(false);
		for (MenuButton mb : buttons)
			if (isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		Gamestate.state = Gamestate.PLAYING;

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
