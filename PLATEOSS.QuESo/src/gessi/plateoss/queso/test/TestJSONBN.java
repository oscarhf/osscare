package gessi.plateoss.queso.test;

import gessi.plateoss.queso.MeasureAdapter;

public class TestJSONBN {

	public static void main(String[] args) {
		try {
			System.out.println("Start");
			/*MeasureAdapter objMA = new MeasureAdapter("Size","numberofpartners");
			System.out.println(objMA.getJSonBayesian());
			*/
			//MeasureAdapter objMA = new MeasureAdapter("Activeness","numberofcommits");
			MeasureAdapter objMA = new MeasureAdapter("Activeness","Activeness");
			System.out.println(objMA.getJSonBayesian());
		/*	objMA = new MeasureAdapter("Activeness","numberofcommiters");
			System.out.println(objMA.getJSonBayesian());
			objMA = new MeasureAdapter("Activeness","daysafterlastrelease");
			System.out.println(objMA.getJSonBayesian());
			objMA = new MeasureAdapter("Activeness","numberofopenedbugs");
			System.out.println(objMA.getJSonBayesian());
			objMA = new MeasureAdapter("Activeness","numberofclosedbugs");
			System.out.println(objMA.getJSonBayesian());
			objMA = new MeasureAdapter("Activeness","versionhistory");
			System.out.println(objMA.getJSonBayesian());
			objMA = new MeasureAdapter("Activeness","numberofmessagessent");
			System.out.println(objMA.getJSonBayesian());*/
			System.out.println("Done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	