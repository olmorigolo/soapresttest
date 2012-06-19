package com.zanox.internal.soapresttest.soap;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.junit.Assert;
import org.junit.Test;

public class SoapRequestTest {
	
	SoapRequest request = null;

	@Test
	public final void testSoapRequest() {
		SoapRequest request = null;
		try {
			request = new SoapRequest("getAdMedium");
		} catch (Exception e) {
			Assert.fail("Could not load document getAdMedium.xml due to error: "+e.getMessage());
		}
		Assert.assertNotNull("Request object should not be null.",request);
		Assert.assertNotNull("Document object should not be null.", request.getDocument());
	}

	@Test
	public final void testSetValue() {
		try {
			request = new SoapRequest("getProgram");
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		
		Assert.assertNotNull(request);
		Assert.assertNotNull(request.getDocument());
		
		request.setValue("//ns:programId", "3096");
 
		Document document = request.getDocument();
		
		Namespace namespace = Namespace.getNamespace("ns", "http://api.zanox.com/namespace/2011-03-01/");
		document.getRootElement().addNamespaceDeclaration(namespace);
		
		XPathFactory xpfac = XPathFactory.instance();
		
		XPathExpression xp = xpfac.compile("//ns:programId", Filters.element(), null, document.getRootElement().getNamespacesInScope());

		List<Element> elements = xp.evaluate(document);
		
		
		Assert.assertNotNull("Result is null.", elements);
		Assert.assertTrue("There should be at least the programId node in elements.", elements.size() == 1);
		Assert.assertTrue("The text of the element should be 3096", elements.get(0).getText().equals("3096"));
	}

	@Test
	public final void testCleanupUnsetValues() {
		try {
			request = new SoapRequest("getAdMedium");
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		
		Assert.assertNotNull(request);
		Assert.assertNotNull(request.getDocument());
		
		request.setValue("//ns:admediumId", "3096");
 
		Document document = request.getDocument();
		
		Namespace namespace = Namespace.getNamespace("ns", "http://api.zanox.com/namespace/2011-03-01/");
		document.getRootElement().addNamespaceDeclaration(namespace);
		
		XPathFactory xpfac = XPathFactory.instance();
		
		XPathExpression xp = xpfac.compile("//ns:adspaceId", Filters.element(), null, document.getRootElement().getNamespacesInScope());

		List<Element> elements = xp.evaluate(document);
		
		Assert.assertNotNull("Result is null.", elements);
		Assert.assertTrue("There should be at least the ns:adspaceId node in elements.", elements.size() == 1);
		Assert.assertTrue("The text of the element should be '?'", elements.get(0).getText().toString().equals("?"));
		
		request.cleanupUnsetValues();

		elements = xp.evaluate(document);
		
		Assert.assertNotNull("Result is null.", elements);
		Assert.assertTrue("There should be at least the ns:adspaceId node in elements.", elements.size() == 0);
	}

}
