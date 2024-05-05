package dto.implement;

import engine.simulator.api.SimulationStatus;

public class SimulationProgressDTO {
    private int currentTick;
    private long timeElapsedInSeconds;
    private SimulationStatus status;

    public SimulationProgressDTO(int currentTick, long timeElapsedInSeconds, SimulationStatus status) {
        this.currentTick = currentTick;
        this.timeElapsedInSeconds = timeElapsedInSeconds;
        this.status = status;
    }
    public SimulationProgressDTO(SimulationStatus status){
        this.status = status;

    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public long getTimeElapsedInSeconds() {
        return timeElapsedInSeconds;
    }

    public void setTimeElapsedInSeconds(long timeElapsedInSeconds) {
        this.timeElapsedInSeconds = timeElapsedInSeconds;
    }

    public SimulationStatus getStatus() {
        return status;
    }

    public void setStatus(SimulationStatus status) {
        this.status = status;
    }
}

