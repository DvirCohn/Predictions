package engine.entity.instance.manager;

import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.entity.instance.EntityInstanceImpl;

import java.util.List;

public interface EntityInstanceManager {

    EntityInstance create(EntityDefinition entityDefinition);
    List<EntityInstance> getInstances();
    void setInstances(List<EntityInstance>instances);
    void updatePopulationCount();
    void createPopulation(Integer population,EntityDefinition entityDefinition);


}
