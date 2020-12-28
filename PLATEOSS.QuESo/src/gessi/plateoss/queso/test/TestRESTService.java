package gessi.plateoss.queso.test;

import gessi.plateoss.queso.MeasureRestClient;

public class TestRESTService {

	public static void main(String[] args) {

		MeasureRestClient objMRC = new MeasureRestClient();
        objMRC.strJSONMeasure("http://oscarapps.eu.ngrok.io/OSSECOMeasuresRESTServer/measures/numberofpartners");
        System.out.println(objMRC.toString());
		

	}
}
