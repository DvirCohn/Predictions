package engine.rule.api;

import engine.action.api.Action;

import java.util.List;

public interface Rule {
    String getName();
    //boolean getActivation();
    boolean isActive(int ticks);
    List<Action>getActionsToPerform();
    void addAction(Action action);
    int getActivationByTicks();
    double getActivationByProbability();
    List<String> getActionsNames();
    float generateRandomProb();
}
