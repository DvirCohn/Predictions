package engine;

import dto.implement.*;
import engine.conventer.xml.loader.Loader;
import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.entity.instance.manager.EntityInstanceManager;
import engine.property.definition.PropertyDefinition;
import engine.simulator.api.SimulationStatus;
import engine.simulator.api.Simulator;
import engine.simulator.impl.SimulatorContainer;
import engine.simulator.impl.SimulatorImplement;
import engine.world.WorldDefinition;
import engine.world.WorldInstance;
import generated.PRDWorld;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EngineImpl implements EngineDefinition {

    private WorldDefinition world;
    //private Map<String, WorldInstance> allSimulationsResult;
    private Map<Integer, Simulator> idToSimulator;
    private ExecutorService threadPool;
    private Integer simulationIdCreator;
    private String path;
    private SimulationsStatusDTO simulationsStatusDTO;
    private WorldInfoDTO worldInfoDTO;

    public EngineImpl(){ }//this.allSimulationsResult = new HashMap<>(); <-- this code was in the c'tor

    @Override
    public Integer initializeSimulation() throws JAXBException, FileNotFoundException {

        try {
            Integer id = createNewSimulation();
            return id;
        }
        catch (FileNotFoundException | JAXBException exception){
            throw exception;
        }
    }

    @Override
    public Integer createNewSimulation() throws FileNotFoundException, JAXBException{
        try {
            this.idToSimulator.put(simulationIdCreator, new SimulatorImplement(path, simulationIdCreator, world));
            simulationIdCreator++;
            return simulationIdCreator - 1;
        }
        catch(FileNotFoundException | JAXBException exception){
            throw exception;
        }
    }


    @Override
    public Boolean executeSimulation(int id, InitSimulationDTO initSimulationDTO) {
        Simulator simulation = idToSimulator.get(id);
        SimulatorContainer executableSimulation = new SimulatorContainer(simulation);
        executableSimulation.getSimulation().initializeSimulation(initSimulationDTO);
        if (simulation != null) {

            threadPool.execute(executableSimulation); // Submit the task to the thread pool

//            allSimulationsResult.put(executableSimulation.getSimulation().getSimulationResult().getKey(),
//                    idToSimulator.get(id).getSimulationResult().getValue());

            return true; // You may return null or another appropriate value here
        } else {
            return false;
        }
    }

    @Override
    public void loadNewSimulation(String path) throws FileNotFoundException, JAXBException{
        this.path = path;
        this.simulationIdCreator = new Integer(0);
        try {
            this.world = new WorldDefinition(loadFile(path));
            this.worldInfoDTO = new WorldInfoDTO(world);
        }
        catch (FileNotFoundException | JAXBException | IllegalArgumentException | NullPointerException exception){
            throw exception;
        }

        this.threadPool = Executors.newFixedThreadPool(world.getThreadsCount());
        this.idToSimulator = new HashMap<>();
        this.simulationsStatusDTO = new SimulationsStatusDTO();

    }

    @Override
    public WorldInfoDTO presentSimulationDetails(){
        return worldInfoDTO;
    }

    @Override
    public EnvironmentDTO getEnvironmentDTO() {
        return presentSimulationDetails().getEnvDTO();
    }

    @Override
    public EndOfSimulationDTO runSimulation(InitSimulationDTO initDTO, int id){
        idToSimulator.get(id).initializeSimulation(initDTO);
        EndOfSimulationDTO dto = idToSimulator.get(id).runSimulation();
//        allSimulationsResult.put(idToSimulator.get(id).getSimulationResult().getKey(),
//                idToSimulator.get(id).getSimulationResult().getValue());
        return dto;
    }

    @Override
    public void updateEnvironment(PropertyDTO propertyDTO, String propertyName, int id) {
        idToSimulator.get(id).getWorldInstance().updateEnvironmentProperties(propertyDTO, propertyName);
    }

//    @Override
//    public Map<String, WorldInstance> getAllSimulationsResult() {
//        if (allSimulationsResult.size() == 0){
//            throw new NoSuchElementException("there is no past simulations");
//        }
//        return allSimulationsResult;
//    }

//    @Override
//    public List<SimulationResultByEntityDTO> showSimulationResultByEntities(String dateKey) {
//
//        String entityName;
//        int startPopulation = 0, endPopulation; // TODO: 17-Sep-23  change the value odf start population from 0 (just for start debbuging)
//        List<SimulationResultByEntityDTO> result = new ArrayList<>();
//
//        WorldInstance wInstance = this.allSimulationsResult.get(dateKey);
//        for (String name : wInstance.getEntityInstanceManagers().keySet()) {
//            entityName = name;
//            //startPopulation = wInstance.getEntityInstanceByName(name).getEntityDefinition().getPopulation(); todo remove the // from this line
//            endPopulation = wInstance.getEntityInstanceManagers().get(name).getInstances().size();
//            result.add(new SimulationResultByEntityDTO(startPopulation, endPopulation, entityName));
//        }
//
//
//        return result;
//    }

//    @Override
//    public HistogramDTO getPropertyHistogram(String entityName,String propertyName,String date) throws NoSuchElementException{
//
//        if(allSimulationsResult.size() == 0){
//            throw new NoSuchElementException("there is no past simulations");
//        }
//       WorldInstance instance = allSimulationsResult.get(date);
//       EntityInstanceManager entInstanceManager = instance.getEntityInstanceManagers().get(entityName);
//       Map<String,Integer> amountOfInstancesForProperty = new HashMap<>();
//
//        for(EntityInstance entity : entInstanceManager.getInstances()){
//            if(entity.getPropertyByName(propertyName) != null){
//                Object value = entity.getPropertyByName(propertyName).getValue();
//                if(!amountOfInstancesForProperty.containsKey(value.toString())){
//                    amountOfInstancesForProperty.put(value.toString(),1);
//                }
//                else{
//                    Integer count = amountOfInstancesForProperty.get(value.toString());
//                    count ++;
//                    amountOfInstancesForProperty.put(value.toString(),count);
//                }
//            }
//        }
//
//        return new HistogramDTO(amountOfInstancesForProperty);
//    }
//    @Override
//    public List<PropertyDTO> getPropertyDTOListByWorld(String date){
//
//        List<PropertyDTO> propertyDTOS = new ArrayList<>();
//        for (EntityDefinition entDef: allSimulationsResult.get(date).getWorldDefinition().getEntityDefinition()) {
//            for (PropertyDefinition propDef: entDef.getProps()) {
//                PropertyDTO propDTO = new PropertyDTO(propDef);
//            }
//        }
//
//        return propertyDTOS;
//    }

//    @Override
//    public List<EntityDTO> getListOfEntities(String date){
//        List<EntityDTO> entityDTOList = new ArrayList<>();
//        for (EntityDefinition entDef: allSimulationsResult.get(date).getWorldDefinition().getEntityDefinition()) {
//            EntityDTO entDTO = new EntityDTO(entDef);
//            entityDTOList.add(entDTO);
//        }
//        return entityDTOList;
//    }

    public void updateSimulationStatusDTO(int id, SimulationStatus status){
        simulationsStatusDTO.getIdToStatus().put(id,status.toString());
    }

    @Override
    public Simulator getSimulatorById(int id){
        if (idToSimulator.containsKey(id)){
            return idToSimulator.get(id);
        }
        else {
            return null;
        }
    }

    @Override
    public AllSimulationDetailsDTO getSimulatorAllDataDTO(int id){
        if (idToSimulator.containsKey(id)){
            return idToSimulator.get(id).getSimulationDetailsDTO();
        }
        else {
            return null;
        }
    }

    @Override
    public PRDWorld loadFile(String path) throws FileNotFoundException, JAXBException {
        Loader loader = new Loader(path);
        try {
            PRDWorld prdWorld = loader.setLoadWorld();
            return prdWorld;
        }
        catch (FileNotFoundException | JAXBException e){
            throw e;
        }
    }

    @Override
    public int countRunningSimulations(){
        int count = 0;
        for (Simulator simulation :idToSimulator.values()){
            if (simulation.getSimulationStatus() == SimulationStatus.RUN){
                count++;
            }
        }
        return count;
    }

    @Override
    public int countEndedSimulations(){
        int count = 0;
        for (Simulator simulation :idToSimulator.values()){
            if (simulation.getSimulationStatus() == SimulationStatus.ENDED){
                count++;
            }
        }
        return count;
    }

    @Override
    public int countWaitingSimulations(){
        int count = 0;
        for (Simulator simulation :idToSimulator.values()){
            if (simulation.getSimulationStatus() == SimulationStatus.WAITING){
                count++;
            }
        }
        return count;
    }

}
