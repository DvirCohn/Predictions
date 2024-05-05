package engine.context;

import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.entity.instance.manager.EntityInstanceManager;
import engine.environment.instance.ActiveEnvironment;
import engine.property.instance.PropertyInstance;

public interface Context {
    EntityInstance getPrimaryEntityInstance();
   // void removeEntity(EntityInstance entityInstance);
    PropertyInstance getEnvironmentVariable(String name);
    ActiveEnvironment getActiveEnvironment();
    //EntityInstanceManager getEntitiesInstancesManager();
    EntityInstanceManager getSecondaryEntitiesInstancesManager(String secondaryEntityName);
    EntityDefinition getSecondaryEntityDefinition();
    EntityInstance[][] getWorldMap();
    Integer getCurrentTick();
    EntityInstance getSecondaryEntityInstance();
    void setSecondaryEntityInstance(EntityInstance secondaryEntityInstance);
    boolean isSecondaryEntityExists();
    String getPrimaryInstanceName();
    String getSecondaryInstanceName();
}
