package com.zanox.internal.soapresttest;

import static junit.framework.Assert.assertEquals;

public final class GenericTestHelper {

	/** Do not use (utility class)! */
	private GenericTestHelper() {
	}

	/**
	 * Executes the given request and checks if error reason and code are correct.
	 * 
	 * @param client
	 * @param expectedErrorReason expected error reason
	 * @param expectedErrorCode expected code
	 */
	public static void executeErrorCase(ServiceClient client, String expectedErrorReason, String expectedErrorCode) {
		Response response = client.executeRequest();

		if (expectedErrorCode != null) {
			assertEquals(expectedErrorCode, response.getErrorCode());
		}

		if (expectedErrorReason != null) {
			assertEquals(expectedErrorReason, response.getErrorReason());
		}
	}

	/**
	 * Sets the clients security to false, executes the request and asserts the correct exception.
	 * 
	 * @param client
	 * @param expectedExceptionReason the expected exception reason
	 */
	public static void doTestWithMissingRights(ServiceClient client, String expectedExceptionReason) {
		client.setIsSecured(false);
		Response response = client.executeRequest();
		assertEquals("103", response.getErrorCode());
		assertEquals(expectedExceptionReason, response.getErrorReason());
	}
}
