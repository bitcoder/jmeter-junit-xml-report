package com.github.netudima.jmeter.junit.report;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import java.util.HashMap;

public class AlternateJtlFileReader {
    public enum JMeterJtlHeader {
        timeStamp,
        elapsed,
        label,
        responseCode,
        responseMessage,
        threadName,
        dataType,
        success,
        failureMessage,
        bytes,
        grpThreads,
        allThreads,
        latency,
        IdleTime
    }

    public void parseCsvJtl(final String path, final AlternateJtlRecordProcessor recordProcessor) throws IOException {
        final Map<String, ArrayList> testsmap = new HashMap<String, ArrayList>();
        try (Reader in = new FileReader(path)) {
            final Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (final CSVRecord record : records) {
                final String label = record.get(JMeterJtlHeader.label);
                final boolean success = Boolean.parseBoolean(record.get(JMeterJtlHeader.success));
                final String failureMessage = record.get(JMeterJtlHeader.failureMessage);
                final String responseMessage = record.get(JMeterJtlHeader.responseMessage);
                final long elapsed = Long.parseLong(record.get(JMeterJtlHeader.elapsed));
                final String threadName = record.get(JMeterJtlHeader.threadName);

                // store in map
                ArrayList<JtlRecord> testslist;
                if (!testsmap.containsKey(threadName)) {
                    testslist = new ArrayList<JtlRecord>();
                    testsmap.put(threadName, testslist);
                } else {
                    testslist = testsmap.get(threadName);
                }
                testslist.add(new JtlRecord(label, success, responseMessage, failureMessage, elapsed));
            }
            // process all tests from each threadgroup
            for (final String suiteName : testsmap.keySet()) {
                Element suite = recordProcessor.processSuite(suiteName);
                for (final JtlRecord record : (ArrayList<JtlRecord>) (testsmap.get(suiteName))) {
                    recordProcessor.process(suite, record);
                }
            }
        }
    }

}