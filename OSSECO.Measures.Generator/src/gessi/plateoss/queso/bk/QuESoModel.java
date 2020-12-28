package gessi.plateoss.queso.bk;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class QuESoModel {

	public String getKHIMeasures(String khi) {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		try {
			
			Object obj = parser.parse(new FileReader("./json/QuESo.json"));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray objQuESo = (JSONArray) jsonObject.get("QuESo");
			String currentDir = System.getProperty("user.dir");
	        System.out.println("Current dir using System:" +currentDir);

			for (int i = 0; i < objQuESo.size(); i++) {
				{
					JSONObject obj1 = (JSONObject) objQuESo.get(i);
					if (obj1.containsValue(khi)) {
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
}
