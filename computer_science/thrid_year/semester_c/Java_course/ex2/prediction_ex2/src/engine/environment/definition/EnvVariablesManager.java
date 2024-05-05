package engine.environment.definition;

import engine.property.definition.PropertyDefinition;
import engine.environment.instance.ActiveEnvironment;

import java.util.Collection;

public interface EnvVariablesManager {
    void addEnvironmentVariable(PropertyDefinition propertyDefinition);
    ActiveEnvironment createActiveEnvironment();
    Collection<PropertyDefinition> getEnvVariables();
    PropertyDefinition getProperty(String name);
}
