package engine.simulator.impl;

import dto.implement.*;
import dto.implement.finaldata.FinalValue;
import dto.implement.finaldata.SimulationFinalDataDTO;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.context.ContextImpl;
import engine.context.build.rules.SecondaryEntityContext;
import engine.context.build.rules.SelectionType;
import engine.conventer.xml.loader.Loader;
import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.entity.instance.manager.EntityInstanceManager;
import engine.entity.instance.manager.EntityInstanceManagerImpl;
import engine.environment.instance.ActiveEnvironment;
import engine.property.definition.PropertyDefinition;
import engine.property.definition.PropertyType;
import engine.property.instance.PropertyInstance;
import engine.rule.api.Rule;
import engine.simulator.api.SimulationStatus;
import engine.simulator.api.Simulator;
import engine.world.WorldDefinition;
import engine.world.WorldInstance;
import generated.PRDWorld;
import javafx.beans.property.Property;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.*;


public class SimulatorImplement implements Simulator {

    private WorldDefinition worldDefinition;
    private WorldInstance worldInstance;
    private Pair<String,WorldInstance> simulationResult;
    private Map<String, EntityInstanceManager> entitiesManager;
    private SimulationStatus simulationStatus =SimulationStatus.WAITING;
    private Map<String,List<Integer>> entNameToEntityPopulationInEachTick;
    private AllSimulationDetailsDTO simulationDetailsDTO;
    private InitSimulationDTO initSimulationDTO;
    private int id;
    private int tick = 0;

    private volatile boolean paused = false;

    public SimulatorImplement(String path, int id, WorldDefinition world) throws FileNotFoundException, JAXBException{

        this.worldDefinition = world;
        this.worldInstance = new WorldInstance(worldDefinition);
        this.entitiesManager = new HashMap<>();
        this.simulationDetailsDTO = new AllSimulationDetailsDTO(null, new SimulationProgressDTO(simulationStatus), new SimulationEntityStatusDTO(), id);
        this.entNameToEntityPopulationInEachTick = new HashMap<>();
        this.id = id;
    }

    public void setWorldInstance(WorldInstance worldInstance) {
        this.worldInstance = worldInstance;
    }

    @Override
    public WorldInstance getWorldInstance() {
        return worldInstance;
    }

    @Override
    public Pair<String,WorldInstance> getSimulationResult() {
        return this.simulationResult;
    }

    @Override public void initializeSimulation(InitSimulationDTO dto){
        this.initSimulationDTO = dto;
        this.initSimulationEnv(dto);
        this.initSimulationPopulation(dto);
    }

