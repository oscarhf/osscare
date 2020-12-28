package gessi.plateoss.osseco.json_adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
* <dl>
* <dt>Purpose: Get the measures of the OSSECO 
* <dd>
*
* <dt>Description:
* <dd> This class implement the adapter of the measures from Hash-tables
* to JSON objects  
* <dt>Example usage:
* <dd>
* <pre>
* </pre>
* </dd>
* </dl>
* @version v0.1, 2016/03/30 (March) -- first release
* @author  Oscar Franco-Bedoya (<a href="oscarhf2002@hotmail.com">oscarhf2002.@hotmail.com</a>)
*/


public class JSonMeasure {


	/**
	 * Return a JSON string with the measure 
	 * @param measure
	 * @param listOfValues
	 * @param source
	 * @return a JSON measure
	 * @throws JSONException
	 * @throws ParseException
	 */
	public String jSonMessure(String measure,
			List<String> listOfValues, String source) throws JSONException, ParseException {
		JSONObject obj = new JSONObject();
		obj.put("Name", measure);
		obj.put("Date", LocalDate.now());
		obj.put("StartDate", getDate(listOfValues,0));
		obj.put("FinalDate", getDate(listOfValues,listOfValues.size()-1));
		obj.put("Source", source);
		obj.put("Type", "Historical");
		/**
		 * It is a Single measure
		 */
		if(listOfValues.size()==1)
		{
			obj.put("Type", "Absolut");
			
		}
		
		obj.put("data", getArrayMeasure(listOfValues));
		return obj.toString();
	}

	
	/**
	 * Transform a List object in a JSONArray object
	 * @param listOfValues
	 * @return a JSONArray with the historical values of the measure
	 */
	private JSONArray getArrayMeasure(List<String> listOfValues) {
		JSONArray array = new JSONArray();
		
		for(String str: listOfValues)
		{
			String strSplit[] = str.split(",");
			JSONObject objd = new JSONObject();
			objd.put("Date",  strSplit[0]);
			objd.put("Value", strSplit[1]);
			array.put(objd);
		}
		return array;
	}


	/**
	 * Return the date in pos of  the listOfValues
	 * @param listOfValues
	 * @param pos
	 * @return
	 * @throws ParseException 
	 */
	private Date getDate(List<String> listOfValues, int pos) throws ParseException
	{

		SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM");
		String strDate[] = listOfValues.get(pos).split(",");
		return sdfSource.parse(strDate[0]);
	}


}
