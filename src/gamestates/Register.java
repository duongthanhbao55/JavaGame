package gamestates;

import static untilz.Constants.UI.Button.B_HEIGHT;
import static untilz.Constants.UI.Button.B_WIDTH;

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

import database.User;
import main.Game;
import ui.LoginButton;
import ui.RegisterSuccess;
import ui.TextBox;
import untilz.LoadSave;
import static untilz.Constants.UI.Tick.*;

public class Register extends State implements Statemethods {
	private BufferedImage backgroundImg;
	private BufferedImage[] ticks;
	private LoginButton[] loginButton = new LoginButton[2];
	private ImageIcon gifIcon;
	private Image scaledGif;
	private int gifX, gifY;
	private int menuX, menuY, menuWidth, menuHeight;
	private TextBox userIDBox, passwordBox, confirmPasswordBox, emailBox;
	private JTextField userID, email;
	private JPasswordField pw, confirmPw;

	private RegisterSuccess registerSuccess;
	private boolean success;
	private boolean registerState;

	private JLabel R_emailLabel;
	private JLabel R_usernameLabel;
	private JLabel R_passwordLabel;
	private JLabel R_cf_passwordLabel;
	
	public Register(Game game) {
		super(game);

		loadBackGround();
		loadImg();
	}
	
	// GETTER && SETTER
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
	public boolean isRegisterState() {
		return registerState;
	}
	

	public void setRegisterState(boolean registerState) {
		this.registerState = registerState;
	}
	
	
	private void loadImg() {
		ticks = new BufferedImage[2];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.TICK);
		for(int i = 0; i < ticks.length; i++) {
			System.out.println(i * TICK_WIDTH_DEFAULT);
			ticks[i] = temp.getSubimage(i * TICK_WIDTH_DEFAULT, 0, TICK_WIDTH_DEFAULT, TICK_HEIGHT_DEFAULT);
		}
	}


	private void loadBackGround() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.LOGIN_BACKGROUND);
		registerSuccess = new RegisterSuccess(game);
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
		loginButton[0] = new LoginButton(Game.GAME_WIDTH / 2, (int) (310 * Game.SCALE), 1, Gamestate.REGISTER);
		loginButton[1] = new LoginButton(B_WIDTH / 2, (int) (Game.GAME_HEIGHT - B_HEIGHT), 0, Gamestate.LOGIN);
	}

	public void loadContainer() {
//		email = new JTextField();
//		R_emailLabel = new JLabel("Email");
//		
//		userID = new JTextField();
//		R_usernameLabel = new JLabel("Username");
//		
//		pw = new JPasswordField();
//		R_passwordLabel = new JLabel("Password");
//		
//		confirmPw = new JPasswordField();
//		R_cf_passwordLabel = new JLabel("Confirm password");
		
		emailBox = new TextBox(menuX + 20, menuY + 185, 1);
		userIDBox = new TextBox(menuX + 20, menuY + 235, 1);
		passwordBox = new TextBox(menuX + 20, menuY + 285, 1);
		confirmPasswordBox = new TextBox(menuX + 20, menuY + 335, 1);
		
		SetUpComponent();
		R_setBounds();
	}

	@Override
	public void update(long currTime) {

		if (success) {
			registerSuccess.update();
		} else {
			R_setBounds();

			for (LoginButton lg : loginButton) {
				lg.update();
			}
			emailBox.update();
			userIDBox.update();
			passwordBox.update();
			confirmPasswordBox.update();
		}
	}

	@Override
	public void render(Graphics g) {

		g.setColor(new Color(236, 240, 241));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		g.drawImage(scaledGif, gifX, gifY, null);

		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
			g.drawImage(ticks[0], 200, 200, TICK_WIDTH, TICK_HEIGHT, null);
		for (LoginButton lg : loginButton) {
			lg.render(g);
		}
		emailBox.render(g);
		userIDBox.render(g);
		passwordBox.render(g);
		confirmPasswordBox.render(g);
		if (success)
			registerSuccess.render(g);
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
		if (success)
			registerSuccess.MousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (LoginButton lg : loginButton) {
			if (isIn(e, lg)) {
				if (lg.isMousePressed()) {
					lg.applyGamestate();// apply only when Pressed before and Release after if pressed in button but
					if (lg.getState() == Gamestate.REGISTER) {
						setRegisterState(true);
						game.getLogin().setLoginState(false);
						success = User.Register(email.getText(), userID.getText(), pw.getText(), confirmPw.getText());
						if(!success) {
							continue;
						}
						System.out.println(success);
						resetTextField();
					} else if (lg.getState() == Gamestate.LOGIN) {
						game.getLogin().setLoginState(true);
						setRegisterState(false);
						Gamestate.state = Gamestate.LOGIN;
						game.getLogin().SetUpComponent();
						game.getLogin().addComponent();
						resetTextField();
					}
					// when release mouse position is out side bound of button, we didn't apply that
					// state
					break;
				}
			}
		}
		if (success) {
			registerSuccess.MouseRelease(e);
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
		if (success)
			registerSuccess.MouseMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private boolean isIn(MouseEvent e, TextBox b) {
		return b.getBounds().contains(e.getX(), e.getY());

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
		email.setVisible(false);
		userID.setVisible(false);
		pw.setVisible(false);
		confirmPw.setVisible(false);
		R_emailLabel.setVisible(false);
		R_usernameLabel.setVisible(false);
		R_passwordLabel.setVisible(false);
		R_cf_passwordLabel.setVisible(false);
	}

	public void SetUpComponent() {
		email = new JTextField();
		R_emailLabel = new JLabel("Email");
		Font font = new Font("Arial", Font.PLAIN, 18); // Tạo font chữ mới với kích thước 18
        email.setFont(font);
        R_emailLabel.setBounds(email.getBounds());
        R_emailLabel.setFont(email.getFont());
        R_emailLabel.setForeground(Color.GRAY);
		email.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				R_emailLabel.setVisible(false);
//				if (email.getText().equals("")) {
//					email.setText("");
//				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(isRegisterState()) {
					if (email.getText().isEmpty()) {
						R_emailLabel.setVisible(true);
					}
				}
			}
		});
		userID = new JTextField();
		R_usernameLabel = new JLabel("Username");
		userID.setFont(font);
        R_usernameLabel.setBounds(userID.getBounds());
        R_usernameLabel.setFont(userID.getFont());
        R_usernameLabel.setForeground(Color.GRAY);
		userID.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				R_usernameLabel.setVisible(false);
