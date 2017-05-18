package net.xylophones.java8;

import java.util.List;

public class DomainObject {

    private final int intField;

    private final String stringField;

    private final List<String> listField;

    private DomainObject(int intField, String stringField, List<String> listField) {
        this.intField = intField;
        this.stringField = stringField;
        this.listField = listField;
    }

    public int getIntField() {
        return intField;
    }

    public String getStringField() {
        return stringField;
    }

    public List<String> getListField() {
        return listField;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(DomainObject copy) {
        return new Builder(copy);
    }

    public static class Builder {

        private int intField;

        private String stringField;

        private List<String> listField;

        public Builder() {
        }

        public Builder(DomainObject domainObj) {
            setIntField(domainObj.intField);
            setStringField(domainObj.stringField);
            setListField(domainObj.listField);
        }

        public void setIntField(int intField) {
            this.intField = intField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public void setListField(List<String> listField) {
            this.listField = listField;
        }

        public DomainObject build() {
            return new DomainObject(intField, stringField, listField);
        }
    }
}
