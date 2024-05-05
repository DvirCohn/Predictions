package engine.property.definition.generator.defenition;

import generated.PRDProperty;

public interface ValueGenerator<T> {
    T generateValue();
    boolean isRandomInit();
}
