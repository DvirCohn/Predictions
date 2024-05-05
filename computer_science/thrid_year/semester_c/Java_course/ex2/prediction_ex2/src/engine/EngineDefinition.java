package engine;

import dto.implement.*;
import engine.simulator.api.Simulator;
import engine.world.WorldDefinition;
import engine.world.WorldInstance;
import generated.PRDWorld;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface EngineDefinition {

    void loadNewSimulation(String path) throws FileNotFoundException, JAXBException;

    WorldInfoDTO presentSimulationDetails();

    EnvironmentDTO getEnvironmentDTO();

    void updateEnvironment(PropertyDTO propertyDTO, String propertyName, int id);

    EndOfSimulationDTO runSimulation(InitSimulationDTO initDTO, int id);

    //Map<String, WorldInstance> getAllSimulationsResult();

    //List<SimulationResultByEntityDTO> showSimulationResultByEntities(String dateKey);

    //List<PropertyDTO> getPropertyDTOListByWorld(String date);

    //List<EntityDTO> getListOfEntities(String date);

    //HistogramDTO getPropertyHistogram(String entityName,String propertyName,String date);

    Boolean executeSimulation(int id, InitSimulationDTO initSimulationDTO);

    Integer createNewSimulation() throws FileNotFoundException, JAXBException;

    Integer initializeSimulation() throws JAXBException, FileNotFoundException;

    Simulator getSimulatorById(int id);

    AllSimulationDetailsDTO getSimulatorAllDataDTO(int id);

    PRDWorld loadFile(String path) throws FileNotFoundException, JAXBException;

    int countWaitingSimulations();

    int countEndedSimulations();

    int countRunningSimulations();

}
