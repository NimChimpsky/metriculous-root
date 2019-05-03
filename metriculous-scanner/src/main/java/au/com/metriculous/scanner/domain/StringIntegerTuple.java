package au.com.metriculous.scanner.domain;

public class StringIntegerTuple {

    private String label;
    private Integer value;

    public StringIntegerTuple(String label, Integer value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public Integer getValue() {
        return value;
    }

}
