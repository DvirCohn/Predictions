package engine.property.definition.generator.random.api;

import engine.property.definition.generator.defenition.ValueGenerator;

import java.util.Random;

public abstract class AbstractRandomValueGenerator<T> implements ValueGenerator<T> {
    protected final Random random;

    protected AbstractRandomValueGenerator() {
        random = new Random();
    }
}
