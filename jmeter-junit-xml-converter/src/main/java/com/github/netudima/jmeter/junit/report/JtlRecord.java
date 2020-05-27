package com.github.netudima.jmeter.junit.report;

public class JtlRecord {
    private final String label;
    private final boolean success;
    private final String responseMessage;
    private final String failureMessage;
    private final long elapsed;

    public JtlRecord(String label, boolean success, String responseMessage, String failureMessage, long elapsed) {
        this.label = label;
        this.success = success;
        this.responseMessage = responseMessage;
        this.failureMessage = failureMessage;
        this.elapsed = elapsed;
    }

    public String getLabel() {
        return label;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public long getElapsed() {
        return elapsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JtlRecord)) return false;

        JtlRecord jtlRecord = (JtlRecord) o;

        if (success != jtlRecord.success) return false;
        if (label != null ? !label.equals(jtlRecord.label) : jtlRecord.label != null) return false;
        if (responseMessage != null ? !responseMessage.equals(jtlRecord.responseMessage) : jtlRecord.responseMessage != null)
            return false;
        return !(failureMessage != null ? !failureMessage.equals(jtlRecord.failureMessage) : jtlRecord.failureMessage != null);

    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (success ? 1 : 0);
        result = 31 * result + (responseMessage != null ? responseMessage.hashCode() : 0);
        result = 31 * result + (failureMessage != null ? failureMessage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JtlRecord{" +
                "label='" + label + '\'' +
                ", success=" + success +
                ", responseMessage='" + responseMessage + '\'' +
                ", failureMessage='" + failureMessage + '\'' +
                '}';
    }
}
