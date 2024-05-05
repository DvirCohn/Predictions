package engine.property.definition;

public interface Rangeable {
    boolean isInRange(Number value);
    Number generateRandomNumberInRange();
    String getRange();
}
