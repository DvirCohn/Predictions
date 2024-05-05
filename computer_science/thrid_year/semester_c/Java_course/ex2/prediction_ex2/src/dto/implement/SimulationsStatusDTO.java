package dto.implement;

import java.util.HashMap;
import java.util.Map;

public class SimulationsStatusDTO {
    private Map<Integer,String> idToStatus;

    public SimulationsStatusDTO() {
        this.idToStatus = new HashMap<>();
    }

    public Map<Integer, String> getIdToStatus() {
        return idToStatus;
    }
}
