package engine.property.instance;

import dto.implement.PropertyDTO;
import engine.property.definition.PropertyDefinition;
import engine.property.definition.PropertyType;
import engine.property.definition.generator.fixed.FixedValueGenerator;
import engine.property.definition.type.FloatProperty;
import engine.property.definition.type.IntegerProperty;

public class PropertyInstanceImpl implements PropertyInstance {

    private PropertyDefinition propertyDefinition;
    private Object value;
    private int lastModified = 0;
    private int numberOfChanges = 0;

    public PropertyInstanceImpl(PropertyDefinition propertyDefinition) {
        this.propertyDefinition = propertyDefinition;
        this.value = propertyDefinition.generatePropertyValue();
    }

    @Override
    public PropertyDefinition getPropertyDefinition() {
        return propertyDefinition;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void updateProperty(PropertyDTO propDTO) throws IllegalArgumentException, NumberFormatException {
        switch (propertyDefinition.getType()){
            case FLOAT:
                Float floatValue = Float.parseFloat(propDTO.getObjectValue());
                FloatProperty floatProp = (FloatProperty) propertyDefinition;
                if(floatProp.isInRange(floatValue)){
                    value = floatValue;
                    propertyDefinition.setValueGenerator(new FixedValueGenerator(value));
                }
                else {
                    throw new IllegalArgumentException ("value inserted out of range");
                }
                break;
            case DECIMAL:
                Integer integerValue = Integer.parseInt(propDTO.getObjectValue());
                IntegerProperty decimalProp = (IntegerProperty) propertyDefinition;
                if(decimalProp.isInRange(integerValue)){
                    value = integerValue;
                    propertyDefinition.setValueGenerator(new FixedValueGenerator(value));
                }
                else {
                    throw new IllegalArgumentException ("value inserted out of range");
                }
                break;
            case BOOLEAN:
                Boolean boolValue = Boolean.parseBoolean(propDTO.getObjectValue());
                if (boolValue == null){
                    throw new IllegalArgumentException ("value inserted is not a boolean value");
                }
                else {
                    value = boolValue;
                    propertyDefinition.setValueGenerator(new FixedValueGenerator(value));
                }
                break;
            case STRING:
                value = propDTO.getObjectValue();
                propertyDefinition.setValueGenerator(new FixedValueGenerator(value));
                break;
        }
    }

    @Override
    public void updateValue(Object val, int currentTick) {
        if (propertyDefinition.getType().equals(PropertyType.DECIMAL) ) {
            IntegerProperty prop = (IntegerProperty) propertyDefinition;
            if(prop.isInRange((Integer)val)){
                this.value = val;
            }
            else if ((Integer)val > prop.getTo()){
                val = prop.getTo();
            }
            else {
                val = prop.getFrom();
            }
        }
        else if (propertyDefinition.getType().equals(PropertyType.FLOAT)) {
            FloatProperty prop = (FloatProperty) propertyDefinition;
            if(prop.isInRange((Float)val)){
                this.value = val;
            }
            else if ((Float)val > prop.getTo()){
                val = prop.getTo();
            }
            else {
                val = prop.getFrom();
            }
        }
        else {
            this.value = val;
        }
        updateCountAndModified(currentTick);
    }

    private void updateCountAndModified(int currentTick) {
        this.lastModified = currentTick;
        this.numberOfChanges++;
    }

    @Override
    public int getLastModifiedTick() {
        return this.lastModified;
    }

    @Override
    public int getNumberOfChanges() {
        return numberOfChanges;
    }
}
