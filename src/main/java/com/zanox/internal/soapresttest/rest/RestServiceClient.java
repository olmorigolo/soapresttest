package com.zanox.internal.soapresttest.rest;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

import com.zanox.internal.soapresttest.AbstractServiceClient;
import com.zanox.internal.soapresttest.HttpVerb;
import com.zanox.internal.soapresttest.Response;
import com.zanox.internal.soapresttest.rest.RestResponse.ResponseType;

/**
 * A fully configurable SOAP client.
 * Can deal with templated operation.
 * 
 * @author martina.willig
 * 
 */
public class RestServiceClient extends AbstractServiceClient {
	
	/**
	 * 
	 * @param requestUrl The request URL. Example: http://api.zanox.com/xml/publisher/
	 * @param verb The HTTP verb.
	 * @param operation The request operation. May contain wildcards {} for templating. Example: admedia?connectid={connectid}&userid={userid}
	 *        Use {@link AbstractServiceClient#setValue(String, String)} to fill the templated operation.
	 */
	public RestServiceClient(String requestUrl, String operation, HttpVerb verb) {
		this.requestUrl = requestUrl;
		this.verb = verb;
		this.operation = operation;
	}

	/**
	 * Executes the request.
	 */
	public Response executeRequest() {

		try {
			// TODO: the following block is rather ugly, and the abstraction of *RequestHelpers has lost the commonalities between
			// rest and soap; this block is a special case for the rest call; we should throw away RequestHelperIntf and
			// just write traditional "if(rest)....else...."
			// in rest operations object replace {xxx}-values:
			String templatedOperation = this.operation;
			// we also need to retain a not-urlencoded-version of newRestOperation - bc its needed for the signature...
			String newRestOperationUnencoded = this.operation;
			// remember all values replaced in the url:
			Set<String> replacedUrlPathParameters = new HashSet<String>();
			if (templatedOperation != null) {
				for (Iterator<Entry<String, String>> iter = templateValues.entrySet().iterator(); iter.hasNext();) {
					Entry<String, String> entry = iter.next();
					// is this parameter present in the request url?
					String urlPathParam = "{" + entry.getKey() + "}";
					if (templatedOperation.contains(urlPathParam)) {
						templatedOperation = templatedOperation.replace(urlPathParam, URLEncoder.encode(entry.getValue(), Charset.forName("UTF-8").toString()));
						newRestOperationUnencoded = newRestOperationUnencoded.replace(urlPathParam, entry.getValue());
						replacedUrlPathParameters.add(entry.getKey());
					}
				}
			}

			String completeRequestUrl = this.requestUrl + templatedOperation;

			if (this.isSecured) {
				auth.secureRESTRequest(templatedOperation, this.verb.name(), httpHeaders);
			}
			
			if (this.debug) {
				System.out.println(getSeparator('#') + "\n\n<<< outgoing REQUEST >>>\nused URL: " + completeRequestUrl + "\nHTTP headers: "
					+ httpHeaders.toString() + "\n");
			}
			long startTime = System.currentTimeMillis();

			HttpMethod method = null;
			switch (this.verb) {
				case GET: {
					method = new GetMethod(completeRequestUrl);
					break;
				}
				case POST: {
					method = new PostMethod(completeRequestUrl);
					break;
				}
				case PUT: {
					method = new PutMethod(completeRequestUrl);
					break;
				}
				case DELETE: {
					method = new DeleteMethod(completeRequestUrl);
					break;
				}
				default:
					throw new RuntimeException("Who did invent the fifth httpverb?");
			}

			for (String httpHeaderName : httpHeaders.keySet()) {
				method.addRequestHeader(httpHeaderName, httpHeaders.get(httpHeaderName));
			}

			// execute the request

			HttpClient httpClient = new HttpClient();

			httpClient.executeMethod(method);

			Header contentTypeHeader = method.getResponseHeader("content-type");
			
			System.out.println("Mimetype: " + contentTypeHeader.getValue());
			
			String mimetype = contentTypeHeader.getValue();
			
			RestResponse response = null;
			if(mimetype.contains("application/xml")){
				response = new RestResponse(method.getResponseBodyAsStream(), ResponseType.XML);
			} else if(mimetype.contains("application/json")){
				response = new RestResponse(method.getResponseBodyAsStream(), ResponseType.JSON);
			} else{
				throw new IllegalStateException("I don't understand the following mimetype: " + mimetype);
			}
			 
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
