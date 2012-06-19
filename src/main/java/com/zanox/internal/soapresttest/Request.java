package com.zanox.internal.soapresttest;

public interface Request {

    void setValue(String name, String value);

    void cleanupUnsetValues();
}
