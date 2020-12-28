package gessi.plateoss.queso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class QuESoModel {

	public String getKHIMeasures(String kqi) {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		try {

			// ***
			/*
			 * String fileContent = "Hello Learner !! Welcome to howtodoinjava.com.";
			 * 
			 * /// TODO: rUTA RELATIVA Y NO ABSOLUTA BufferedWriter writer = new
			 * BufferedWriter(new FileWriter("AquiEstoy.txt")); writer.write(fileContent);
			 * writer.close();
			 */
			
			/**
			 * TODO: AQUI SE CAMBIA EL catalina
			 */
			

			//String currentDir = System.getProperty("user.dir");
			//File configDir = new File(currentDir, "json");

			File configDir = new File(System.getProperty("catalina.base")+"/webapps/OSSECOMeasuresRESTServer","json");
			

			File configFile = new File(configDir, "queso.json");
			Object obj = parser.parse(new FileReader(configFile));

			// String currentDir=configDir.getAbsolutePath();
			// System.out.println("Current dir using System:" + currentDir);
			// Object obj = parser.parse(new FileReader("./json/QuESo.json"));
			// Object obj = parser.parse(new FileReader(currentDir + "/json/QuESo.json"));

			JSONObject jsonObject = (JSONObject) obj;
			JSONArray objQuESo = (JSONArray) jsonObject.get("QuESo_KHIs");

			for (int i = 0; i < objQuESo.size(); i++) {
				{
					JSONObject obj1 = (JSONObject) objQuESo.get(i);
					if (obj1.containsValue(kqi)) {
						System.out.println(obj1.get("Measures").toString());
						return obj1.get("Measures").toString();
					}
				}

			}

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "{}";
	}

	public String getKHIMeasures() {

		// TODO Auto-generated method stub
		String lstMeasures = "{";
		JSONParser parser = new JSONParser();
		try {
			

			/**
			 * TODO: AQUI SE CAMBIA EL catalina
			 */

			//File configDir = new File(System.getProperty("user.dir"),"json");
			
			File configDir = new File(System.getProperty("catalina.base") + "/webapps/OSSECOMeasuresRESTServer","json");
			
			
			File configFile = new File(configDir, "queso.json");
			Object obj = parser.parse(new FileReader(configFile));

			JSONObject jsonObject = (JSONObject) obj;
			JSONArray objQuESo = (JSONArray) jsonObject.get("QuESo_KHIs");

			for (int i = 0; i < objQuESo.size(); i++) {
				JSONObject obj1 = (JSONObject) objQuESo.get(i);
				lstMeasures += obj1.get("Measures").toString();
				if (i < objQuESo.size() - 1) {
					lstMeasures += ",";
				}
			}

		}

		catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lstMeasures + "}";
	}

	public String getKHIs() {

	
		String lstMeasures = "";

		JSONParser parser = new JSONParser();
		try {

			String currentDir = System.getProperty("user.dir");
			Object obj = parser.parse(new FileReader(currentDir + "/json/QuESo.json"));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray objQuESo = (JSONArray) jsonObject.get("QuESo_KHIs");

			for (int i = 0; i < objQuESo.size(); i++) {
				JSONObject obj1 = (JSONObject) objQuESo.get(i);
				lstMeasures += obj1.get("KQI").toString();
				if (i < objQuESo.size() - 1) {
					lstMeasures += ",";
				}
			}

		}

		catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lstMeasures;
	}

}
