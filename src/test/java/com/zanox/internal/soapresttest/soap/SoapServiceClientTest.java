package com.zanox.internal.soapresttest.soap;

import static junit.framework.Assert.assertEquals;
import groovy.xml.MarkupBuilder;

import java.io.StringWriter;

import org.apache.commons.lang.SystemUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Port;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.Service;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import com.zanox.internal.soapresttest.ServiceClient;

public class SoapServiceClientTest {

	@Test
	public final void testUnsecuredSoap() {
		ServiceClient client =
			new SoapServiceClient("http://coreservices.staging.zanox.com/affiliatenetwork/promoservice/v1/soap", "getPromo").setDebug(true).setValue(
				"//soapenv:Body/v1:getPromoRequest//id", "14275");

		SoapResponse response = (SoapResponse) client.executeRequest();

		response.registerNamespace("ns2", "http://coreservices.zanox.com/affiliatenetwork/v1");
		response.registerNamespace("ns3", "http://coreservices.zanox.com/affiliatenetwork/promoservice/v1");

		assertEquals("Program Launch Groupon Schweiz", response.evalValue("//ns2:name"));
	}

	@Test
	public final void testParseWSDL() {
		String wsdlUrl = "http://coreservices.zanox.com/affiliatenetwork/wsdl/promo-service-1.1.wsdl";
		String serviceName = "PromoService";
		WSDLParser parser = new WSDLParser();
		Definitions defs = parser.parse(wsdlUrl);
		String requestUrl = null;

		for (Service service : defs.getServices()) {
			if (service.getName().equals(serviceName)) {
				if (service.getPorts().size() == 1) {
					requestUrl = service.getPorts().get(0).getAddress().getLocation();
				} else {

				}
				for (Port op : service.getPorts()) {
					System.out.println(" -portTypeName:" + op.getName() + " adress:" + op.getAddress().getLocation() + " bindingname:"
						+ op.getBinding().getName());
				}
				requestUrl = service.getPorts().get(0).getAddress().getLocation();
			}
		}
	}

	@Test
	public final void testFindRequestUrl() {
		String wsdlUrl = "http://coreservices.zanox.com/affiliatenetwork/wsdl/promo-service-1.1.wsdl";
		WSDLParser parser = new WSDLParser();
		Definitions defs = parser.parse(wsdlUrl);

		for (PortType pt : defs.getPortTypes()) {
			System.out.println(pt.getName());
			for (Operation op : pt.getOperations()) {
				System.out.println(" -" + op.getName());
			}
		}
	}
	

	@Test
	@Ignore // just for further development
	public final void testTemplateCreation() {
		String wsdlUrl = "http://coreservices.zanox.com/affiliatenetwork/wsdl/promo-service-1.1.wsdl";
		String operation = "getPromo";
		String porttypename = "PromoServicePort";
		String bindingName = "PromoServiceBinding";

		WSDLParser parser = new WSDLParser();

		Definitions wsdl = parser.parse(wsdlUrl);

		StringWriter writer = new StringWriter();
		SOARequestCreator creator = new SOARequestCreator();
		creator.setBuilder(new MarkupBuilder(writer));
		creator.setDefinitions(wsdl);
		creator.setCreator(new RequestTemplateCreator());

		// find Service with given name
		String requestUrl = null;
		//
		// if (servicename != null) {
		// // search the specific service
		// for (Service service : wsdl.getServices()) {
		// if (service.getName().equals(servicename)) {
		// requestUrl = service.getPorts().get(0).getAddress().getLocation();
		// bindingName = service.getPorts().get(0).getBinding().getName();
		// }
		// }
		// } else {
		// // take the first service from the list
		// Service service = wsdl.getServices().get(0);
		// servicename = service.getName();
		// requestUrl = service.getPorts().get(0).getAddress().getLocation();
		// bindingName = service.getPorts().get(0).getBinding().getName();
		//
		// }
		System.out.println("PortTypes with disposed methods: " + SystemUtils.LINE_SEPARATOR);
		for (PortType pt : wsdl.getPortTypes()) {
			System.out.println(pt.getName());
			for (Operation op : pt.getOperations()) {
				System.out.println(" -" + op.getName());
			}
		}
		System.out.println(SystemUtils.LINE_SEPARATOR);
		System.out.println("Services: " + SystemUtils.LINE_SEPARATOR);
		for (Service srv : wsdl.getServices()) {
			System.out.println(srv.getName());
			for (Port op : srv.getPorts()) {
				System.out.println(" -portTypeName:" + op.getName() + " adress:" + op.getAddress().getLocation() + " bindingname:"
					+ op.getBinding().getName());
			}
		}

		// creator.createRequest(PortType name, Operation name, Binding name);
		creator.createRequest(porttypename, operation, bindingName);

		System.out.println(writer);
	}

	@Test
	@Ignore // just for further development
	public final void testSOAMODEL() {
		WSDLParser parser = new WSDLParser();

		Definitions wsdl = parser.parse("http://coreservices.zanox.com/affiliatenetwork/wsdl/promo-service-1.1.wsdl");

		StringWriter writer = new StringWriter();
		SOARequestCreator creator = new SOARequestCreator();
		creator.setBuilder(new MarkupBuilder(writer));
		creator.setDefinitions(wsdl);
		creator.setCreator(new RequestTemplateCreator());
 
		// creator.createRequest(PortType name, Operation name, Binding name);
		creator.createRequest("PromoServicePort", "getPromo", "PromoServiceBinding");

		System.out.println(writer);
	}
}
