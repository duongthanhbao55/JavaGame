package main;

import java.awt.Graphics;
import java.sql.SQLException;
import java.util.HashMap;

import Template.TaskTemplate;
import audio.AudioPlayer;
import database.MySQL;
import entities.Player;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Login;
import gamestates.Menu;
import gamestates.Playing;
import gamestates.Register;
import gamestates.SetPlayerName;
import ui.AudioOptions;
import untilz.HelpMethods;

public class Game implements Runnable{
	
	private GameWindow gameWindow;//set up window to draw on it
	private GamePanel gamePanel;//control all the thing we draw on Window
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 180;
	
	private Playing playing = null;
	private Menu menu;
	private Login login;
	private Register register;
	private SetPlayerName setPlayerName;
	private GameOptions gameOptions;
	private AudioOptions audioOptions;
	private AudioPlayer audioPlayer;
	
	
	protected static int post;
    private static String host;
    private static String mysql_host;
    private static String mysql_database;
    private static String mysql_user;
    private static String mysql_pass;
    protected static byte vData;
    protected static byte vMap;
    protected static byte vSkill;
    protected static byte vItem;
    protected static byte vEvent;
    protected static byte vEvent_1;
    protected static byte max_CreateChar;
    protected static int max_upLevel;
    protected static byte max_Friend;
    protected static byte max_Enemies;
    protected static byte qua_top;
    protected static int up_exp;
    
    
    public static long exps[];
    public static byte[][] tasks;//id npc u need to talk( -1 talk with npc or orther mission, -2 kill enemies task or)
    public static byte[][] mapTasks;// index map of task( -1 talk with npc or orther mission,if(-2) if(tasks == idmap) => kill enemeis else if(task == -2) => useItemTask}
 
