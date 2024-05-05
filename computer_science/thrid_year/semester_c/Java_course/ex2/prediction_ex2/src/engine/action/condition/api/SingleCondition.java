package engine.action.condition.api;

import engine.context.Context;
import engine.expression.Expression;
import engine.expression.ReturnValueType;
import engine.property.definition.PropertyType;
import engine.property.instance.PropertyInstance;

import java.util.InputMismatchException;

public class SingleCondition implements Condition {

    private Expression value;//value field
    private String operator;
    private String entity;
    private Expression propertyExpression;
    private final Integer numberOfConditions = 1;


    public SingleCondition(String operator, Expression byExpression, Expression propertyExpression,  String entity) {
        this.value = byExpression;
        this.operator = operator;
        this.entity = entity;
        this.propertyExpression = propertyExpression;
    }


    @Override
    public boolean test(Context context)  {

        //PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(this.propertyName);
        Object valueExpression = this.value.translateExpression(context);
        Object propExpression = this.propertyExpression.translateExpression(context);
        boolean result = false;
        if(this.propertyExpression.getType() == ReturnValueType.FLOAT
                || this.propertyExpression.getType() == ReturnValueType.INTEGER){
            if(valueExpression.getClass() != Integer.class && valueExpression.getClass() != Float.class){
                throw new NumberFormatException("cannot do operation on two different types");
            }
        }
        else if(this.propertyExpression.getType() == ReturnValueType.STRING){}
        else if (this.propertyExpression.getType() != this.value.getType()) {
            throw new ClassCastException("Can't compare different kind of types.");
        }
        if (SingleConditionOperator.checkOperator(operator)) {
            // check for same type
            if (operator.equals("=")) {
                if (this.propertyExpression.getType() == ReturnValueType.STRING){
                    result = propExpression.equals(valueExpression.toString());
                }
                else{
                    result = propExpression.equals(valueExpression);
                }
            } else if (operator.equals("!=")) {
                if (this.propertyExpression.getType() == ReturnValueType.STRING){
                    result = !propExpression.equals(valueExpression.toString());
                }
                else{
                    result = !(propExpression.equals(valueExpression));
                }
            } else if (operator.equals("bt") ) {
                if (this.propertyExpression.getType() == ReturnValueType.INTEGER || this.propertyExpression.getType() == ReturnValueType.FLOAT) {
                    if (valueExpression instanceof Integer) {
                        if(propExpression instanceof Integer){
                            result = (Integer) propExpression > (Integer) valueExpression;
                        }
                        else {
                            result = (Float) propExpression > (Integer) valueExpression;
                        }

                    }
                    else {
                        if(propExpression instanceof Integer){
                            result = (Integer) propExpression > (Float) valueExpression;
                        }
                        else{
                            result = (Float) propExpression > (Float) valueExpression;
                        }
                    }
                }

            } else if (operator.equals("lt")) {
                if (this.propertyExpression.getType() == ReturnValueType.INTEGER || this.propertyExpression.getType() == ReturnValueType.FLOAT) {
                    if (valueExpression instanceof Integer) {
                        if (propExpression instanceof Integer){
                            result = (Integer)propExpression < (Integer) valueExpression;
                        }
                        else {
                            result = (Float)propExpression < (Integer) valueExpression;
                        }
                    }
                    else {
                        if (propExpression instanceof Integer) {
                            result = (Integer) propExpression < (Float) valueExpression;
                        }
                        else {
                            result = (Float) propExpression < (Float) valueExpression;
                        }
                    }

                }

            } else {
                throw new InputMismatchException("Not valid operator: "+operator);
            }

        }
        return result;

    }

    @Override
    public ConditionType getType() {
        return ConditionType.SINGLE;
    }

    @Override
    public String getOperator() {
        return operator;
    }

    @Override
    public String getPropertyExpression(){
        return this.propertyExpression.getExpression();
    }

    @Override
    public Integer getNumberOfConditions() {
        return this.numberOfConditions;
    }

    @Override
    public String getValue(){
        return value.getExpression();
    }
}
