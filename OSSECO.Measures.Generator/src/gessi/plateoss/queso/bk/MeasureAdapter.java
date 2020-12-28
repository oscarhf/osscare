package gessi.plateoss.queso.bk;

import gessi.plateoss.commons.json.bk.BayesianJSON;

public class MeasureAdapter {
	private static BayesianLearner iBayesianLearner = null;
	private String measure;

	/**
	 * Constructor 
	 * @param Kqi : QuEso quality-subcharacteristic
	 * @param measure : measure
	 * @throws Exception
	 */
	public MeasureAdapter(String Kqi, String measure) throws Exception {
		super();
		this.measure= measure;
		iBayesianLearner = new BayesianLearner(Kqi);
		
	}
	/**
	 * Transform a BN-node measure into JSON
	 * @return
	 * @throws Exception
	 */
	public String getJSonBayesian() throws Exception {

		BayesianNode node = iBayesianLearner.getNodeStates(measure);
		BayesianJSON jsonMeasure = new BayesianJSON();
		System.out.println("Node-->"+node.toString());
		String jSonMeasureStr = jsonMeasure.getJSONBayesianNode(measure,node);
		return jSonMeasureStr;
	}
	

}
