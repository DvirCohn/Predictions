package engine.action.entitiesActions;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.context.build.rules.SecondaryEntityContext;
import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.entity.instance.EntityInstanceImpl;

import java.util.ArrayList;
import java.util.List;

public class KillAction extends AbstractAction {

    private String entityName;
    public KillAction(EntityDefinition entityDefinition, String entityName, SecondaryEntityContext secondaryEntityContext) {
        super(ActionType.KILL, entityDefinition, secondaryEntityContext);
        this.entityName = entityName;
    }

    public KillAction(EntityDefinition entityDefinition, String entityName) {
        super(ActionType.KILL, entityDefinition);
        this.entityName = entityName;
    }

    @Override
    public void invoke(Context context) {
        if (context.getPrimaryInstanceName().equals(entityName)){
            context.getPrimaryEntityInstance().setAlive(false);

        }
        else if (context.isSecondaryEntityExists() && context.getSecondaryInstanceName().equals(entityName)) {
            context.getSecondaryEntityInstance().setAlive(false);
        }
    }

}
