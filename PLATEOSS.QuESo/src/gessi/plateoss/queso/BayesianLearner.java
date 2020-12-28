package gessi.plateoss.queso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*import smile.Network;
import smile.Network.NodeType;
import smile.SMILEException;
import smile.learning.DataMatch;
import smile.learning.DataSet;
import smile.learning.EM;*/

import smile.*;
import smile.Network.NodeType;
import smile.learning.DataMatch;
import smile.learning.DataSet;
import smile.learning.EM;

public class BayesianLearner {

	static {
		gessi.plateoss.tools.ToolClassInSeparateJarInSharedDirectory.loadNativeLibrary();
	}

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

	private ArrayList<String> listAbsolutMeasures = new ArrayList<String>();

	/**
	 * 
	 * @param Khi
	 * @throws Exception
	 */
	public BayesianLearner(String Kqi) throws Exception {

		try {
			// License issued by BayesFusion Licensing Server
			// This code must be executed before any other jSMILE object is created
			new smile.License(
				"SMILE LICENSE 6be1f776 36754bf0 c79ded9a " +
				"THIS IS AN ACADEMIC LICENSE AND CAN BE USED " +
				"SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " +
				"AS DEFINED IN THE BAYESFUSION ACADEMIC " +
				"SOFTWARE LICENSING AGREEMENT. " +
				"Serial #: 5sv7g0zek32cl2rmb0unbc0rl " +
				"Issued for: OSCAR HERNAN FRANCO BEDOYA (oscar.franco@ucaldas.edu.co) " +
				"Academic institution: Universidad de Caldas " +
				"Valid until: 2021-05-01 " +
				"Issued by BayesFusion activation server",
				new byte[] {
				45,-81,-55,12,124,-127,54,-110,7,123,-84,-66,-122,-91,-90,31,
				-34,69,-87,-92,98,19,79,113,-47,28,86,-48,82,-25,-76,12,
				63,68,76,46,-34,-125,-41,-87,94,-103,119,113,-73,22,127,41,
				49,97,89,-12,-52,117,45,-66,-94,65,61,109,-83,76,1,-92
				}
			);

		} catch (RuntimeException e) {
			System.out.println("**************************************************");
			System.out.println(e.getMessage());
			System.out.println("**************************************************");

		}

		this.Kqi = Kqi;
		JSONParser parser = new JSONParser();

		/**
		 * Loading QuESo Model from file
		 */
		quesoModel = new QuESoModel();

		/**
		 * Loading the measures for the KQI
		 */

		String obj1 = quesoModel.getKHIMeasures();
		
		Object obj = parser.parse(quesoModel.getKHIMeasures(Kqi));
		
		
		measuresJSON = (JSONArray) obj;
		iNetwork = createBNStructureFromFile();
		iNetwork = learningBNParameters();
	}

	/**
	 * Create the BN Structure for the Khi with default probabilities
	 * 
	 * @param Kqi : The QuESo quality Sub-characteristic
	 * @return The standard Bayesian network
	 * @throws ParseException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private Network createBNStructureFromFile() throws FileNotFoundException, IOException, ParseException {

		System.out.println("Inicio CBNS");
		Network net = new Network();
		System.out.println("Crea red CBNS");

		// default distribution
		double[] aSuccessDef4 = { 1.0 / 3, 1.0 / 3, 1.0 / 3 };
		/**
		 * Khi Outcome Node
		 */
	
		net.addNode(NodeType.CPT, Kqi);
		
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
			System.out.println("KQI-->" + Kqi + "Measure-->" + strMeasure);
			net.addNode(Network.NodeType.CPT, strMeasure);
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

