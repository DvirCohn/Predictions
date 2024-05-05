package engine.simulator.api;

import dto.implement.AllSimulationDetailsDTO;
import dto.implement.EndOfSimulationDTO;
import dto.implement.InitSimulationDTO;
import dto.implement.WorldInfoDTO;
import engine.world.WorldInstance;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Date;

public interface Simulator {

    // this interface is used as overview to the app methodology.
    // each function has an appopriate method in the excersice document.

    WorldInstance getWorldInstance();

    //Object loadFile(String path) throws FileNotFoundException, JAXBException; // first option

    EndOfSimulationDTO runSimulation();// third simulate

    public void initializeSimulation(InitSimulationDTO dto);

    Pair<String,WorldInstance> getSimulationResult();

    AllSimulationDetailsDTO getSimulationDetailsDTO();

    void setSimulationStatus(SimulationStatus simulationStatus);

    InitSimulationDTO getInitSimulationDTO();
    SimulationStatus getSimulationStatus();

    //optionnnnnn
    void setPaused(boolean paused);

}
