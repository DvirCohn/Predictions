package engine.property.definition.generator.defenition;

import engine.property.definition.generator.fixed.FixedValueGenerator;
import engine.property.definition.generator.random.impl.bool.RandomBooleanValueGenerator;
import engine.property.definition.generator.random.impl.numeric.RandomIntegerGenerator;

public interface ValueGeneratorFactory {

    static <T> ValueGenerator<T> createFixed(T value) {
        return new FixedValueGenerator<>(value);
    }

    static ValueGenerator<Boolean> createRandomBoolean() {
        return new RandomBooleanValueGenerator();
    }

    static ValueGenerator<Integer> createRandomInteger(Integer from, Integer to) {
        return new RandomIntegerGenerator(from, to);
    }

}
