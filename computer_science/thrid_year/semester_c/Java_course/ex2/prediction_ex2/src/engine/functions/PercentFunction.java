package engine.functions;

import engine.context.Context;
import engine.expression.Expression;

public class PercentFunction extends AbstractSupportFunction {

    Expression percent;
    Expression number;

    public PercentFunction(String funcName, Expression percent, Expression number) {
        super(funcName, FunctionType.PERCENT);
        this.number = number;
        this.percent = percent;
    }

    public Float invoke(Context context) throws ClassCastException{
        try{
            Float num = (Float) number.translateExpression(context);
            Float per = (Float) percent.translateExpression(context);

            return num * per / 100;
        }
        catch (ClassCastException e){
            throw e;
        }
    }
}
