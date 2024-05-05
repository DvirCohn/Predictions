package dto.implement;

import engine.environment.definition.EnvVariablesManager;
import engine.property.definition.PropertyDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvironmentDTO {


    Map<String,PropertyDTO> envProps;

    public EnvironmentDTO(EnvVariablesManager envDef){

        envProps = new HashMap<>();
        for (PropertyDefinition envProp: envDef.getEnvVariables()) {
            PropertyDTO propertyDTO = new PropertyDTO(envProp);
            envProps.put(propertyDTO.getName(), propertyDTO);
        }
    }

    public Map<String,PropertyDTO> getEnvProps() {
        return envProps;
    }

    public StringBuffer getEnvironmentInfo(String environmentName){
        StringBuffer info = new StringBuffer();
        PropertyDTO propertyDTO = this.getEnvProps().get(environmentName);
        info.append(environmentName+":\n\n");
        info.append("Type - "+propertyDTO.getType()+"\n");
        info.append("Range - "+propertyDTO.getGetRange()+"\n");
        return info;

    }
}
