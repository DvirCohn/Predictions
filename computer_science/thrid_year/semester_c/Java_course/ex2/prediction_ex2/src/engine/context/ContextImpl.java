package engine.context;

import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.entity.instance.manager.EntityInstanceManager;
import engine.environment.instance.ActiveEnvironment;
import engine.property.instance.PropertyInstance;
import engine.world.WorldInstance;

import java.util.List;
import java.util.Map;

public class ContextImpl implements Context {

    private EntityInstance primaryEntityInstance;
    private EntityInstance[][] worldMap;
    Map<String ,EntityInstanceManager> entityInstanceManagers;
    private ActiveEnvironment activeEnvironment;
    private EntityInstance secondaryEntityInstance;

    private boolean isSecondaryEntityExists;
    private Integer currentTick;

    public ContextImpl(EntityInstance primaryEntityInstance, ActiveEnvironment activeEnvironment,
                       EntityInstance[][] worldMap,Integer currentTick, Map<String ,EntityInstanceManager> entityInstanceManagers) {
        this.primaryEntityInstance = primaryEntityInstance;
        this.activeEnvironment = activeEnvironment;
        this.worldMap = worldMap;
        this.currentTick = currentTick;
        this.isSecondaryEntityExists = false;
        this.entityInstanceManagers = entityInstanceManagers;
    }

    public ContextImpl(EntityInstance primaryEntityInstance, ActiveEnvironment activeEnvironment, EntityInstance[][] worldMap,
                       Integer currentTick, EntityInstance secondaryEntityInstance, Map<String ,EntityInstanceManager> entityInstanceManagers) {
        this.primaryEntityInstance = primaryEntityInstance;
        this.activeEnvironment = activeEnvironment;
        this.worldMap = worldMap;
        this.currentTick = currentTick;
        this.isSecondaryEntityExists = true;
        this.secondaryEntityInstance = secondaryEntityInstance;
        this.entityInstanceManagers = entityInstanceManagers;
    }

    @Override
    public ActiveEnvironment getActiveEnvironment(){
        return this.activeEnvironment;
    }

    @Override
    public EntityInstance getPrimaryEntityInstance() {
        return primaryEntityInstance;
    }

    @Override
    public EntityInstanceManager getSecondaryEntitiesInstancesManager(String secondaryEntityName) {
        return entityInstanceManagers.get(secondaryEntityName);
    }

    @Override
    public EntityDefinition getSecondaryEntityDefinition() {
        return this.secondaryEntityInstance.getEntityDefinition();
    }

    @Override
    public PropertyInstance getEnvironmentVariable(String name) {
        return activeEnvironment.getProperty(name);
    }

//    @Override
//    public EntityInstanceManager getEntitiesInstancesManager() {
//        return this.entityInstanceManager;
//    }

    @Override
    public EntityInstance[][] getWorldMap(){
        return worldMap;
    }

    @Override
    public Integer getCurrentTick() {
        return this.currentTick;
    }

    @Override
    public EntityInstance getSecondaryEntityInstance() {
        return secondaryEntityInstance;
    }

    @Override
    public void setSecondaryEntityInstance(EntityInstance secondaryEntityInstance) {
        if (secondaryEntityInstance != null){
            this.isSecondaryEntityExists = true;
            this.secondaryEntityInstance = secondaryEntityInstance;
        }
        else{
            System.out.println("null pointer exception???");
        }
        if (secondaryEntityInstance.getEntityDefinition().getName().equals("ent-1")){
            System.out.println("stop here");
        }

    }

    @Override
    public boolean isSecondaryEntityExists() {
        return isSecondaryEntityExists;
    }

    @Override
    public String getPrimaryInstanceName() {
        return primaryEntityInstance.getEntityDefinition().getName();
    }

    @Override
    public String getSecondaryInstanceName() {
        return secondaryEntityInstance.getEntityDefinition().getName();
    }
}
