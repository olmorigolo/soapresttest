package com.zanox.internal.soapresttest;

import java.util.HashMap;
import java.util.Map;

/**
 * An Implementation of a service client for service tests.
 * 
 * Can deal with either templated xml (for soap requests) or parametrized rest-operations.
 * 
 * @author martina.willig
 * 
 */
public abstract class AbstractServiceClient implements ServiceClient {

	protected HttpVerb verb = HttpVerb.POST;

	protected String templateName;

	protected Map<String, String> httpHeaders = new HashMap<String, String>();

	protected Map<String, String> templateValues = new HashMap<String, String>();

	protected String operation;

	protected Boolean debug = false;

	protected Boolean isSecured = false;

	protected HashMap<String, String> namespaces;

	protected Authentication auth;

	protected String requestUrl;
	
	public ServiceClient addHttpHeader(String headerName, String headerValue) {
		httpHeaders.put(headerName, headerValue);
		return this;
	}

	public ServiceClient setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	public ServiceClient setIsSecured(boolean isSecured) {
		this.isSecured = isSecured;
		return this;
	}

	public ServiceClient setAuth(Authentication auth) {
		this.auth = auth;
		this.isSecured = true;
		return this;
	}

	public ServiceClient setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
		return this;
	}

	public ServiceClient setValue(String paramName, String value) {
		this.templateValues.put(paramName, value);
		return this;
	}

	public void unsetValue(String paramName) {
		templateValues.remove(paramName);
	}

	public ServiceClient resetValues() {
		templateValues.clear();
		return this;
	}

	public abstract Response executeRequest();

	protected String getSeparator(char ch) {
		StringBuilder sb = new StringBuilder("\n");
		for (int ii = 0; ii < 80; ii++) {
			sb.append(ch);
		}
		sb.append(sb.toString());
		return sb.toString();
	}

}
