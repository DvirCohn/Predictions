package engine.action.api;


import engine.action.condition.ConditionAction.ActionsToDo;
import engine.action.condition.ConditionAction.ConditionAction;
import engine.action.condition.api.Condition;
import engine.context.Context;
import engine.context.build.rules.SecondaryEntityContext;
import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;

import java.util.List;
import java.util.Optional;

public interface Action {

    void invoke(Context context) throws Exception;
    ActionType getActionType();
    EntityDefinition getContextEntity();
    EntityDefinition getEntityDefinition();
    boolean isSecondEntityExists();
    Optional<SecondaryEntityContext> getSecondaryEntityContext();
}
