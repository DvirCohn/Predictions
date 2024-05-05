package engine.action.condition.ConditionAction;

import engine.action.api.Action;
import engine.context.Context;

import java.util.List;

public class ActionsToDo {

    private List<Action> actions2do;

    public ActionsToDo(List<Action> actions) {
        this.actions2do = actions;
    }
    public void invoke(Context context) throws Exception { //
        for(Action action:actions2do){
            action.invoke(context);
        }
    }

    public List<Action> getActions2do() {
        return actions2do;
    }
}
