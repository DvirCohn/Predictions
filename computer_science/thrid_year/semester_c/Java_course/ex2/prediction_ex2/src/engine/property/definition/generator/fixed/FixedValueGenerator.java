package engine.property.definition.generator.fixed;

import engine.property.definition.generator.defenition.ValueGenerator;
import generated.PRDProperty;

public class FixedValueGenerator<T> implements ValueGenerator<T> {

    private final T fixedValue;

    public FixedValueGenerator(T fixedValue) {
        this.fixedValue = fixedValue;
    }

    @Override
    public T generateValue() {
        return fixedValue;
    }

    @Override
    public boolean isRandomInit() {
        return false;
    }
}
