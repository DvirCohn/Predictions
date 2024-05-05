package engine.action.numericAction;

import engine.action.api.ActionType;
import engine.action.api.Calculation;
import engine.context.Context;
import engine.context.build.rules.SecondaryEntityContext;
import engine.entity.definition.EntityDefinition;
import engine.expression.Expression;
import engine.property.instance.PropertyInstance;


public class Divide extends Calculation {

    private String arg1Expression;
    private String arg2Expression;

    private Expression expression1;
    private  Expression expression2;

    public Divide(EntityDefinition entityDefinition, String resultProperty, String arg1Expression, String arg2Expression
            , SecondaryEntityContext secondaryEntityContext) {
        super(entityDefinition,resultProperty, ActionType.DIVIDE, secondaryEntityContext);
        this.arg1Expression = arg1Expression;
        this.arg2Expression = arg2Expression;
        this.expression1 = new Expression(this.arg1Expression);
        this.expression2 = new Expression(this.arg2Expression);
    }

    public Divide(EntityDefinition entityDefinition, String resultProperty, String arg1Expression, String arg2Expression) {
        super(entityDefinition,resultProperty, ActionType.DIVIDE);
        this.arg1Expression = arg1Expression;
        this.arg2Expression = arg2Expression;
        this.expression1 = new Expression(this.arg1Expression);
        this.expression2 = new Expression(this.arg2Expression);
    }

    @Override
    public void invoke(Context context) {
        Float result;

        result = (Float)this.expression1.translateExpression(context) / (Float)this.expression2.translateExpression(context);
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(this.resultProperty);
        propertyInstance.updateValue(result, context.getCurrentTick());

    }

    public String getArg1Expression() {
        return arg1Expression;
    }

    public String getArg2Expression() {
        return arg2Expression;
    }
}
