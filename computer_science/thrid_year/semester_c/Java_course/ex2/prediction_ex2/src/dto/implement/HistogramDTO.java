package dto.implement;

import java.util.Map;

public class HistogramDTO {
    private Map<String, Integer> PropertyNameToAmount;

    public HistogramDTO(Map<String, Integer> propertyNameToAmount) {
        PropertyNameToAmount = propertyNameToAmount;
    }

    public Map<String, Integer> getPropertyNameToAmount() {
        return PropertyNameToAmount;
    }
}
