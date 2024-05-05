package engine.entity.definition;

import engine.property.definition.PropertyDefinition;

import java.util.List;

public interface EntityDefinition {
    String getName();

    List<PropertyDefinition> getProps();

    PropertyDefinition getPropertyByName(String propertyName);
}