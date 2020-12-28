package gessi.plateoss.queso.tests;

import gessi.plateoss.queso.bk.MeasureAdapter;

public class TestJSONBN {

	public static void main(String[] args) {
		try {
			MeasureAdapter objMA = new MeasureAdapter("Activeness","versionhistory");
			System.out.println(objMA.getJSonBayesian());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
