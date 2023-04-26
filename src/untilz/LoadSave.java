package untilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import Load.CacheDataLoader;
import Map.PhysicalMap;

public class LoadSave {


	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String MENU_BACKGROUND_IMG = "Grassy_Mountains_preview_fullcolor.png";
	public static final String PLAYING_BG_IMG = "parallax-mountain-bg.png";
	public static final String BIG_CLOUNDS = "big_clouds.png";
	public static final String SMALL_CLOUNDS = "small_clouds.png";
	public static final String BG_SKY = "sky.png";
	public static final String BG_FAR_CLOUDS = "far-clouds.png";
	public static final String BG_NEAR_CLOUDS = "near-clouds.png";
	public static final String BG_FAR_MOUNTAINS = "far-mountains.png";
	public static final String BG_MOUNTAINS = "mountains.png";
	public static final String BG_TREES = "trees.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String COMPLETED_IMG = "completed_sprite.png";
	public static final String BACKGROUND_SCENE = "PIXEL SCENERY_BACKGROUNDS MASTERPOST.gif";
	public static final String DEATH_SCREEN = "death_screen.png";
	public static final String OPTION_BACKGROUND = "options_background.png";
	public static final String TEXT_BOX = "panel_Example2.png";
	public static final String LOGIN_BACKGROUND = "Login_BackGround.png";
	public static final String LOGIN_BUTTON = "LoginButton.png";
	public static final String UI_BUTTON = "UIButton.png";
	public static final String WELCOME_BANNER = "WelcomeBanner.png";
	public static final String EMPTY_BACKGROUND = "EmptyBoard.png";
	public static final String TICK = "tick.png";
	public static final String SUCCESS_BACKGROUND = "RegisterSuccess.png";
	public static final String MESSAGE_EXCLAMATION = "Message-Exclamation.png";
	public static final String MESSAGE_QUESTION = "Message-Question.png";
	
	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}
}
