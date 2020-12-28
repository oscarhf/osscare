package gessi.plateoss.queso.bk;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MeasureRestClient {
	
	private String urlOSSECO;

	public MeasureRestClient(String urlOSSECO) {
		super();
		this.urlOSSECO = urlOSSECO;
	}
	
	public String strJSONMeasure(String strMeasure)
	{
		String output ="";
		try {

			Client client = Client.create();

			WebResource webResource = client
			   .resource(urlOSSECO+strMeasure);

			ClientResponse response = webResource.accept("application/json")
	                   .get(ClientResponse.class);

			if (response.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatus());
			}

			output = response.getEntity(String.class);

		  } catch (Exception e) {

			System.out.println(e.getMessage());
			throw e;

		  }
		
			return output;
			
		
		
	}
	

}
