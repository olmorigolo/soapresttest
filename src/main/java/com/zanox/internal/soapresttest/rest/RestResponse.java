package com.zanox.internal.soapresttest.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.jdom2.JDOMException;
import org.json.JSONException;

import com.zanox.internal.soapresttest.Response;
import com.zanox.internal.soapresttest.XmlResponse;
import com.zanox.internal.soapresttest.soap.SoapResponse;

public class RestResponse implements Response {

	private XmlResponse xmlResponse;
	private JSONResponse jsonResponse;

	private ResponseType type = ResponseType.XML;

	public ResponseType getResponseType(){
		return this.type;
	}
	
	public Response getResponse() {
		if (type.equals(ResponseType.XML)) {
			return xmlResponse;
		} else {
			return jsonResponse;
		}
	}

	public RestResponse(InputStream stream, ResponseType type) throws IOException {
		this.type = type;
		if (type.equals(ResponseType.XML)) {
			try {
				xmlResponse = new SoapResponse(stream);
			} catch (JDOMException e) {
				throw new IOException("Could not parse input as XML: " + e.getMessage(), e);
			}
		} else if (type.equals(ResponseType.JSON)) {
			try {
				String myString = IOUtils.toString(stream, Charset.forName("UTF-8").toString());
				jsonResponse = new JSONResponse(myString);
			} catch (JSONException e) {
				throw new IOException("Could not parse input as JSON: " + e.getMessage(), e);
			}
		}
	}

	public boolean isError() {
		if (type.equals(ResponseType.XML)) {
			return xmlResponse.isError();
		} else {
			return jsonResponse.isError();
		}
	}

	public String evalValue(String expression) {
		if (type.equals(ResponseType.XML)) {
			return xmlResponse.evalValue(expression);
		} else {
			return jsonResponse.evalValue(expression);
		}
	}

	public String getErrorCode() {
		if (type.equals(ResponseType.XML)) {
			return xmlResponse.getErrorCode();
		} else {
			return jsonResponse.getErrorCode();
		}
	}

	public String getFaultMessage() {
		if (type.equals(ResponseType.XML)) {
			return xmlResponse.getFaultMessage();
		} else {
			return jsonResponse.getFaultMessage();
		}
	}

	public String getErrorMessage() {
		if (type.equals(ResponseType.XML)) {
			return xmlResponse.getErrorMessage();
		} else {
			return jsonResponse.getErrorMessage();
		}
	}

	public String getErrorReason() {
		if (type.equals(ResponseType.XML)) {
			return xmlResponse.getErrorReason();
		} else {
			return jsonResponse.getErrorReason();
		}
	}

	/**
	 * Pretty prints the content of this document.
	 */
	public void prettyPrint() {
		if (type.equals(ResponseType.XML)) {
			xmlResponse.prettyPrint();
		} else {
			jsonResponse.prettyPrint();
		}
	}

	public enum ResponseType {
		XML, JSON
	}
}
