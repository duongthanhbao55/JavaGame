package gamestates;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import database.MySQL;
import main.Game;
import ui.LoginButton;
import ui.TextBox;
import untilz.LoadSave;

public class SetPlayerName extends State implements Statemethods{
	
	private JTextField playerName;
	private TextBox playerNameBox;
	private BufferedImage backgroundImg;
	private LoginButton[] createButton = new LoginButton[1];
	private ImageIcon gifIcon;
	private Image scaledGif;
	private int gifX, gifY;
	private int menuX, menuY, menuWidth, menuHeight;
	public SetPlayerName(Game game) {
		super(game);
		loadBackGround();
	}
	
	public static String limitLengthNickname(String nickname) {
		if(nickname.length() > 12) {
			String newNickname = nickname.substring(0, 12);
			return newNickname;
		}
		String oldNickname = nickname;
		return oldNickname;
	}
	
	private void loadBackGround() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.EMPTY_BACKGROUND);
		gifIcon = new ImageIcon(getClass().getClassLoader().getResource(LoadSave.BACKGROUND_SCENE));
		gifX = 0;
		gifY = 0;
		scaledGif = gifIcon.getImage().getScaledInstance(Game.GAME_WIDTH, Game.GAME_HEIGHT, Image.SCALE_DEFAULT);
		menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE)*2;
		menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE)*2;
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (45 * Game.SCALE);
		loadButton();
		loadContainer();
	}
	public void loadButton() {
		createButton[0] = new LoginButton(Game.GAME_WIDTH / 2, (int) (50 * Game.SCALE + 150 * Game.SCALE), 2, Gamestate.SETNAME);
	}
	public void loadContainer() {
		SetUpComponent();
		playerNameBox = new TextBox((int)(menuX + 25 * 1.3), menuY + 125, 1.3f);	
	}
	@Override
	public void update(long currTime) {
		playerName.setBounds((int) (playerNameBox.getBounds().getX() + 12 * Game.SCALE),
				(int) (playerNameBox.getBounds().getY() + 8 * Game.SCALE),
				(int) (playerNameBox.getBounds().getWidth() - 16 * Game.SCALE),
				(int) (playerNameBox.getBounds().getHeight() - 6 * Game.SCALE));	
		playerNameBox.update();		
		for (LoginButton lg : createButton) {
			lg.update();
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(scaledGif, gifX, gifY, null);
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
		
		playerNameBox.render(g);
		for (LoginButton lg : createButton) {
			lg.render(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (LoginButton lg : createButton) {
			if (isIn(e, lg)) {
				lg.setMousePressed(true);
				break;
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (LoginButton lg : createButton)
			if (isIn(e, lg)) {
				if (lg.isMousePressed()) {
					lg.applyGamestate();// apply only when Pressed before and Release after if pressed in button but
					if (lg.getState() == Gamestate.SETNAME) {
						String nickname = limitLengthNickname(playerName.getText());
						game.getMenu().setPlayerName(nickname);
						MySQL.setNickname(game.getLogin().getUser().getUsername(), nickname);
						Gamestate.state = Gamestate.MENU;
						resetTextField();
					} else if (lg.getState() == Gamestate.REGISTER) {
						Gamestate.state = Gamestate.REGISTER;
						game.getRegister().SetUpComponent();
						game.getRegister().addComponent();
						resetTextField();
					}
					// when release mouse position is out side bound of button, we didn't apply that
					// state
					break;
				}
			}
		resetTextBox();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (LoginButton lg : createButton)
			lg.setMouseOver(false);
		for (LoginButton lg : createButton)
			if (isIn(e, lg)) {
				lg.setMouseOver(true);
				break;
			}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	private void resetTextField() {
		playerName.setVisible(false);
	}
	public void resetTextBox() {
		playerNameBox.resetBools();
		for (LoginButton lg : createButton)
			lg.resetBools();
	}
	private boolean isIn(MouseEvent e, LoginButton b) {
		return b.getBounds().contains(e.getX(), e.getY());

	}
	public void SetUpComponent() {
		playerName = new JTextField("character name");
		Font font = new Font("Arial", Font.PLAIN, 25); // Tạo font chữ mới với kích thước 18
		playerName.setFont(font);
		playerName.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (playerName.getText().equals("character name")) {
					playerName.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (playerName.getText().isEmpty()) {
					playerName.setText("character name");
				}
			}
		});
		
		playerName.setBorder(null);
		playerName.setOpaque(false);


	}

	public void addComponent() {
		game.getGamePanel().add(playerName);
	}
	
}
