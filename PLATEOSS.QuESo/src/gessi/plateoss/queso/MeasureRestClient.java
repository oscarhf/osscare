package gessi.plateoss.queso;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MeasureRestClient {
	

	public String strJSONMeasure(String strEndpoint)
	{
		String output ="";
		try {

			Client client = Client.create();

			WebResource webResource = client
			   .resource(strEndpoint);

			ClientResponse response = webResource.accept("application/json")
	                   .get(ClientResponse.class);

			if (response.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatus());
			}

			output = response.getEntity(String.class);

		  } catch (Exception e) {

			e.printStackTrace();

		  }
		
			return output;
			
		
		
	}
	

}
