package com.zanox.internal.soapresttest.soap;

import groovy.xml.MarkupBuilder;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import com.zanox.internal.soapresttest.AbstractServiceClient;
import com.zanox.internal.soapresttest.HttpVerb;
import com.zanox.internal.soapresttest.Request;

/**
 * A fully configurable SOAP client.
 * Can deal with templates stored by default under src/test/resources/xml.
 * 
 * @author martina.willig
 * 
 */
public class SoapServiceClient extends AbstractServiceClient {

	/**
	 * Creates a new configurable client. Is not secure by default.
	 * 
	 * @param requestUrl The request URL.
	 * @param operation The operation name. Is used as templateName by default. (src/test/resources/operation.xml)
	 */
	public SoapServiceClient(String requestUrl, String operation) {
		this.requestUrl = requestUrl;
		this.templateName = operation;
		this.operation = operation;
	}

	/**
	 * Creates a new configurable client. Is not secure by default.
	 * 
	 * @param requestUrl The request URL.
	 * @param operation The operation name.
	 * @param templateName The templateName. (src/test/resources/templateName.xml)
	 */
	public SoapServiceClient(String requestUrl, String operation, String templateName) {
		this.requestUrl = requestUrl;
		this.templateName = templateName;
		this.operation = operation;
		this.verb = HttpVerb.POST;
	}
	
	/**
	 * Create a template for the given operation based on the wsdl. Is not secure by default.
	 * 
	 * @param wsdl The wsdl. Only static urls are supported.
	 */
	private void createTemplate(String wsdl, String operation){
		WSDLParser parser = new WSDLParser();
		 
	    Definitions definitions = parser.parse(wsdl); //"http://coreservices.zanox.com/affiliatenetwork/promoservice/v1/soap?wsdl");
	    StringWriter writer = new StringWriter();
	    SOARequestCreator creator = new SOARequestCreator();
	    creator.setBuilder(new MarkupBuilder(writer));
	    creator.setDefinitions(definitions);
	    creator.setCreator(new RequestTemplateCreator());
	    
	    //creator.createRequest(PortType name, Operation name, Binding name);
	    creator.createRequest("PromoService", operation, "PromoServiceBinding");

	    // TODO write File create template file in src/test/resources/operation.xml
	    System.out.println(writer);
	}

	/**
	 * Executes the request.
	 */
	public SoapResponse executeRequest() {

		if (this.isSecured) {
			auth.secureSOAPRequest(operation, this.verb.name(), templateValues);
		}
		try {

			Request requestTemplate;

			requestTemplate = new SoapRequest(templateName);
			for (Entry<String, String> entry : templateValues.entrySet()) {
				if (entry.getValue() != null) {
					requestTemplate.setValue(entry.getKey(), entry.getValue());
				}
			}
			requestTemplate.cleanupUnsetValues();

			if (this.debug) {
				System.out.println(getSeparator('#') + "\n\n<<< outgoing REQUEST >>>\nused URL: " + this.requestUrl + "\ndata:\n"
					+ requestTemplate.toString() + "\nHTTP headers: " + httpHeaders.toString() + "\n");
			}
			long startTime = System.currentTimeMillis();

			HttpMethod method = null;

			method = new PostMethod(this.requestUrl);
			ByteArrayRequestEntity reqEntity =
				new ByteArrayRequestEntity(requestTemplate.toString().getBytes(Charset.forName("UTF-8")), "text/xml;charset=UTF-8");

			((PostMethod) method).setRequestEntity(reqEntity);

			for (String httpHeaderName : httpHeaders.keySet()) {
				method.addRequestHeader(httpHeaderName, httpHeaders.get(httpHeaderName));
			}
			
			//method.addRequestHeader("SOAPAction", "urn:com-zanox:mdm:20120701-05R6/IMDMService/readBusinessPartnerByUserId");

			// execute the request

			HttpClient httpClient = new HttpClient();

			httpClient.executeMethod(method);

			// return response
			SoapResponse response = new SoapResponse(method.getResponseBodyAsStream());

			long time = (System.currentTimeMillis() - startTime);
			if (this.debug) {
				System.out.println(getSeparator('-') + "\n\n<<< incomming RESPONSE (" + time / 1000f + " mSecs) >>>\n");
				response.prettyPrint();
				System.out.println(getSeparator('#'));
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
