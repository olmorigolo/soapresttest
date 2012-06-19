package com.zanox.internal.soapresttest.rest;

import org.junit.Assert;
import org.junit.Test;

import com.zanox.internal.soapresttest.HttpVerb;
import com.zanox.internal.soapresttest.ServiceClient;
import com.zanox.internal.soapresttest.rest.RestResponse.ResponseType;

public class RestServiceClientTest {

	@Test
	public final void testSecuredRestCall() {
		ServiceClient client =
			new RestServiceClient("http://api.zanox.com/xml/2011-03-01/", "admedia?connectid={connectid}", HttpVerb.GET).setDebug(true).setValue(
				"connectid", "580599047DF8F5311043");

		RestResponse response = (RestResponse) client.executeRequest();

		Assert.assertNotNull(response.getResponse());
		
		Assert.assertEquals(ResponseType.XML, response.getResponseType());
	}
	
	@Test
	public final void testSecuredRestJSONCall(){
		ServiceClient client =
			new RestServiceClient("http://api.zanox.com/json/2011-03-01/", "admedia?connectid={connectid}", HttpVerb.GET).setDebug(true).setValue(
				"connectid", "580599047DF8F5311043");
		
		RestResponse response = (RestResponse) client.executeRequest();
		
		Assert.assertEquals(ResponseType.JSON, response.getResponseType());
	}

}
