package engine.property.definition.type;

import engine.property.definition.AbstractProperty;
import engine.property.definition.PropertyDefinition;
import engine.property.definition.PropertyType;
import engine.property.definition.generator.defenition.ValueGenerator;
import engine.property.definition.generator.fixed.FixedValueGenerator;
import engine.property.definition.generator.random.impl.bool.RandomBooleanValueGenerator;
import generated.PRDEnvProperty;
import generated.PRDProperty;

public class BooleanProperty extends AbstractProperty implements PropertyDefinition {

    boolean value;

    public BooleanProperty(String name, boolean value,ValueGenerator<BooleanProperty> valueGenerator) {
        super(name, PropertyType.BOOLEAN, valueGenerator);
        this.value = value;
    }

    public BooleanProperty(PRDProperty generatedProperty, ValueGenerator valueGenerator){
        super(generatedProperty.getPRDName(), PropertyType.BOOLEAN, valueGenerator);
        value = (boolean)valueGenerator.generateValue();
    }

    public BooleanProperty(PRDEnvProperty envProperty){
        super(envProperty.getPRDName(), PropertyType.BOOLEAN, new RandomBooleanValueGenerator());
        value = false;
    }

    public String getValue(){
        if(value){
            return "true";
        }
        else{
            return "false";
        }
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
    public Boolean generatePropertyValue() {
        return (Boolean)getValueGenerator().generateValue();
    }

}
