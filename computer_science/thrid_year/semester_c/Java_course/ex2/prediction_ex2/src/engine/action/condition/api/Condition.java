package engine.action.condition.api;

import engine.action.condition.ConditionAction.ActionsToDo;
import engine.context.Context;

import java.util.List;

public interface Condition {

    boolean test(Context context);
    ConditionType getType();
    String getOperator();
    Integer getNumberOfConditions();
    String getPropertyExpression();
    String getValue();
    //Condition getCondition();
    //List<ActionsToDo> getActionsToDo();


}
