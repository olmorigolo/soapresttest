package com.zanox.internal.soapresttest.soap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SoapResponseTest {
	InputStream stream = null;
	
	@Before
	public void setUp() throws Exception {
		try {
			stream = new FileInputStream("src/test/resources/core_service_failure.xml");
		} catch (FileNotFoundException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public final void test() {
		SoapResponse response= null;
		try {
			response = new SoapResponse(stream);
		} catch (JDOMException e) {
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}

		response.toString();
		List<Element> elements = response.evalXpath("//env:Fault//faultstring");
		
		Assert.assertNotNull("There should be at least a faultstring", elements);
		Assert.assertFalse("There should be at least a faultstring", elements.size() == 0);
	}

}
