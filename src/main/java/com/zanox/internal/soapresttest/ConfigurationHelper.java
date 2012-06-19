package com.zanox.internal.soapresttest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;

/**
 * this class manages all the config stuff for the rest tests, e.g. static id's and request
 * templates we use no test infrastructure for our tests (spring, seam etc), so we have to do
 * everything by hand;
 * 
 * @author uwe.janner
 * 
 */
public final class ConfigurationHelper {

	/**
	 * good old singleton pattern as we use no ioc framework.
	 */
	public static final ConfigurationHelper INSTANCE = new ConfigurationHelper();

	private Properties props;

	private ConfigurationHelper() {
		props = new Properties();
		InputStream in = null;
		try {
			String hostname = InetAddress.getLocalHost().getHostName();
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			in = cl.getResourceAsStream("tests.properties." + hostname);
			if (in == null) {
				in = cl.getResourceAsStream("tests.properties");
			}
			if (in == null) {
				throw new FileNotFoundException("tests.properties");
			}
			props.load(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public String getConnectId() {
		return props.getProperty("connectID");
	}

	public String getSecretKey() {
		return props.getProperty("secretKey");
	}

	public String getSoapRequestUrl(Boolean isSecured) {
		if (!isSecured) {
			return props.getProperty("soapRequestUrl");
		} else if (isSecured) {
			final String soapRequestBaseUrl = props.getProperty("soapRequestUrl");
			final String soapMethod = getSoapMethod();
			return soapRequestBaseUrl + "/" + soapMethod;
		}

		throw new IllegalStateException("Please specify the serviceType.");
	}

	public String getSoapMethod() {
		return props.getProperty("soapMethod");
	}

	public String getRestRequestUrl() {
		return props.getProperty("restRequestUrl");
	}

	public String getNamespace() {
		return props.getProperty("namespace");
	}

	public boolean getLogRequests() {
		return "true".equals(props.getProperty("logRequests"));
	}

	public boolean getLogResponses() {
		return "true".equals(props.getProperty("logResponses"));
	}

	public String getTestResourceAsString(String name) {
		String resourceName = "/xml/" + name + ".xml";
		InputStream is = getClass().getResourceAsStream(resourceName);
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8").name()));
					while ((line = reader.readLine()) != null) {
						sb.append(line).append(SystemUtils.LINE_SEPARATOR);
					}
				} catch (Exception e) {
					throw new RuntimeException("Could not read " + resourceName, e);
				}
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					// cant do anything here...
				}
			}
			return sb.toString();
		}
		throw new RuntimeException("The test resource " + resourceName + " does not exist!");
	}
}
