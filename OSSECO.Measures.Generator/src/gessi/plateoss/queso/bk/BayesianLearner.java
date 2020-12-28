package gessi.plateoss.queso.bk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import org.omg.CosNaming.IstringHelper;

import smile.Network;
import smile.Network.NodeType;
import smile.SMILEException;
import smile.learning.DataMatch;
import smile.learning.DataSet;
import smile.learning.EM;

public class BayesianLearner {
	/*
	 * Smile Bayesian network
	 */
	private Network iNetwork;
	/*
	 * Key quality indicator
	 */
	private String Kqi;
	/**
	 * QuESo Model
	 */
	private QuESoModel quesoModel;
	/**
	 * JSON measures for the KQI
	 */
	private JSONArray measuresJSON;

	/**
	 * List of measures without historical data
	 */
	private ArrayList<String> listAbsolutMeasures = new ArrayList<String>();
	/**
	 * List of measures without any data
	 */
	private ArrayList<String> listExpertMeasures = new ArrayList<String>();

	/**
	 * 
	 * @param Khi
	 * @throws Exception
	 */
	public BayesianLearner(String Kqi) throws Exception {

		this.Kqi = Kqi;
		JSONParser parser = new JSONParser();

		/**
		 * Loading QuESo Model from file
		 */
		quesoModel = new QuESoModel();

		/**
		 * Loading the measures for the KQI
		 */

		Object obj = parser.parse(quesoModel.getKHIMeasures(Kqi));
		measuresJSON = (JSONArray) obj;
		// iNetwork = createBNStructureFromJSON();
		// iNetwork = learningBNParameters();
		iNetwork = learningBNParameters2();
	}

	/**
	 * Create the BN Structure for the Khi with default probabilities and
	 * default structure
	 * 
	 * @param Kqi
	 *            : The QuESo quality Sub-characteristic
	 * @return The standard Bayesian network
	 * @throws ParseException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Deprecated
	private Network createBNStructureFromJSON() throws FileNotFoundException,
			IOException, ParseException {

		Network net = new Network();

		// default distribution
		double[] aSuccessDef4 = { 1.0 / 3, 1.0 / 3, 1.0 / 3 };
		/**
		 * Khi Outcome Node
		 */

		net.addNode(NodeType.Cpt, Kqi);
		// Node States
		net.addOutcome(Kqi, "Poor");
		net.addOutcome(Kqi, "Good");
		net.deleteOutcome(Kqi, 0);
		net.deleteOutcome(Kqi, 0);

		/**
		 * Creating Nodes and dependencies
		 */
		for (int i = 0; i < measuresJSON.size(); i++) {

			JSONObject jSONMeasure = (JSONObject) measuresJSON.get(i);
			String strMeasure = jSONMeasure.get("Measure").toString();
			net.addNode(Network.NodeType.Cpt, strMeasure);
			// Node States
			net.addOutcome(strMeasure, "Low");
			net.addOutcome(strMeasure, "Medium");
			net.addOutcome(strMeasure, "High");
			net.deleteOutcome(strMeasure, 0);
			net.deleteOutcome(strMeasure, 0);

			// Add conditional dependence
			net.addArc(strMeasure, Kqi);
			net.setNodeDefinition(strMeasure, aSuccessDef4);
		}

