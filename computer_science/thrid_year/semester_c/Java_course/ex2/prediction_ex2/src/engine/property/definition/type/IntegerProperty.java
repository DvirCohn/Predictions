package engine.property.definition.type;

import engine.property.definition.AbstractProperty;
import engine.property.definition.PropertyType;
import engine.property.definition.Rangeable;
import engine.property.definition.generator.defenition.ValueGenerator;
import engine.property.definition.generator.random.impl.numeric.RandomIntegerGenerator;
import generated.PRDEnvProperty;
import generated.PRDProperty;

import java.util.concurrent.ThreadLocalRandom;

public class IntegerProperty extends AbstractProperty implements Rangeable {
    private int from;
    private int to;

    public IntegerProperty(int begin, int end, PropertyType type, String name, ValueGenerator<IntegerProperty> valueGenerator){
        super(name, type, valueGenerator);
        from = begin;
        to = end;
    }

    public IntegerProperty(PRDProperty generatedProperty, ValueGenerator valueGenerator){
        super(generatedProperty.getPRDName(),PropertyType.DECIMAL,valueGenerator);
        from = (int) generatedProperty.getPRDRange().getFrom();
        to = (int) generatedProperty.getPRDRange().getTo();
    }

    public IntegerProperty(PRDEnvProperty envProperty){
        super(envProperty.getPRDName(), PropertyType.DECIMAL,
                new RandomIntegerGenerator((int)envProperty.getPRDRange().getFrom(), (int)envProperty.getPRDRange().getTo()));
        from = (int)envProperty.getPRDRange().getFrom();
        to = (int)envProperty.getPRDRange().getTo();
    }

    @Override
    public Float getTo() {
        return new Float(to);
    }
    @Override
    public Float getFrom() {
        return new Float(from) ;
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
        if (value instanceof Integer) {
            return ((Integer)value >= from && (Integer)value <= to);
        }
        else {
            //throw ex
        }
        return false;
    }

    @Override
    public Integer generateRandomNumberInRange() {
        return ThreadLocalRandom.current().nextInt(from, to + 1);
    }

    @Override
    public String toString() {
        return "Name: " + super.getName() + "\n"
                +"Type: " + super.getType() + "\n"
                + getRange() + "\n";
    }

    @Override
    public Integer generatePropertyValue() {
        return (Integer) getValueGenerator().generateValue();
    }
}
