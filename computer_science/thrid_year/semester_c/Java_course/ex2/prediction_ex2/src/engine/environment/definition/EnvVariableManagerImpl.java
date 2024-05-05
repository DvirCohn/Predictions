package engine.environment.definition;

import engine.property.definition.PropertyDefinition;
import engine.environment.instance.ActiveEnvironment;
import engine.environment.instance.ActiveEnvironmentImpl;
import engine.property.definition.type.BooleanProperty;
import engine.property.definition.type.FloatProperty;
import engine.property.definition.type.IntegerProperty;
import engine.property.definition.type.StringProperty;
import generated.PRDEnvProperty;
import generated.PRDEnvironment;
import java.util.Collection;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class EnvVariableManagerImpl implements EnvVariablesManager {

    private final Map<String, PropertyDefinition> propNameToPropDefinition;

    public EnvVariableManagerImpl() {
        propNameToPropDefinition = new HashMap<>();
    }

    public EnvVariableManagerImpl(PRDEnvironment generatedEnv) {
        propNameToPropDefinition = new HashMap<>();
        for (PRDEnvProperty envProperty: generatedEnv.getPRDEnvProperty()) {

            try {
                PropertyDefinition propertyDefinition = getPropertyDefinitionByPRDEnv(envProperty);
                addEnvironmentVariable(propertyDefinition);
            }
            catch (Exception exception){
                throw exception;
            }
        }
    }

    private PropertyDefinition getPropertyDefinitionByPRDEnv(PRDEnvProperty envProperty) {
        switch (envProperty.getType().toLowerCase()) {
            case "decimal":
                return new IntegerProperty(envProperty);
            case "float":
                if (envProperty.getPRDRange() == null){
                    throw new NumberFormatException("must initialize range of a numeric property");
                }
                return new FloatProperty(envProperty);
            case "boolean":
                return new BooleanProperty(envProperty);
            case "string":
                return new StringProperty(envProperty);
            default:
                throw new InputMismatchException("not valid propert type: "+envProperty.getType()); // switch to throw exception
        }
    }

    @Override
    public void addEnvironmentVariable(PropertyDefinition propertyDefinition) throws IllegalArgumentException {
        if(this.propNameToPropDefinition.containsKey(propertyDefinition.getName())){
            throw  new IllegalArgumentException("Can't create property" + propertyDefinition.getName() +" because it is already exists, in Environment ");// throw exception that the environment as 2 separate variables with the same name
        }
        else{
            propNameToPropDefinition.put(propertyDefinition.getName(), propertyDefinition);

        }
    }

    @Override
    public ActiveEnvironment createActiveEnvironment() {
        // need to complete and return an actual ActiveEnv created from this environment definition
        return new ActiveEnvironmentImpl();
    }

    @Override
    public Collection<PropertyDefinition> getEnvVariables() {
        return propNameToPropDefinition.values();
    }

    @Override
    public PropertyDefinition getProperty(String name) {
        if (!propNameToPropDefinition.containsKey(name)) {
            throw new IllegalArgumentException("Can't find env variable with name " + name);
        }
        return propNameToPropDefinition.get(name);
    }
}
