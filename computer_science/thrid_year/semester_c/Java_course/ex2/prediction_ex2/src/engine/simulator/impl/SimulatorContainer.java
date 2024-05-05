package engine.simulator.impl;

import dto.implement.EndOfSimulationDTO;
import engine.simulator.api.Simulator;

import java.util.concurrent.Callable;

public class SimulatorContainer implements Runnable, Callable {
    Simulator simulation;
    EndOfSimulationDTO endOfSimulationDTO;

    public SimulatorContainer(Simulator simulation) {
        this.simulation = simulation;
    }

    @Override
    public void run() {
        setEndOfSimulationDTO(simulation.runSimulation());
    }

    public void setEndOfSimulationDTO(EndOfSimulationDTO endOfSimulationDTO) {
        this.endOfSimulationDTO = endOfSimulationDTO;
    }

    public EndOfSimulationDTO getEndOfSimulationDTO() {
        return endOfSimulationDTO;
    }

    public Simulator getSimulation() {
        return simulation;
    }

    @Override
    public Object call() throws Exception {
        setEndOfSimulationDTO(simulation.runSimulation());
        return null;
    }
}
