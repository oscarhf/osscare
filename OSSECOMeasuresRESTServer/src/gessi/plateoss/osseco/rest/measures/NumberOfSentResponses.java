package gessi.plateoss.osseco.rest.measures;

import gessi.plateoss.osseco.json_adapter.JSonMeasure;
import gessi.plateoss.osseco.measure_calculator.MeasureListAdapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/numberofsentresponses")
public class NumberOfSentResponses {
	@GET
	@Produces("application/json")
	public Response getNoP() throws Exception {
		
		MeasureListAdapter objMHTAdapter= new MeasureListAdapter();
		JSonMeasure objJSonMeasure = new JSonMeasure();
		String result =objJSonMeasure.jSonMessure("numberofsentresponses",objMHTAdapter.getCollectionMeasureList("sent_response_date"), "eclipse");
		return Response.status(200).entity(result).build();
	}

}
