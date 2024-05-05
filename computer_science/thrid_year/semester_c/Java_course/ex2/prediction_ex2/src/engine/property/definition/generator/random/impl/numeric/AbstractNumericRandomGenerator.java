package engine.property.definition.generator.random.impl.numeric;

import engine.property.definition.generator.random.api.AbstractRandomValueGenerator;

public abstract class AbstractNumericRandomGenerator<T> extends AbstractRandomValueGenerator<T> {

    protected final T from;
    protected final T to;

    protected AbstractNumericRandomGenerator(T from, T to) {
        this.from = from;
        this.to = to;
    }

}
