package engine.functions;

import engine.context.Context;
import engine.property.instance.PropertyInstance;

public class TickFunction extends AbstractSupportFunction{

    String entityName;
    String propName;
    Context context;

    public TickFunction(String funcName, String entityName, String propName, Context context) {
        super(funcName, FunctionType.TICKS);
        this.entityName = entityName;
        this.propName = propName;
        this.context = context;
    }

    public Integer invoke(int currentTick){
        Integer propTick = getPropertyLastModifiedTick();
        if (propTick != null) {
            return currentTick - propTick;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    private Integer getPropertyLastModifiedTick(){

        Integer lastModifiedTick = null;

        if (entityName.equals(context.getPrimaryEntityInstance().getEntityDefinition().getName())) {
            try{
                PropertyInstance propInst = context.getPrimaryEntityInstance().getPropertyByName(propName);
                lastModifiedTick = new Integer(propInst.getLastModifiedTick());
            }
            catch (IllegalArgumentException ex){
                throw ex;
            }
        }
        else if (context.isSecondaryEntityExists() && entityName.equals(context.getSecondaryEntityInstance().getEntityDefinition().getName())) {
            try{
                PropertyInstance propInst = context.getSecondaryEntityInstance().getPropertyByName(propName);
                lastModifiedTick = new Integer(propInst.getLastModifiedTick());
            }
            catch (IllegalArgumentException ex){
                throw ex;
            }
        }

        return lastModifiedTick;
    }
}
