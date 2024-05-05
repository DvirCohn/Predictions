package engine.action.entitiesActions;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.context.build.rules.SecondaryEntityContext;
import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.expression.Expression;
import engine.property.instance.PropertyInstance;
import generated.PRDAction;

import java.util.List;

public class SetAction extends AbstractAction {

    private String propertyName;
    private String byExpression;
    private  Expression expression;

    public SetAction(EntityDefinition entityDefinition, String property, String byExpression
                    , SecondaryEntityContext secondaryEntityContext) {
        super(ActionType.SET, entityDefinition, secondaryEntityContext);
        this.propertyName = property;
        this.byExpression = byExpression;
        this.expression = new Expression(this.byExpression);
    }
    public SetAction(EntityDefinition entityDefinition, String property, String byExpression) {
        super(ActionType.SET, entityDefinition);
        this.propertyName = property;
        this.byExpression = byExpression;
        this.expression = new Expression(this.byExpression);
    }

    @Override
    public void invoke(Context context) {

        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(this.propertyName);
        propertyInstance.updateValue(this.expression.translateExpression(context), context.getCurrentTick());
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getByExpression() {
        return byExpression;
    }
}
