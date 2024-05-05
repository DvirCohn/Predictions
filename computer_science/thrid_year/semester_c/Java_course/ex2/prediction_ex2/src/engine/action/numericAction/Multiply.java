package engine.action.numericAction;

import engine.action.api.ActionType;
import engine.action.api.Calculation;
import engine.context.Context;
import engine.context.build.rules.SecondaryEntityContext;
import engine.entity.definition.EntityDefinition;
import engine.expression.Expression;
import engine.property.definition.PropertyType;
import engine.property.instance.PropertyInstance;


public class Multiply extends Calculation {


    private String arg1Expression;
    private String arg2Expression;
    private Expression expression1;
    private  Expression expression2;



    public Multiply(EntityDefinition entityDefinition, String resultProperty, String arg1Expression, String arg2Expression
                    ,SecondaryEntityContext secondaryEntityContext) {
        super(entityDefinition,resultProperty, ActionType.MULTIPLY, secondaryEntityContext);
        this.arg1Expression = arg1Expression;
        this.arg2Expression = arg2Expression;
        this.expression1 = new Expression(this.arg1Expression);
        this.expression2 = new Expression(this.arg2Expression);
    }
    public Multiply(EntityDefinition entityDefinition, String resultProperty, String arg1Expression, String arg2Expression) {
        super(entityDefinition,resultProperty, ActionType.MULTIPLY);
        this.arg1Expression = arg1Expression;
        this.arg2Expression = arg2Expression;
        this.expression1 = new Expression(this.arg1Expression);
        this.expression2 = new Expression(this.arg2Expression);
    }

    @Override
    public void invoke(Context context) {

        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(this.resultProperty);
        if(!verifyNumericPropertyType(propertyInstance)){
            throw new UnsupportedOperationException("Can't perform Multiply operation on this property type: "+propertyInstance.getPropertyDefinition().getType().toString());
        }
        else {

            if(propertyInstance.getPropertyDefinition().getType() == PropertyType.DECIMAL){
                try{
                    Integer result = null;
                    result = multiply((Integer)expression1.translateExpression(context),(Integer)expression2.translateExpression(context));
                    propertyInstance.updateValue(result, context.getCurrentTick());
                }
                catch (Exception e){
                    System.out.println(e + " in multiply ");
                }
            }
            else {
                try{
                    Float result;
                    result = multiply((Float)expression1.translateExpression(context),(Float)expression2.translateExpression(context));
                    propertyInstance.updateValue(result, context.getCurrentTick());
                }
                catch (Exception e){
                    System.out.println(e + " in multiply ");
                }
            }
        }
    }

    public static Integer multiply(Integer operand1, Integer operand2){
        return operand1 * operand2;
    }

    public static Float multiply(Float operand1, Float operand2) {
        return operand1.floatValue() * operand2.floatValue();
    }

    public String getArg1Expression() {
        return arg1Expression;
    }

    public String getArg2Expression() {
        return arg2Expression;
    }
}
