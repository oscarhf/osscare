package gessi.plateoss.queso;

import java.util.Hashtable;

public class BayesianNode {

	private String Id;
	private Hashtable<String,String> listOfStates= new Hashtable<String, String>();
	public BayesianNode(String id) {
		super();
		Id = id;
	}
	public void addState(String state, String value)
	{
		listOfStates.put(state, value);
	}
	public Hashtable<String, String> getListOfStates() {
		return listOfStates;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	
	
	
	
}
