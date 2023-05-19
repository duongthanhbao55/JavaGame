package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import database.MySQL;
import database.User;
import entities.Player;
import main.Game;
import ui.LoginButton;
import ui.TextBox;
import untilz.LoadSave;

import static untilz.Constants.UI.Button.B_HEIGHT;
import static untilz.Constants.UI.Button.B_WIDTH;

public class Login extends State implements Statemethods {

	private BufferedImage backgroundImg;
	private LoginButton[] loginButton = new LoginButton[2];
	private ImageIcon gifIcon;
	private Image scaledGif;
	private int gifX, gifY;
	private int menuX, menuY, menuWidth, menuHeight;
	private TextBox userIDBox, passwordBox;
	private JTextField userID;
	private JPasswordField pw;
	private User user;
	private Player player;
	
	private JLabel L_usernameLabel;
	private JLabel L_passwordLabel;
	
	private boolean loginState;

	public Login(Game game) {
		super(game);
		loadBackGround();
	}
	
	// GETTER & SETTER
	
	public boolean isLoginState() {
		return loginState;
	}
	
	public void setLoginState(boolean loginState) {
		this.loginState = loginState;
	}
	
	public User getUser() {
		return user;
	}
	

	private void loadBackGround() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.LOGIN_BACKGROUND);
		gifIcon = new ImageIcon(getClass().getClassLoader().getResource(LoadSave.BACKGROUND_SCENE1));
		gifX = 0;
		gifY = 0;
		scaledGif = gifIcon.getImage().getScaledInstance(Game.GAME_WIDTH, Game.GAME_HEIGHT, Image.SCALE_DEFAULT);
		menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (45 * Game.SCALE);
		loadButton();
		loadContainer();
	}



	public void loadButton() {
		loginButton[0] = new LoginButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE + 150), 0, Gamestate.LOGIN);
		loginButton[1] = new LoginButton(B_WIDTH / 2, (int) (Game.GAME_HEIGHT - B_HEIGHT), 1, Gamestate.REGISTER);
	}

	public void loadContainer() {
//		userID = new JTextField();
//		L_usernameLabel = new JLabel("Username");
//		pw = new JPasswordField();
//		L_passwordLabel = new JLabel("Password");
		SetUpComponent();
		addComponent();
		
		
		userIDBox = new TextBox(menuX + 20, menuY + 185, 1);
		passwordBox = new TextBox(menuX + 20, menuY + 235, 1);
		
		
		L_setBounds();
	}

	@Override
	public void update(long currTime) {
		L_setBounds();

		for (LoginButton lg : loginButton) {
			lg.update();
		}
		userIDBox.update();
		passwordBox.update();

	}

	@Override
	public void render(Graphics g) {

		g.setColor(new Color(236, 240, 241));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		g.drawImage(scaledGif, gifX, gifY, null);

		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
		for (LoginButton lg : loginButton) {
			lg.render(g);
		}
		userIDBox.render(g);
		passwordBox.render(g);

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (LoginButton lg : loginButton) {
			if (isIn(e, lg)) {
				lg.setMousePressed(true);
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (LoginButton lg : loginButton)
			if (isIn(e, lg)) {
				if (lg.isMousePressed()) {
					lg.applyGamestate();// apply only when Pressed before and Release after if pressed in button but
					if (lg.getState() == Gamestate.LOGIN) {
						setLoginState(true);
						game.getRegister().setRegisterState(false);
						user = User.Login(userID.getText(), pw.getText());
						if(user == null) {
							continue; // Username or password INCORRECT
						}
						player = user.getPlayer();				
						game.getPlaying().initPlayer(this.player);
//						if (player.getPlayerName() == "") {
//							game.getSetNamePlayer().addComponent();
//							Gamestate.state = Gamestate.SETNAME;
//							resetTextField();
//						} else {
//							Gamestate.state = Gamestate.MENU;
//							game.getMenu().setPlayerName(player.getPlayerName());
//							resetTextField();
//						}
						if(MySQL.haveNickname(user.getUsername()) == null) {
							resetTextField();
							game.getSetNamePlayer().addComponent();
							Gamestate.state = Gamestate.SETNAME;
						}
						else {
							game.getMenu().setPlayerName(MySQL.haveNickname(user.getUsername()));
							Gamestate.state = Gamestate.MENU;
							resetTextField();
						}
					} else if (lg.getState() == Gamestate.REGISTER) {
						game.getRegister().setRegisterState(true);
						setLoginState(false);
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
		for (LoginButton lg : loginButton)
			lg.setMouseOver(false);
		for (LoginButton lg : loginButton)
			if (isIn(e, lg)) {
				lg.setMouseOver(true);
				break;
			}
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	public Player getPlayer() {
		return this.player;
	}
	private boolean isIn(MouseEvent e, LoginButton b) {
		return b.getBounds().contains(e.getX(), e.getY());

	}

	public void resetTextBox() {
		userIDBox.resetBools();
		for (LoginButton lg : loginButton)
			lg.resetBools();
	}

	private void resetTextField() {
		userID.setVisible(false);
		pw.setVisible(false);
		
		L_usernameLabel.setVisible(false);
		L_passwordLabel.setVisible(false);
	}

	public void SetUpComponent() {
		userID = new JTextField();
		L_usernameLabel = new JLabel("Username");
		Font font = new Font("Arial", Font.PLAIN, 18); // Tạo font chữ mới với kích thước 18
		userID.setFont(font);
		L_usernameLabel.setBounds(userID.getBounds());
        L_usernameLabel.setFont(userID.getFont());
        L_usernameLabel.setForeground(Color.GRAY);
		userID.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				L_usernameLabel.setVisible(false);
//				if (userID.getText().equals("username")) {
//					userID.setText("");
//				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(isLoginState()) {
					if (userID.getText().isEmpty()) {
						L_usernameLabel.setVisible(true);
					}
				}
			}
		});
		pw = new JPasswordField();
		L_passwordLabel = new JLabel("Password");
		pw.setFont(font);
		L_passwordLabel.setBounds(pw.getBounds());
		L_passwordLabel.setFont(pw.getFont());
		L_passwordLabel.setForeground(Color.GRAY);
		pw.setEchoChar('\0');

		pw.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				L_passwordLabel.setVisible(false);
				pw.setEchoChar('●');
//				if (String.valueOf(pw.getPassword()).equals("password")) {
//					pw.setText("");
//				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(isLoginState()) {
					if (String.valueOf(pw.getPassword()).isEmpty()) {
						L_passwordLabel.setVisible(true);
						pw.setEchoChar('\0');
//					pw.setText("password");
					}
				}
			}
		});
		userID.setBorder(null);
		userID.setOpaque(false);
		pw.setBorder(null);
		pw.setOpaque(false);
		
		L_usernameLabel.setBorder(null);
		L_usernameLabel.setOpaque(false);
		L_passwordLabel.setBorder(null);
		L_passwordLabel.setOpaque(false);
	}

	public void addComponent() {
		game.getGamePanel().add(userID);
		game.getGamePanel().add(pw);
		
		game.getGamePanel().add(L_usernameLabel);
		game.getGamePanel().add(L_passwordLabel);
	}
	
	public void L_setBounds() {
		userID.setBounds((int) (userIDBox.getBounds().getX() + 8 * Game.SCALE),
				(int) (userIDBox.getBounds().getY() + 3 * Game.SCALE),
				(int) (userIDBox.getBounds().getWidth() - 16 * Game.SCALE),
				(int) (userIDBox.getBounds().getHeight() - 6 * Game.SCALE));
		pw.setBounds((int) (passwordBox.getBounds().getX() + 8 * Game.SCALE),
				(int) (passwordBox.getBounds().getY() + 3 * Game.SCALE),
				(int) (passwordBox.getBounds().getWidth() - 16 * Game.SCALE),
				(int) (passwordBox.getBounds().getHeight() - 6 * Game.SCALE));
		
		L_usernameLabel.setBounds(userID.getBounds());
		L_passwordLabel.setBounds(pw.getBounds());
		
	}
	
}