    public static TaskTemplate[] taskTemplates;

	
	public final static int TILE_DEFAULT_SIZE = 16;
	public final static float SCALE = 1.5f;
	public final static int TILES_IN_WIDTH = 60;
	public final static int TILES_IN_HEIGHT = 30;
	public final static int TILES_SIZE = (int)(TILE_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
	public Game()
	{
		gamePanel = new GamePanel(this);		
		initClasses();
		gameWindow = new GameWindow(gamePanel);	
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();//input focus on this class
		startGameLoop();
	}
	
	private void initClasses() {
		init();
		Init.init();
		audioOptions = new AudioOptions(this);
		audioPlayer = new AudioPlayer();
		gameOptions = new GameOptions(this);
		menu = new Menu(this);
		login = new Login(this);
		register = new Register(this);
		setPlayerName = new SetPlayerName(this);
		playing = new Playing(this);
	}
	public void reStart() {
		if(playing.isReStart()) {
			playing = new Playing(this);
			playing.initPlayer(Player.getPlayer(login.getUser(), MySQL.getPlayerID(login.getUser().getUsername())));
			playing.setReStart(false);
		}
	}
	public void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void update(long currTime) {

		switch(Gamestate.state) {
		case LOGIN:
			if(playing.getLogout()) {
				playing = new Playing(this);
				playing.setLogout(false);
			}
			login.setLoginState(true);
			login.update(currTime);
			break;
		case REGISTER:
			register.update(currTime);
			break;
		case SETNAME:
			setPlayerName.update(currTime);
			break;
		case MENU:			
			reStart();		
			menu.update(currTime);
			break;
		case PLAYING:
			reStart();
			playing.update(currTime);
			break;
		case OPTIONS:
			gameOptions.update(currTime);
			break;
		case QUIT:
			//TODO something
			
		default:
			System.exit(0);
			break;
		
		}
		
		
	}
	public void render(Graphics g) {	
		
		
		switch(Gamestate.state) {
		case LOGIN:
			login.render(g);
			break;
		case REGISTER:
			register.render(g);
			break;
		case SETNAME:
			setPlayerName.render(g);
			break;
		case MENU:
			menu.render(g);
			break;
		case PLAYING:
			playing.render(g);
			break;
		case OPTIONS:
			gameOptions.render(g);
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
				
//				System.out.println("FPS: " + frames + " | UPS: " + updates);
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
	public Login getLogin() {
		return login;
	}
	public Register getRegister() {
		return register;
	}
	public GameOptions getGameOptions() {
		return gameOptions;
	}
	public AudioOptions getAudioOptions() {
		return audioOptions;
	}
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
	public SetPlayerName getSetNamePlayer() {
		return setPlayerName;
	}
	public GamePanel getGamePanel() {
		return gamePanel;
	}
	public void initRegister() {
		register = new Register(this);
	}
	private static void loadConfigFile() {
        final byte[] ab = MainClass.getFile("ninja.conf");
        if (ab == null) {
            System.out.println("Config file not found!");
            System.exit(0);
        }
        final String data = new String(ab);
        final HashMap<String, String> configMap = new HashMap<String, String>();
        final StringBuilder sbd = new StringBuilder();
        boolean bo = false;
        for (int i = 0; i <= data.length(); ++i) {
            final char es;
           
            if (i == data.length() || (es = data.charAt(i)) == '\n') {
                bo = false;
                final String sbf = sbd.toString().trim();
                if (sbf != null && !sbf.equals("") && sbf.charAt(0) != '#') {
                    final int j = sbf.indexOf(58);
                    if (j > 0) {
                        final String key = sbf.substring(0, j).trim();
                        final String value = sbf.substring(j + 1).trim();
                        configMap.put(key, value);
                        System.out.println("config: " + key + "-" + value);
                    }
                }
                sbd.setLength(0);
            }
            else {
                if (es == '#') {
                    bo = true;
                    System.out.println(true);
                }
                if (!bo) {
                    sbd.append(es);
                }
            }
        }
        if (configMap.containsKey("debug")) {
            HelpMethods.setDebug(Boolean.parseBoolean(configMap.get("debug")));
        }
        else {
            HelpMethods.setDebug(false);
        }
        if (configMap.containsKey("host")) {
        	Game.host = configMap.get("host");
        }
        else {
        	Game.host = "localhost";
        }
        if (configMap.containsKey("post")) {
        	Game.post = Short.parseShort(configMap.get("post"));
        }
        else {
        	Game.post = 14444;
        }
        if (configMap.containsKey("mysql-host")) {
        	Game.mysql_host = configMap.get("mysql-host");
        }
        else {
        	Game.mysql_host = "localhost";
        }
        if (configMap.containsKey("mysql-user")) {
        	Game.mysql_user = configMap.get("mysql-user");
        }
        else {
        	Game.mysql_user = "root";
        }
        if (configMap.containsKey("mysql-password")) {
        	Game.mysql_pass = configMap.get("mysql-password");
        }
        else {
        	Game.mysql_pass = "";
        }
        if (configMap.containsKey("mysql-database")) {
            Game.mysql_database = configMap.get("mysql-database");
        }
        else {
            Game.mysql_database = "ninja";
        }
        if (configMap.containsKey("version-Data")) {
            Game.vData = Byte.parseByte(configMap.get("version-Data"));
            Game.vData += HelpMethods.nextInt(10);
        }
        else {
            Game.vData = 1;
        }
        if (configMap.containsKey("version-Map")) {
            Game.vMap = Byte.parseByte(configMap.get("version-Map"));
            Game.vMap += HelpMethods.nextInt(10);
        }
        else {
            Game.vMap = 1;
        }
        if (configMap.containsKey("version-Skill")) {
            Game.vSkill = Byte.parseByte(configMap.get("version-Skill"));
        }
        else {
            Game.vSkill = 1;
        }
        if (configMap.containsKey("version-Item")) {
            Game.vItem = Byte.parseByte(configMap.get("version-Item"));
            Game.vItem += HelpMethods.nextInt(10);
        }
        else {
            Game.vItem = 1;
        }
        if (configMap.containsKey("version-Event")) {
            Game.vEvent = Byte.parseByte(configMap.get("version-Event"));
        }
        else {
            Game.vEvent = 0;
        }
        if (configMap.containsKey("version-Event_1")) {
            Game.vEvent_1 = Byte.parseByte(configMap.get("version-Event_1"));
        }
        else {
            Game.vEvent_1 = 0;
        }
        if (configMap.containsKey("max-taoNhanVat")) {
            Game.max_CreateChar = Byte.parseByte(configMap.get("max-taoNhanVat"));
        }
        else {
            Game.max_CreateChar = 3;
        }
        if (configMap.containsKey("max-upLevel")) {
            Game.max_upLevel = Integer.parseInt(configMap.get("max-upLevel"));
        }
        else {
            Game.max_upLevel = 130;
        }
        if (configMap.containsKey("max-Friend")) {
            Game.max_Friend = Byte.parseByte(configMap.get("max-Friend"));
        }
        else {
            Game.max_Friend = 50;
        }
        if (configMap.containsKey("max-Enemies")) {
            Game.max_Enemies = Byte.parseByte(configMap.get("max-Enemies"));
        }
        else {
            Game.max_Enemies = 20;
        }
        if (configMap.containsKey("qua-top")) {
            Game.qua_top = Byte.parseByte(configMap.get("qua-top"));
        }
        else {
            Game.qua_top = 0;
        }
        if (configMap.containsKey("up-exp")) {
            Game.up_exp = Integer.parseInt(configMap.get("up-exp"));
        }
        else {
            Game.up_exp = 1;
        }
    }
    
    protected static void init() {
        loadConfigFile();
        try {
            //MySQL.createConnection(0, Game.mysql_host, "hiepsiro_nsodata", Game.mysql_user, Game.mysql_pass);
            MySQL.createConnection(0, Game.mysql_host, Game.mysql_database, Game.mysql_user, Game.mysql_pass);
            //Init.init();
            final MySQL mySQL = new MySQL(0);
            //mySQL.stat.executeUpdate("UPDATE `user` SET `online` = 0;");
            //mySQL.stat.executeUpdate("UPDATE `player` SET `caveId` = -1;");
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    } 
	
}
