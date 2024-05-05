package engine.property.instance;

import dto.implement.PropertyDTO;
import engine.property.definition.PropertyDefinition;

public interface PropertyInstance {
    PropertyDefinition getPropertyDefinition();
    Object getValue();
    void updateValue(Object val,int currentTick);
    void updateProperty(PropertyDTO propDTO);
    int getLastModifiedTick();
    int getNumberOfChanges();
}
