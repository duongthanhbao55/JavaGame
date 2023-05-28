package database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.regex.Pattern;

import gamestates.Login;
import objects.InventoryManager;

public class MySQL {
	protected final int MySQL_ID;
	public Statement stat;
	protected final Object LOCK;
	public static int baseId;
	protected static final Object LOCK_MYSQL;
	public static final int DATABASE_DATA = 0;
	public static final int DATABASE_GAME = 1;
	private static final HashMap<Integer, Connection> conn_map;
	private static final String URL_FORMAT = "jdbc:mysql://%s/%s";

	public MySQL(final int connId) throws SQLException {
		this.LOCK = new Object();
		synchronized (MySQL.LOCK_MYSQL) {
			this.MySQL_ID = MySQL.baseId++;
			this.stat = MySQL.conn_map.get(connId).createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
		}
	}

	public void close() {
		try {
			synchronized (this.LOCK) {
				this.stat.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection createConnection(final int connId, final String host, final String database,
			final String user, final String pass) {
		try {
			return MySQL.conn_map.put(connId,
					DriverManager.getConnection(String.format("jdbc:mysql://%s:3306/%s", host, database), user, pass));
		} catch (SQLException e2) {
			e2.printStackTrace();
			System.exit(0);
			return null;
		}
	}

	public static Connection getConnection(final int connId) {
		return MySQL.conn_map.get(connId);
	}

	public static boolean disconnect(final int connId) {
		try {
			MySQL.conn_map.remove(connId).close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	static {
		MySQL.baseId = 0;
		LOCK_MYSQL = new Object();
		conn_map = new HashMap<Integer, Connection>();
	}

	// INSERT ACCOUNT
	public static void insertAccounts(String email, String username, String password) {
		Connection connection = getConnection(0);
		String SQLquery = "INSERT INTO accounts(email, username, password) VALUES(?,?,?)";
		try {
			PreparedStatement preparedstatement = connection.prepareStatement(SQLquery);
			preparedstatement.setString(1, email);
			preparedstatement.setString(2, username);
			preparedstatement.setString(3, password);
			int rs = preparedstatement.executeUpdate();
			System.out.println("Insert " + rs + " account");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setNickname(String username, String nickname) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL setNickname(?,?)}");
			cs.setString(1, username);
			cs.setString(2, nickname);
			cs.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String findUsername(String username) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{? = CALL findUsername(?)}");
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, username);
			cs.execute();
//			String rs = cs.getString(1);
//			System.out.println(rs);
			return cs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String findNickname(String nickname) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{? = CALL findNickname(?)}");
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, nickname);
			cs.execute();
//			String rs = cs.getString(1);
//			System.out.println(rs);
			return cs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String findEmail(String email) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{? = CALL findEmail(?)}");
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, email);
			cs.execute();
//			String rs = cs.getString(1);
//			System.out.println(rs);
			return cs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String findPassword(String username) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{? = CALL findPassword(?)}");
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, username);
			cs.execute();
//			String rs = cs.getString(1);
//			System.out.println(rs);
			return cs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String haveNickname(String username) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{? = CALL haveNickname(?)}");
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, username);
			cs.execute();
			return cs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static int getPlayerID(String username) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{? = CALL findPlayerID(?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, username);
			cs.execute();
			return cs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	// Check password is correct

	public static boolean passwordIsCorrect(String username, String password) {
		System.out.println("check password");
		return password.equals(findPassword(username));
	}

	// Check login

	public static boolean loginSuccessfully(String username, String password,Login login) {
		if (usernameWasUsed(username) && passwordIsCorrect(username, password)) {
			System.out.println("Login Successfully");
			return true;
		}
		login.setWarningIndex(0);
		System.out.println("Username or password INCORRECT");
		return false;
	}

	// Check username was used

	public static boolean usernameWasUsed(String username) {
		System.out.println("check username");
		return username.equals(findUsername(username));
	}

	// Check email was used

	public static boolean emailWasUsed(String email) {
		return email.equals(findEmail(email));
	}

	// Check nickname was used

	public static boolean nicknameWasUsed(String nickname) {
		return nickname.equals(findNickname(nickname));
	}

	// Check register

	public static void registerSuccessfully(String email, String firstname, String lastname, String username,
			String password) {
		// do something
	}

	// check email valid

	public static boolean emailIsValid(String email) { // return true if email valid
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null) {
			return false;
		}
		return pat.matcher(email).matches();
	}

	// GET UERID

	public static int getUserID(String username) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{? = CALL getUserID(?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, username);
			cs.execute();
			return cs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	// CREATE playerID

	public static int createPlayerID(int userID) {
		int playerID;
		String temp = String.valueOf(userID);
		playerID = temp.hashCode();
		return playerID;
	}
	// CREATE PLAYER

	public static void createPlayer(int playerID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL createPlayer(?)}");
			cs.setInt(1, playerID);
			cs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// CREATE MOB STATUS

	public static void createMobStatus(int playerID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL createMobStatus(?)}");
			cs.setInt(1, playerID);
			cs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// SAVE MOB STATUS
	public static void saveMobStatus(int index,int xMob, int yMob, int health,int status, long refreshTime, long deadTime, int playerID,
			int mobID, int mapID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL saveMobStatus(?,?,?,?,?,?,?,?,?,?)}");
			System.out.println("xMob: " + xMob);
			System.out.println("yMob: " + yMob);
			System.out.println("index: " + index);
			cs.setInt(1, index);
			cs.setInt(2, xMob);
			cs.setInt(3, yMob);
			cs.setInt(4, status);
			cs.setInt(5, health);
			cs.setLong(6, refreshTime);
			cs.setLong(7, deadTime);
			cs.setInt(8, playerID);
			cs.setInt(9, mobID);
			cs.setInt(10, mapID);
			cs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// CREATE NPC STATUS

	public static void createNpcStatus(int playerID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL createNpcStatus(?)}");
			cs.setInt(1, playerID);
			cs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// SAVE NPC STATUS
	public static void saveNpcStatus(int xNpc, int yNpc, int map_id, int playerID, int npcID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL saveNpcStatus(?,?,?,?,?)}");
			cs.setInt(1, xNpc);
			cs.setInt(2, yNpc);
			cs.setInt(3, map_id);
			cs.setInt(4, playerID);
			cs.setInt(5, npcID);
			cs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// UPDATE PLAYERID IN ACCOUNTS

	public static void updatePlayerIdInAccounts(int userID, int playerID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL updateAccountPlayerID(?,?)}");
			cs.setInt(1, userID);
			cs.setInt(2, playerID);
			cs.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ITEM IS EXISTS

	public static int itemIsExistsInBalo(int itemID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{? = CALL itemIsExists(?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, itemID);
			cs.execute();
			return cs.getInt(1); // return 0 if the username is not found in database
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	// INCREASE QUANTITY ITEM

	public static void increaseQuantityItem(int playerID, int itemID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL increaseQuantityItem(?,?)}");
			cs.setInt(1, playerID);
			cs.setInt(2, itemID);
			cs.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// add item to the balo

	public static void baloAddItem(int playerID, int itemID, int squarePosition, int isEquip) {
		if (itemID == itemIsExistsInBalo(itemID)) {
			increaseQuantityItem(playerID, itemID);
		} else {
			Connection connection = getConnection(0);
			try {
				CallableStatement cs = connection.prepareCall("{CALL invenAddItem(?,?,?,?)}");
				cs.setInt(1, playerID);
				cs.setInt(2, itemID);
				cs.setInt(3, squarePosition);
				cs.setInt(4, isEquip);
				cs.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// drop item in balo

	public static void baloDropItem(int playerID, int itemID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL invenDropItem(?,?)}");
			cs.setInt(1, playerID);
			cs.setInt(2, itemID);
			cs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//equip item
	
	// start do task

	public static void startDoTask(int playerID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL startDoTask(?,?,?)}");
			cs.setInt(1, playerID);
			cs.setInt(2, 0);
			cs.setInt(3, 0);
			cs.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	// finish do task

	public static void finishDoTask(int playerID, int taskID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL finishDoTask(?,?)}");
			cs.setInt(1, playerID);
			cs.setInt(2, taskID);
			cs.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// drop mission

	public static void dropMission(int playerID, int taskID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL dropMission(?,?)}");
			cs.setInt(1, playerID);
			cs.setInt(2, taskID);
			cs.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// save task state

	public static void saveTaskState(int playerID, int taskID, int taskIdx, int taskC) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL saveTaskState(?,?,?,?)}");
			cs.setInt(1, playerID);
			cs.setInt(2, taskID);
			cs.setInt(3, taskIdx);
			cs.setInt(4, taskC);
			cs.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// save player state

	public static void savePlayerState(int exp, int level, int atk, int def, int hp,int mana, int coin, int x, int y, int mapID,
			int playerID) {
		Connection connection = getConnection(0);
		try {
			CallableStatement cs = connection.prepareCall("{CALL savePlayer(?,?,?,?,?,?,?,?,?,?,?)}");
			cs.setInt(1, exp);
			cs.setInt(2, level);
			cs.setInt(3, atk);
			cs.setInt(4, def);
			cs.setInt(5, hp);
			cs.setInt(6, mana);
			cs.setInt(7, coin);
			cs.setInt(8, x);
			cs.setInt(9, y);
			cs.setInt(10, mapID);
			cs.setInt(11, playerID);
			cs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
