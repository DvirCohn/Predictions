package dto.implement.action.condition;

import engine.action.condition.ConditionAction.ConditionAction;

public class SingularConditionActionDTO extends AbstractConditionActionDTO{

    public SingularConditionActionDTO(ConditionAction conditionAction){
        super(conditionAction.getActionType().toString(), conditionAction.getEntityDefinition().getName(),
                    conditionAction.isSecondEntityExists(), conditionAction.getSecondaryEntityContext().get().getSecondaryEntityName(),
                    conditionAction.getCondition().getOperator());
        this.getNameOfArgumentToValue().put("Singularity", "Single");
        this.getNameOfArgumentToValue().put("Operator", conditionAction.getCondition().getOperator());
        this.getNameOfArgumentToValue().put("Property", conditionAction.getCondition().getPropertyExpression());
        this.getNameOfArgumentToValue().put("Value", conditionAction.getCondition().getValue());
    }
    public SingularConditionActionDTO(ConditionAction conditionAction, boolean flag){
        super(conditionAction.getActionType().toString(), conditionAction.getEntityDefinition().getName(),
                conditionAction.isSecondEntityExists(), null,
                conditionAction.getCondition().getOperator());
        this.getNameOfArgumentToValue().put("Operator", conditionAction.getCondition().getOperator());
        this.getNameOfArgumentToValue().put("Property", conditionAction.getCondition().getPropertyExpression());
        this.getNameOfArgumentToValue().put("Value", conditionAction.getCondition().getValue());

    }

    @Override
    public StringBuffer getActionInfo() {
        StringBuffer singleCondInfo =super.getActionInfo();

        return singleCondInfo;
    }
}
