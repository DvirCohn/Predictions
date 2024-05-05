package engine.expression;

import engine.context.Context;
import engine.context.build.rules.RuleContext;
import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.functions.*;
import engine.property.definition.PropertyDefinition;
import engine.property.definition.PropertyType;

import java.util.Map;

public class Expression {
    private final String expression;
    private ReturnValueType type;

    public Expression(String expression){
        this.expression = expression;
    }

    public Object translateExpression(Context context){
        String[] functionNameAndArgument = extractWords(expression);
        if(functionNameAndArgument != null) {
            return invokeMethod(functionNameAndArgument[0], functionNameAndArgument[1], context);
        }
        else if (expressionIsEntityProperty(context.getPrimaryEntityInstance())){
            return context.getPrimaryEntityInstance().getPropertyByName(expression).getValue();
        }
        else {
            return tryParse();
        }
    }

    public static Boolean checkNumericArg(String expression, RuleContext ruleContext){
        String[] functionNameAndArgument = extractWords(expression);
        if(functionNameAndArgument != null){
            return checkValidFunction(functionNameAndArgument[0], functionNameAndArgument[1], ruleContext);
        }
        else if (isExpressionProperty(expression, ruleContext.getPrimaryEntity())){
            return checkIsNumericProperty(expression, ruleContext.getPrimaryEntity());
        }
        else{
            try{
                Integer integer = Integer.getInteger(expression);
                return true;
            }catch(NumberFormatException e){
                try{
                    Float aFloat = Float.parseFloat(expression);
                    return true;
                }
                catch(NumberFormatException ex){return false;}
            }
        }
    }

    public static boolean isValidEntityAndPropertyPair(Map<String, EntityDefinition> entNameToDefinition, String entName,String propName){
        boolean isValid = false;

        if (entNameToDefinition.containsKey(entName)){
            isValid = isExpressionProperty(propName, entNameToDefinition.get(entName));
        }

        return isValid;
    }

    public static Boolean isExpressionProperty(String expression,EntityDefinition entity){
        for(PropertyDefinition propDef : entity.getProps()){
            if(expression.equals(propDef.getName())){
                return true;
            }
        }
        return false;
    }

    public static Boolean checkIsNumericProperty(String property,EntityDefinition entityDefinition){
        if( entityDefinition.getPropertyByName(property).getType() != PropertyType.DECIMAL
            && entityDefinition.getPropertyByName(property).getType()  != PropertyType.FLOAT){
            return false;
        }

        return true;
    }
    
    public static Boolean checkValidFunction(String func, String arg, RuleContext ruleContext){
        boolean valid = true;
        if(func.equals("random")){
            try{
                Integer integer = Integer.getInteger(arg);
                
            }catch(NumberFormatException e){
                valid = false;
            }
        }
        else if(func.toLowerCase().equals("environment")){
            
            if(ruleContext.getEnvironmentDefinition().getProperty(arg).getType() != PropertyType.DECIMAL &&
                    ruleContext.getEnvironmentDefinition().getProperty(arg).getType() != PropertyType.FLOAT){
                valid = false;
            }
            
        }
        else if (func.toLowerCase().equals("evaluate") || func.toLowerCase().equals("ticks")) {
            String[] splitWords = splitWordsBySeparatorFormat(arg,".");
            if (splitWords != null){
                if (!isValidEntityAndPropertyPair(ruleContext.getEntityMap(), splitWords[0],splitWords[1])){
                    valid = false;
                }
            }
            else {
                valid = false;
            }
        }
        else if (func.toLowerCase().equals("percent")) {
            valid = isValidPercentFormat(arg, ruleContext);
        }
        else {
            valid = false;
        }

        return valid;

    }

    private Object tryParse() {
//        try{
//            Integer integer = new Integer(expression);
//            return integer;
//        }catch(NumberFormatException e){}
        try{
            Float aFloat = new Float(expression);
            return aFloat;
        }
        catch(NumberFormatException e){}
        try{
            Boolean bool = Boolean.getBoolean(expression); // to check if return not null!!!!
            return bool;
        }
        catch (SecurityException e){}

        return expression;
    }

