package gessi.plateoss.commons.json.bk;



import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;




import org.json.JSONArray;
import org.json.JSONObject;

import gessi.plateoss.queso.bk.BayesianNode;

public class BayesianJSON {
	
	public String getJSONBayesianNode(String measure, BayesianNode node)
	{
		JSONObject obj = new JSONObject();
		obj.put("Name", measure);
		obj.put("Type", "Bayesian");
		obj.put("Nodes", getArrayNode(node));
		return obj.toString();
		
	}

	/**
	 *
	 * @param node
	 * @return
	 */

	private JSONArray getArrayNode(BayesianNode node) {
		JSONArray array = new JSONArray();

	
		Hashtable<String, String> contr = (Hashtable<String, String>) node
				.getListOfStates();
		Set<String> keys = contr.keySet();
		Iterator<String> itr = keys.iterator();
		String str;
		while (itr.hasNext()) {
			str = itr.next();
			JSONObject objd = new JSONObject();
			objd.put("State", str);
			objd.put("Value", contr.get(str));
			array.put(objd);
		}
		return array;
	}


}
