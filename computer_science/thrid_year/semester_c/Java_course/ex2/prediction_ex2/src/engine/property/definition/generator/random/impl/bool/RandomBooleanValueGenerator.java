package engine.property.definition.generator.random.impl.bool;

import engine.property.definition.generator.defenition.ValueGenerator;
import engine.property.definition.generator.random.api.AbstractRandomValueGenerator;
import generated.PRDProperty;

public class RandomBooleanValueGenerator extends AbstractRandomValueGenerator<Boolean> {

    @Override
    public Boolean generateValue() {
        return random.nextBoolean();
    }

    @Override
    public boolean isRandomInit() {
        return true;
    }
}
