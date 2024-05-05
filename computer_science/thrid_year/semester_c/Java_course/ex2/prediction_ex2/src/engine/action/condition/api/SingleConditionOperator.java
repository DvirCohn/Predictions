package engine.action.condition.api;

public enum SingleConditionOperator {
    EQUAL,NOTEQUAL,BT,LT;

     public static boolean checkOperator(String optionalOperator){
        switch (optionalOperator.toLowerCase()){
            case "=":
            case "!=":
            case "bt":
            case "lt": return true;
        }
        return  false;
    }
}
