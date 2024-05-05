package engine.conventer;

import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.action.condition.ConditionAction.ActionsToDo;
import engine.action.condition.ConditionAction.ConditionAction;
import engine.action.condition.api.Condition;
import engine.action.condition.api.ConditionType;
import engine.action.condition.api.MultipleCondition;
import engine.action.condition.api.SingleCondition;
import engine.action.entitiesActions.*;
import engine.action.numericAction.Divide;
import engine.action.numericAction.Multiply;
import engine.context.build.rules.RuleContext;
import engine.context.build.rules.SecondaryEntityContext;
import engine.entity.definition.EntityDefinition;
import engine.environment.definition.EnvVariablesManager;
import engine.expression.Expression;
import engine.expression.ReturnValueType;
import engine.property.definition.PropertyDefinition;
import engine.property.definition.PropertyType;
import engine.property.definition.generator.defenition.ValueGenerator;
import engine.property.definition.generator.fixed.FixedValueGenerator;
import engine.property.definition.generator.random.impl.bool.RandomBooleanValueGenerator;
import engine.property.definition.generator.random.impl.numeric.RandomFloatGenerator;
import engine.property.definition.generator.random.impl.numeric.RandomIntegerGenerator;
import engine.property.definition.generator.random.impl.string.RandomStringValueGenerator;
import engine.property.definition.type.BooleanProperty;
import engine.property.definition.type.FloatProperty;
import engine.property.definition.type.IntegerProperty;
import engine.property.definition.type.StringProperty;
import generated.*;
import engine.conventer.xml.loader.Loader;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Converter {

    private Loader loader;
    private PRDWorld generatedWorld;

    public Converter(){}

    public static List<PropertyDefinition> buildPropertiesFromPRDProperties(PRDProperties generatedProperties){
        List<PropertyDefinition> propertyDefinitions = new ArrayList<>();
        for (PRDProperty generatedProp: generatedProperties.getPRDProperty()) {
            if(Converter.checkPropertyTypeName(generatedProp.getType()) ){

                if(checkValidPropertiesByNames(generatedProp.getPRDName(),propertyDefinitions)) {
                    ValueGenerator<?> valueGenerator = getValueGeneratorByType(generatedProp);
                    PropertyDefinition propertyDefinition =
                            generatePropertyFromPRDPropertyAndValueGenerator(generatedProp, valueGenerator);
                    propertyDefinitions.add(propertyDefinition);
                }
                else{
                    //throw exception that this entity has separate properties with the same name
                }
            }
            else {
                // throw some exception that is not valid property type;
            }

        }
        return propertyDefinitions;
    }

    public static Boolean checkValidPropertiesByNames(String propName,List<PropertyDefinition> props){

        Boolean res = true;
        if(props.isEmpty()){
            return res;
        }
        for(PropertyDefinition propDef:props){
            if(propName.equals(propDef.getName())){
                res = false;
            }
        }
        return res;

    }

    private static PropertyDefinition generatePropertyFromPRDPropertyAndValueGenerator(PRDProperty generatedProp, ValueGenerator<?> valueGenerator) {
        switch (generatedProp.getType().toLowerCase()) {
            case "decimal":
                return new IntegerProperty(generatedProp ,valueGenerator);
            case "float":
                return new FloatProperty(generatedProp, valueGenerator);
            case "boolean":
                return new BooleanProperty(generatedProp, valueGenerator);
            case "string":
                return new StringProperty(generatedProp, valueGenerator);
            default:
                return null;
        }
    }

    private static ValueGenerator<?> getValueGeneratorByType(PRDProperty generatedProp) {
        if (generatedProp.getPRDValue().isRandomInitialize()) {
            return getRandomValueGeneratorByType(generatedProp);
        }
        else {
            return getFixedValueGeneratorByType(generatedProp);
        }
    }

    private static ValueGenerator<?> getFixedValueGeneratorByType(PRDProperty generatedProp) {

        switch (generatedProp.getType().toLowerCase()) {
            case "decimal":
                return new FixedValueGenerator<Integer>
                        (Integer.parseInt(generatedProp.getPRDValue().getInit()));
            case "float":
                return new FixedValueGenerator<Float>
                        (Float.parseFloat(generatedProp.getPRDValue().getInit()));
            case "boolean":
                return new FixedValueGenerator<Boolean>
                        (Boolean.getBoolean(generatedProp.getPRDValue().getInit()));
            case "string":
                return new FixedValueGenerator<String>
                        (generatedProp.getPRDValue().getInit());
            default:
                return null;
        }
    }

    private static ValueGenerator<?> getRandomValueGeneratorByType(PRDProperty generatedProp) {
        switch(generatedProp.getType().toLowerCase()){
            case "float":
                return new RandomFloatGenerator((float) generatedProp.getPRDRange().getFrom(),
                        (float) generatedProp.getPRDRange().getTo());
            case "decimal":
                return new RandomIntegerGenerator((int) generatedProp.getPRDRange().getFrom(),
                        (int) generatedProp.getPRDRange().getTo());
            case "boolean":
                return new RandomBooleanValueGenerator();
            case "string":
                return new RandomStringValueGenerator();
            default:
                return null;
        }
    }

    public static boolean checkPropertyTypeName(String type){
        boolean result = false;
        for (PropertyType propType: PropertyType.values()) {
            if (propType.toString().toUpperCase().equals(type.toUpperCase())){
                result = true;
            }
        }
        return result;
    }

    //TODO think how to build the proximity action (the list of its own actions)

    public static Action buildAction(PRDAction prdAction, List<EntityDefinition> entities, EnvVariablesManager envDef) throws NoSuchElementException{

        String type = prdAction.getType();
        String entityType = prdAction.getEntity();
        String property;
        String byExpression1;
        String byExpression2;
        String secondaryEntityName = null;
        boolean isSecondaryEntityExist = false;
        EntityDefinition primaryEntity;
        RuleContext ruleContext;
        SecondaryEntityContext secondaryEntityContext;

        if (type.equals(ActionType.PROXIMITY.toString().toLowerCase())){
            ruleContext = new RuleContext(envDef,findEntityInList(prdAction.getPRDBetween().getSourceEntity(), entities));
        }
        else if (type.equals(ActionType.REPLACE.toString().toLowerCase())){
            ruleContext = new RuleContext(envDef,findEntityInList(prdAction.getKill(), entities));
        }
        else {
            ruleContext = new RuleContext(envDef,findEntityInList(entityType, entities));
        }
        ruleContext.initMap(entities);

        if (type.toLowerCase().equals(ActionType.REPLACE.toString().toLowerCase())) {
            secondaryEntityContext = new SecondaryEntityContext(prdAction, ruleContext, findEntityInList(prdAction.getCreate(), entities));
        }
        else if(prdAction.getPRDSecondaryEntity() != null){
            secondaryEntityName = prdAction.getPRDSecondaryEntity().getEntity();
            if(!checkExistEntity(secondaryEntityName,entities)){
                throw new NoSuchElementException("Can't active an action on an entity that does not exist in the system");
            }
            isSecondaryEntityExist = true;
            secondaryEntityContext = new SecondaryEntityContext(prdAction, ruleContext, findEntityInList(secondaryEntityName, entities));

        }
        else {
            secondaryEntityContext = new SecondaryEntityContext();
        }

        if (type.equals(ActionType.PROXIMITY.toString().toLowerCase())) {
            try{
                if (isSecondaryEntityExist){
                    return new ProximityAction(getEntityDefinition(prdAction.getPRDBetween().getSourceEntity(), entities), prdAction.getPRDEnvDepth().getOf(), prdAction.getPRDBetween().getSourceEntity(), prdAction.getPRDBetween().getTargetEntity(), prdAction.getPRDActions(), entities, envDef, secondaryEntityContext,ruleContext);
                }
                else {
                    return new ProximityAction(getEntityDefinition(prdAction.getPRDBetween().getSourceEntity(), entities), prdAction.getPRDEnvDepth().getOf(), prdAction.getPRDBetween().getSourceEntity(), prdAction.getPRDBetween().getTargetEntity(), prdAction.getPRDActions(), entities, envDef,ruleContext);
                }

            }
            catch (Exception ex){
                ex.printStackTrace(System.out);
            }

        }
        else if (type.equals(ActionType.REPLACE.toString().toLowerCase())) {
            try {
                return new ReplaceAction(getEntityDefinition(prdAction.getKill(), entities), prdAction, secondaryEntityContext);

            } catch (IllegalArgumentException | ClassCastException e) {
                throw e;
            }
        }


        if(!checkExistEntity(entityType,entities)){
           throw new NoSuchElementException("Can't active an action on an entity that does not exist in the system");
        }



        if(type.equals(ActionType.INCREASE.toString().toLowerCase())){
            property = prdAction.getProperty();
            byExpression1 = prdAction.getBy();

            if(!existPropertyInEntity(property,findEntityInList(entityType, entities))){
                throw new NoSuchElementException("Can't active an action on an entity that does not contain action property");
            }
            if(!Expression.checkNumericArg(byExpression1,ruleContext)){
                throw new NumberFormatException("Can't convert action argument to a number");
            }

            if (isSecondaryEntityExist){
                return new IncreaseAction(getEntityDefinition(entityType,entities),property,byExpression1, secondaryEntityContext);
            }
            else {
                return new IncreaseAction(getEntityDefinition(entityType,entities),property,byExpression1);
            }

        }
        else if (type.equals(ActionType.DECREASE.toString().toLowerCase())) {
            property = prdAction.getProperty();
            byExpression1 = prdAction.getBy();
            if(!existPropertyInEntity(property,findEntityInList(entityType, entities))){
                throw new NoSuchElementException("Can't active an action on an entity that does not contain action property");
            }
            if(!Expression.checkNumericArg(byExpression1,ruleContext)){
                throw new NumberFormatException("Can't convert action argument to a number");
            }
            if(isSecondaryEntityExist){
                return new DecreaseAction(getEntityDefinition(entityType,entities),property,byExpression1, secondaryEntityContext);
            }
            else{
                return new DecreaseAction(getEntityDefinition(entityType,entities),property,byExpression1);
            }

        }
        else if (type.equals(ActionType.SET.toString().toLowerCase())){
            property = prdAction.getProperty();
            byExpression1 = prdAction.getValue();
            if(!existPropertyInEntity(property,findEntityInList(entityType, entities))){
                throw new NoSuchElementException("Can't active an action on an entity that does not contain action property");
            }

            if (isSecondaryEntityExist){
                return new SetAction(getEntityDefinition(entityType,entities),property,byExpression1, secondaryEntityContext);
            }
            else{
                return new SetAction(getEntityDefinition(entityType,entities),property,byExpression1);
            }

        }
        else if (type.equals(ActionType.KILL.toString().toLowerCase())) {
            if (isSecondaryEntityExist){
                return new KillAction(getEntityDefinition(entityType,entities),entityType, secondaryEntityContext);
            }
            else{
                return new KillAction(getEntityDefinition(entityType,entities),entityType);
            }
        }
        else if (type.equals(ActionType.CALCULATION.toString().toLowerCase())) {
            property = prdAction.getResultProp();
            PRDDivide prdDivide = prdAction.getPRDDivide();
            PRDMultiply prdMultiply = prdAction.getPRDMultiply();
            if(!existPropertyInEntity(property,findEntityInList(entityType, entities))){
                throw new NoSuchElementException("Can't active an action on an entity that does not contain action property");
            }

            if(prdDivide != null){
                byExpression1 = prdDivide.getArg1();
                byExpression2 = prdDivide.getArg2();
                if(!Expression.checkNumericArg(byExpression1,ruleContext)){
                    throw new NumberFormatException("Can't convert action argument to a number");
                }
                if(!Expression.checkNumericArg(byExpression2,ruleContext)){
                    throw new NumberFormatException("Can't convert action argument to a number");
                }

                if (isSecondaryEntityExist){
                    return new Divide(getEntityDefinition(entityType,entities),property,byExpression1,byExpression2, secondaryEntityContext);
                }
                else{
                    return new Divide(getEntityDefinition(entityType,entities),property,byExpression1,byExpression2);
                }

            }
            if(prdMultiply != null){
                byExpression1 = prdMultiply.getArg1();
                byExpression2 = prdMultiply.getArg2();
                if(!Expression.checkNumericArg(byExpression1,ruleContext)){
                    throw new NumberFormatException("Can't convert action argument to a number");
                }
                if(!Expression.checkNumericArg(byExpression2, ruleContext)){
                    throw new NumberFormatException("Can't convert action argument to a number");
                }

                if (isSecondaryEntityExist){
                    return new Multiply(getEntityDefinition(entityType,entities),property,byExpression1,byExpression2, secondaryEntityContext);
                }
                else{
                    return new Multiply(getEntityDefinition(entityType,entities),property,byExpression1,byExpression2);
                }

            }

        }
        else if (type.equals(ActionType.CONDITION.toString().toLowerCase())) {
            if (isSecondaryEntityExist){
                return new ConditionAction(getEntityDefinition(entityType,entities),conditionConverter(prdAction.getPRDCondition(), ruleContext),createActions4Condition(prdAction,entities,envDef),secondaryEntityContext);
            }
            else{
                return new ConditionAction(getEntityDefinition(entityType,entities),conditionConverter(prdAction.getPRDCondition(), ruleContext),createActions4Condition(prdAction,entities,envDef));
            }


        }
        // TODO: 9/2/2023  if this is not needed for condition than throw execption of invalid action type in XML! 
        return null;// maybe****
    }



    public static Boolean checkValidPropertyActionInEntity(String property, String entityType, List<EntityDefinition> entities){
        Boolean res = false;
        if(checkExistEntity(entityType,entities)){
            for(EntityDefinition entityDefinition:entities){
                if (entityDefinition.getName().equals(entityType)){
                    if(existPropertyInEntity(property,entityDefinition)){
                        res = true;
                    }

                }

            }

        }

        return  res;
    }

    public static Boolean existPropertyInEntity(String propertyName, EntityDefinition entityDefinition){
        Boolean res = false;
        for(PropertyDefinition propDef:entityDefinition.getProps()){
            if(propertyName.equals(propDef.getName())){
                res = true;
            }
        }
        return res;
    }

    public static EntityDefinition getEntityDefinition(String entityType, List<EntityDefinition> entities){
        for (EntityDefinition entityDefinition :entities){
            if(entityType.equals(entityDefinition.getName())){
                return entityDefinition;
            }
        }
        return  null;
    }


    public static boolean checkExistEntity(String entityType, List<EntityDefinition> entities){
        for (EntityDefinition entityDefinition :entities){
            if(entityType.equals(entityDefinition.getName())){
                return true;
            }
        }
        return  false;
    }

    public static Condition conditionConverter(PRDCondition prdCondition, RuleContext ruleContext){
        String condType = prdCondition.getSingularity();
        if(condType.equals(ConditionType.SINGLE.toString().toLowerCase())){
            Expression propExpression = new Expression(prdCondition.getProperty());
            Expression valueExpression = new Expression(prdCondition.getValue());
            //String entityType= prdCondition.getEntity();
            propExpression.updateTypeFromExpression(ruleContext);
            valueExpression.updateTypeFromExpression(ruleContext);
            if (isComparableExpressions(propExpression, valueExpression, prdCondition.getOperator())) {
                return new SingleCondition(prdCondition.getOperator(), valueExpression,propExpression, prdCondition.getEntity());
            }
            else {
                throw new IllegalArgumentException("incomparable types in condition");
            }
//            if(existPropertyInEntity(prdCondition.getProperty(),entityDefinition)){
//                //throw new NoSuchElementException("Can't active an action on an entity that does not contain action property");
//                return new SingleCondition(prdCondition.getOperator(),prdCondition.getValue(),prdCondition.getEntity(), prdCondition.getProperty());
//            }
//            else{
//                Expression expression = new Expression(prdCondition.getProperty());
//                return new SingleCondition()
//
//            }

        }
        else {
            List<Condition> subConditions = new ArrayList<>();
            for(PRDCondition prdCond:prdCondition.getPRDCondition()){
                subConditions.add(conditionConverter(prdCond,ruleContext));
            }
            return new MultipleCondition(subConditions,prdCondition.getLogical());

        }
    }

    public static List<ActionsToDo> createActions4Condition(PRDAction prdAction, List<EntityDefinition>entities,EnvVariablesManager envDef){
        PRDThen prdThen = prdAction.getPRDThen();
        List<Action> thenActions = new ArrayList<>();
        List<ActionsToDo> toDo = new ArrayList<>();
        for(PRDAction prdAct:prdThen.getPRDAction()){
            thenActions.add(buildAction(prdAct,entities,envDef));
        }
        ActionsToDo then = new ActionsToDo(thenActions);
        toDo.add(then);
        PRDElse prdElse = prdAction.getPRDElse();
        if(prdElse != null){
            List<Action> elseActions = new ArrayList<>();
            for(PRDAction prdAct : prdElse.getPRDAction()){
                elseActions.add(buildAction(prdAct,entities,envDef));
            }
            ActionsToDo Else = new ActionsToDo(elseActions);

            toDo.add(Else);
        }
        return toDo;

    }

    public static EntityDefinition findEntityInList(String name, List<EntityDefinition> entityDefinitionList){
        for (EntityDefinition entDef: entityDefinitionList) {
            if (name.equals(entDef.getName())){
                return entDef;
            }
        }
        return null;
    }

    public static boolean isComparableExpressions(Expression expression1, Expression expression2, String operator) {
        boolean isComparable = false;

        if (operator.toLowerCase().equals("bt") || operator.toLowerCase().equals("lt")) {
            if ((expression1.getType().equals(ReturnValueType.FLOAT)  || expression1.getType().equals(ReturnValueType.INTEGER))
                    && (expression2.getType().equals(ReturnValueType.FLOAT) || expression2.getType().equals(ReturnValueType.INTEGER))) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (expression1.getType().equals(expression2.getType())){
                return true;
            }
            else {
                return false;
            }
        }

    }
}
