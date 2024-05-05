package engine.context.build.rules;

import engine.action.api.ActionType;
import engine.action.condition.api.Condition;
import engine.conventer.Converter;
import engine.entity.definition.EntityDefinition;
import generated.PRDAction;

//for build
public class SecondaryEntityContext {
    private Integer numberOfEntities;
    private SelectionType selectionType;
    private Condition condition;
    private EntityDefinition secondaryEntity;
    private boolean isSecondaryEntity = false;

    public SecondaryEntityContext(){}

    public SecondaryEntityContext(PRDAction generatedAction,RuleContext ruleContext, EntityDefinition secondaryEntity){
        if (generatedAction.getPRDSecondaryEntity() != null || generatedAction.getType().toLowerCase().equals(ActionType.REPLACE.toString().toLowerCase())){
            setSecondaryEntityContext(generatedAction,ruleContext, secondaryEntity);
        }
    }

    private void setSecondaryEntityContext(PRDAction generatedAction,RuleContext ruleContext, EntityDefinition secondaryEntity) {
        try{
            if(!generatedAction.getType().toLowerCase().equals(ActionType.REPLACE.toString().toLowerCase())){
                if (generatedAction.getPRDSecondaryEntity().getPRDSelection().getCount().toLowerCase().equals("all")){
                    this.selectionType = SelectionType.ALL;
                }
                else {
                    setNumberOfEntities(new Integer(generatedAction.getPRDSecondaryEntity().getPRDSelection().getCount()));
                    this.selectionType = SelectionType.SELECTED;
                }
                this.condition = Converter.conditionConverter(generatedAction.getPRDSecondaryEntity().getPRDSelection().getPRDCondition(), ruleContext);
            }

            this.secondaryEntity = secondaryEntity;
            this.isSecondaryEntity = true;
        }
        catch (IllegalArgumentException exception){throw exception;}
    }

    private void setNumberOfEntities(Integer numberOfEntities){
        this.numberOfEntities = numberOfEntities;
    }

    public SelectionType getSelectionType() {
        return this.selectionType;
    }

    public String getSecondaryEntityName() {
        return this.secondaryEntity.getName();
    }

    public boolean isSecondaryEntity() {
        return this.isSecondaryEntity;
    }

    public Integer getNumberOfEntities() {
        return numberOfEntities;
    }

    public Condition getCondition() {
        return condition;
    }

    public EntityDefinition getSecondaryEntity() {
        return secondaryEntity;
    }
}

