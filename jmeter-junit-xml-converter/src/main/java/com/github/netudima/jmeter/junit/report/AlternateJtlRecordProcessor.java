package com.github.netudima.jmeter.junit.report;

import org.w3c.dom.Element;

public interface AlternateJtlRecordProcessor {
    void process(Element suite, JtlRecord jtlRecord);
    Element processSuite(String suiteName);
}
