package engine.environment.instance;

import engine.property.instance.PropertyInstance;

import java.util.Map;

public interface ActiveEnvironment {
    PropertyInstance getProperty(String name);
    void addPropertyInstance(PropertyInstance propertyInstance);
    Map<String, PropertyInstance> getEnvVariables();
}
