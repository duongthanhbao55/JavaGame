package gamestates;

import static Load.CacheDataLoader.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import Level.EnemyManager;
import Level.LevelManager;
import Load.CacheDataLoader;
import Map.TileLayer;
import entities.Player;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompleteOverlay;
import ui.PauseOverlay;
import untilz.LoadSave;
import static untilz.Constants.Enviroment.*;

public class Playing extends State implements Statemethods {
	// VARIABLE
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private LevelCompleteOverlay levelCompleteOverlay;
	//
	private boolean paused = false;

	// CAMERA
	private int xLvlOffset = 0;
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;
	private int mapWidth;
	// BACKGROUND
	private BufferedImage backgroundImg, farCloud, nearCloud, farMountain, Mountain, tree;
	private int[] smallCloudsPos;
	private Random rnd = new Random();

	private boolean gameOver;
	private boolean lvlCompleted;

	// CONSTRUCTOR
	public Playing(Game game) {
		super(game);
		initClasses();

		calcLvlOffset();
		loadStartLevel();
		loadImages();
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++) {
			smallCloudsPos[i] = (int) (70 * Game.SCALE) + rnd.nextInt((int) (150 * Game.SCALE));
		}
		
	}

	public void loadNextLevel() {
		resetAll();
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrLevel().getPlayerSpawn());
	}

	private void loadStartLevel() {
		enemyManager.loadEnimies(levelManager.getCurrLevel());
		objectManager.loadObject(levelManager.getCurrLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrLevel().getLvlOffset();
		mapWidth = levelManager.getCurrLevel().getLvlTilesWide() * Game.TILES_SIZE;
	}

	// FUNCTION
	private void loadImages() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.BG_SKY);
		farCloud = LoadSave.GetSpriteAtlas(LoadSave.BG_FAR_CLOUDS);
		nearCloud = LoadSave.GetSpriteAtlas(LoadSave.BG_NEAR_CLOUDS);
		farMountain = LoadSave.GetSpriteAtlas(LoadSave.BG_FAR_MOUNTAINS);
		Mountain = LoadSave.GetSpriteAtlas(LoadSave.BG_MOUNTAINS);
		tree = LoadSave.GetSpriteAtlas(LoadSave.BG_TREES);

	}

	private void initClasses() {
		player = new Player(200, 200, (int) (Game.TILES_SIZE * 4), (int) (Game.TILES_SIZE * 2), this);
		CacheDataLoader.getInstance().readAllMap(this);
		levelManager = new LevelManager(game);

		ArrayList<TileLayer> mapLayer = levelManager.getCurrLevel().getMapLayer();
		player.LoadLvlData(mapLayer.get(0).getTileMap());
		player.setSpawn(levelManager.getCurrLevel().getPlayerSpawn());
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompleteOverlay = new LevelCompleteOverlay(this);
	}

	

	@Override
	public void update(long currTime) {
		int[][] collisionLayer = levelManager.getCurrLevel().getMapLayer().get(0).getTileMap();
		if (paused) {
			pauseOverlay.update();
		} else if (lvlCompleted) {
			levelCompleteOverlay.update();
		} else if (!gameOver) {
			levelManager.getCurrLevel().Update();
			objectManager.update(currTime, collisionLayer, player);
			player.update(currTime);
			enemyManager.update(currTime, collisionLayer, player);
			CheckCloseToBorder();
		}
	}

	private void CheckCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;

	}

	@Override
	public void render(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		drawClouds(g);

		levelManager.getCurrLevel().Render(g, xLvlOffset);
		objectManager.render(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.render(g, xLvlOffset);

		if (paused) {
			pauseOverlay.render(g);
		} else if (gameOver) {
			gameOverOverlay.Draw(g);
		} else if (lvlCompleted) {
			levelCompleteOverlay.render(g);
		}
			
	}

	public ObjectManager getObjectManager() {
		return objectManager;
	}

	private void drawClouds(Graphics g) {
		for (int i = 0; i < (int) (mapWidth / FAR_CLOUD_WIDTH); i++) {
			g.drawImage(farCloud, i * FAR_CLOUD_WIDTH - (int) (xLvlOffset * 0.1), (int) (236 * Game.SCALE),
					FAR_CLOUD_WIDTH, FAR_CLOUD_HEIGHT, null);
		}
		for (int i = 0; i < (int) (mapWidth / NEAR_CLOUD_WIDTH); i++) {
			g.drawImage(nearCloud, i * NEAR_CLOUD_WIDTH - (int) (xLvlOffset * 0.2), (int) (236 * Game.SCALE),
					NEAR_CLOUD_WIDTH, NEAR_CLOUD_HEIGHT, null);
		}
		for (int i = 0; i < (int) (mapWidth / FAR_MOUNTAIN_WIDTH); i++) {
			g.drawImage(farMountain, i * FAR_MOUNTAIN_WIDTH - (int) (xLvlOffset * 0.4), (int) (236 * Game.SCALE),
					FAR_MOUNTAIN_WIDTH, FAR_MOUNTAIN_HEIGHT, null);
		}
		for (int i = 0; i < (int) (mapWidth / MOUNTAIN_WIDTH); i++) {
			g.drawImage(Mountain, i * MOUNTAIN_WIDTH - (int) (xLvlOffset * 0.6), (int) (236 * Game.SCALE),
					MOUNTAIN_WIDTH, MOUNTAIN_HEIGHT, null);
		}
		for (int i = 0; i < (int) (mapWidth / TREE_SIZE); i++) {
			g.drawImage(tree, i * TREE_SIZE - (int) (xLvlOffset * 0.8), (int) (236 * Game.SCALE), TREE_SIZE, TREE_SIZE,
					null);
		}
//		for(int i = 0; i < smallCloudsPos.length; i++) {
//			g.drawImage(smallCloud, (SMALL_CLOUD_WIDTH * 4 * i) - (int)(xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
//		}

	}

	public void resetAll() {
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObject();
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void mouseDragged(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseDragged(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mousePressed(e);
			else if (lvlCompleted)
				levelCompleteOverlay.MousePressed(e);
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseReleased(e);
			else if (lvlCompleted)
				levelCompleteOverlay.MouseRelease(e);
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseMoved(e);
			else if (lvlCompleted)
				levelCompleteOverlay.MouseMoved(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver)
			gameOverOverlay.keyPressed(e);
		else

			switch (e.getKeyCode()) {

			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true);
				break;
			case KeyEvent.VK_J:
				player.setAttack1(true);
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;
			}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver)
			switch (e.getKeyCode()) {


			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;
			}
	}

	public void unpauseGame() {
		paused = false;
	}

	public Player getPlayer() {
		return player;
	}

	public void WindowFocusLost() {
		player.resetDirBooleans();
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHited(attackBox, player);
	}
	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}
	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}
	public void checkSpikesTouched(Player player) {
		objectManager.checkSpikesTouched(player);
	}
	
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}
	public int getMaxLvlOffsetX() {
		return maxLvlOffsetX;
	}

	public void setMaxLvlOffsetX(int maxLvlOffsetX) {
		this.maxLvlOffsetX = maxLvlOffsetX;
	}
	public void setLevelCompleted(boolean lvlCompleted) {
		this.lvlCompleted = lvlCompleted;
	}
	public LevelManager getLevelManager() {
		return this.levelManager;
	}
}
