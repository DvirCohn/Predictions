package engine.property.definition;

public enum PropertyType {
    DECIMAL {
        @Override
        PropertyType propertyType() {
            return DECIMAL;
        }
       @Override
        public  Integer convert(Object value) {
            if (!(value instanceof Integer)) {
                throw new IllegalArgumentException("value " + value + " is not of a DECIMAL type (expected Integer class)");
            }
            return (Integer) value;
        }
    },
    FLOAT {
        @Override
        PropertyType propertyType() {
            return FLOAT;
        }
        @Override
        public Float convert(Object value) {
            if (!(value instanceof Float)) {
                throw new IllegalArgumentException("value " + value + " is not of a FLOAT type (expected Integer class)");
            }
            return (Float) value;
        }
    },
    BOOLEAN {
        @Override
        PropertyType propertyType() {
            return BOOLEAN;
        }
        @Override
        public Boolean convert(Object value) {
            if (!(value instanceof Boolean)) {
                throw new IllegalArgumentException("value " + value + " is not of a BOOLEAN type (expected Integer class)");
            }
            return (Boolean) value;
        }
    },
    STRING {
        @Override
        PropertyType propertyType() {
            return STRING;
        }
        @Override
        public String convert(Object value) {
            if (!(value instanceof String)) {
                throw new IllegalArgumentException("value " + value + " is not of a STRING type (expected Integer class)");
            }
            return (String) value;
        }
    };

    abstract PropertyType propertyType();


    public abstract Object convert(Object value);
}
