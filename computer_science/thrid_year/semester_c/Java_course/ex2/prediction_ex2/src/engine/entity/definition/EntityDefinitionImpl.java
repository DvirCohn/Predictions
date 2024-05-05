package engine.entity.definition;

import engine.context.Context;
import engine.conventer.Converter;
import engine.property.definition.PropertyDefinition;
import generated.PRDEntity;
import generated.PRDProperties;
import generated.PRDProperty;

import java.util.List;
import java.util.ArrayList;


public class EntityDefinitionImpl implements EntityDefinition {
    private final String name;

    // TODO: 9/2/2023 receive population from user!!! 

    private final List<PropertyDefinition> properties;



    public EntityDefinitionImpl(PRDEntity prdEntity){
        this.name = prdEntity.getName();
        this.properties = PropertyListFromPRDProperties(prdEntity.getPRDProperties());
    }

    private List<PropertyDefinition> PropertyListFromPRDProperties(PRDProperties prdProperties) {
        List<PropertyDefinition> propertyDefinitions = new ArrayList<>();
        propertyDefinitions = Converter.buildPropertiesFromPRDProperties(prdProperties);
        return propertyDefinitions;
    }


    @Override
    public String getName() {
        return name;
    }



    @Override
    public List<PropertyDefinition> getProps() {
        return properties;
    }

    @Override
    public PropertyDefinition getPropertyByName(String propertyName) {

        for(PropertyDefinition propDef:properties){
            if(propDef.getName().equals(propertyName)){
                return propDef;
            }
        }
        return null;
    }
}