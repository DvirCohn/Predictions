package engine.action.condition.api;


import engine.context.Context;

import java.util.InputMismatchException;
import java.util.List;

public class MultipleCondition implements Condition {


    private MultipleConditionOperator logicOperator;
    private List<Condition> conditions;
    private String operator;




    public MultipleCondition(List<Condition> conditions,String operator) {

        this.operator = operator;
        this.conditions = conditions;
        this.logicOperator = checkStringOperator(this.operator);
        if(logicOperator == null){
            throw new InputMismatchException("Cant support "+operator+" operation. only OR/And");
        }

    }

    private MultipleConditionOperator checkStringOperator(String op){
        switch (op){
            case "or": return MultipleConditionOperator.OR;
            case "and" : return MultipleConditionOperator.AND;
        }
        return null;
    }


    @Override
    public boolean test(Context context) {

        boolean res = conditions.get(0).test(context);
        for(Condition cond:this.conditions){
            res = logicOperator.check(res,cond.test(context));
        }
        return res;

    }

    @Override
    public ConditionType getType() {
        return ConditionType.MULTIPLE;
    }

    @Override
    public String getOperator() {
        return operator;
    }

    @Override
    public Integer getNumberOfConditions() {
        return this.conditions.size();
    }

    @Override
    public String getPropertyExpression() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }

}



