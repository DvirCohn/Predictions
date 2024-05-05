package engine.action.condition.api;

public enum MultipleConditionOperator {

    AND,OR;

     boolean check(boolean operand1,boolean operand2){
        switch(this){
            case OR: return operand1 || operand2 ;
            case AND: return operand1 && operand2;
        }
        throw  new AssertionError("Unknown operator: " + this);
    }
}
