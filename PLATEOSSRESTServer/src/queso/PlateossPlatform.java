package queso;

import gessi.plateoss.commons.json.PlateosBD;
import gessi.plateoss.queso.MeasureAdapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/plateossplatform")
public class PlateossPlatform {
	@Path("{command}")
	@GET
	@Produces("application/json")

	public Response getBnKqi(@PathParam("command") String command) throws Exception {
		PlateosBD objPBD = new PlateosBD();
		String result="";

		if (command.contentEquals("list")) {
			result = objPBD.getOssecosList();
		}
		else
		{
		     result = objPBD.getOssecoInstance(command);	
		}
		
		return Response.status(200).entity(result).build();
	}

	@Path("{kqi}/{measure}")
	@GET
	@Produces("application/json")

	public Response getBnKqiMeas(@PathParam("kqi") String kqi, @PathParam("measure") String measure) throws Exception {
		System.out.println(kqi + "--" + measure);
		MeasureAdapter objMA = new MeasureAdapter(kqi, measure);
		String result = objMA.getJSonBayesian();
		return Response.status(200).entity(result).build();

	}
}
