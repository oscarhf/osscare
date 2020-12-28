package gessi.plateoss.queso.test;

import gessi.plateoss.queso.ModelInstanceJSON;

public class TestFileGenerator {

	public static void main(String[] args) {

		try {
			System.out.println("Start");
			System.out.println(ModelInstanceJSON.generateFile());
			//MeasureRestClient objMRC = new MeasureRestClient();
			//System.out.println(objMRC.strJSONMeasure("http://queso.sa.ngrok.io/OSSECOMeasuresRESTServer/measures/numberofpartners"));
			System.out.println("End");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Pailas");
			e.printStackTrace();
		}
		
	}

}
