package fxAppliction.app;


import fxAppliction.detailsTabComponent.DetailsController;
import dto.implement.WorldInfoDTO;
import engine.EngineDefinition;
import engine.EngineImpl;
import fxAppliction.headerComponent.HeaderController;
import fxAppliction.newExecTabComponent.NewExecutionController;
import fxAppliction.resultTabComponent.ResultsController;
import fxAppliction.task.UiTask;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;

import javax.xml.bind.JAXBException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppController {

    @FXML private HeaderController headerComponentController;
    @FXML private SplitPane headerComponent;
    @FXML private DetailsController detailsComponentController;
    @FXML private ScrollPane detailsComponent;
    @FXML private NewExecutionController newExecComponentController;
    @FXML private GridPane newExecComponent;

    @FXML private ResultsController resultsComponentController;
    @FXML private GridPane resultsComponent;

    @FXML private TabPane tabsContainer;

    public NewExecutionController getNewExecComponentController() {
        return this.newExecComponentController;
    }

    private EngineDefinition engine;

    private WorldInfoDTO worldInfoDTO;

    List<Integer> simulationIds;

    private ExecutorService threadpool;

    @FXML
    public void initialize() {
        if (headerComponentController != null && detailsComponentController != null && newExecComponentController != null && resultsComponentController != null ) {
            headerComponentController.setMainController(this);
            detailsComponentController.setMainController(this);
            newExecComponentController.setMainController(this);
            resultsComponentController.setMainController(this);
            this.engine = new EngineImpl();
            this.simulationIds = new ArrayList<>();
            this.threadpool = Executors.newFixedThreadPool(1);
        }
    }
    public void setHeaderComponentController(HeaderController headerComponentController) {
        this.headerComponentController = headerComponentController;
        headerComponentController.setMainController(this);
    }

    public void setDetailsComponentController(DetailsController detailsComponentController) {
        this.detailsComponentController = detailsComponentController;
        this.detailsComponentController.setMainController(this);
    }
    public void setNewExecComponentController(NewExecutionController newExecComponentController) {
        this.newExecComponentController = newExecComponentController;
        this.newExecComponentController.setMainController(this);
    }

    public void setResultsComponentController(ResultsController resultsComponentController) {
        this.resultsComponentController = resultsComponentController;
        this.resultsComponentController.setMainController(this);
    }

    public HeaderController getHeaderComponentController() {
        return this.headerComponentController;
    }

    public void loadFilePressed(){


        String path = headerComponentController.getSelectedFilepath();
        try {

            this.engine.loadNewSimulation(path);
            this.worldInfoDTO = this.engine.presentSimulationDetails();
            this.resultsComponentController.getSecondsLabel().setText("Seconds: ");
            this.resultsComponentController.getTicksLabel().setText("Ticks: ");
            this.resultsComponentController.getEntitiesToPopTable().getItems().clear();
            this.resultsComponentController.getSimulationsTable().getItems().clear();
            this.newExecComponentController.getSetEntitiesList().getChildren().clear();
            this.newExecComponentController.getEnironmentVbox().getChildren().clear();
            this.detailsComponentController.createDetailsTreeView(this.worldInfoDTO);
            this.newExecComponentController.setEntitiesPopulation(this.worldInfoDTO);
            this.newExecComponentController.setEnvironemtVariable(this.worldInfoDTO);
            this.getHeaderComponentController().getQueueManagment().setDisable(false);



        }
        catch (FileNotFoundException | JAXBException | IllegalArgumentException | NoSuchElementException exception){

//            System.out.println(exception.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error XML file");

            alert.setContentText(exception.getMessage());

            alert.showAndWait();
        }
    }

    public WorldInfoDTO getWorldInfoDTO(){
        return this.worldInfoDTO;
    }

    public TabPane getTabPane(){
        return this.tabsContainer;
    }

    public EngineDefinition getEngine() {
        return engine;
    }

    public String getXMLPath(){
        return this.headerComponentController.getSelectedFilepath();
    }

    public List<Integer> getSimulationIds() {
        return this.simulationIds;
    }

    public ResultsController getResultsComponentController() {
        return this.resultsComponentController;
    }

    public Boolean executeUIupdates( UiTask uiTask){
        threadpool.execute(uiTask);
        return true;
    }
}