    private Boolean expressionIsEntityProperty(EntityInstance entityInstance) {
        return entityInstance.getProperties().containsKey(expression);
    }

    // TODO: 9/14/2023 change the invokeMethod method to be appopriate to all support functions  
    public Object invokeMethod(String functionName, String arg,Context context) {
        Expression argExpression = new Expression(arg);

        if(functionName.equals("environment")){
            try {
                EnvironmentFunction findEnv = new EnvironmentFunction(functionName, context.getActiveEnvironment());
                return findEnv.invoke(arg).getValue();

            }
            catch(IllegalArgumentException e){
                return null;
            }
        }
        else if (functionName.equals("random")){
            try{
                Integer integer = new Integer(arg);
                RandomFunction generateRandom = new RandomFunction(functionName);
                return generateRandom.invoke(integer);
            }
            catch (NumberFormatException e){
                throw new IllegalArgumentException("Argument fot the random function isn't a number");
            }
        }
        else if (functionName.equals("evaluate")){
            String entityNameAndProperty [] = splitWordsBySeparatorFormat(arg,".");
            if(entityNameAndProperty != null){
                EvaluateFunction eval = new EvaluateFunction(functionName,entityNameAndProperty[0],entityNameAndProperty[1]);
                return eval.invoke(context);//,entityNameAndProperty[0],entityNameAndProperty[1]);
            }
            
            
        }// TODO: 9/14/2023 add ticks and percent function's invoke and translate args as an expression   
        else if (functionName.equals("ticks")) {
            String entityNameAndProperty [] = splitWordsBySeparatorFormat(arg, ".");
            TickFunction tickFunction = new TickFunction(functionName,entityNameAndProperty[0],entityNameAndProperty[1], context);
            return tickFunction.invoke(context.getCurrentTick());
        }
        else if (functionName.equals("percent")){
            String[] expressions = splitWordsBySeparatorFormat(arg,",");
            if (expressions != null){
                PercentFunction percentFunction = new PercentFunction(functionName, new Expression(expressions[0]), new Expression(expressions[1]));
                return percentFunction.invoke(context);
            }
        }
        return null;
    }

    private static String[] extractWords(String input) {
        int openParenIndex = input.indexOf('(');
        int closeParenIndex = input.lastIndexOf(')');

        if (openParenIndex != -1 && closeParenIndex != -1 && closeParenIndex > openParenIndex) {
            String word1 = input.substring(0, openParenIndex);
            String word2 = input.substring(openParenIndex + 1, closeParenIndex);
            return new String[]{word1, word2};
        } else {
            return null; // Invalid format
        }
    }

    private static String[] splitWordsBySeparatorFormat(String input, String separator) {
        int dotIndex = input.indexOf(separator);

        if (dotIndex > 0 && dotIndex < input.length() - 1) {
            String word1 = input.substring(0, dotIndex);
            String word2 = input.substring(dotIndex + 1);

            // Check if word1 and word2 are non-empty
            if (!word1.isEmpty() && !word2.isEmpty()) {
                return new String[]{word1, word2};
            }
        }

        return null; // Invalid format
    }

    public static boolean isValidPercentFormat(String input, RuleContext ruleContext) {

        // Split the content by ","
        String[] parts = input.split(",");

        // Check if there are exactly two parts
        if (parts.length == 2) {

            // Try to parse the parts as numbers
            Expression firstEx = new Expression(parts[0]);
            Expression secondEx = new Expression(parts[1]);

            firstEx.updateTypeFromExpression(ruleContext);
            secondEx.updateTypeFromExpression(ruleContext);

            if ((firstEx.getType().equals(ReturnValueType.FLOAT) || firstEx.getType().equals(ReturnValueType.INTEGER))
             && (secondEx.getType().equals(ReturnValueType.FLOAT) || secondEx.getType().equals(ReturnValueType.INTEGER))){
                return true;
            }

        }

        return false;
    }

    private void setType(ReturnValueType type){
        this.type = type;
    }

