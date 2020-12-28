package gessi.plateoss.queso.tests;

import gessi.plateoss.queso.bk.MeasureRestClient;

public class TestRESTService {

	public static void main(String[] args) {

		MeasureRestClient objMRC = new MeasureRestClient("http://localhost:8080/OSSECOMeasuresRESTServer/measures/");
        objMRC.strJSONMeasure("numberofevents");
        System.out.println(objMRC.toString());
		

	}
}
