package dto.implement;

import engine.property.definition.PropertyDefinition;
import engine.property.definition.PropertyType;
import engine.property.definition.Rangeable;
import engine.property.definition.type.FloatProperty;
import engine.property.definition.type.IntegerProperty;
import engine.property.instance.PropertyInstance;

public class PropertyDTO {

    private boolean randomInit;
    private String name;
    private String type;
    private String getRange = null;
    private String objectValue = null;
    private Float from;
    private Float to;

    public void setObjectValue(String objectValue) {
        this.objectValue = objectValue;
    }

    public PropertyDTO(PropertyDefinition propDef) {
        this.name = propDef.getName();
        this.type = propDef.getType().toString();
        this.randomInit = propDef.getValueGenerator().isRandomInit();
        this.objectValue = propDef.getValueGenerator().generateValue().toString();
        if(propDef.getType().equals(PropertyType.DECIMAL) ||propDef.getType().equals(PropertyType.FLOAT)){
            getRange = propDef.getRange();
            this.from = propDef.getFrom();
            this.to = propDef.getTo();
        }

    }

    public String getObjectValue() {
        return objectValue;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isRandomInit() {
        return randomInit;
    }

    public String getGetRange() {
        return getRange;
    }

    public Float getFrom() {
        return this.from;
    }

    public Float getTo() {
        return this.to;
    }
}
