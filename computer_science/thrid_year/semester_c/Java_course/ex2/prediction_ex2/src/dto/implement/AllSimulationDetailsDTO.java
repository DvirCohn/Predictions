package dto.implement;

import dto.implement.finaldata.SimulationFinalDataDTO;
import engine.simulator.api.SimulationStatus;

public class AllSimulationDetailsDTO {

    private SimulationFinalDataDTO endedSimulationInformation;
    private SimulationProgressDTO simulationProgressDTO;
    private SimulationEntityStatusDTO entityStatusDTO;
    private SimulationStatus status;
    private Integer id;


    public AllSimulationDetailsDTO(SimulationFinalDataDTO endedSimulationInformation, SimulationProgressDTO simulationProgressDTO, SimulationEntityStatusDTO entityStatusDTO, int id) {
        this.endedSimulationInformation = endedSimulationInformation;
        this.simulationProgressDTO = simulationProgressDTO;
        this.status = simulationProgressDTO.getStatus();
        this.entityStatusDTO = entityStatusDTO;
        this.id = id;

    }


    public void setStatus(SimulationStatus status) {
        this.status = status;
    }

    public SimulationEntityStatusDTO getEntityStatusDTO() {
        return entityStatusDTO;
    }

    public SimulationFinalDataDTO getEndedSimulationInformation() {
        return endedSimulationInformation;
    }

    public SimulationProgressDTO getSimulationProgressDTO() {
        return simulationProgressDTO;
    }

    public void setEndedSimulationInformation(SimulationFinalDataDTO endedSimulationInformation) {
        this.endedSimulationInformation = endedSimulationInformation;
    }

    public void setEntityStatusDTO(SimulationEntityStatusDTO entityStatusDTO) {
        this.entityStatusDTO = entityStatusDTO;
    }

    public void setSimulationProgressDTO(SimulationProgressDTO simulationProgressDTO) {
        this.simulationProgressDTO = simulationProgressDTO;
    }

    public SimulationStatus getStatus() {
        return this.status;
    }

    public Integer getId() {
        return this.id;
    }
}
