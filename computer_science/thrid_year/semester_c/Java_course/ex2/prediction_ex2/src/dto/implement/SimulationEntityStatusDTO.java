package dto.implement;

import java.util.HashMap;
import java.util.Map;

public class SimulationEntityStatusDTO {
    private Map<String, Integer> entNameToPopulation;

    public SimulationEntityStatusDTO() {
        this.entNameToPopulation = new HashMap<>();
    }

    public Map<String, Integer> getEntNameToPopulation() {
        return entNameToPopulation;
    }

}
