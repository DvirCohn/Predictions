package engine.context.build.rules;

import engine.entity.definition.EntityDefinition;
import engine.environment.definition.EnvVariablesManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleContext {
    EnvVariablesManager environmentDefinition;
    Map<String, EntityDefinition> entNameToDefinition;
    EntityDefinition primaryEntity;


    public RuleContext(EnvVariablesManager environmentDefinition, EntityDefinition primaryEntity) {
        this.environmentDefinition = environmentDefinition;
        this.primaryEntity = primaryEntity;
        this.entNameToDefinition = new HashMap<>();
    }

    public void initMap(List<EntityDefinition> entities){
        for (EntityDefinition entDef: entities) {
            entNameToDefinition.put(entDef.getName(), entDef);
        }
    }

    public EnvVariablesManager getEnvironmentDefinition() {
        return environmentDefinition;
    }

    public Map<String, EntityDefinition> getEntityMap() {
        return entNameToDefinition;
    }

    public EntityDefinition getPrimaryEntity() {
        return primaryEntity;
    }
}
