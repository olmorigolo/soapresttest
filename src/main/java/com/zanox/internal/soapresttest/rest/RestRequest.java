/**
 * 
 */
package com.zanox.internal.soapresttest.rest;

import java.util.HashMap;

import com.zanox.internal.soapresttest.Request;

class RestRequest implements Request {

	private HashMap<String, String> values;

	public RestRequest() {
		values = new HashMap<String, String>();
	}

	public void setValue(String name, String value) {
		values.put(name, value);
	}

	public void cleanupUnsetValues() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("?");

		for (String key : values.keySet()) {
			builder.append(key);
			builder.append("=");
			builder.append(values.get(key));
			builder.append("&");
		}

		builder.setLength(builder.length() - 1);

		return builder.toString();
	}
}
