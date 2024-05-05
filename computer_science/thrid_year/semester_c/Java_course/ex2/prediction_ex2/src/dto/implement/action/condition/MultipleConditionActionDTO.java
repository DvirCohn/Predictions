package dto.implement.action.condition;

import engine.action.condition.ConditionAction.ConditionAction;
import engine.action.condition.api.MultipleCondition;

public class MultipleConditionActionDTO extends AbstractConditionActionDTO {

    private Integer numberOfActionsInThen;
    private Integer numberOfActionsInElse;
    private Integer numberOfConditions;



    public MultipleConditionActionDTO(ConditionAction conditionAction){
        super(conditionAction.getActionType().toString(), conditionAction.getEntityDefinition().getName(),
                conditionAction.isSecondEntityExists(), conditionAction.getSecondaryEntityContext().get().getSecondaryEntityName(),
                conditionAction.getCondition().getOperator());

        this.numberOfActionsInThen = new Integer(getNumberOfActionInThen(conditionAction));
        this.numberOfActionsInElse = new Integer (getNumberOfActionInElse(conditionAction));
        this.numberOfConditions = new Integer (getAmountOfConditions(conditionAction));

        this.getNameOfArgumentToValue().put("Operator", conditionAction.getCondition().getOperator().toString());
        this.getNameOfArgumentToValue().put("Number of action in then", numberOfActionsInThen.toString());
        this.getNameOfArgumentToValue().put("Number of action in Else", numberOfActionsInElse.toString());
        this.getNameOfArgumentToValue().put("Number of conditions", numberOfConditions.toString());

    }

    public MultipleConditionActionDTO(ConditionAction conditionAction, boolean flag){
        super(conditionAction.getActionType().toString(), conditionAction.getEntityDefinition().getName(),
                conditionAction.getSecondaryEntityContext().isPresent(), null,
                conditionAction.getCondition().getOperator());

        this.numberOfActionsInThen = new Integer(getNumberOfActionInThen(conditionAction));
        this.numberOfActionsInElse = new Integer (getNumberOfActionInElse(conditionAction));
        this.numberOfConditions = new Integer (getAmountOfConditions(conditionAction));

        this.getNameOfArgumentToValue().put("Singularity", "Multiple");
        this.getNameOfArgumentToValue().put("Number of action in then", numberOfActionsInThen.toString());
        this.getNameOfArgumentToValue().put("Number of action in Else", numberOfActionsInElse.toString());
        this.getNameOfArgumentToValue().put("Number of conditions", numberOfConditions.toString());

    }


    public static int getNumberOfActionInThen(ConditionAction conditionAction){
        return conditionAction.getActionsToDo().get(0).getActions2do().size();
    }

    public static int getNumberOfActionInElse(ConditionAction conditionAction){
        if (conditionAction.getActionsToDo().size() == 2){
            return conditionAction.getActionsToDo().get(1).getActions2do().size();
        }
        return 0;
    }

    public int getAmountOfConditions(ConditionAction conditionAction){
       return  conditionAction.getCondition().getNumberOfConditions();
    }
}
