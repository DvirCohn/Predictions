package engine.world;

import dto.implement.EnvironmentDTO;
import dto.implement.InitSimulationDTO;
import dto.implement.PropertyDTO;
import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.entity.instance.EntityInstanceImpl;
import engine.entity.instance.manager.EntityInstanceManager;
import engine.entity.instance.manager.EntityInstanceManagerImpl;
import engine.environment.definition.EnvVariableManagerImpl;
import engine.environment.definition.EnvVariablesManager;
import engine.environment.instance.ActiveEnvironment;
import engine.environment.instance.ActiveEnvironmentImpl;
import engine.rule.api.Rule;
import engine.termination.Termination;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

public class WorldInstance {

    private Map<String,EntityInstanceManager> entityInstanceManagers;
    private WorldDefinition worldDefinition;
    private ActiveEnvironment environment;
    private List<EntityInstance> entitiesInstances; // singular show for each type
    private EntityInstance[][] worldMap;

    public WorldInstance(WorldDefinition wd){
        this.worldDefinition = wd;
        this.environment = new ActiveEnvironmentImpl(wd.getEnvironment());
        this.entitiesInstances = createEntities(wd.getEntityDefinition());
        this.worldMap = new EntityInstance[wd.getRows()][getWorldDefinition().getCols()];
    }

    private List<EntityInstance> createEntities(List<EntityDefinition> entitiesDefinition) {
        List<EntityInstance> entityInstances = new ArrayList<>();
        for (EntityDefinition entDef: entitiesDefinition) {
            EntityInstance entInst = new EntityInstanceImpl(entDef);
            entityInstances.add(entInst);
        }
        return entityInstances;
    }

    private List<EntityInstanceManager> creatEntitiesManagers(List<EntityInstance> entityInstanceList) {
        List<EntityInstanceManager> managerList = new ArrayList<>();
        for (EntityInstance entInstance: entityInstanceList) {
            EntityInstanceManager instanceManager = new EntityInstanceManagerImpl();
            //int i = 0;
            //while (i < entInstance.getEntityDefinition().getPopulation()){
            //    instanceManager.create(entInstance.getEntityDefinition());
            //}
            managerList.add(instanceManager);
        }
        return managerList;
    }

    private List<EntityInstance> createEntities(WorldDefinition wd) {
        List<EntityInstance> entityInstanceList = new ArrayList<>();
        for (EntityDefinition entDefinition: wd.getEntityDefinition()) {
            EntityInstance instance = new EntityInstanceImpl(entDefinition);
            entityInstanceList.add(instance);
        }
        return entityInstanceList;
    }

    public ActiveEnvironment getEnvironment() {
        return environment;
    }

    public List<Rule> getRules(){
        return worldDefinition.getRules();
    }

    public WorldDefinition getWorldDefinition() {
        return worldDefinition;
    }

    public void updateEnvironmentProperties(PropertyDTO propertyDTO, String propertyName) throws InputMismatchException, IllegalArgumentException{
        if(environment.getEnvVariables().containsKey(propertyName)){
            try{
                environment.getEnvVariables().get(propertyName).updateProperty(propertyDTO);
            }
            catch(IllegalArgumentException e){
                throw e;
            }

        }
        else{
            throw new InputMismatchException("could not find property with the name " + propertyName);
        }
    }

    public List<EntityInstance> getEntitiesInstances() {
        return entitiesInstances;
    }

    public Termination getTermination() {
        return this.worldDefinition.getTermination();
    }

    public void setEntityInstanceManagers(Map<String,EntityInstanceManager> entityInstanceManagers) {
        this.entityInstanceManagers = entityInstanceManagers;
    }

    public Map<String,EntityInstanceManager> getEntityInstanceManagers() {
        return entityInstanceManagers;
    }

    public EntityInstance getEntityInstanceByName(String name){
        for (EntityInstance entity: this.entitiesInstances){
             if(entity.getEntityDefinition().getName().equals(name)) {
                 return entity;
             }
        }
        return null;
    }


    public EntityInstance[][] getWorldMap() {
        return worldMap;
    }
}
