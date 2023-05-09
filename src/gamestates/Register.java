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
	private TextBox userIDBox, passwordBox, confirmPasswordBox;
	private JTextField userID;
	private JPasswordField pw, confirmPw;

	private RegisterSuccess registerSuccess;
	private boolean success;

	public Register(Game game) {
		super(game);

		loadBackGround();
		loadImg();
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
		loginButton[0] = new LoginButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE + 200), 1, Gamestate.REGISTER);
		loginButton[1] = new LoginButton(B_WIDTH / 2, (int) (Game.GAME_HEIGHT - B_HEIGHT), 0, Gamestate.LOGIN);
	}

	public void loadContainer() {
		SetUpComponent();
		userIDBox = new TextBox(menuX + 20, menuY + 185, 1);
		passwordBox = new TextBox(menuX + 20, menuY + 235, 1);
		confirmPasswordBox = new TextBox(menuX + 20, menuY + 285, 1);
	}

	@Override
	public void update(long currTime) {

		if (success) {
			registerSuccess.update();
		} else {
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

			for (LoginButton lg : loginButton) {
				lg.update();
			}
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
		for (LoginButton lg : loginButton)
			if (isIn(e, lg)) {
				if (lg.isMousePressed()) {
					lg.applyGamestate();// apply only when Pressed before and Release after if pressed in button but
					if (lg.getState() == Gamestate.LOGIN) {
						Gamestate.state = Gamestate.LOGIN;
						game.getLogin().SetUpComponent();
						game.getLogin().addComponent();
						resetTextField();
					} else if (lg.getState() == Gamestate.REGISTER) {
						resetTextField();
						success = User.Register(userID.getText(), pw.getText());
						System.out.println(success);
					}
					// when release mouse position is out side bound of button, we didn't apply that
					// state
					break;
				}
			}
		if (success)
			registerSuccess.MouseRelease(e);
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
		userID.setVisible(false);
		pw.setVisible(false);
		confirmPw.setVisible(false);
	}

	public void SetUpComponent() {
		userID = new JTextField("username");
		Font font = new Font("Arial", Font.PLAIN, 18); // Tạo font chữ mới với kích thước 18
		userID.setFont(font);
		userID.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (userID.getText().equals("username")) {
					userID.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (userID.getText().isEmpty()) {
					userID.setText("username");
				}
			}
		});
		pw = new JPasswordField("password");
		pw.setFont(font);
		pw.setEchoChar('\0');

		pw.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (String.valueOf(pw.getPassword()).equals("password")) {
					pw.setEchoChar('●');
					pw.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (String.valueOf(pw.getPassword()).isEmpty()) {
					pw.setEchoChar('\0');
					pw.setText("password");
				}
			}
		});
		confirmPw = new JPasswordField("confirm password");
		confirmPw.setFont(font);
		confirmPw.setEchoChar('\0');

		confirmPw.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (String.valueOf(confirmPw.getPassword()).equals("confirm password")) {
					confirmPw.setEchoChar('●');
					confirmPw.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (String.valueOf(confirmPw.getPassword()).isEmpty()) {
					confirmPw.setEchoChar('\0');
					confirmPw.setText("confirm password");
				}
			}
		});
		userID.setBorder(null);
		userID.setOpaque(false);
		pw.setBorder(null);
		pw.setOpaque(false);
		confirmPw.setBorder(null);
		confirmPw.setOpaque(false);
	}

	public void addComponent() {
		game.getGamePanel().add(userID);
		game.getGamePanel().add(pw);
		game.getGamePanel().add(confirmPw);

	}
}