    @Override
    public EndOfSimulationDTO runSimulation() {
        setSimulationStatus(SimulationStatus.RUN);
        initMap();
        initNameToPopulation();
        Context context;
        int countException = 0;
        worldInstance.getWorldDefinition().getTermination().setEndTime();

        long simulationStartTimeMillis = System.currentTimeMillis();
        List<Rule> activeRules;
        List<Action> actionList;
        List<Action> killAndReplaceList;
        while(!worldInstance.getWorldDefinition().getTermination().isSimulationOver(tick)
                && simulationStatus != SimulationStatus.STOP &&simulationStatus != SimulationStatus.ENDED){

           // while(getSimulationStatus() == (SimulationStatus.PAUSE)){}
            if (paused) {
                try {
                    Thread.sleep(100); // Sleep briefly while paused to reduce CPU usage
                    if (getSimulationStatus() == SimulationStatus.ENDED){
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                }
                continue; // Continue to check the paused flag
            }
            updateNameToPopulation();
            moveAllEntities();
            activeRules = new ArrayList<>();
            actionList = new ArrayList<>();
            for (Rule rule:worldInstance.getRules()){
                if(rule.isActive(tick)){
                    activeRules.add(rule);
                    for(Action action : rule.getActionsToPerform()){
                        actionList.add(action);
                    }
                }
            }

            List<EntityInstance> secondaryEntities;
            for (EntityInstanceManager entityManager :entitiesManager.values()){
                for(EntityInstance entityInstance: entityManager.getInstances()){
                    for(Action action : actionList){
                        if(entityInstance.getEntityDefinition().getName().equals(action.getEntityDefinition().getName())){
                            if(action.getSecondaryEntityContext().isPresent()){
                                String secondEntityName = action.getSecondaryEntityContext().get().getSecondaryEntityName();
                                secondaryEntities = getSecondaryEntitiesForAction(action.getSecondaryEntityContext().get(), entitiesManager.get(secondEntityName),this.worldInstance.getEnvironment(),
                                        this.worldInstance.getWorldMap(),tick);
                                for(EntityInstance secondaryEnt : secondaryEntities){
                                    context = new ContextImpl(entityInstance,this.worldInstance.getEnvironment(),
                                            this.worldInstance.getWorldMap(),tick,secondaryEnt, entitiesManager);
                                    try{
//                                        if (action.getActionType().equals(ActionType.PROXIMITY)){
//                                            System.out.println("proximity action. id = " + id);
//                                        }
                                        action.invoke(context);
                                    }
                                    catch (Exception e){
                                        // TODO: 16-Sep-23 catch the appopriate exception
                                       System.out.println(countException + " exception!!!!! in simulation run loop" +
                                                "\nSECONDARY entity");
                                       System.out.println(e);
                                       countException++;
                                    }
                                }
                            }
                            else{
                                context = new ContextImpl(entityInstance, this.worldInstance.getEnvironment(),
                                this.worldInstance.getWorldMap(),tick, entitiesManager);
                                try{
//                                    if (action.getActionType().equals(ActionType.PROXIMITY)){
//                                           System.out.println("proximity action! id: "+ id);
//                                    }
                                    action.invoke(context);
                                }
                                catch(Exception e){
                                    // TODO: 16-Sep-23 catch the appopriate exception
                                    System.out.println(countException + " exception!!!!! in simulation run loop" +
                                            "\n single entity");
                                    System.out.println(e);
                                    e.printStackTrace(System.out);
                                    countException++;
                                }
                            }
                        }
                    }
                }
            }

            long currentTimeMillis = System.currentTimeMillis();
            long elapsedMillis = currentTimeMillis - simulationStartTimeMillis;

            // Convert elapsed time to seconds
            long elapsedSeconds = elapsedMillis / 1000;
            setProgressDTO(tick,elapsedSeconds, simulationStatus);
            removeDeadInstances();
            updateEntityStatusDTO();
            tick++;
        }

        getProgressDTO().setStatus(SimulationStatus.ENDED);


        setFinalDataDTO();
        Date date = new Date();
        worldInstance.setEntityInstanceManagers(entitiesManager);
        this.simulationResult = new Pair<>(date.toString(), worldInstance);

        return null;
    }

    private void setFinalDataDTO() {
        Map<String, Map<String,HistogramDTO>> histogramDTOMap = new HashMap<>();
        Map<String, Map<String,FinalValue>> averages = new HashMap<>();
        for (EntityDefinition entity: worldDefinition.getEntityDefinition()){
            Map<String,HistogramDTO> propertyNameToHistogram = new HashMap<>();
            Map<String, FinalValue> propertyAverages = new HashMap<>();
            for (PropertyDefinition property: entity.getProps()) {
                Map<String,Integer> histogram = new HashMap<>();
                Float sumValues = null;
                Float sumTickChanges = new Float(0);
                if ((property.getType().equals(PropertyType.FLOAT) ||property.getType().equals(PropertyType.DECIMAL))){
                    sumValues = new Float(0);
                }
                for (EntityInstance entityInstance: entitiesManager.get(entity.getName()).getInstances()) {
                    if(sumValues != null){
                        sumValues += (float)entityInstance.getPropertyByName(property.getName()).getValue();
                    }

                    sumTickChanges += entityInstance.getPropertyByName(property.getName()).getNumberOfChanges();

                    if (histogram.containsKey(entityInstance.getPropertyByName(property.getName()).getValue().toString())){
                        Integer count = histogram.get(entityInstance.getPropertyByName(property.getName()).getValue().toString());
                        count ++;
                        histogram.put(entityInstance.getPropertyByName(property.getName()).getValue().toString(),count);
                    }else {
                        histogram.put(entityInstance.getPropertyByName(property.getName()).getValue().toString(), 1);
                    }
                }
                if (sumValues != null){
                    sumValues = sumValues / entitiesManager.get(entity.getName()).getInstances().size();
                }
                propertyAverages.put(property.getName(),new FinalValue(sumTickChanges/tick, sumValues));
                propertyNameToHistogram.put(property.getName(), new HistogramDTO(histogram));
            }
            averages.put(entity.getName(), propertyAverages);
            histogramDTOMap.put(entity.getName(),propertyNameToHistogram);
        }

        this.simulationDetailsDTO.setEndedSimulationInformation(new SimulationFinalDataDTO(this.entNameToEntityPopulationInEachTick, histogramDTOMap, averages));

    }

    //optionnnn
    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private void updateNameToPopulation() {
        for (String name: entitiesManager.keySet()){
            entNameToEntityPopulationInEachTick.get(name).add(entitiesManager.get(name).getInstances().size());
        }
    }

    private void initNameToPopulation() {
        for (String name:entitiesManager.keySet()) {
            entNameToEntityPopulationInEachTick.put(name, new ArrayList<>());
        }
    }

    private void updateEntityStatusDTO() {
        for (String entName : entitiesManager.keySet()){
            this.simulationDetailsDTO.getEntityStatusDTO().getEntNameToPopulation().put(entName, new Integer(entitiesManager.get(entName).getInstances().size()));
        }
    }

    private void removeDeadInstances() {
        for (EntityInstanceManager entManager: entitiesManager.values()) {
            List<EntityInstance> entityInstanceList = new ArrayList<>();
            for (EntityInstance entityInstance : entManager.getInstances()){
                if (entityInstance.isAlive()){
                    entityInstanceList.add(entityInstance);
                }
            }
            entManager.setInstances(entityInstanceList);
        }
    }

    private List<EntityInstance> getSecondaryEntitiesForAction(SecondaryEntityContext secondaryEntityContext,
                                                               EntityInstanceManager entityInstanceManager, ActiveEnvironment env, EntityInstance[][] map, int tick) {
        List<EntityInstance> instanceList = new ArrayList<>();
        Integer maxNumberOfEntities;
        if (secondaryEntityContext.getSelectionType().equals(SelectionType.SELECTED)){
            maxNumberOfEntities = new Integer(secondaryEntityContext.getNumberOfEntities());
        }
        else {
            maxNumberOfEntities = new Integer(entityInstanceManager.getInstances().size());
        }
        int counter = 0;
        ContextImpl context;
        for (EntityInstance entInst: entityInstanceManager.getInstances()) {
            context = new ContextImpl(entInst,env,map,tick,entitiesManager);
            try{
                boolean isValidEntity = secondaryEntityContext.getCondition().test(context);
                if(isValidEntity){
                    instanceList.add(entInst);
                    counter++;
                    if (maxNumberOfEntities.equals(counter)){
                        break;
                    }
                }
            }
            catch (ClassCastException | NumberFormatException | InputMismatchException e){throw e;}

        }
        return instanceList;
    }

    // TODO: 16-Sep-23 the number of each entity type is check in the UI layer
    private void initMap(){

        for (String entityName: entitiesManager.keySet()) {
            for (EntityInstance entInst: entitiesManager.get(entityName).getInstances()) {
                placeEntitiesOnWorldMap(entInst);
            }

        }
    }

    public void moveAllEntities(){
        for (EntityInstanceManager entManager : entitiesManager.values()){
            for (EntityInstance entInst : entManager.getInstances()){
                entInst.makeMove(this.worldInstance.getWorldMap());
            }
        }

    }

    private void placeEntitiesOnWorldMap(EntityInstance entInst) {
        int row, col;
        Random random = new Random();

        do {
            row = random.nextInt(worldDefinition.getRows());
            col = random.nextInt(worldDefinition.getCols());
        } while (worldInstance.getWorldMap()[row][col] != null);

        entInst.setLocation(col, row);
        worldInstance.getWorldMap()[row][col] = entInst;//new EntityInstanceImpl(entInst.getEntityDefinition());
    }

    public void initSimulationEnv(InitSimulationDTO dto){
        for (String envPropName: dto.getEnvNameToPropertyDTO().keySet()) {
            this.worldInstance.getEnvironment()
                    .getProperty(envPropName)
                    .updateProperty(dto.getEnvNameToPropertyDTO().get(envPropName));
        }
    }
    public void initSimulationPopulation(InitSimulationDTO dto){

        for (EntityDefinition entDef: worldDefinition.getEntityDefinition()) {

            EntityInstanceManager entManager = new EntityInstanceManagerImpl();
            entManager.createPopulation(dto.getEntNameToPopulation().get(entDef.getName()),entDef);
            // entity manager = null cant do that->
            // instead create new entity manager
            //entitiesManager.get(entDef.getName()).createPopulation(dto.getEntNameToPopulation().get(entDef.getName()), entDef);
            entitiesManager.put(entDef.getName(),entManager);

        }
    }

    public SimulationProgressDTO getProgressDTO() {
        return this.simulationDetailsDTO.getSimulationProgressDTO();
    }

    private void setProgressDTO(int currentTick, long elapsedTime, SimulationStatus status){
        this.simulationDetailsDTO.getSimulationProgressDTO().setCurrentTick(currentTick);
        this.simulationDetailsDTO.getSimulationProgressDTO().setTimeElapsedInSeconds(elapsedTime);
        this.simulationDetailsDTO.getSimulationProgressDTO().setStatus(status);
    }
    @Override
    public void setSimulationStatus(SimulationStatus simulationStatus) {
        this.simulationStatus = simulationStatus;
    }

    @Override
    public AllSimulationDetailsDTO getSimulationDetailsDTO() {
        return simulationDetailsDTO;
    }

    @Override
    public InitSimulationDTO getInitSimulationDTO(){
        return this.initSimulationDTO;
    }

    @Override
    public SimulationStatus getSimulationStatus() {
        return this.simulationStatus;
    }
}
