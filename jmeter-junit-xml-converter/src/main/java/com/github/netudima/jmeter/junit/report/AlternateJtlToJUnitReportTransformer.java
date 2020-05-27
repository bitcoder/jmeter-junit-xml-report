package com.github.netudima.jmeter.junit.report;

import java.io.IOException;

import org.w3c.dom.Element;

public class AlternateJtlToJUnitReportTransformer {

    public void transform(String jtlFile, String junitReportFile, String testSuiteName) throws IOException {
        try (final AlternateDomXmlJUnitReportWriter writer
                     = new AlternateDomXmlJUnitReportWriter(junitReportFile, testSuiteName)) {
            AlternateJtlFileReader reader = new AlternateJtlFileReader();
            reader.parseCsvJtl(jtlFile, new AlternateJtlRecordProcessor() {
                @Override
                public void process(Element suite, JtlRecord jtlRecord) {
                    writer.write(suite, jtlRecord);
                }

                @Override
                public Element processSuite(String suiteName) {
                    return writer.createSuite(suiteName);
                }
            });
        }
    }

    public void transform(String jtlFile, String junitReportFile) throws IOException {
        transform(jtlFile, junitReportFile, "");
    }
}
