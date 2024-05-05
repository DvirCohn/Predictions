package engine.property.definition.type;

import engine.property.definition.AbstractProperty;
import engine.property.definition.PropertyType;
import engine.property.definition.Rangeable;
import engine.property.definition.generator.defenition.ValueGenerator;
import engine.property.definition.generator.random.impl.numeric.RandomFloatGenerator;
import generated.PRDEnvProperty;
import generated.PRDProperty;

import java.util.Random;

public class FloatProperty extends AbstractProperty implements Rangeable {

    private float from;
    private float to;


    public FloatProperty(float begin, float end, PropertyType type, String name, ValueGenerator<FloatProperty> valueGenerator){
        super(name, type, valueGenerator);
        from = begin;
        to = end;
    }

    public FloatProperty(PRDProperty generatedProperty, ValueGenerator valueGenerator){
        super(generatedProperty.getPRDName(),PropertyType.FLOAT,valueGenerator);
        from = (float) generatedProperty.getPRDRange().getFrom();
        to = (float) generatedProperty.getPRDRange().getTo();
    }

    public FloatProperty(PRDEnvProperty envProperty){
        super(envProperty.getPRDName(), PropertyType.FLOAT,
                new RandomFloatGenerator((float)envProperty.getPRDRange().getFrom(),(float)envProperty.getPRDRange().getTo()));
        from = (float)envProperty.getPRDRange().getFrom();
        to = (float)envProperty.getPRDRange().getTo();
    }

    @Override
    public Float getTo() {
        return to;
    }
    @Override
    public Float getFrom() {
        return from;
    }

    @Override
    public String getRange(){
        StringBuilder sb = new StringBuilder();
        sb.append("From ");
        sb.append(from);
        sb.append(" to ");
        sb.append(to);
        return sb.toString();
    }

    @Override
    public boolean isInRange(Number value) {
        if(value instanceof Float){
            return ((Float)value <= to && (Float)value >= from);
        }
        else{
            //throw ex
        }
        return false;
    }

    @Override
    public Float generateRandomNumberInRange() {
        Random random = new Random();
        return from + random.nextFloat() * (to - from);
    }

    @Override
    public String toString() {
        return "Name: " + super.getName() + "\n"
                +"Type: " + super.getType() + "\n"
                + getRange() + "\n";
    }

    @Override
    public Float generatePropertyValue() {
        return (Float) getValueGenerator().generateValue();
    }


}
