package com.zanox.internal.soapresttest.security;

import java.util.Map;

import com.zanox.internal.soapresttest.Authentication;

/**
 * A container class for authentication.
 * 
 * @author martina.willig
 * 
 */
public class ZanoxAuthenticationHelper implements Authentication {

	private String connectId;

	private String secretKey;

	private String serviceName;

	public String getConnectId() {
		return connectId;
	}

	public void setConnectId(String connectId) {
		this.connectId = connectId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	 

	private void setRestAuthorizationData(final String connectID, final String timeStamp, final String nonce, final String sig,
		final Map<String, String> headers) {
		String auth = "ZXWS " + connectID + ":" + sig;
		headers.put("Authorization", auth);
		headers.put("Nonce", nonce);
		headers.put("Date", timeStamp);
		headers.put("Accept", "application/xml;charset=UTF-8");
	}
	
	/**
	 * Adds template values for connectId, timestamp, nonce and signature to the templateValues map.
	 */
	private void setSOAPAuthorizationData(String connectId, String timestamp, String nonce, String signature, Map<String, String> templateValues) {
		templateValues.put("//soapenv:Body/*/ns:connectId", connectId);
		templateValues.put("//soapenv:Body/*/ns:timestamp", timestamp);
		templateValues.put("//soapenv:Body/*/ns:nonce", nonce);
		templateValues.put("//soapenv:Body/*/ns:signature", signature);

		templateValues.put("//soapenv:Body/*/connectId", connectId);
		templateValues.put("//soapenv:Body/*/timestamp", timestamp);
		templateValues.put("//soapenv:Body/*/nonce", nonce);
		templateValues.put("//soapenv:Body/*/signature", signature);
	}


	public void secureRESTRequest(String operation, String verb, Map<String, String> httpHeaders) {
		String timestamp = ZanoxSecurityUtils.createRestTimestamp(null);
		String nonce = ZanoxSecurityUtils.createNonce();
		String signature = ZanoxSecurityUtils.createRestSignature(this.getSecretKey(), nonce, operation, verb, timestamp);
		setRestAuthorizationData(this.getConnectId(), timestamp, nonce, signature, httpHeaders);
	}

	public void secureSOAPRequest(String operation, String verb, Map<String, String> templateValues) {
		String timestamp = ZanoxSecurityUtils.createSoapTimestamp(null);
		String nonce = ZanoxSecurityUtils.createNonce();
		String signature = ZanoxSecurityUtils.createSoapSignature(this.getSecretKey(), nonce, this.getServiceName(), operation, timestamp);
		setSOAPAuthorizationData(this.getConnectId(), timestamp, nonce, signature, templateValues);
	}

}
