package com.zanox.internal.soapresttest.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.zanox.internal.soapresttest.Response;

public class JSONResponse implements Response {

	protected JSONObject data;
	
	public JSONObject getData() {
		return data;
	}

	protected void setData(JSONObject data) {
		this.data = data;
	}
	
	public JSONResponse(String content) throws JSONException{
		data = new JSONObject(content);
	}
	
	public JSONResponse(InputStream stream) throws JSONException, IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(stream, writer, "UTF-8");
		String theString = writer.toString();
		data = new JSONObject(theString);
	}
	
	public boolean isError() {
		// TODO Auto-generated method stub
		return false;
	}

	public String evalValue(String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getErrorCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFaultMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getErrorReason() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Pretty prints the content of this document.
	 */
	public void prettyPrint() {
		try {
			System.out.println(data.toString(4));
		} catch (JSONException e) {
			System.out.println("Error while sysout JSON data: " + e.getMessage());
		}
	}
}
