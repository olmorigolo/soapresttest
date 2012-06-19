package com.zanox.internal.soapresttest;

import java.util.Map;

/**
 * An interface for authentication operations.
 * 
 * @author martina.willig@zanox.com
 *
 */
public interface Authentication {

	abstract void secureRESTRequest(String operation, String verb, Map<String, String> httpHeaders);
	
	abstract void secureSOAPRequest(String operation, String verb, Map<String, String> templateValues);
}
