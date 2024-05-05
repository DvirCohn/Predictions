package engine.rule.api;

public interface Activation {
    boolean isActive(int ticks);

    public boolean isActiveByProbability();
}
