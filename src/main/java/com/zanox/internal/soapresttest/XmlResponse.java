package com.zanox.internal.soapresttest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.Verifier;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public abstract class XmlResponse implements Response {

	protected Document document;
	
	public Document getDocument() {
		return document;
	}

	protected void setDocument(Document document) {
		this.document = document;
	}
	/**
	 * Create a new reponse object.
	 * 
	 * @param response The response data.
	 * @throws JDOMException if the response data is corrupt
	 * @throws IOException if the response data is corrupt
	 */
	public XmlResponse(InputStream response) throws JDOMException, IOException {
		SAXBuilder sb = new SAXBuilder(XMLReaders.NONVALIDATING);
		this.setDocument(sb.build(response));
	}

	/**
	 * Add a namespace declaration to the response document's root node.
	 * 
	 * @param prefix The namespace prefix
	 * @param uri The namespace uri
	 */
	public void registerNamespace(String prefix, String uri) {
		Element root = document.getRootElement();
		
		Verifier.checkNamespacePrefix(prefix);
		Verifier.checkNamespaceURI(uri);
		
		if(root.getNamespace(prefix) != null){
			Namespace existingNamespace = root.getNamespace(prefix);
			root.removeNamespaceDeclaration(existingNamespace);
		}
		Namespace namespace = Namespace.getNamespace(prefix, uri);
		root.addNamespaceDeclaration(namespace);
	}

	/**
	 * Evaluate an xpath expression against the response document.
	 * 
	 * @param expression The xpath expression
	 * @return the list of matching elements
	 * @throws XPathExpressionException .
	 */
	public List<Element> evalXpath(String expression) {
		XPathFactory xpfac = XPathFactory.instance();

		XPathExpression xp = xpfac.compile(expression, Filters.element(), null, document.getRootElement().getNamespacesInScope());

		return xp.evaluate(this.document.getRootElement());
	}
	
	/**
	 * Extract a single value by xpath.
	 * 
	 * @param expression .
	 * @return matching node as string; if xpath matches multiple values, only first is returned
	 */
	public String evalValue(String expression) {
		
		List<Element> evaluate = evalXpath(expression);
		
		if (evaluate.size() > 0) {
			return evaluate.get(0).getText();
		}
		return null;
	}
	
	/**
	 * Pretty prints the content of this document.
	 */
	public void prettyPrint() {
		XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
		try {
			xout.output(this.getDocument(), System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}