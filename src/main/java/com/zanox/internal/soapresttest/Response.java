package com.zanox.internal.soapresttest;

public interface Response {

	abstract boolean isError();

	abstract String evalValue(String expression);

	abstract String getErrorCode();

	abstract String getFaultMessage();

	abstract String getErrorMessage();

	abstract String getErrorReason();
	
	abstract void prettyPrint();
}
