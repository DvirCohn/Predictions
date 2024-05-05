package engine.property.definition;

import engine.property.definition.generator.defenition.ValueGenerator;

public interface PropertyDefinition {
    String getName();
    PropertyType getType();
    Object generatePropertyValue();
    ValueGenerator getValueGenerator();
    String getRange();
    void setValueGenerator(ValueGenerator valueGenerator);

    Float getFrom();
    Float getTo();
}
