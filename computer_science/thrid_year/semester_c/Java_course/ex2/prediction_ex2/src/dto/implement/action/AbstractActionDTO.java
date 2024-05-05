package dto.implement.action;

import engine.action.entitiesActions.*;

import java.util.HashMap;
import java.util.Map;

public class AbstractActionDTO implements ActionDTO{
    private String type;
    private String primaryEntity;
    private boolean isSecondaryEntityExists;
    private String secondaryEntity;
    private Map<String, String> nameOfArgumentToValue;

    public AbstractActionDTO(String type, String primaryEntity, boolean isSecondaryEntityExists, String secondaryEntity) {
        this.type = type;
        this.primaryEntity = primaryEntity;
        this.isSecondaryEntityExists = isSecondaryEntityExists;
        this.secondaryEntity = secondaryEntity;
        this.nameOfArgumentToValue = new HashMap<>();
    }

    @Override
    public StringBuffer getActionInfo(){
        StringBuffer info = new StringBuffer();

        if(this.type.toLowerCase().equals("proximity")){
            info.append("Action type: " + type + "\n");


        }
        else {

            info.append("Action type: " + type + "\n");
            info.append("Primary entity: " + primaryEntity + "\n");

            if (isSecondaryEntityExists) {
                info.append("Secondary entity:" + secondaryEntity + "\n");
            } else {
                info.append("Secondary entity: does not exist for this action\n");
            }
        }
        for (String key : nameOfArgumentToValue.keySet()){
            info.append("value of " + key + ": " + nameOfArgumentToValue.get(key)+"\n");
        }


        return info;
    }

    public AbstractActionDTO(IncreaseAction increaseAction){

        this.type = increaseAction.getActionType().toString();
        this.primaryEntity = increaseAction.getEntityDefinition().getName();
        if (increaseAction.getSecondaryEntityContext().isPresent()){
            this.secondaryEntity = increaseAction.getSecondaryEntityContext().get().getSecondaryEntityName();
            this.isSecondaryEntityExists = true;
        }
        else {
            this.isSecondaryEntityExists = false;
        }
        this.nameOfArgumentToValue = new HashMap<>();
        this.nameOfArgumentToValue.put("property",increaseAction.getPropertyName());
        this.nameOfArgumentToValue.put("by", increaseAction.getByExpression());
    }

    public AbstractActionDTO(DecreaseAction decreaseAction){
        this.type = decreaseAction.getActionType().toString();
        this.primaryEntity = decreaseAction.getEntityDefinition().getName();
        if (decreaseAction.getSecondaryEntityContext().isPresent()){
            this.secondaryEntity = decreaseAction.getSecondaryEntityContext().get().getSecondaryEntityName();
            this.isSecondaryEntityExists = true;
        }
        else {
            this.isSecondaryEntityExists = false;
        }
        this.nameOfArgumentToValue = new HashMap<>();
        this.nameOfArgumentToValue.put("property",decreaseAction.getPropertyName());
        this.nameOfArgumentToValue.put("by", decreaseAction.getByExpression());
    }

    public AbstractActionDTO(SetAction setAction){
        this.type = setAction.getActionType().toString();
        this.primaryEntity = setAction.getEntityDefinition().getName();
        if (setAction.getSecondaryEntityContext().isPresent()){
            this.secondaryEntity = setAction.getSecondaryEntityContext().get().getSecondaryEntityName();
            this.isSecondaryEntityExists = true;
        }
        else {
            this.isSecondaryEntityExists = false;
        }
        this.nameOfArgumentToValue = new HashMap<>();
        this.nameOfArgumentToValue.put("property",setAction.getPropertyName());
        this.nameOfArgumentToValue.put("value", setAction.getByExpression());
    }

    public AbstractActionDTO(KillAction killAction){
        this.type = killAction.getActionType().toString();
        this.primaryEntity = killAction.getEntityDefinition().getName();
        this.isSecondaryEntityExists = killAction.isSecondEntityExists();
        if (killAction.getSecondaryEntityContext().isPresent()){
            this.secondaryEntity = killAction.getSecondaryEntityContext().get().getSecondaryEntityName();
            this.isSecondaryEntityExists = true;
        }
        else {
            this.isSecondaryEntityExists = false;
        }
        this.nameOfArgumentToValue = new HashMap<>();
    }

    public AbstractActionDTO(ReplaceAction replaceAction){
        this.type = replaceAction.getActionType().toString();
        this.primaryEntity = replaceAction.getEntityDefinition().getName();
        this.isSecondaryEntityExists = replaceAction.isSecondEntityExists();
        if (replaceAction.getSecondaryEntityContext().isPresent()){
            this.secondaryEntity = replaceAction.getSecondaryEntityContext().get().getSecondaryEntityName();
        }
        this.nameOfArgumentToValue = new HashMap<>();
    }
    public AbstractActionDTO(ProximityAction proximityAction){
        this.type = proximityAction.getActionType().toString();
        this.primaryEntity = "null"; //setAction.getEntityDefinition().getName();
        if (proximityAction.getSecondaryEntityContext().isPresent()){
            this.secondaryEntity = proximityAction.getSecondaryEntityContext().get().getSecondaryEntityName();
            this.isSecondaryEntityExists = true;
        }
        else {
            this.isSecondaryEntityExists = false;
        }
        this.nameOfArgumentToValue = new HashMap<>();
        this.getNameOfArgumentToValue().put("Source entity", proximityAction.getSourceName());
        this.getNameOfArgumentToValue().put("Target entity", proximityAction.getTargetName());
        this.getNameOfArgumentToValue().put("Depth", proximityAction.getDepthEx().getExpression());
        this.getNameOfArgumentToValue().put("Number of action to do", proximityAction.getActionsNumber().toString());
    }


    public Map<String, String> getNameOfArgumentToValue() {
        return nameOfArgumentToValue;
    }

    @Override
    public String getName() {
        return this.type;
    }
    @Override
    public String getPrimaryEntity() {
        return primaryEntity;
    }

    @Override
    public boolean isSecondaryEntityExists(){
        return this.isSecondaryEntityExists;
    }

    @Override
    public String getSecondaryEntity() {
        return this.secondaryEntity;
    }
}
