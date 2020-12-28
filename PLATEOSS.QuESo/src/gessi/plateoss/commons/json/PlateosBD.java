package gessi.plateoss.commons.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import gessi.plateoss.queso.BayesianNode;

public class PlateosBD {

	private void getOssecoLists(List<String> ossecoListNames, List<String> ossecoListEndPoints)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		Connection conn = getConnection();
		PreparedStatement readStatement;

		readStatement = conn.prepareStatement("Select * from ossecos_list order by name");

		ResultSet rs = readStatement.executeQuery();

		while (rs.next()) {
			ossecoListNames.add(rs.getString("name"));
			ossecoListEndPoints.add(rs.getString("end_point"));

		}
		System.out.println("connection closed");
		conn.close();

	}

	/***
	 * 
	 * @return
	 */

	private static Connection getConnection()

	{
		Properties prop = new Properties();

		File configFile = null;

		try {

			File configDir = new File(System.getProperty("catalina.base")+"/webapps/OSSECOMeasuresRESTServer", "conf");
			configFile = new File(configDir, "config.properties");
			InputStream stream = new FileInputStream(configFile);
			prop.load(stream);

			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			String dbms = prop.getProperty("dbms");
			String port = prop.getProperty("port");
			String server = prop.getProperty("server");
			String dbName = prop.getProperty("dbNamep");

			String[] propName = { "useSSL", "serverTimezone" };
			String[] propValue = { "false", "UTC" };

			String url = "jdbc:mysql://" + server + ":" + port + "/" + dbName;

			int length = 0;
			if (propName != null) {
				length = propName.length;
				url += "?";
			}
			for (int i = 0; i < length; i++) {
				url += propName[i] + "=" + propValue[i] + ((i < length - 1 == true) ? "&" : "");
			}
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;

		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		return null;

	}

	/**
	 * 
	 * @param node
	 * @return
	 */

	public String getOssecosList() {
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();

		ArrayList<String> ossecosListNames = new ArrayList<String>();
		ArrayList<String> ossecoListEndPoints = new ArrayList<String>();

		try {
			getOssecoLists(ossecosListNames, ossecoListEndPoints);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < ossecosListNames.size(); i++) {
			JSONObject objd = new JSONObject();
			objd.put("Name", ossecosListNames.get(i));
			objd.put("EndPoint", ossecoListEndPoints.get(i));
			array.put(objd);
		}

		obj.put("OssecosList", array);
		return obj.toString();

	}

	/***
	 * 
	 * @param ossecoName
	 * @return
	 * @throws SQLException
	 */
	public String getEndPoint(String ossecoName) throws SQLException {

		Connection conn = getConnection();
		PreparedStatement readStatement;
		String endPoint = "";

		readStatement = conn.prepareStatement("Select * from ossecos_list WHERE name=?");
		readStatement.setString(1, ossecoName);

		ResultSet rs = readStatement.executeQuery();

		if (rs.next()) {

			endPoint = rs.getString("end_point");
		}
		System.out.println("connection closed");
		conn.close();

		return endPoint;

	}

	/***
	 * 
	 * @param ossecoName
	 * @return
	 * @throws SQLException
	 */
	private String getOssecoPath(String ossecoName) throws SQLException {

		Connection conn = getConnection();
		PreparedStatement readStatement;
		String endPoint = "";

		readStatement = conn.prepareStatement("Select * from ossecos_list WHERE name=?");
		readStatement.setString(1, ossecoName);

		ResultSet rs = readStatement.executeQuery();

		if (rs.next()) {

			endPoint = rs.getString("model_path");
		}
		System.out.println("connection closed");
		System.out.println(endPoint);

		conn.close();

		return endPoint;

	}

	public String getOssecoInstance(String ossecoName) {
		File file = null;

		String fileContent="Error";
		try {

			File configDir = new File(System.getProperty("catalina.base")+"/webapps/OSSECOMeasuresRESTServer", "json");
			file = new File(configDir, ossecoName + ".json");

			FileReader fr = new FileReader(file.getAbsolutePath());
			char[] chars = new char[(int) file.length()];
			fr.read(chars);
			fileContent = new String(chars);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileContent;
	}

}
