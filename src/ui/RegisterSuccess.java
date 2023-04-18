package ui;


import java.awt.Color;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import untilz.LoadSave;



public class RegisterSuccess {
	private Game game;
	private LoginButton login;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;
	
	public RegisterSuccess(Game game) {
		this.game = game;
		initImg();
		initButtons();
	}

	private void initButtons() {
		login = new LoginButton(Game.GAME_WIDTH / 2, (int) (195 * Game.SCALE), 0, Gamestate.LOGIN);
	}

	private void initImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.SUCCESS_BACKGROUND);
		bgW = (int)(img.getWidth() * Game.SCALE);
		bgH = (int)(img.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW/2;
		bgY = (int)(75 * Game.SCALE);
		
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		g.drawImage(img, bgX, bgY, bgW, bgH, null);
		login.render(g);
	}
	
	public void update() {
		login.update();
	}
	
	private boolean isIn(LoginButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
	public void MouseMoved(MouseEvent e) {
		login.setMouseOver(false);
		
		if(isIn(login, e))
			login.setMouseOver(true);
	}
	public void MouseRelease(MouseEvent e) {
		if(isIn(login, e)) {
			if( login.isMousePressed()) {
				login.applyGamestate();
				game.getLogin().SetUpComponent();
				game.getLogin().addComponent();		
			}
				
		}		
		login.resetBools();
	}
	public void MousePressed(MouseEvent e) {
		if(isIn(login, e))
			login.setMousePressed(true);

	}
}
