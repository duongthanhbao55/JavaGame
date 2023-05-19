package database;

import entities.Player;
import gamestates.Register;
import static untilz.HelpMethods.strSQL;


import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class User {
	protected Player player;
	private String username;
	private String password;
	private String nickname;
	private String email;
	// GETTER & SETTER

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User() {
		this.player = null;
	}

//	public static User Login(final String uName, final String pass) {
//		User user = new User();
//
//		try {
//			final MySQL mySQL = new MySQL(0);
//			try {
//
//				ResultSet red = mySQL.stat.executeQuery("SELECT * FROM `accounts` WHERE (`username` LIKE '" + strSQL(uName)
//						+ "' AND `password` LIKE '" + strSQL(pass) + "') LIMIT 1;");
//				if (red.first()) {
//					final String username = red.getString("username");
//					final JSONArray jrs = (JSONArray) JSONValue.parseWithException(red.getString("playerID"));
//
//					user.username = username;
//					user.player = Player.getPlayer(user, Integer.parseInt(jrs.get(0).toString()));
//					return user;
//				}
//			} finally {
//				mySQL.close();
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return null;
//	}
	public static User Login(final String uName, final String pass) {
		System.out.println(uName + " " + pass);
		User user = new User();
		if(MySQL.loginSuccessfully(uName, pass)) {
			user.username = uName;
			JSONArray jrs;
			try {
				jrs = (JSONArray) JSONValue.parseWithException(MySQL.getPlayerID(uName));
				user.player = Player.getPlayer(user, Integer.parseInt(jrs.get(0).toString()));
				return user;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static boolean Register(String email, String uName, String password, String confirmPw,Register register) {
		
		boolean chk_email = email.length() == 0;
		boolean chk_uname = uName.length() == 0;
		boolean chk_password = password.length() == 0;
//		System.out.println(email);
		
		
		if(chk_email || chk_uname || chk_password) {
			System.out.println("Please complete all information !");
			register.setWarningIndex(0);
			return false;
		}

		if(!MySQL.emailIsValid(email)) {
			System.out.println("This email is not valid !");
			register.setWarningIndex(1);
			return false;
		}
		else {
			if(MySQL.emailWasUsed(email)) {
				System.out.println("This email was used !");
				register.setWarningIndex(2);
				return false;
			}
		}
		if(MySQL.usernameWasUsed(uName)) {
			System.out.println("Username was used !");
			register.setWarningIndex(3);
			return false;
		}
		if(!password.equals(confirmPw)) {
			System.out.println("Password INCORRECT");
			register.setWarningIndex(4);
			return false;
		}
		MySQL.insertAccounts(email, uName, password);
		System.out.println("Register Successfully !");
		return true;
	}

//	public static boolean Register(String uName, String password, String email) {
//		boolean result = false;
//		try {
//			final MySQL mySQL = new MySQL(0);
//			try {
//				Connection conn = MySQL.getConnection(0);
//				String sql = "INSERT INTO user (userid, user, status, password, player, admin, created_at, email, tester) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//				PreparedStatement pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, 1); // set the value for user
//				pstmt.setString(2, uName); // set the value for user
//				pstmt.setInt(3, 1); // set the value for status
//				pstmt.setString(4, password); // set the value for password
//				pstmt.setString(5, "[]");
//				pstmt.setInt(6, 0); // set the value for admin
//				pstmt.setString(7, null);
//				pstmt.setString(8, "test@gmail.com");
//				pstmt.setInt(9, 0); // set the value for tester
//
//				int rowsUpdate = pstmt.executeUpdate();
//				if (rowsUpdate > 0) {
//					System.out.println("New user has been inserted successfully!");
//					return true;
//				} else {
//					System.out.println("User not found with id = " + uName);
//				}
//			} finally {
//				mySQL.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	public Player getPlayer() {
		return this.player;
	}
}
