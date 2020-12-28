package gessi.plateoss.queso;

import java.util.ArrayList;

public class BayesianNetwork {
	
	private String Khi;
	
	
	public BayesianNetwork(String khi) {
		super();
		Khi = khi;
	}

	public String getKhi() {
		return Khi;
	}

	
	private ArrayList<BayesianNode> listOfNodes = new ArrayList<BayesianNode>();

	public ArrayList<BayesianNode> getListOfNodes() {
		return listOfNodes;
	}
	
	public void addNode(BayesianNode nodeBN)
	{
		listOfNodes.add(nodeBN);
	}

}
