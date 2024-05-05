package engine.property.definition.generator.random.impl.numeric;

import engine.property.definition.generator.defenition.ValueGenerator;
import generated.PRDProperty;

public class RandomIntegerGenerator extends AbstractNumericRandomGenerator<Integer> {

    public RandomIntegerGenerator(Integer from, Integer to) {
        super(from, to);
    }

    @Override
    public Integer generateValue() {
        return from + random.nextInt(to - from);
    }

    @Override
    public boolean isRandomInit() {
        return true;
    }
}
