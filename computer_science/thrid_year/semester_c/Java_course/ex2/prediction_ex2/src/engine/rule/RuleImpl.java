package engine.rule;

import engine.action.api.Action;
import engine.conventer.Converter;
import engine.entity.definition.EntityDefinition;
import engine.environment.definition.EnvVariablesManager;
import engine.rule.api.Activation;
import engine.rule.api.Rule;
import generated.*;

import java.util.ArrayList;
import java.util.List;

public class RuleImpl implements Rule, Activation {

    private final String name;
    private final List<Action> actions;
    private double probability = 1;
    private int activeByThisManyTicks = 1;

    public RuleImpl(String name) {
        this.name = name;
        actions = new ArrayList<>();
    }

    public RuleImpl(PRDRule prdRule, List<EntityDefinition> entityDefinitions, EnvVariablesManager envVariablesManager){
        this.name = prdRule.getName();
        actions = getActionListFromPRDActions(prdRule.getPRDActions(), entityDefinitions, envVariablesManager);
        if (prdRule.getPRDActivation() != null){
            if (prdRule.getPRDActivation().getProbability() != null){
                probability = prdRule.getPRDActivation().getProbability();
            }
            if (prdRule.getPRDActivation().getTicks() != null){
                this.activeByThisManyTicks = prdRule.getPRDActivation().getTicks();
            }
        }
    }

    private List<Action> getActionListFromPRDActions(PRDActions prdActions, List<EntityDefinition> entityDefinitions, EnvVariablesManager envDef) {
        List<Action> actions = new ArrayList<>();
        for (PRDAction generatedAction : prdActions.getPRDAction()) {
            Action action = Converter.buildAction(generatedAction,entityDefinitions, envDef);
            actions.add(action);
        }
        return actions;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isActive(int ticks) {
        return (ticks % activeByThisManyTicks == 0 && isActiveByProbability());
    }

    @Override
    public List<Action> getActionsToPerform() {
        return actions;
    }

    @Override
    public void addAction(Action action) {
        actions.add(action);
    }

    @Override
    public int getActivationByTicks(){
        return activeByThisManyTicks;
    }


    @Override
    public double getActivationByProbability(){
        return this.probability;
    }

    @Override
    public List<String> getActionsNames(){
        List<String> names = new ArrayList<>();
        for (Action action: actions) {
            names.add(action.getActionType().toString());
        }
        return names;
    }

    public void setProbability(float prob){
        if(prob >= 0 && prob <= 1 ){
            probability = prob;
        }
    }
    @Override
    public float generateRandomProb(){
        return (float) (Math.random() * (1.0 - 0.0) + 0.0);
    }

    @Override
    public boolean isActiveByProbability(){
        float prob = generateRandomProb();
        if(prob < probability) {
            return true;
        }
        return false;
    }


}
