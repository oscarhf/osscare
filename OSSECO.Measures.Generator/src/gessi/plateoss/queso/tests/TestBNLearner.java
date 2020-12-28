package gessi.plateoss.queso.tests;

import gessi.plateoss.queso.bk.BayesianLearner;


public class TestBNLearner {

	public static void main(String[] args) {

		 try {
			@SuppressWarnings("unused")
			//BayesianLearner objMRCa = new BayesianLearner("Size");
			BayesianLearner objMRC = new BayesianLearner("Heterogeneity");
			System.out.println("BN learned");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