		// Update the beliefs
//		net.updateBeliefs();
		// net.writeFile("D:/OSSECOImplentation/tmps/" + Kqi + ".xdsl");
		return net;
	}

	/**
	 * Calculate the new probabilities using EM Algorithm with the historical data
	 * of the Khi loaded in the Constructor
	 * 
	 * @param iNetwork
	 * @return Network with learned probabilities distributions
	 * @throws Exception
	 */
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
				// System.out.println("id-->" + k + " Variable-->" +
				// ds.getVariableId(k) + " Not discretized");
				// TODO : improve this code

			}
		}

		// Change the name of the states in the general structure
		for (int i = 0; i < ds.getVariableCount(); i++) {
			String[] stateNames = ds.getStateNames(i);
			System.out.println("Variable-->" + ds.getVariableId(i) + "number of states-->" + stateNames.length);
			if ((iNetwork.getOutcomeCount(ds.getVariableId(i)) > 2) && (stateNames.length > 0)) {
				for (int j = 0; j < iNetwork.getOutcomeCount(ds.getVariableId(i)); j++) {
					System.out.println("Variable-->" + ds.getVariableId(i) + " state name-->" + stateNames[j]);
					iNetwork.setOutcomeId(ds.getVariableId(i), j, stateNames[j]);
				}
			} /*else {
				System.out.println("VariableA-->" + ds.getVariableId(i));
				iNetwork.setOutcomeId(ds.getVariableId(i), 0, "Low");
				iNetwork.setOutcomeId(ds.getVariableId(i), 1, "High");
			}*/
		}
		// iNetwork.getOutcomeIds(nodeHandle)

		/**
		 * Set variables with absolute values
		 */

		double[] aSuccessDef2 = { 0.0, 1.0 };
		for (String strM : listAbsolutMeasures) {
			System.out.println("Absolute-->" + strM);
			iNetwork.deleteOutcome(strM, 0);
			iNetwork.addOutcome(strM, "Fake");
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
		iNetwork.setTarget(Kqi, true);
		//iNetwork.writeFile("C:/OSSECOImplentation/tmps/" + Kqi + "BL.xdsl");
		EM em = new EM();

		/***
		 * NOTE: Values of KQIs without data are assigned using the Dirichlet
		 * distribution by default
		 */

		System.out.println("Before learn");
		iNetwork.setBayesianAlgorithm(7);
		em.setSeed(0);
		em.setRelevance(false);
		em.setUniformizeParameters(false);

		// em.learn(ds, iNetwork, matches);

		String strAbsolute[] = new String[listAbsolutMeasures.size()];
		strAbsolute = listAbsolutMeasures.toArray(strAbsolute);
		em.learn(ds, iNetwork, matches, strAbsolute);

		String[] nodesList = iNetwork.getAllNodeIds();

		for (String strNode : nodesList) {

			if (!strNode.equals(Kqi)) {

				iNetwork.setVirtualEvidence(strNode, iNetwork.getNodeDefinition(strNode));

			}
		}

		iNetwork.updateBeliefs();

	//	iNetwork.writeFile("C:/OSSECOImplentation/tmps/" + Kqi + "NL.xdsl");
		System.out.println("After learn");

		return iNetwork;
	}

	/**
	 * Load Data Set
	 * 
	 * @return Data Set
	 * @throws Exception
	 */
	private DataSet loadDataSet() throws Exception {

		DataSet ds = new DataSet();

		// TODO: Change for specific measure end-point
		MeasureRestClient objMRC = new MeasureRestClient();

		/**
		* 
		*/
		for (int i = 0; i < measuresJSON.size(); i++) {

			JSONObject jSONMeasure = (JSONObject) measuresJSON.get(i);
			String strEndpoint = jSONMeasure.get("Endpoint").toString();
			String strMeasure = jSONMeasure.get("Measure").toString();
			/**
			 * Only add measures with historical dataset
			 */
			if (!addVariableDataSet(objMRC.strJSONMeasure(strEndpoint), strMeasure, ds)) {
				listAbsolutMeasures.add(strMeasure);

			}

		}
		return ds;

	}

	/**
	 * 
	 * @param listValues
	 * @param measure
	 * @param ds
	 * @throws ParseException
	 */
	private boolean addVariableDataSet(String strValuesJSON, String measure, DataSet ds) throws ParseException {

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
		}
		/**
		 * Add historical values to the BN
		 */

		ds.addIntVariable(measure);

		int record = 0;

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jSONMeasure = (JSONObject) jsonArray.get(i);
			ds.addEmptyRecord();
			System.out.println(
					jsonArray.size() + " Measure-->" + measure + " Data-->" + jSONMeasure.get("Value").toString());
			ds.setInt(ds.findVariable(measure), record++, Integer.parseInt(jSONMeasure.get("Value").toString()));

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
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		String[] listOfNodes = iNetwork.getAllNodeIds();
		int i = 0;
		BayesianNode iBayesianNode = null;
		String strNode = "";
		while (i < listOfNodes.length & !strNode.equals(nodeID)) {
			strNode = listOfNodes[i++];

			if (strNode.equals(nodeID)) {

				iBayesianNode = new BayesianNode(strNode);
				String[] listOfStates = iNetwork.getOutcomeIds(strNode);
				double[] listOfValues = null;
				if (!nodeID.equals(Kqi))
					listOfValues = iNetwork.getNodeDefinition(strNode);
				else
					listOfValues = iNetwork.getNodeValue (strNode);
				// double[] listOfValues1 = iNetwork. (strNode);

				int j = 0;
				for (String strState : listOfStates) {
					iBayesianNode.addState(strState, Double.toString(listOfValues[j++]));
				}
			}
		}
		return iBayesianNode;
	}

}
