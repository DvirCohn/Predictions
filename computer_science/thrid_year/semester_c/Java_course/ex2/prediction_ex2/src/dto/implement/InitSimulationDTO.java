package dto.implement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitSimulationDTO {

    private Map<String, Integer> entNameToPopulation;
    private Map<String, PropertyDTO> envNameToPropertyDTO;

    public InitSimulationDTO(Map<String, Integer> entNameToPopulation, Map<String, PropertyDTO> envNameToPropertyDTO) {
        this.entNameToPopulation = entNameToPopulation;
        this.envNameToPropertyDTO = envNameToPropertyDTO;
    }

    public InitSimulationDTO(){}

    public Map<String, PropertyDTO> getEnvNameToPropertyDTO() {
        return envNameToPropertyDTO;
    }

    public Map<String, Integer> getEntNameToPopulation() {
        return entNameToPopulation;
    }
}
