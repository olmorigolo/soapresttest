package com.zanox.internal.soapresttest;

/**
 * A fully configurable client interface for REST and SOAP requests.
 * Can deals with templates.
 * 
 * @author martina.willig
 * 
 */
public interface ServiceClient {

	Response executeRequest();

	ServiceClient setValue(String paramName, String string);
	
	ServiceClient addHttpHeader(String headerName, String headerValue);

	void unsetValue(String paramName);

	ServiceClient setAuth(Authentication auth);

	ServiceClient setIsSecured(boolean isSecured);

	ServiceClient setDebug(boolean debug);
}
