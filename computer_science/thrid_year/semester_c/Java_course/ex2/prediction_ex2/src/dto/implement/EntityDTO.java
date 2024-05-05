package dto.implement;

import engine.entity.definition.EntityDefinition;
import engine.property.definition.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public class EntityDTO {

    private String name;
    List<PropertyDTO> propertyDTO;

    public EntityDTO( String name, List<PropertyDTO> propertyDTO) {

        this.name = name;
        this.propertyDTO = propertyDTO;
    }



    public String getName() {
        return name;
    }

    public List<PropertyDTO> getPropertyDTO() {
        return propertyDTO;
    }

    public EntityDTO(EntityDefinition entDef){

        this.name = entDef.getName();
        this.propertyDTO = createPropertyDTOList(entDef.getProps());
    }

    private List<PropertyDTO> createPropertyDTOList(List<PropertyDefinition> props) {
        List<PropertyDTO> propsDTO= new ArrayList<>();

        for (PropertyDefinition propDef: props) {
            PropertyDTO propDTOInstance = new PropertyDTO(propDef);
            propsDTO.add(propDTOInstance);
        }

        return propsDTO;
    }

    public StringBuffer getEntityInfo(){

        StringBuffer info = new StringBuffer();
        info.append("Entity type: "+ this.getName()+"\n");
        for(PropertyDTO propDTO:getPropertyDTO()){
            info.append(propDTO.getName()+ ":\n");
            info.append("Type - "+ propDTO.getType()+ "\n");
            info.append("Range - "+ propDTO.getGetRange()+ "\n");
            info.append("Randomize - "+ propDTO.isRandomInit()+ "\n\n");

        }
        return info;
    }


}

