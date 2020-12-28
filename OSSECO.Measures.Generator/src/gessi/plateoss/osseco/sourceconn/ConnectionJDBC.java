package gessi.plateoss.osseco.sourceconn;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

/**
 * <dl>
 * <dt>Purpose: Utilities for JDBC Connection
 * <dd>
 *
 * <dt>Description:
 * <dd>With this Java class you can get connection with DataBases from your Java
 * programs.
 * <dt>Example usage:
 * <dd>
 * 
 * <pre>
 * </pre>
 * 
 * </dd>
 * </dl>
 * 
 * @version v0.1, 2016/03/30 (March) -- first release
 * @author Oscar Franco-Bedoya
 *         (<a href="oscarhf2002@hotmail.com">oscarhf2002.@hotmail.com</a>)
 */

public class ConnectionJDBC {
	/**
	 * The user credentials
	 */
	String user;
	String password;
	/**
	 * The name of the data base management system
	 */
	String dbms;
	/**
	 * Dir and port of the DB server
	 */
	String server;
	String port;
	/**
	 * The data base name
	 */
	String dbName;

	/**
	 * Constructor
	 * 
	 * @param user
	 * @param password
	 * @param dbms
	 * @param server
	 * @param port
	 * @param dbName
	 */
	public ConnectionJDBC(String user, String password, String dbms, String server, String port, String dbName) {
		super();
		this.user = user;
		this.password = password;
		this.dbms = dbms;
		this.server = server;
		this.port = port;
		this.dbName = dbName;
	}

	/**
	 * Return the Connection to the data base
	 * 
	 * @return the DB connection
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */

	public Connection getConnection()
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		// String url1 =
		// "jdbc:mysql://127.0.0.1:3306/osseco_measures?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"+
		// "";
		// onn = DriverManager.getConnection(url1, "root", "09122006Hf");

	
		
		
		String[] propName = { "useUnicode", "useJDBCCompliantTimezoneShift", "useLegacyDatetimeCode",
				"serverTimezone" };
		String[] propValue = { "true", "true", "false", "UTC" };


		Connection conn = getConnection(propName,propValue, server, port, dbName, user, password);
		
		System.out.println("Connected to database");
		return conn;
	}

	private static Connection getConnection(String[] propName, String[] propValue, String server, String port, 
			String dbName, String user, String password )

	{
		Properties prop = new Properties();
		InputStream input = null;

		try {

			//input = new FileInputStream("config.properties");
			// input = new FileInputStream("config.remote.properties");

			//prop.load(input);
			/*
			 * String user = prop.getProperty("user"); String password =
			 * prop.getProperty("password"); String dbms = prop.getProperty("dbms"); String
			 * port = prop.getProperty("port"); String server = prop.getProperty("server");
			 * String dbName = prop.getProperty("dbName");
			 * 
			 */	// String url1 =
			// "jdbc:mysql://127.0.0.1:3306/osseco_measures?useSSL=false&serverTimezone=UTC"
			// + "";

			String url = "jdbc:mysql://" + server + ":" + port + "/" + dbName + "?";

			int length = 0;
			if (propName != null) {
				length = propName.length;
			}
			for (int i = 0; i < length; i++) {
				url += propName[i] + "=" + propValue[i] + ((i < length - 1 == true) ? "&" : "");
			}
			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;

	}
}
