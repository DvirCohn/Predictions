package engine.property.definition.generator.random.impl.numeric;

import engine.property.definition.generator.defenition.ValueGenerator;
import generated.PRDProperty;

public class RandomFloatGenerator  extends AbstractNumericRandomGenerator<Float>{
    public RandomFloatGenerator(Float from, Float to) {
        super(from, to);
    }

    @Override
    public Float generateValue() {
        return from + (random.nextFloat() * (to - from));
    }

    @Override
    public boolean isRandomInit() {
        return true;
    }
}
