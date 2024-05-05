package engine.functions;

import engine.context.Context;
import engine.entity.instance.EntityInstance;
import engine.property.instance.PropertyInstance;

public class EvaluateFunction extends AbstractSupportFunction{
    String entityName;
    String propertyName;
    public EvaluateFunction(String funcName, String entityName, String propertyName) {
        super(funcName, FunctionType.EVALUATE);
        this.entityName = entityName;
        this.propertyName = propertyName;
    }

    public Object invoke(Context context){

        if(context.getPrimaryInstanceName().equals(entityName)) {
            try {
                PropertyInstance propInst = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
                return propInst.getValue();
            }
            catch (IllegalArgumentException e) {
                throw e;
            }
        }
        else if(context.isSecondaryEntityExists() &&
                context.getSecondaryInstanceName().equals(entityName)){
            try {
                PropertyInstance propInst = context.getSecondaryEntityInstance().getPropertyByName(propertyName);
                return propInst.getValue();
            }
            catch (IllegalArgumentException e) {
                throw e;
            }
        }
        else {
            if (context.isSecondaryEntityExists()){
                System.out.println(context.getSecondaryInstanceName());
            }
            throw new IllegalArgumentException("Can't activate Evaluate function on different entity");
        }
    }
}
