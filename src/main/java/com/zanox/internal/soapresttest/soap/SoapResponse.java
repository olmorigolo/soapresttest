/**
 *
 */
package com.zanox.internal.soapresttest.soap;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.JDOMException;


/**
 * <p>
 * A Response encapsulates the data returned from a webservice call and offers methods to access data elements.
 * </p>
 * 
 * @author martina.willig
 * 
 */
public class SoapResponse extends com.zanox.internal.soapresttest.XmlResponse {

	public SoapResponse(InputStream response) throws JDOMException, IOException {
		super(response);
	}

	/**
	 * .
	 * 
	 * @return true if an error entry is present
	 */
	public boolean isError() {
		return evalValue("//soap:Fault") != null;
	}

	/**
	 * .
	 * 
	 * @return extract the error code
	 */
	public String getErrorCode() {
		return evalValue("//zx:SOAPException/zx:code");
	}

	/**
	 * .
	 * 
	 * @return extract fault message for SOAP or error reson for REST
	 */
	public String getFaultMessage() {
		return evalValue("//zx:SOAPException/zx:message");
	}

	/**
	 * .
	 * 
	 * @return extract the error message
	 */
	public String getErrorMessage() {
		return evalValue("//soap:Fault/faultstring");
	}

	/**
	 * .
	 * 
	 * @return extract the error reason
	 */
	public String getErrorReason() {
		return evalValue("//zx:SOAPException/zx:reason");
	}
}
