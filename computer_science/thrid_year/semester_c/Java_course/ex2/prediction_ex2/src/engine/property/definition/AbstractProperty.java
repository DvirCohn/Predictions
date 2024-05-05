package engine.property.definition;

import engine.property.definition.generator.defenition.ValueGenerator;

public abstract class AbstractProperty implements PropertyDefinition {

    private final String name;
    private final PropertyType type;
    private ValueGenerator<?> valueGenerator;

    public AbstractProperty(String name, PropertyType type, ValueGenerator<?> valueGenerator) {
        this.name = name;
        this.type = type;
        this.valueGenerator = valueGenerator;
    }

    @Override
    public ValueGenerator<?> getValueGenerator() {
        return valueGenerator;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public PropertyType getType(){
        return type.propertyType();
    }
    @Override
    abstract public String toString();

    @Override
    public void setValueGenerator(ValueGenerator valueGenerator) {
        this.valueGenerator = valueGenerator;
    }

    @Override
    public Float getFrom() {
        return null;
    }

    @Override
    public Float getTo() {
        return null;
    }
}
