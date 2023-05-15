package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import Level.EnemyManager;
import Level.LevelManager;
import Level.NPCManager;
import Load.CacheDataLoader;
import Map.TileLayer;
import Task.Task;
import Template.NpcTemplate;
import database.ItemManager;
import entities.NPC;
import entities.Player;
import main.Game;
import objects.Equipment;
import objects.EquipmentEffect;
import objects.InventoryManager;
import objects.ObjectManager;
import objects.PlayerStatus;
import ui.Confirm;
import ui.GameOverOverlay;
import ui.LevelCompleteOverlay;
import ui.PauseOverlay;
import ui.Selector;
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
	private NPCManager npcManager;
	private ItemManager itemManager;
	private InventoryManager inventoryManager;
	private Equipment equipment;
	private EquipmentEffect equipmentEffect;
	private Confirm confirmUI;
	private PlayerStatus playerStatus;

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
	private boolean playerDying;

	// CONSTRUCTOR
	public Playing(Game game) {
		super(game);
		initClasses();
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++) {
			smallCloudsPos[i] = (int) (70 * Game.SCALE) + rnd.nextInt((int) (150 * Game.SCALE));
		}
	}

	public void loadNextLevel() {
		resetAll();
		//.loadNextLevel();
		player.setSpawn(levelManager.getCurrLevel().getPlayerSpawn());
	}

	public void initTask() {
		for (NpcTemplate npc : NPCManager.arrNpcTemplate) {
			if (Task.isTaskNPC(player, (short) npc.npcTemplateId)) {
				npcManager.getNpcWizard1s().get(npc.npcTemplateId).setHaveTask(true, player);
				NPC.setCurrNpcId(npc.npcTemplateId);
				break;
			}
		}
	}

	public void initPlayer(Player player) {
		this.player = player;
		this.player.setPlaying(this);
		levelManager = new LevelManager(game);
		ArrayList<TileLayer> mapLayer = levelManager.getCurrLevel().getMapLayer();
		player.LoadLvlData(mapLayer.get(0).getTileMap());
		enemyManager = new EnemyManager(this);
		inventoryManager.initDataInventory();
		equipment.initEquipment();
		loadAll();
		initTask();
	}

	private void initClasses() {

		CacheDataLoader.getInstance().readAllMap(this);
		objectManager = new ObjectManager(this);
		npcManager = new NPCManager(this);
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompleteOverlay = new LevelCompleteOverlay(this);
		confirmUI = new Confirm(this);
		itemManager = new ItemManager(this);
		inventoryManager = new InventoryManager(this);
		equipment = new Equipment(this);
		playerStatus = new PlayerStatus(this);
		Selector.getInstance().setBounds(inventoryManager.getSlots()[0].getBounds());
		Selector.getInstance().setSlotEquipment(equipment.getSlots());
		Selector.getInstance().setSlotInventory(inventoryManager.getSlots());
		equipmentEffect = new EquipmentEffect(this);
		Selector.getInstance().setEquipmentEffect(equipmentEffect);
	}

	@Override
	public void update(long currTime) {
		int[][] collisionLayer = levelManager.getCurrLevel().getMapLayer().get(0).getTileMap();
		if (paused) {
			pauseOverlay.update();
		} else if (lvlCompleted) {
			levelCompleteOverlay.update();
		} else if (gameOver) {
			gameOverOverlay.update();
		} else if (playerDying) {
			player.update(currTime);
		} else {
			levelManager.getCurrLevel().Update();
			levelManager.update();
			objectManager.update(currTime, collisionLayer, player);
			npcManager.update(currTime, collisionLayer, player);
			player.update(currTime);
			
			enemyManager.update(currTime, collisionLayer, player);
			itemManager.update();			
			if (Confirm.isShow())
				confirmUI.update();
			if(inventoryManager.isOpen()) {
				inventoryManager.update();
				equipment.update();
				playerStatus.update();
				Selector.getInstance().update(currTime);
			}
			CheckCloseToBorder();
		}
	}

	private void loadStartLevel() {
		enemyManager.loadEnimies(levelManager.getCurrLevel());
		objectManager.loadObject(levelManager.getCurrLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrLevel().getLvlOffset();
		mapWidth = levelManager.getCurrLevel().getLvlTilesWide() * Game.TILES_SIZE;
	}

	public void loadAll() {
		calcLvlOffset();
		loadStartLevel();
		loadImages();
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
		npcManager.render(g, xLvlOffset);
		itemManager.render(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.render(g, xLvlOffset);
		npcManager.drawDialogue(g);
		
		if(inventoryManager.isOpen()) {
			inventoryManager.render(g);
			equipment.render(g);
			playerStatus.render(g);
			Selector.getInstance().render(g);
		}
		if (Confirm.isShow())
			confirmUI.render(g,xLvlOffset);
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
		playerDying = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObject();
		npcManager.resetNPC();
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
			else if (Confirm.isShow())
				confirmUI.MousePressed(e);
			else if(inventoryManager.isOpen())
				inventoryManager.mousePressed(e);
			
		} else {
			gameOverOverlay.MousePressed(e);
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseReleased(e);
			else if (lvlCompleted)
				levelCompleteOverlay.MouseRelease(e);
			else if (Confirm.isShow())
				confirmUI.MouseRelease(e);
			else if(inventoryManager.isOpen())
				inventoryManager.mouseReleased(e);
		} else {
			gameOverOverlay.MouseRelease(e);
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseMoved(e);
			else if (lvlCompleted)
				levelCompleteOverlay.MouseMoved(e);
			else if (Confirm.isShow())
				confirmUI.MouseMoved(e);
			else if(inventoryManager.isOpen())
				inventoryManager.mouseMoved(e);
		} else {
			gameOverOverlay.MouseMoved(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver)
			gameOverOverlay.keyPressed(e);
		else
			if(inventoryManager.isOpen()) {
				Selector.getInstance().keyPressed(e);
			}
			switch (e.getKeyCode()) {

			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_W:
				player.setJump(true);
				break;
			case KeyEvent.VK_J:
				player.setAttack1(true);
				break;
			case KeyEvent.VK_F:
				checkNPCContact(player.getHitbox());
				checkPotionTouched(player.getHitbox());
				checkItemContact(player);
				break;
			case KeyEvent.VK_B:
				inventoryManager.setOpen(!inventoryManager.isOpen());
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
			case KeyEvent.VK_W:
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

	public void checkNPCContact(Rectangle2D.Float hitbox) {
		npcManager.checkNPCTouched(hitbox, player);
	}

	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	public void checkSpikesTouched(Player player) {
		objectManager.checkSpikesTouched(player);
	}

	public void checkItemContact(Player player) {
		itemManager.checkItemContact(player);
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

	public LevelManager getLevelManager() {
		return this.levelManager;
	}

	public NPCManager getNpcManager() {
		return this.npcManager;
	}

	public ItemManager getItemManager() {
		return this.itemManager;
	}

	public InventoryManager getInventoryManager() {
		return this.inventoryManager;
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}

	public void setLevelCompleted(boolean lvlCompleted) {
		this.lvlCompleted = lvlCompleted;
	}

}
