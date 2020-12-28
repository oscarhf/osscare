package queso;


import gessi.plateoss.queso.MeasureAdapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/bayesianqueso")
public class BayesianQueso {
	@Path("{kqi}")
	@GET
	@Produces("application/json")

	public Response getBnKqi(@PathParam("kqi") String kqi)throws Exception {
		MeasureAdapter objMA = new MeasureAdapter(kqi,kqi);
		System.out.println("KQI--"+kqi);
		String result = objMA.getJSonBayesian();
		return Response.status(200).entity(result).build();
			
	}
	@Path("{kqi}/{measure}")
	@GET
	@Produces("application/json")

	public Response getBnKqiMeas(@PathParam("kqi") String kqi,
			@PathParam("measure") String measure  )throws Exception {
		System.out.println(kqi+"--"+measure);
		MeasureAdapter objMA = new MeasureAdapter(kqi,measure);
		String result = objMA.getJSonBayesian();
		return Response.status(200).entity(result).build();
			
	}
}
