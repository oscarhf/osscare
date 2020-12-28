package gessi.plateoss.queso.test;

import gessi.plateoss.queso.BayesianLearner;


public class TestBNLearner {

	public static void main(String[] args) {

		 try {
			//BayesianLearner objMRCa = new BayesianLearner("Size");
			BayesianLearner objMRCa = new BayesianLearner("Activeness");
			System.out.println(objMRCa.toString());
			
			System.out.println("BN learned");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