    public void updateTypeFromExpression(RuleContext context){
        FunctionType functionType = isSupportFunction();
        if (functionType != null){
            // add function that convert function to Return Value Type
            String[] funcNameAndArgument = extractWords(expression);
            setType(this.functionReturnedValue(functionType,funcNameAndArgument[1],context));
        }
        else if (isExpressionEntityProperty(context, expression)) {
            PropertyType propType = context.getPrimaryEntity().getPropertyByName(expression).getType();
            switch (propType){
                case DECIMAL:
                    setType(ReturnValueType.INTEGER);
                    break;
                case FLOAT:
                    setType(ReturnValueType.FLOAT);
                    break;
                case BOOLEAN:
                    setType(ReturnValueType.BOOLEAN);
                    break;
                case STRING:
                    setType(ReturnValueType.STRING);
                    break;
            }
        }
        else {
            setType(freeArgReturnValue(this.expression));
        }
    }

    private ReturnValueType freeArgReturnValue(String expression){
        try{
            Integer integer = new Integer(expression);
            return ReturnValueType.INTEGER;
        }catch(NumberFormatException e){}
        try{
            Float aFloat = new Float(expression);
            return ReturnValueType.FLOAT;
        }
        catch(NumberFormatException e){}
//        try{
//            Boolean bool = Boolean.getBoolean(expression); // to check if return not null!!!!
//            return ReturnValueType.BOOLEAN;
//        }
//        catch (SecurityException e){}
        if (expression.toLowerCase().equals("true") || expression.toLowerCase().equals("false")){
            return ReturnValueType.BOOLEAN;
        }

        return ReturnValueType.STRING;
    }

    private FunctionType isSupportFunction(){
        String[] tryExtractSupportFunction = extractWords(expression);
        FunctionType functionType = null;
        if(tryExtractSupportFunction != null){
            try {
                functionType = FunctionType.valueOf(tryExtractSupportFunction[0].toUpperCase());
            }
            catch (IllegalArgumentException exception){
                new IllegalArgumentException("can't convert Expression to support function, invalid support function name");
            }
        }
        return functionType;
    }

    private boolean isExpressionEntityProperty(RuleContext context, String propName){
        boolean valid = false;
        if (context.getPrimaryEntity().getPropertyByName(propName) != null){
            valid = true;
        }
        return valid;
    }

    public ReturnValueType functionReturnedValue(FunctionType function, String arg, RuleContext ruleContext){
        switch (function){
            case PERCENT:
                if(isValidPercentFormat(arg, ruleContext)){
                    return ReturnValueType.FLOAT;

                }
                else{
                    throw new IllegalArgumentException("illegal arguments for percent function");
                }
            case RANDOM:
            case TICKS:
                 return ReturnValueType.INTEGER;
            case ENVIRONMENT:
                switch (ruleContext.getEnvironmentDefinition().getProperty(arg).getType()){// get the value type of the environment variable
                    case FLOAT:
                        return ReturnValueType.FLOAT;
                    case BOOLEAN:
                        return ReturnValueType.BOOLEAN;
                    case STRING:
                        return ReturnValueType.STRING;
                    case DECIMAL:
                        return ReturnValueType.INTEGER;
                }
            case EVALUATE:
                return findValueInProperty(arg, ruleContext);
            default:
                return null;
        }
    }

    private ReturnValueType findValueInProperty(String arg, RuleContext ruleContext){
        String[] extractEntNameAndPropName = splitWordsBySeparatorFormat(arg, ".");
        if (extractEntNameAndPropName != null){
            if (ruleContext.getEntityMap().containsKey(extractEntNameAndPropName[0])){
                if (ruleContext.getEntityMap().get(extractEntNameAndPropName[0]).getPropertyByName(extractEntNameAndPropName[1]) != null){
                    PropertyType propertyType = ruleContext.getEntityMap().get(extractEntNameAndPropName[0]).getPropertyByName(extractEntNameAndPropName[1]).getType();
                    switch (propertyType){
                        case FLOAT:
                            return ReturnValueType.FLOAT;
                        case BOOLEAN:
                            return ReturnValueType.BOOLEAN;
                        case STRING:
                            return ReturnValueType.STRING;
                        case DECIMAL:
                            return ReturnValueType.INTEGER;
                    }
                }
            }
        }
        return null;
    }

    public ReturnValueType getType() {
        return type;
    }

    public String getExpression() {
        return expression;
    }
}
