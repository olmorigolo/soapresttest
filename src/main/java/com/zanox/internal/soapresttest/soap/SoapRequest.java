package com.zanox.internal.soapresttest.soap;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import com.zanox.internal.soapresttest.Request;

/**
 * A request object for soap requests. Is based on a xml template.
 * 
 * @author martina.willig
 * 
 */
public class SoapRequest implements Request {

	private Document document;

	public Document getDocument() {
		return document;
	}

	private void setDocument(Document document) {
		this.document = document;
	}

	/**
	 * Create a new xml document based on the given templateName.xml file.
	 * Search in path src/test/resources/xml/ for a matching template
	 * 
	 * @param templateName
	 * @throws Exception
	 */
	public SoapRequest(String templateName) throws Exception {
		String resourceName = "/xml/"+ templateName + ".xml";
		SAXBuilder sb = new SAXBuilder(XMLReaders.NONVALIDATING);
		this.setDocument(sb.build(getClass().getResource(resourceName)));
	}

	/**
	 * Set the text() value of the given element to value.
	 */
	public void setValue(String name, String value) {
		XPathFactory xpfac = XPathFactory.instance();
		Set<Namespace> namespaces = new HashSet<Namespace>();

		namespaces.addAll(document.getNamespacesIntroduced());
		namespaces.addAll(document.getNamespacesInherited());
		namespaces.addAll(document.getNamespacesInScope());
		namespaces.addAll(document.getRootElement().getAdditionalNamespaces());
		namespaces.addAll(document.getRootElement().getNamespacesInScope());

		XPathExpression xp = xpfac.compile(name, Filters.element(), null, namespaces);
		for (Object att : xp.evaluate(this.document)) {
			((Element) att).setText(value);
		}
	}

	/**
	 * Remove all nodes matching the xpath //*[text()='?']
	 */
	public void cleanupUnsetValues() {
		XPathFactory xpfac = XPathFactory.instance();
		Set<Namespace> namespaces = new HashSet<Namespace>();
		namespaces.addAll(document.getNamespacesIntroduced());
		namespaces.addAll(document.getNamespacesInherited());
		namespaces.addAll(document.getNamespacesInScope());
		namespaces.addAll(document.getRootElement().getAdditionalNamespaces());
		namespaces.addAll(document.getRootElement().getNamespacesInScope());
		
		XPathExpression xp = xpfac.compile("//*[text()='?']", Filters.element(), null, namespaces);
	 
		for (Object node : xp.evaluate(this.document)) {
			((Element)node).detach();
		}
		
	}

	@Override
	public String toString() {
		XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
		return xout.outputString(this.getDocument());
	}

}
