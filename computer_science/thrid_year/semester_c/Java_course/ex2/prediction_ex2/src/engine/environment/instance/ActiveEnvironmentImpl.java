package engine.environment.instance;

import engine.environment.definition.EnvVariablesManager;
import engine.property.definition.PropertyDefinition;
import engine.property.instance.PropertyInstance;
import engine.property.instance.PropertyInstanceImpl;

import java.util.HashMap;
import java.util.Map;

public class ActiveEnvironmentImpl implements ActiveEnvironment {


    private final Map<String, PropertyInstance> envVariables;

    public ActiveEnvironmentImpl() {
        envVariables = new HashMap<>();
    }

    public ActiveEnvironmentImpl(EnvVariablesManager environmentDef) {
        envVariables = createActiveEnvImpl(environmentDef);
    }

    private Map<String, PropertyInstance> createActiveEnvImpl(EnvVariablesManager environmentDef) {
        Map<String, PropertyInstance> envMap = new HashMap<>();
        for (PropertyDefinition propDef: environmentDef.getEnvVariables()) {
            envMap.put(propDef.getName() ,new PropertyInstanceImpl(propDef));
        }
        return envMap;
    }

    @Override
    public Map<String, PropertyInstance> getEnvVariables() {
        return envVariables;
    }

    @Override
    public PropertyInstance getProperty(String name) throws IllegalArgumentException{
        if (!envVariables.containsKey(name)) {
            throw new IllegalArgumentException("Can't find env variable with name " + name);
        }
        return envVariables.get(name);
    }

    @Override
    public void addPropertyInstance(PropertyInstance propertyInstance) {
        envVariables.put(propertyInstance.getPropertyDefinition().getName(), propertyInstance);
    }
}
