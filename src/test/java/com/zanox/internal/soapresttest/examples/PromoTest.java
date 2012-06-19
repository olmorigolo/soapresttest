package com.zanox.internal.soapresttest.examples;

import static junit.framework.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.zanox.internal.soapresttest.ServiceClient;
import com.zanox.internal.soapresttest.security.ZanoxAuthenticationHelper;
import com.zanox.internal.soapresttest.soap.SoapResponse;
import com.zanox.internal.soapresttest.soap.SoapServiceClient;

public class PromoTest {

	public PromoTest() {
	}

	/**
	 * Demonstrates a test call to an unsecured service.
	 */
	@Test
	public void testGetPromoSOAP() {

		ServiceClient client =
			new SoapServiceClient("http://coreservices.zanox.com/affiliatenetwork/promoservice/v1/soap", "getPromo").setDebug(true).setValue(
				"//soapenv:Body/v1:getPromoRequest//id", "14275");

		SoapResponse response = (SoapResponse) client.executeRequest();

		response.registerNamespace("ns2", "http://coreservices.zanox.com/affiliatenetwork/v1");
		response.registerNamespace("ns3", "http://coreservices.zanox.com/affiliatenetwork/promoservice/v1");

		assertEquals("Program Launch Groupon Schweiz", response.evalValue("//ns2:name"));
	}

	/**
	 * Demonstrates a test call to a secured service with invalid authentication data.
	 */
	@Test
	public void testGetProgramSecuredSOAP() {

		ServiceClient service =
			new SoapServiceClient("http://api.zanox.com/soap/2011-03-01/", "getProgram").setDebug(true)
				.setValue("//ns:programId", "3096").setValue("//ns:connectId", "580599047DF8F5311043");

		SoapResponse response = (SoapResponse) service.executeRequest();
		 
		response.registerNamespace("pfx", "http://api.zanox.com/namespace/2011-03-01/");
		
		assertEquals(false, response.isError());

		assertEquals("La Redoute FR", response.evalValue("//pfx:name"));
		
	}
	
	
	/**
	 * Demonstrates a test call to a secured service with the authentication bean ad
	 * false authentication data.
	 */
	@Test
	public void testGetProgramSecuredSOAPWithFalseAuthData() {

		ZanoxAuthenticationHelper auth = new ZanoxAuthenticationHelper();
		auth.setConnectId("999999999999999999");
		auth.setSecretKey("99999999999999999999999999999999");
		auth.setServiceName("PublisherService");

		ServiceClient service =
			new SoapServiceClient("http://api.zanox.com/soap/2011-03-01/", "getProgram").setAuth(auth).setDebug(true)
				.setValue("//ns:programId", "3096");

		SoapResponse response = (SoapResponse) service.executeRequest();
		 
		assertEquals(true, response.isError());

	}
}
