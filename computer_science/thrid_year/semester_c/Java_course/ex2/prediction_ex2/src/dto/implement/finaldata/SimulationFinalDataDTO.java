package dto.implement.finaldata;

import dto.implement.HistogramDTO;
import engine.entity.instance.manager.EntityInstanceManager;

import java.util.List;
import java.util.Map;

public class SimulationFinalDataDTO {
    private Map<String, List<Integer>> nameToPopulationPerTick;
    private Map<String,Map<String,HistogramDTO>> entNameToPropertyHistogramDTO;
    Map<String,Map<String,FinalValue>> entitiesPropertiesToAverageValue;

    public SimulationFinalDataDTO(Map<String, List<Integer>> nameToPopulationPerTick, Map<String,Map<String,HistogramDTO>> histogramDTO, Map<String,Map<String,FinalValue>> averages) {
        this.nameToPopulationPerTick = nameToPopulationPerTick;
        this.entNameToPropertyHistogramDTO = histogramDTO;
        this.entitiesPropertiesToAverageValue = averages;
    }

}
