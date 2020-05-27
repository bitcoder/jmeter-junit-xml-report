package com.github.netudima.jmeter.junit.report;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AlternateDomXmlJUnitReportWriter implements Closeable {
    private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    private final String fileName;
    private final Document doc;
    private Element rootElement;

    private final String testSuiteName;
    private final String className;

    private int testsCount;
    private int failures;
    private int errors;
    private int skipped;

    public AlternateDomXmlJUnitReportWriter(File file, String testSuiteName) {
        this(file.getAbsolutePath(), testSuiteName);
    }
    public AlternateDomXmlJUnitReportWriter(String fileName, String testSuiteName) {
        this.fileName = fileName;
        this.testSuiteName = testSuiteName;
        this.className = testSuiteName;
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("fail to init XML writer", e);
        }
        doc = documentBuilder.newDocument();
    
        rootElement = doc.createElement("testsuites");
        doc.appendChild(rootElement);
    }

    public Element createSuite(String suiteName) {
        Element suite = doc.createElement("testsuite");
        suite.setAttribute("name", suiteName);
        rootElement.appendChild(suite);
        return suite;
    }

    public void write(Element suite, JtlRecord jtlRecord) {
        testsCount++;

        Element testCase = doc.createElement("testcase");
        testCase.setAttribute("classname", className);
        testCase.setAttribute("name", jtlRecord.getLabel());
        testCase.setAttribute("time", String.format("%.3f", jtlRecord.getElapsed()/1000.0));
        if (!jtlRecord.isSuccess()) {
            Element failureDetails;
            String failureMessage;
            String responseMessage = jtlRecord.getResponseMessage();
            if (jtlRecord.getFailureMessage() != null && !jtlRecord.getFailureMessage().isEmpty()) {
                failures++;
                failureDetails = doc.createElement("failure");
                failureMessage = jtlRecord.getFailureMessage();  
                failureDetails.setAttribute("message", responseMessage);
                failureDetails.setTextContent(failureMessage);
            } else {
                errors++;
                failureDetails = doc.createElement("error");
                failureMessage = jtlRecord.getResponseMessage();
                failureDetails.setAttribute("message", failureMessage);
                failureDetails.setTextContent(failureMessage);
            }
            testCase.appendChild(failureDetails);
        }
        suite.appendChild(testCase);
    }


    @Override
    public void close() throws IOException {
        try {
            rootElement.setAttribute("tests", Integer.toString(testsCount));
            rootElement.setAttribute("failures", Integer.toString(failures));
            rootElement.setAttribute("errors", Integer.toString(errors));
            rootElement.setAttribute("skipped", Integer.toString(skipped));
            flush();
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }

    private void flush() throws TransformerException, IOException {
        // output DOM XML to console
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(doc);
        StreamResult streamResult;
        if (fileName != null) {
            streamResult = new StreamResult(new BufferedWriter(new FileWriter(new File(fileName))));
        } else {
            streamResult = new StreamResult(System.out);
        }
        transformer.transform(source, streamResult);

    }
}