//				if (userID.getText().equals("username")) {
//					userID.setText("");
//				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(isRegisterState()) {
					if (userID.getText().isEmpty()) {
						R_usernameLabel.setVisible(true);
					}
				}
			}
		});
		pw = new JPasswordField();
		R_passwordLabel = new JLabel("Password");
		pw.setFont(font);
        R_passwordLabel.setBounds(userID.getBounds());
        R_passwordLabel.setFont(userID.getFont());
        R_passwordLabel.setForeground(Color.GRAY);
		pw.setEchoChar('\0');

		pw.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				R_passwordLabel.setVisible(false);
				pw.setEchoChar('●');
//				if (String.valueOf(pw.getPassword()).equals("password")) {
//					pw.setText("");
//				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(isRegisterState()) {
					if (String.valueOf(pw.getPassword()).isEmpty()) {
						R_passwordLabel.setVisible(true);
						pw.setEchoChar('\0');
//					pw.setText("password");
					}
				}
			}
		});
		confirmPw = new JPasswordField();
		R_cf_passwordLabel = new JLabel("Confirm password");
		confirmPw.setFont(font);
        R_cf_passwordLabel.setBounds(userID.getBounds());
        R_cf_passwordLabel.setFont(userID.getFont());
        R_cf_passwordLabel.setForeground(Color.GRAY);
		confirmPw.setEchoChar('\0');

		confirmPw.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				R_cf_passwordLabel.setVisible(false);
				confirmPw.setEchoChar('●');
//				if (String.valueOf(confirmPw.getPassword()).equals("confirm password")) {
//					confirmPw.setText("");
//				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(isRegisterState()) {
					if (String.valueOf(confirmPw.getPassword()).isEmpty()) {
						R_cf_passwordLabel.setVisible(true);
						confirmPw.setEchoChar('\0');
//					confirmPw.setText("confirm password");
					}
				}
			}
		});
		email.setBorder(null);
		email.setOpaque(false);
		userID.setBorder(null);
		userID.setOpaque(false);
		pw.setBorder(null);
		pw.setOpaque(false);
		confirmPw.setBorder(null);
		confirmPw.setOpaque(false);
		
		R_emailLabel.setBorder(null);
		R_emailLabel.setOpaque(false);
		
		R_usernameLabel.setBorder(null);
		R_usernameLabel.setOpaque(false);
		
		R_passwordLabel.setBorder(null);
		R_passwordLabel.setOpaque(false);
		
		R_cf_passwordLabel.setBorder(null);
		R_cf_passwordLabel.setOpaque(false);
	}

	public void addComponent() {
		game.getGamePanel().add(email);
		game.getGamePanel().add(userID);
		game.getGamePanel().add(pw);
		game.getGamePanel().add(confirmPw);
		game.getGamePanel().add(R_emailLabel);
		game.getGamePanel().add(R_usernameLabel);
		game.getGamePanel().add(R_passwordLabel);
		game.getGamePanel().add(R_cf_passwordLabel);
	}
	
	public void R_setBounds() {
		email.setBounds((int) (emailBox.getBounds().getX() + 8 * Game.SCALE),
				(int) (emailBox.getBounds().getY() + 3 * Game.SCALE),
				(int) (emailBox.getBounds().getWidth() - 16 * Game.SCALE),
				(int) (emailBox.getBounds().getHeight() - 6 * Game.SCALE));
		userID.setBounds((int) (userIDBox.getBounds().getX() + 8 * Game.SCALE),
				(int) (userIDBox.getBounds().getY() + 3 * Game.SCALE),
				(int) (userIDBox.getBounds().getWidth() - 16 * Game.SCALE),
				(int) (userIDBox.getBounds().getHeight() - 6 * Game.SCALE));
		pw.setBounds((int) (passwordBox.getBounds().getX() + 8 * Game.SCALE),
				(int) (passwordBox.getBounds().getY() + 3 * Game.SCALE),
				(int) (passwordBox.getBounds().getWidth() - 16 * Game.SCALE),
				(int) (passwordBox.getBounds().getHeight() - 6 * Game.SCALE));
		confirmPw.setBounds((int) (confirmPasswordBox.getBounds().getX() + 8 * Game.SCALE),
				(int) (confirmPasswordBox.getBounds().getY() + 3 * Game.SCALE),
				(int) (confirmPasswordBox.getBounds().getWidth() - 16 * Game.SCALE),
				(int) (confirmPasswordBox.getBounds().getHeight() - 6 * Game.SCALE));
		R_emailLabel.setBounds(email.getBounds());
		R_usernameLabel.setBounds(userID.getBounds());
		R_passwordLabel.setBounds(pw.getBounds());
		R_cf_passwordLabel.setBounds(confirmPw.getBounds());
	}
	
}
