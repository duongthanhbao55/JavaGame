package gamestates;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UrmButton;
import untilz.LoadSave;

import static untilz.Constants.UI.UrmButton.URM_SIZE;;

public class GameOptions extends State implements Statemethods {

	private AudioOptions audioOptions;
	private BufferedImage optionsBackGroundImg;
	private ImageIcon gifIcon;
	Image scaledGif;
    private int gifX, gifY;
	private int bgX, bgY, bgW, bgH;
	private UrmButton menuB;
	
	
	
	public GameOptions(Game game) {
		super(game);
		loadBackGround();
		loadButton();
		audioOptions = game.getAudioOptions();
	}

	private void loadBackGround() {
		gifIcon = new ImageIcon(getClass().getClassLoader().getResource(LoadSave.BACKGROUND_SCENE2));
        gifX = 0;
        gifY = 0;
        scaledGif = gifIcon.getImage().getScaledInstance(Game.GAME_WIDTH, Game.GAME_HEIGHT, Image.SCALE_DEFAULT);
		optionsBackGroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTION_BACKGROUND);
		bgW = (int) (optionsBackGroundImg.getWidth() * Game.SCALE);
		bgH = (int) (optionsBackGroundImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (57 * Game.SCALE);
		
	}

	private void loadButton() {
		int menuX = (int)(451* Game.SCALE);
		int menuY = (int)(350 * Game.SCALE);
		
		menuB = new UrmButton(menuX,menuY,URM_SIZE,URM_SIZE,2);	
		
	}

	@Override
	public void update(long currTime) {
		audioOptions.update();
		menuB.update();	
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(scaledGif, gifX, gifY, null);
		g.drawImage(optionsBackGroundImg, bgX, bgY, bgW, bgH, null);
		
		audioOptions.render(g);
		menuB.render(g);
		
		
	}

	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(isIn(e,menuB)) {
			menuB.setMousePressed(true);
		}else
			audioOptions.mousePressed(e);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isIn(e,menuB)) {
			if(menuB.isMousePressed()) 
				Gamestate.state = Gamestate.MENU;
			
		}else
			audioOptions.mouseReleased(e);
		
		menuB.resetBools();	
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);
		
		if(isIn(e,menuB)) {
			menuB.setMouseOver(true);
		}else {
			audioOptions.mouseMoved(e);
		}
			
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Gamestate.state = Gamestate.MENU;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());

	}
}
