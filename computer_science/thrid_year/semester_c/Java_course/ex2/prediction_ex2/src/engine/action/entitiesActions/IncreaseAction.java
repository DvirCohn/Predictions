package engine.action.entitiesActions;
import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.context.build.rules.SecondaryEntityContext;
import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.expression.Expression;
import engine.property.definition.PropertyType;
import engine.property.instance.PropertyInstance;


public class IncreaseAction extends AbstractAction {
    private String propertyName;
    private Expression expression;
    private  String byExpression;

    public IncreaseAction(EntityDefinition entityDefinition, String property, String byExpression
            , SecondaryEntityContext secondaryEntityContext) {
        super(ActionType.INCREASE, entityDefinition, secondaryEntityContext);
        this.propertyName = property;
        this.byExpression = byExpression;
        expression =  new Expression(byExpression);
    }
    public IncreaseAction(EntityDefinition entityDefinition, String property,String byExpression) {
        super(ActionType.INCREASE, entityDefinition);
        this.propertyName = property;
        this.byExpression = byExpression;
        expression =  new Expression(byExpression);
    }

    @Override
    public void invoke(Context context) {
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(this.propertyName);

        if(!verifyNumericPropertyType(propertyInstance)){
            throw new IllegalArgumentException("increase action can't operate on a none number property [" + propertyName);
        }
        if(propertyInstance.getPropertyDefinition().getType() == PropertyType.FLOAT){
            Float arg1 = (Float) PropertyType.FLOAT.convert(propertyInstance.getValue());
            Float arg2 = (Float) expression.translateExpression(context);
            Float result = arg1 + arg2;
            propertyInstance.updateValue(result, context.getCurrentTick());


        } else if (propertyInstance.getPropertyDefinition().getType() == PropertyType.DECIMAL) {
            Integer arg1 = (Integer) PropertyType.DECIMAL.convert(propertyInstance.getValue());
            Integer arg2 = (Integer) expression.translateExpression(context);
            Integer result = arg1 + arg2;
            propertyInstance.updateValue(result, context.getCurrentTick());

        }
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getByExpression() {
        return byExpression;
    }
}
