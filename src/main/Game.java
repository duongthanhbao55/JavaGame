package main;

import java.awt.Graphics;

import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import untilz.LoadSave;

public class Game implements Runnable{
	
	private GameWindow gameWindow;//set up window to draw on it
	private GamePanel gamePanel;//control all the thing we draw on Window
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 180;
	
	private Playing playing;
	private Menu menu;
	

	//private LevelManager levelManager;
	
	public final static int TILE_DEFAULT_SIZE = 16;
	public final static float SCALE = 1.5f;
	public final static int TILES_IN_WIDTH = 60;
	public final static int TILES_IN_HEIGHT = 30;
	public final static int TILES_SIZE = (int)(TILE_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	public final static int COLLISION_LAYER_MAP1 = 0;
	public final static int COLLISION_LAYER_MAP2_1 = 1;
	public final static int COLLISION_LAYER_MAP2_2 = 3;
	
	public Game()
	{
		initClasses();
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();//input focus on this class
		startGameLoop();
	}
	
	private void initClasses() {
		menu = new Menu(this);
		playing = new Playing(this);
		//LoadSave.GetAllLevels();
	}

	public void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void update(long currTime) {
		switch(Gamestate.state) {
		case MENU:
			menu.update(currTime);
			break;
		case PLAYING:
			playing.update(currTime);
			break;
		case OPTIONS:
		case QUIT:
		default:
			System.exit(0);
			break;
		
		}
		
		
	}
	public void render(Graphics g) {	
		
		
		switch(Gamestate.state) {
		case MENU:
			menu.render(g);
			break;
		case PLAYING:
			playing.render(g);
			break;
		default:
			break;
		
		}
	}
	
	@Override
	public void run() {
		
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
		
		long previousTime = System.nanoTime();
		
		
		
		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();		
		
		double deltaU = 0;
		double deltaF = 0;
		while(true) {
			
			
			long currTime = System.nanoTime();
			deltaU += (currTime - previousTime)/ timePerUpdate;
			deltaF += (currTime - previousTime)/ timePerFrame;
			previousTime = currTime;
			
			if(deltaU >= 1)
			{
				update(currTime);
				updates++;
				deltaU--;
			}
			if(deltaF >= 1)
			{
				gamePanel.repaint();//repaint already have render function
				frames++;
				deltaF--;
			}
			
			
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
			
		}
			
	}
	public void WindowFocusLost() {
		if(Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}
	public Menu getMenu() {
		return menu;
	}
	public Playing getPlaying() {
		return playing;
	}
	
	
}
