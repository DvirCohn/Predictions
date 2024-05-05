package engine.action.entitiesActions;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.context.build.rules.SecondaryEntityContext;
import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.entity.instance.EntityInstanceImpl;
import generated.PRDAction;

import java.util.List;

public class ReplaceAction extends AbstractAction  {

    private String entNameToCreate;
    private String entNameToKill;
    private String creationMode;
    private EntityDefinition toCreate;

    public ReplaceAction(EntityDefinition entityDefinition, PRDAction generatedAction
            , SecondaryEntityContext secondaryEntityContext) {
        super(ActionType.REPLACE, entityDefinition, secondaryEntityContext);
        this.creationMode = generatedAction.getMode();
        this.entNameToCreate = generatedAction.getCreate();
        this.entNameToKill = generatedAction.getKill();
        this.toCreate = secondaryEntityContext.getSecondaryEntity();
        validateDataMembers();
    }

//    public ReplaceAction(EntityDefinition entityDefinition, PRDAction generatedAction) {
//        super(ActionType.REPLACE, entityDefinition);
//        this.creationMode = generatedAction.getMode();
//        this.entNameToCreate = generatedAction.getCreate();
//        this.entNameToKill = generatedAction.getKill();
//
//        validateDataMembers();
//    }

    @Override
    public void invoke(Context context) throws Exception {
        EntityInstance toKill = context.getPrimaryEntityInstance();

        EntityInstance newEntity = new EntityInstanceImpl(toCreate);

        if (creationMode.toLowerCase().equals("derived")){

            for (String propInstName : toKill.getProperties().keySet()) {
                if (newEntity.getProperties().containsKey(propInstName)){
                    try{
                        newEntity.getPropertyByName(propInstName).updateValue(toKill.getPropertyByName(propInstName).getValue(), context.getCurrentTick());
                    }
                    catch (ClassCastException ex){
                        throw new ClassCastException("Could not create new entity due to same property name with different types");
                    }
                }
            }

        }
        else {
            if (context.getPrimaryEntityInstance().getEntityDefinition().getName().toLowerCase().equals(entNameToCreate)) {
                toCreate = context.getPrimaryEntityInstance().getEntityDefinition();
            }
            else {
                toCreate = context.getSecondaryEntityDefinition();
            }
        }
        newEntity.setLocation(toKill.getLocation());
        context.getSecondaryEntitiesInstancesManager(entNameToCreate).getInstances().add(newEntity);

        KillAction kill = new KillAction(context.getPrimaryEntityInstance().getEntityDefinition(),context.getPrimaryEntityInstance().getEntityDefinition().getName());
        kill.invoke(context);

    }

    private void validateDataMembers(){

        if (!(creationMode.toLowerCase().equals("derived") || creationMode.toLowerCase().equals("scratch"))){
            throw new IllegalArgumentException("invalid creation mode in replace action, only derived and scratch are valid");
        }

        if (entNameToCreate == null){
            throw new IllegalArgumentException("invalid name to create new entity, in replace action");
        }

        if (entNameToKill == null){
            throw new IllegalArgumentException("invalid name to kill new entity, in replace action");
        }
    }
}
