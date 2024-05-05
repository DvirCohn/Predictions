package dto.implement;

import dto.implement.action.AbstractActionDTO;
import dto.implement.action.ActionDTO;
import dto.implement.action.CalculationActionDTO;
import dto.implement.action.condition.MultipleConditionActionDTO;
import dto.implement.action.condition.SingularConditionActionDTO;
import engine.action.api.Action;
import engine.action.condition.ConditionAction.ConditionAction;
import engine.action.condition.api.ConditionType;
import engine.action.entitiesActions.*;
import engine.action.numericAction.Divide;
import engine.action.numericAction.Multiply;
import engine.rule.api.Rule;

import java.util.*;

public class RuleDTO {
    private String name;
    private int activateByTicks;
    private double activateByProbability;
    private int numberOfAction;
    private List<String> actionType;
    private Map<Integer,ActionDTO>actionDTOMap;

    public RuleDTO(String name, int activateByTicks, double activateByProbability, int numberOfAction, List<String> actionType) {
        this.name = name;
        this.activateByTicks = activateByTicks;
        this.activateByProbability = activateByProbability;
        this.numberOfAction = numberOfAction;
        this.actionType = actionType;
        this.actionDTOMap = new LinkedHashMap<>();
    }

    public RuleDTO(Rule rule){
        this(rule.getName(), rule.getActivationByTicks(), rule.getActivationByProbability(),
                rule.getActionsToPerform().size(), rule.getActionsNames());
        setActionDTOList(rule);
    }

    public String getName() {
        return name;
    }

    public int getActivateByTicks() {
        return activateByTicks;
    }

    public double getActivateByProbability() {
        return activateByProbability;
    }

    public int getNumberOfAction() {
        return numberOfAction;
    }

    public List<String> getActionType() {
        return actionType;
    }

    private void setActionDTOList(Rule rule){
        int count = 0;
        for (Action action : rule.getActionsToPerform()){
            switch (action.getActionType()){
                case INCREASE:
                    actionDTOMap.put(new Integer(count), new AbstractActionDTO((IncreaseAction)action));
                    break;
                case DECREASE:
                    actionDTOMap.put(new Integer(count), new AbstractActionDTO((DecreaseAction)action));
                    break;
                case KILL:
                    actionDTOMap.put(new Integer(count), new AbstractActionDTO((KillAction)action));
                    break;
                case SET:
                    actionDTOMap.put(new Integer(count), new AbstractActionDTO((SetAction)action));
                    break;
                case REPLACE:
                    actionDTOMap.put(new Integer(count), new AbstractActionDTO((ReplaceAction) action));
                    break;
                case DIVIDE:
                    actionDTOMap.put(new Integer(count), new CalculationActionDTO((Divide) action));
                    break;
                case MULTIPLY:
                    actionDTOMap.put(new Integer(count), new CalculationActionDTO((Multiply) action));
                    break;
                case PROXIMITY:
                    actionDTOMap.put(new Integer(count), new AbstractActionDTO((ProximityAction)action));
                    break;
                case CONDITION:
                    ConditionAction condAction = (ConditionAction)action;
                    if (((ConditionAction) action).getSecondaryEntityContext().isPresent()){
                        if(condAction.getCondition().getType().equals(ConditionType.MULTIPLE)){
                            actionDTOMap.put(new Integer(count), new MultipleConditionActionDTO(condAction));
                        }
                        else{
                            actionDTOMap.put(new Integer(count), new SingularConditionActionDTO(condAction));
                        }
                    }
                    else {
                        if(condAction.getCondition().getType().equals(ConditionType.MULTIPLE)){
                            actionDTOMap.put(new Integer(count), new MultipleConditionActionDTO(condAction, false));
                        }
                        else{
                            actionDTOMap.put(new Integer(count), new SingularConditionActionDTO(condAction, false));
                        }
                    }

                    break;
                default:
                    break;
            }
            count++;
        }
    }

    public Map<Integer, ActionDTO> getActionDTOMap(){
        return this.actionDTOMap;
    }

    public StringBuffer getActivation(){
        StringBuffer info = new StringBuffer();
        info.append("Activation:"+"\n\n");
        info.append("Active by ticks - "+this.activateByTicks+"\n");
        info.append("Active by probability - "+this.activateByProbability+"\n");
        return info;
    }

}
