package engine.entity.instance.manager;

import engine.property.definition.PropertyDefinition;
import engine.property.instance.PropertyInstance;
import engine.property.instance.PropertyInstanceImpl;
import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.entity.instance.EntityInstanceImpl;

import java.util.ArrayList;
import java.util.List;

public class EntityInstanceManagerImpl implements EntityInstanceManager {
    private List<EntityInstance> instances;
    private int population;

    public EntityInstanceManagerImpl() {
        instances = new ArrayList<>();
    }

    @Override
    public EntityInstance create(EntityDefinition entityDefinition) {

        EntityInstance newEntityInstance = new EntityInstanceImpl(entityDefinition);
        instances.add(newEntityInstance);

        for (PropertyDefinition propertyDefinition : entityDefinition.getProps()) {
            PropertyInstance newPropertyInstance = new PropertyInstanceImpl(propertyDefinition);
            newEntityInstance.addPropertyInstance(newPropertyInstance);
        }

        population = this.instances.size();
        return newEntityInstance;
    }

    @Override
    public void createPopulation(Integer count,EntityDefinition entityDefinition){
        int i = 0;

        for (;i< count ;i++){
           create(entityDefinition);
        }
    }

    @Override
    public List<EntityInstance> getInstances() {
        return instances;
    }

    @Override
    public void setInstances(List<EntityInstance> instances) {
        this.instances = instances;
    }

    @Override
    public void updatePopulationCount() {
        this.population  = this.instances.size();
    }
}
