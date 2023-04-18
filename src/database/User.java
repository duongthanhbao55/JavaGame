package database;

import entities.Player;
import static untilz.HelpMethods.strSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	private int userID;
	private Player player;

	public User() {
		this.player = null;
	}

	public static User Login(final String uName, final String pass) {
		User user = null;

		try {
			final MySQL mySQL = new MySQL(0);
			try {

				ResultSet red = mySQL.stat.executeQuery("SELECT * FROM `user` WHERE (`user` LIKE '" + strSQL(uName)
						+ "' AND `password` LIKE '" + strSQL(pass) + "') LIMIT 1;");
				if (red.first()) {
					final int username = red.getInt("userid");
					final String player = red.getString("player");
					System.out.println(username);
					System.out.println(player);

				}
			} finally {
				mySQL.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}

	public static boolean Register(String uName, String password) {
		boolean result = false;
		try {
			final MySQL mySQL = new MySQL(0);
			try {
				Connection conn = mySQL.getConnection(0);
				String sql = "INSERT INTO user (userid, user, status, password, player, admin, created_at, email, tester) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, 1); // set the value for user
				pstmt.setString(2, uName); // set the value for user
				pstmt.setInt(3, 1); // set the value for status
				pstmt.setString(4, password); // set the value for password
				pstmt.setString(5,"[]");			
				pstmt.setInt(6, 0); // set the value for admin
				pstmt.setString(7,null);
				pstmt.setString(8, "test@gmail.com");
				pstmt.setInt(9, 0); // set the value for tester

				int rowsUpdate = pstmt.executeUpdate();
				if(rowsUpdate > 0) {
					System.out.println("New user has been inserted successfully!");
					return true;
				} else{
					System.out.println("User not found with id = " + uName);
				}			
			} finally {
				mySQL.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
