package gessi.plateoss.queso;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ModelInstanceJSON {

	public static String generateFile() throws Exception {
		JSONParser parser = new JSONParser();
		QuESoModel quesoModel;
		quesoModel = new QuESoModel();
		JSONArray measuresJSON;

		 String[] quesoKHIs = quesoModel.getKHIs().split(","); // tiene los KHIs

		//String[] quesoKHIs = { "Size", "Visibility" }; // tiene los KHIs

		/**
		 * File instruction set
		 */
		String currentDir = System.getProperty("user.dir");

		File configDir = new File(currentDir, "json");
		File configFile = new File(configDir, "eclipse.json");

		FileWriter writer = new FileWriter(configFile);

		String strJson;

		strJson = "{\r\n" + "			\"OSSECO_name\": \"Eclipse\",\r\n" + "			\"QuESo_KHIs\": [\r\n";
		writer.write(strJson);

		for (String kqi : quesoKHIs) {

			System.out.println("Generados los KHIs");
			Object obj = parser.parse(quesoModel.getKHIMeasures(kqi)); // obtiene las metricas

			measuresJSON = (JSONArray) obj;

			MeasureRestClient objMRC = new MeasureRestClient();

			strJson = "				{" + "\"KQI\": \"" + kqi + "\",\r\n"
					+ "\"BayesianEndPoint\":\"http://gessi-sw.essi.upc.edu:8080/PLATEOSSRESTServer/bayesian/bayesianqueso/"
					+ kqi + "\",\r\n" + "\"KQIData\": \r\n";

			writer.write(strJson);
			writer.write("\r\n");

			try {

				JSONObject jSONMeasure;
				String strEndpoint;
				String strMeasure;
				String strBayesian;

				MeasureAdapter objMA = new MeasureAdapter(kqi, kqi); // obtiene kqi Bayesiano

				System.out.println("=====================================");
				String strBayesianMeasure = objMA.getJSonBayesian();
				System.out.println(strBayesianMeasure);
				writer.write(strBayesianMeasure);
				writer.write(",\r\n");

				strJson = "\r\n\"Measures\"" + ": [";

				writer.write(strJson);

				for (int i = 0; i <  measuresJSON.size() ; i++) {

					jSONMeasure = (JSONObject) measuresJSON.get(i);
					strEndpoint = jSONMeasure.get("Endpoint").toString();
					strMeasure = jSONMeasure.get("Measure").toString();

					writer.write("\r\n{\r\n");
					strJson = "\"Measure\":\"" + strMeasure + "\",\r\n";
					strJson += "\"RawEndpoint\": \"" + strEndpoint + "\",\r\n";
					strJson += "\"RawData\":";
					writer.write(strJson);

					String strCrudeMeasures = objMRC.strJSONMeasure(strEndpoint); // obtiene las metricas crudas
					System.out.println(strCrudeMeasures);
					writer.write(strCrudeMeasures);
					writer.write(",\r\n");

					strJson = "\"BayesianEndPoint\": \"http://gessi-sw.essi.upc.edu:8080/PLATEOSSRESTServer/bayesian/bayesianqueso/Activeness/"
							+ strMeasure + "\",\r\n" + "					\"BayesianData\":";

					writer.write(strJson);

					objMA.setMeasure(strMeasure);
					strBayesian = objMA.getJSonBayesian();
					writer.write(strBayesian);
					if (i == measuresJSON.size() - 1) {
						strJson = ",\"Timeset\": \"\"}";
					} else {
						strJson = ",\"Timeset\": \"\"},";
					}

					// strJson = ",\"Timeset\": \"\"},";
					writer.write(strJson);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			if(kqi.contentEquals(quesoKHIs[quesoKHIs.length - 1])) { 
			writer.write("\r\n]\r\n}");
			}
			else {
				writer.write("\r\n]\r\n},");
			}
				

		}

		writer.write("\r\n");
		strJson = "]\r\n}";
		//strJson += "]\r\n}";
		writer.write(strJson);
		writer.close();

		// MeasureAdapter objMA = new MeasureAdapter("Size", "numberofpartners");

		// String result = objMA.getJSonBayesian();
		// return result;
		return "";
	}

}
