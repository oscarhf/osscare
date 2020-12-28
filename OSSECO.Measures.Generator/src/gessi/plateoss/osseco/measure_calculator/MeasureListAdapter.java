package gessi.plateoss.osseco.measure_calculator;

import gessi.plateoss.osseco.sourceconn.ConnectionJDBC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <dl>
 * <dt>Purpose: Get the measures of the OSSECO
 * <dd>
 *
 * <dt>Description:
 * <dd>This class implement the adapter of the measures from the OSSECO data
 * base to Hash-tables
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
public class MeasureListAdapter {

	private String user;
	private String password;
	private String dbms;
	private String server;
	private String port;
	private String dbName;

	public MeasureListAdapter() {
		super();
		/*
		 * Properties prop = new Properties(); InputStream input = null;
		 * 
		 * try {
		 * 
		 * //input = new FileInputStream(
		 * "D:/OSSECOImplentation/pocmeasure/OSSECO.Measures.Generator/config.properties"
		 * ); //input = new FileInputStream("config.remote.properties");
		 * 
		 * // load a properties file //prop.load(input);
		 * 
		 * // get the property value and print it out
		 * 
		 * user = prop.getProperty("user"); password = prop.getProperty("password");
		 * dbms = prop.getProperty("dbms"); port = prop.getProperty("port"); server =
		 * prop.getProperty("server"); dbName = prop.getProperty("dbName");
		 * 
		 * } catch (IOException ex) { ex.printStackTrace(); } finally { if (input !=
		 * null) { try { input.close(); } catch (IOException e) { e.printStackTrace(); }
		 * } }
		 */
	}

	/**
	 * Return the hash table with the measures with historical data
	 * 
	 * @param tableName
	 * @return the hash table with the measures
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public List<String> getCollectionMeasureList(String tableName)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<String> listMeasures = new ArrayList<String>();

		// String url1=
		// "jdbc:mysql://127.0.0.1:3306/osseco_measures?useSSL=false&serverTimezone=UTC"
		// + "";
		// Connection conn= new
		// ConnectionJDBC(user,password,dbms,server,port,dbName).getConnection();

		// String url1 =
		// "jdbc:mysql://127.0.0.1:3306/osseco_measures?useSSL=false&serverTimezone=UTC"
		// + "";
		// Class.forName("com.mysql.cj.jdbc.Driver");
		// Connection conn = DriverManager.getConnection(url1, "root", "09122006Hf");

		String[] strProp = { "useSSL", "serverTimezone" };
		String[] strVal = { "false", "UTC" };

		Connection conn = getConnection(strProp, strVal);

		/*
		 * //String url1=
		 * "jdbc:mysql://18.228.172.240:3306/osseco_measures?useSSL=false&serverTimezone=UTC"
		 * + ""; try { // The newInstance() call is a work around for some // broken
		 * Java implementations
		 * 
		 * Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); } catch (Exception
		 * ex) { System.out.println(ex); }
		 */

		// String url1=
		// "jdbc:mysql://"+server+":"+port+"/"+dbName+"?useSSL=false&serverTimezone=UTC"
		// + "";
		// Connection conn = DriverManager.getConnection(url1,"root","Y2849868K");

		// Connection conn = new ConnectionJDBC(user, password, dbms, server, port,
		// dbName).getConnection();

//		Connection conn = new ConnectionJDBC("ba16735c7f9a7f", "a8f86cb3",
//				"mysql", "br-cdbr-azure-south-a.cloudapp.net", "3306",
//				"ossecodatabase").getConnection();

		PreparedStatement readStatement = conn.prepareStatement("Select * from " + tableName + " order by year, month");

		ResultSet rs = readStatement.executeQuery();
		while (rs.next()) {
			listMeasures.add(rs.getString("year") + "-" + rs.getString("month") + "," + rs.getString("count"));

		}
		System.out.println("connection closed");
		conn.close();
		return listMeasures;
	}

	/**
	 * Return the hash table with single measures
	 * 
	 * @param tableName
	 * @param measure
	 * @return
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public List<String> getSingleMeasure(String tableName, String measure)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<String> listMeasures = new ArrayList<String>();

		// String url1 =
		// "jdbc:mysql://localhost:3306/osseco_measures?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
		// + "";

		String[] propName = { "useUnicode", "useJDBCCompliantTimezoneShift", "useLegacyDatetimeCode",
				"serverTimezone" };
		String[] propValue = { "true", "true", "false", "UTC" };

		Connection conn = getConnection(propName, propValue);

		System.out.println("********************************************");

		// String url1=
		// "jdbc:mysql://"+server+":"+port+"/"+dbName+"?useSSL=false&serverTimezone=UTC"
		// +
		// "";
		// ***********************************
		// CONEXION REMOTA
		// ***********************************

		// String url1=
		// "jdbc:mysql://18.228.172.240:3306/osseco_measures?useSSL=false&serverTimezone=UTC"
		// + "";
		/*
		 * try { // The newInstance() call is a work around for some // broken Java
		 * implementations
		 * 
		 * Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); } catch (Exception
		 * ex) { System.out.println(ex); }
		 */
		// String url1=
		// "jdbc:mysql://"+server+":"+port+"/"+dbName+"?useSSL=false&serverTimezone=UTC"
		// +
		// "";
		// Connection conn = DriverManager.getConnection(url1,"root","Y2849868K");

		// Connection conn = new ConnectionJDBC(user, password, dbms, server, port,
		// dbName).getConnection();

		/*
		 * Connection conn = new ConnectionJDBC("ba16735c7f9a7f", "a8f86cb3", "mysql",
		 * "br-cdbr-azure-south-a.cloudapp.net", "3306",
		 * "ossecodatabase").getConnection();
		 */



		PreparedStatement readStatement = conn
				.prepareStatement("Select * from " + tableName + " Where " + "measure" + "=" + "\"" + measure + "\"");

		ResultSet rs = readStatement.executeQuery();
		while (rs.next()) {
			listMeasures.add(rs.getString("year") + "-" + rs.getString("month") + "," + rs.getString("count"));

		}
		System.out.println("connection closed");
		conn.close();

		return listMeasures;

	}

	private static Connection getConnection(String[] propName, String[] propValue)

	{
		
		
		
		File configFile=null;
		


		try {


			
			File configDir = new File(System.getProperty("catalina.base")+"/webapps/OSSECOMeasuresRESTServer", "conf");
			configFile = new File(configDir, "config.properties");
			InputStream stream = new FileInputStream(configFile);
			Properties prop = new Properties();
			prop.load(stream);
						
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			String dbms = prop.getProperty("dbms");
			String port = prop.getProperty("port");
			String server = prop.getProperty("server");
			String dbName = prop.getProperty("dbName");

			// String url1 =
			// "jdbc:mysql://127.0.0.1:3306/osseco_measures?useSSL=false&serverTimezone=UTC"
			// + "";

			String url = "jdbc:mysql://" + server + ":" + port + "/" + dbName;

			int length = 0;
			if (propName != null) {
				length = propName.length;
				url += "?";
			}
			for (int i = 0; i < length; i++) {
				url += propName[i] + "=" + propValue[i] + ((i < length - 1 == true) ? "&" : "");
			}
			Class.forName("com.mysql.cj.jdbc.Driver.");
			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;

		} catch (IOException | SQLException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}
}
