package engine.property.definition.type;

import engine.property.definition.AbstractProperty;
import engine.property.definition.PropertyType;
import engine.property.definition.generator.defenition.ValueGenerator;
import engine.property.definition.generator.random.impl.string.RandomStringValueGenerator;
import generated.PRDEnvProperty;
import generated.PRDProperty;

public class StringProperty extends AbstractProperty {

    private String value;

    public StringProperty(String name, String value, ValueGenerator<StringProperty> valueGenerator){
        super(name, PropertyType.STRING, valueGenerator);
        this.value = value;
    }
    public StringProperty(PRDProperty generatedProperty, ValueGenerator valueGenerator){
        super(generatedProperty.getPRDName(),PropertyType.STRING,valueGenerator);
        value = (String) valueGenerator.generateValue();
    }

    public StringProperty(PRDEnvProperty envProperty){
        super(envProperty.getPRDName(), PropertyType.STRING, new RandomStringValueGenerator());
        value = null;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getRange(){
        return null;
    }

    @Override
    public String toString() {
        return "Name: " + super.getName() + "\n"
                +"Type: " + super.getType() + "\n"
                +"Value: " + getValue() + "\n";
    }

    @Override
    public String generatePropertyValue() {
        return (String) getValueGenerator().generateValue();
    }
}
