package engine.action.condition.ConditionAction;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.action.condition.api.Condition;
import engine.context.Context;
import engine.context.build.rules.SecondaryEntityContext;
import engine.entity.definition.EntityDefinition;

import java.util.List;

public class ConditionAction extends AbstractAction {

    private Condition condition;
    private List<ActionsToDo> toDo;

    public ConditionAction(EntityDefinition entityDefinition, Condition condition, List<ActionsToDo> toDo
                          , SecondaryEntityContext secondaryEntityContext) {
        super(ActionType.CONDITION,entityDefinition, secondaryEntityContext);
        this.condition = condition;
        this.toDo = toDo;
    }

    public ConditionAction(EntityDefinition entityDefinition, Condition condition, List<ActionsToDo> toDo) {
        super(ActionType.CONDITION,entityDefinition, null);
        this.condition = condition;
        this.toDo = toDo;
    }

    @Override
    public void invoke(Context context) throws Exception {
        if(condition.test(context)){
            //then actions
            this.toDo.get(0).invoke(context);
        }
        else {
            if(this.toDo.size() == 2){ // if there is an else block
                //else actions
                this.toDo.get(1).invoke(context);
            }

        }
    }


    public List<ActionsToDo> getActionsToDo(){
        return toDo;
    }


    public Condition getCondition() {
        return condition;
    }
}
