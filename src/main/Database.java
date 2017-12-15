package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Database {

	public Connection conn = null;
			
	public Database() {
		
		String url = "jdbc:mysql://localhost/crawler_threads";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(url, "root", "");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void finalize() throws Throwable {
		if(conn != null || !conn.isClosed())
			conn.close();
	}

	public static synchronized void sendEmailsToDatabase(Vector emails) {
		
		PreparedStatement preStat = null;
		
		String emailText;
		String email;
		for(int i = 0; i < emails.size(); i++) {
			emailText = "INSERT INTO emails2 (EMAIL) VALUES (?)";
			email = (String) emails.get(i);
			try {
				preStat = Main.db.conn.prepareStatement(emailText);
				preStat.setString(1, email);
				preStat.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Main.emails.removeAllElements();
		Main.counter = 0;
	}
	
}
