package engine.entity.instance;

import engine.entity.definition.EntityDefinition;
import engine.entity.definition.EntityDefinitionImpl;
import engine.property.instance.PropertyInstance;
import engine.property.instance.PropertyInstanceImpl;


import java.awt.*;
import java.util.Map;

public interface EntityInstance {
    int getId();
    PropertyInstance getPropertyByName(String name) throws IllegalArgumentException;
    void addPropertyInstance(PropertyInstance propertyInstance);
    Map<String, PropertyInstance> getProperties();
    EntityDefinition getEntityDefinition();
    void setLocation(int x, int y);
    Point getEntityLocation();
    void makeMove(EntityInstance[][] map);
    boolean isAlive();
    void setAlive(boolean alive);
    Point getLocation();
    void setLocation(Point point);

}