		net.writeFile("D:/OSSECOImplentation/tmps/" + Kqi + ".xdsl");
		return net;
	}

	public Network learningBNParameters2() throws Exception {

		// default distribution
		double[] kpiDef2 = { 0.5, 0.5 };

		/**
		 * Load Historical data from OSSECO REST Server
		 */
		DataSet ds = loadDataSet();
		Network net = new Network();

		/**
		 * Kqi Outcome Node
		 */
		net.addNode(NodeType.Cpt, Kqi);
		// Node States
		net.addOutcome(Kqi, "Poor");
		net.addOutcome(Kqi, "Good");
		net.deleteOutcome(Kqi, 0);
		net.deleteOutcome(Kqi, 0);

		/**
		 * Intermediate Nodes
		 */
		net.addNode(NodeType.Cpt, "Historical");
		// Node States
		net.addOutcome(Kqi, "Poor");
		net.addOutcome(Kqi, "Good");
		net.deleteOutcome(Kqi, 0);
		net.deleteOutcome(Kqi, 0);

		/**
		 * Add conditional dependences
		 */

		net.addArc("Historical", Kqi);
		net.setNodeDefinition("Historical", kpiDef2);
		
		if (listAbsolutMeasures.size() > 1) {
			net.addNode(NodeType.Cpt, "Absolute");
			// Node States
			net.addOutcome(Kqi, "Poor");
			net.addOutcome(Kqi, "Good");
			net.deleteOutcome(Kqi, 0);
			net.deleteOutcome(Kqi, 0);
			net.addArc("Absolute", Kqi);

			net.setNodeDefinition("Absolute", kpiDef2);
		}
		if (listExpertMeasures.size() > 1) {

			net.addNode(NodeType.Cpt, "NoData");
			// Node States
			net.addOutcome(Kqi, "Poor");
			net.addOutcome(Kqi, "Good");
			net.deleteOutcome(Kqi, 0);
			net.deleteOutcome(Kqi, 0);

			net.addArc("NoData", Kqi);
			net.setNodeDefinition("NoData", kpiDef2);
		}

		/**
		 * Sub network of historical nodes
		 */

		for (int i = 0; i < ds.getVariableCount(); i++) {
			String nodeStr = ds.getVariableId(i);
			net.addNode(NodeType.Cpt, nodeStr);
			// Node States
			net.addOutcome(nodeStr, "Poor");
			net.addOutcome(nodeStr, "Good");
			net.deleteOutcome(nodeStr, 0);
			net.deleteOutcome(nodeStr, 0);
			net.addArc(nodeStr, "Historical");
			net.setNodeDefinition(nodeStr, kpiDef2);
		}

		/**
		 * Sub network absolute nodes
		 */
		kpiDef2[0] = 0.0;
		kpiDef2[1] = 1.0;

		for (int i = 0; i < listAbsolutMeasures.size(); i++) {
			String nodeStr = listAbsolutMeasures.get(i);
			net.addNode(NodeType.Cpt, nodeStr);
			// Node States
			net.addOutcome(nodeStr, "Poor");
			net.addOutcome(nodeStr, "Good");
			net.deleteOutcome(nodeStr, 0);
			net.deleteOutcome(nodeStr, 0);
			if (listAbsolutMeasures.size() == 1) {
				net.addArc(nodeStr, Kqi);
			}
			else
			{
				net.addArc(nodeStr, "Absolute");
			}
			net.setNodeDefinition(nodeStr, kpiDef2);
		}

		/**
		 * Sub network no data nodes
		 */
		for (int i = 0; i < listExpertMeasures.size(); i++) {
			String nodeStr = listExpertMeasures.get(i);
			net.addNode(NodeType.Cpt, nodeStr);
			// Node States
			net.addOutcome(nodeStr, "Poor");
			net.addOutcome(nodeStr, "Good");
			net.deleteOutcome(nodeStr, 0);
			net.deleteOutcome(nodeStr, 0);
			if (listExpertMeasures.size() == 1) {
				net.addArc(nodeStr, Kqi);
			}
			else
			{
				net.addArc(nodeStr, "Absolute");
			}
			net.setNodeDefinition(nodeStr, kpiDef2);
		}

		// Change the name of the states in the general structure
		for (int i = 0; i < ds.getVariableCount(); i++) {
			String[] stateNames = ds.getStateNames(i);
			for (int j = 0; j < net.getOutcomeCount(ds.getVariableId(i)); j++) {
				net.setOutcomeId(ds.getVariableId(i), j, stateNames[j]);
			}
		}

		DataMatch[] matches = ds.matchNetwork(net);
		EM em = new EM();

		/***
		 * NOTE: Values of KQIs without data are assigned using the Dirichlet
		 * distribution by default
		 */

		// iNetwork.readFile("D:/OSSECOImplentation/tmps/" + Kqi + "BL.xdsl");

		net.setBayesianAlgorithm(6);

		String strAbsolute[] = new String[listAbsolutMeasures.size()];
		strAbsolute = listAbsolutMeasures.toArray(strAbsolute);
		em.learn(ds, net, matches, strAbsolute);
		net.updateBeliefs();

		net.writeFile("D:/OSSECOImplentation/tmps/" + Kqi + ".xdsl");

		return net;
	}

	/**
	 * Refactoring the BN structure and calculate the new probabilities using EM
	 * Algorithm with the historical data of the Khi loaded in the Constructor
	 * 
	 * @param iNetwork
	 * @return Network with learned probabilities distributions
	 * @throws Exception
	 */
	@Deprecated
	public Network learningBNParameters() throws Exception {
		DataSet ds = new DataSet();

		ds = loadDataSet();
		int k = 0;

		// Dicretization

		for (; k < ds.getVariableCount(); k++) {
			try {

				ds.discretize(k, 0, 3, "S");

			} catch (SMILEException e) {
				// It is an absolute variable
				// System.out.println("id-->" + k + "  Variable-->" +
				// ds.getVariableId(k) + " Not discretized");
				// TODO : improve this code

			}
		}

		// Change the name of the states in the general structure
		for (int i = 0; i < ds.getVariableCount(); i++) {
			String[] stateNames = ds.getStateNames(i);
			System.out.println("Variable-->" + ds.getVariableId(i)
					+ "number of states-->" + stateNames.length);
			if ((iNetwork.getOutcomeCount(ds.getVariableId(i)) > 2)
					&& (stateNames.length > 0)) {
				for (int j = 0; j < iNetwork.getOutcomeCount(ds
						.getVariableId(i)); j++) {
					System.out.println("Variable-->" + ds.getVariableId(i)
							+ " state name-->" + stateNames[j]);
					iNetwork.setOutcomeId(ds.getVariableId(i), j, stateNames[j]);
				}
			} /*
			 * else { System.out.println("VariableA-->" + ds.getVariableId(i));
			 * iNetwork.setOutcomeId(ds.getVariableId(i), 0, "No");
			 * iNetwork.setOutcomeId(ds.getVariableId(i), 1, "Yes"); }
			 */
		}
		// iNetwork.getOutcomeIds(nodeHandle)

		/**
		 * Set variables with absolute values
		 */

		double[] aSuccessDef2 = { 0.0, 1.0 };
		for (String strM : listAbsolutMeasures) {
			System.out.println("Absolute-->" + strM);
			iNetwork.deleteOutcome(strM, 0);
			iNetwork.addOutcome(strM, "Low");
			iNetwork.deleteOutcome(strM, 0);
			iNetwork.addOutcome(strM, "High");
			iNetwork.deleteOutcome(strM, 0);
			iNetwork.setNodeDefinition(strM, aSuccessDef2);
		}

		System.out.println("Before Match");
		DataMatch[] matches = ds.matchNetwork(iNetwork);
		System.out.println("After Match");
		// iNetwork.setTarget(Kqi, true);
		iNetwork.writeFile("D:/OSSECOImplentation/tmps/" + Kqi + "BL.xdsl");
		EM em = new EM();

		/***
		 * NOTE: Values of KQIs without data are assigned using the Dirichlet
		 * distribution by default
		 */

		// iNetwork.readFile("D:/OSSECOImplentation/tmps/" + Kqi + "BL.xdsl");

		System.out.println("Before learn");
		iNetwork.setBayesianAlgorithm(6);
		em.setSeed(0);
		em.setRelevance(false);
		em.setUniformizeParameters(false);

		// em.learn(ds, iNetwork, matches);

		String strAbsolute[] = new String[listAbsolutMeasures.size()];
		strAbsolute = listAbsolutMeasures.toArray(strAbsolute);
		em.learn(ds, iNetwork, matches, strAbsolute);
		// em.learn(ds, iNetwork, matches);

		System.out.println("After learn");
		iNetwork.updateBeliefs();

		iNetwork.writeFile("D:/OSSECOImplentation/tmps/" + Kqi + "L.xdsl");

		return iNetwork;
	}

	/**
	 * Load Data Set from the WEB services
	 * 
	 * @return Data Set
	 * @throws Exception
	 */
	// TODO: Configure the url of the services
	private DataSet loadDataSet() throws Exception {

		try
		{
		DataSet ds = new DataSet();
		

		MeasureRestClient objMRC = new MeasureRestClient(
				"http://localhost:8080/OSSECOMeasuresRESTServer/measures/");

		/**
		 * 
		 */
		for (int i = 0; i < measuresJSON.size(); i++) {

			JSONObject jSONMeasure = (JSONObject) measuresJSON.get(i);
			String strMeasure = jSONMeasure.get("Measure").toString();
			/**
			 * Only add measures with historical dataset
			 */
			try {
				if (!addVariableDataSet(objMRC.strJSONMeasure(strMeasure),strMeasure, ds)) {
					/**
					 * Identification of Absolute measures
					 */
					listAbsolutMeasures.add(strMeasure);
				}
			} catch (RuntimeException ex) {
				/**
				 * Identification of measures without data
				 */
				listExpertMeasures.add(strMeasure);
			}
		}

		// Discretization of parameters

		for (int k = 0; k < ds.getVariableCount(); k++) {
			try {

				ds.discretize(k, 0, 2, "S");

			} catch (SMILEException e) {
				/**
				 * the discretization process failed
				 */
				throw e;
			}
		}

		return ds;
		}
		catch (Exception ex)
		{
			System.out.println(ex.toString());
			return null;
		}

	}

	/**
	 * 
	 * @param listValues
	 * @param measure
	 * @param ds
	 * @throws ParseException
	 */
	private boolean addVariableDataSet(String strValuesJSON, String measure,
			DataSet ds) throws ParseException {

		boolean historical = false;
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(strValuesJSON);
		JSONObject jsonValues = (JSONObject) obj;
		Object arr = parser.parse(jsonValues.get("data").toString());
		JSONArray jsonArray = (JSONArray) arr;

		/**
		 * TODO: Change for another selection strategy
		 */
		if (jsonValues.get("Type").equals("Historical")) {
			historical = true;

			/**
			 * Add historical values to the BN
			 */

			ds.addIntVariable(measure);
			int record = 0;

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jSONMeasure = (JSONObject) jsonArray.get(i);
				ds.addEmptyRecord();
				try {
					ds.setInt(ds.findVariable(measure), record++, Integer
							.parseInt(jSONMeasure.get("Value").toString()));
				} catch (NumberFormatException ex) {
					ds.setFloat(ds.findVariable(measure), record++, Float
							.parseFloat(jSONMeasure.get("Value").toString()));

				}
				// System.out.println("Measure-->"+measure+" Data-->"+jSONMeasure.get("Value").toString());
			}
		}
		return historical;
	}

	/***
	 * 
	 * @param nodeID
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public BayesianNode getNodeStates(String nodeID)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		String[] listOfNodes = iNetwork.getAllNodeIds();
		int i = 0;
		BayesianNode iBayesianNode = null;
		String strNode = "";
		while (i < listOfNodes.length & !strNode.equals(nodeID)) {
			strNode = listOfNodes[i++];

			if (strNode.equals(nodeID)) {

				iBayesianNode = new BayesianNode(strNode);
				String[] listOfStates = iNetwork.getOutcomeIds(strNode);
				double[] listOfValues = iNetwork.getNodeValue(strNode);
				// double[] listOfValues1 = iNetwork.get (strNode);

				int j = 0;
				for (String strState : listOfStates) {
					iBayesianNode.addState(strState,
							Double.toString(listOfValues[j++]));
				}
			}
		}
		return iBayesianNode;
	}

}
