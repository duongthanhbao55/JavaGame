package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.Game;
import ui.MenuButton;
import ui.PauseButton;
import ui.TextBox;
import untilz.LoadSave;

public class Login extends State implements Statemethods{

	private BufferedImage backgroundImg;
	private BufferedImage loginPanel;
	private ImageIcon gifIcon;
	private Image scaledGif;
    private int gifX, gifY;
	private int menuX, menuY, menuWidth, menuHeight;
	private int loginPanelWidth, loginPanelHeight;
	private boolean active = false;
	private String username = "";
	private String password = "";
	private TextBox userIDBox, passwordBox;
	private JTextField userID;
	private JPasswordField pw;
	
	public Login(Game game) {
		super(game);
		loadBackGround();
	}
	private void loadBackGround() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.LOGIN_BACKGROUND);
		loginPanel = LoadSave.GetSpriteAtlas(LoadSave.TEXT_BOX);
		gifIcon = new ImageIcon(getClass().getClassLoader().getResource(LoadSave.BACKGROUND_SCENE));
        gifX = 0;
        gifY = 0;
        scaledGif = gifIcon.getImage().getScaledInstance(Game.GAME_WIDTH, Game.GAME_HEIGHT, Image.SCALE_DEFAULT);
		menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (45 * Game.SCALE);
		initContainer();
		
	}
	public void initContainer() {
		userID = new JTextField("username");
		pw = new JPasswordField("password");
		userIDBox = new TextBox(menuX + 20,menuY + 185,0);
		passwordBox = new TextBox(menuX + 20,menuY + 235,0);
		userID.setBorder(null);
		userID.setOpaque(false);
		pw.setBorder(null);
		pw.setOpaque(false);
		game.getGamePanel().add(userID);
		game.getGamePanel().add(pw);
	}
	@Override
	public void update(long currTime) {
		userID.setBounds((int)(userIDBox.getBounds().getX() + 8 * Game.SCALE),(int)(userIDBox.getBounds().getY() + 3* Game.SCALE),(int)(userIDBox.getBounds().getWidth() - 16*Game.SCALE),(int)(userIDBox.getBounds().getHeight() - 6*Game.SCALE));
		pw.setBounds((int)(passwordBox.getBounds().getX() + 8 * Game.SCALE),(int)(passwordBox.getBounds().getY() + 3* Game.SCALE),(int)(passwordBox.getBounds().getWidth() - 16*Game.SCALE),(int)(passwordBox.getBounds().getHeight() - 6*Game.SCALE));
		userIDBox.update();
		passwordBox.update();
		
	}

	@Override
	public void render(Graphics g) {

		g.setColor(new Color(236, 240, 241));
	    g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
	    g.drawImage(scaledGif, gifX, gifY, null);
	    
	    g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
	    userIDBox.render(g);
	    passwordBox.render(g);


	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
//			if (isIn(e, textBox)) {// isIn is extends function
//				textBox.setMousePressed(true);
//			}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		resetTextBox();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
//		 if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && username.length() > 0) {
//			 	username = username.substring(0, username.length() - 1);
//	        } else if (e.getKeyCode() != KeyEvent.VK_BACK_SPACE && (Character.isLetterOrDigit(e.getKeyChar()))) {
//	        	username += e.getKeyChar();
//	        }	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	private boolean isIn(MouseEvent e, TextBox b) {
		return b.getBounds().contains(e.getX(), e.getY());

	}
	public void resetTextBox() {
		userIDBox.resetBools();
	}

}
