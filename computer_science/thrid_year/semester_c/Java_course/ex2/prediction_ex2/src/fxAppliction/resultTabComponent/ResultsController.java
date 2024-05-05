package fxAppliction.resultTabComponent;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import dto.implement.AllSimulationDetailsDTO;
import engine.simulator.api.SimulationStatus;
import fxAppliction.app.AppController;
import fxAppliction.headerComponent.HeaderController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

public class ResultsController {


    @FXML
    private GridPane resultsComponent;

    @FXML
    private TableView<AllSimulationDetailsDTO> simulationsTable;


    @FXML
    private TableColumn<AllSimulationDetailsDTO, String> simulationCol;

    @FXML
    private TableColumn<AllSimulationDetailsDTO, String> statusCol;
    @FXML
    private VBox ticksAndSecLabsVbox;

    @FXML
    private Label ticksLabel;

    @FXML
    private Label secondsLabel;

    @FXML
    private TableColumn<KeyValueEntry,String> entityCol;

    @FXML
    private TableColumn<KeyValueEntry,Integer> populationCol;
    @FXML
    private TableView<KeyValueEntry> entitiesToPopTable;

    @FXML
    private VBox controlButtonsVbox;
    @FXML
    private Button resumeBtn;

    @FXML
    private Button pauseBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private Button rerunBtn;

    private AppController mainController;

    private Map<Integer,AllSimulationDetailsDTO> allSimulationsDTO = new HashMap<>();

    private ObservableList<AllSimulationDetailsDTO> allSimulationDetailsDTOObservableList  = FXCollections.observableArrayList();

    private Integer selectedSimulationID;
    private List <AllSimulationDetailsDTO>tempList;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {



        simulationCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));


        this.simulationsTable.setItems(this.allSimulationDetailsDTOObservableList);
        simulationsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setSelectedSimulationID(newValue.getId());
                this.setAllButtons(this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).getSimulationStatus());

            }

        });




    }

    public Label getTicksLabel() {
        return ticksLabel;
    }

    public Label getSecondsLabel() {
        return secondsLabel;
    }

    public void updateProgressSimulationDetails(){

        if(this.selectedSimulationID != null) {
            Integer ticks = this.allSimulationsDTO.get(this.selectedSimulationID).getSimulationProgressDTO().getCurrentTick();
            Long seconds = this.allSimulationsDTO.get(this.selectedSimulationID).getSimulationProgressDTO().getTimeElapsedInSeconds();
            ticksLabel.setText("Ticks: " + ticks.toString());
            secondsLabel.setText("Seconds: " + seconds.toString());

            Map<String,Integer> entitiesToPopulation = this.allSimulationsDTO.get(this.selectedSimulationID).getEntityStatusDTO().getEntNameToPopulation();
            List<KeyValueEntry>keyvalueList = entitiesToPopulation.entrySet(). stream()
                    .map(entry->new KeyValueEntry(entry.getKey(),entry.getValue()))
                    .collect(Collectors.toList());

            this.entitiesToPopTable.getItems().setAll(keyvalueList);

            this.entityCol.setCellValueFactory(cellData -> {
                String key = cellData.getValue().getKey();

                return new SimpleStringProperty(key);
            });
            this.populationCol.setCellValueFactory(cellData -> {
                int value = cellData.getValue().getValue();

                return new SimpleIntegerProperty(value).asObject();
            });

            this.setAllButtons(this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).getSimulationStatus());

        }




    }
    public void updateSimulationStatusRunInTable(Integer id) {
        Optional<AllSimulationDetailsDTO> optionalDto =new ArrayList<>( allSimulationsDTO.values()).stream()
                .filter(dto -> dto.getId().equals(id))
                .findFirst();
        ((Optional<AllSimulationDetailsDTO>) optionalDto).get().setStatus(SimulationStatus.RUN);
        this.simulationsTable.refresh();
    }





    public void updateSimulationStatusEndedInTable(Integer id) {
        Optional<AllSimulationDetailsDTO> optionalDto =new ArrayList<>( allSimulationsDTO.values()).stream()
                .filter(dto -> dto.getId().equals(id))
                .findFirst();
        ((Optional<AllSimulationDetailsDTO>) optionalDto).get().setStatus(SimulationStatus.ENDED);
        this.simulationsTable.refresh();
    }


    public TableView<AllSimulationDetailsDTO> getSimulationsTable() {
        return simulationsTable;
    }

    public VBox getTicksAndSecLabsVbox() {
        return ticksAndSecLabsVbox;
    }

    public TableView<KeyValueEntry> getEntitiesToPopTable() {
        return entitiesToPopTable;
    }

    public void setAllButtons(SimulationStatus status){
        if(status == SimulationStatus.ENDED){
            resumeBtn.setDisable(true);
            pauseBtn.setDisable(true);
            stopBtn.setDisable(true);
            rerunBtn.setDisable(false);
        } else if (status == SimulationStatus.PAUSE) {
            resumeBtn.setDisable(false);
            pauseBtn.setDisable(true);
            stopBtn.setDisable(false);
            rerunBtn.setDisable(true);
        } else if (status == SimulationStatus.RUN) {
            resumeBtn.setDisable(true);
            pauseBtn.setDisable(false);
            stopBtn.setDisable(false);
            rerunBtn.setDisable(true);
        }
    }
    @FXML
    void onPauseClicked(MouseEvent event) {
        if( this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).getSimulationStatus() == (SimulationStatus.RUN)) {

            this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).setSimulationStatus(SimulationStatus.PAUSE);
            this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).setPaused(true);

            setAllButtons(SimulationStatus.PAUSE);
//            pauseBtn.setDisable(true);
//            resumeBtn.setDisable(false);
//            stopBtn.setDisable(false);
//            rerunBtn.setDisable(true);
        }
    }

    @FXML
    void onRerunClicked(MouseEvent event) {
        if( this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).getSimulationStatus() == (SimulationStatus.ENDED)) {

            this.mainController.getNewExecComponentController().setDetailsForNewRun(this.mainController.getNewExecComponentController().getInitSimulationDTO());
            this.mainController.getTabPane().getSelectionModel().select(1);

        }
    }

    @FXML
    void onResumeClicked(MouseEvent event) {
        if( this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).getSimulationStatus() == (SimulationStatus.PAUSE)){


            this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).setSimulationStatus(SimulationStatus.RUN);
            this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).setPaused(false);
            setAllButtons(SimulationStatus.RUN);

//            resumeBtn.setDisable(true);
//            pauseBtn.setDisable(false);
//            stopBtn.setDisable(false);
//            rerunBtn.setDisable(true);
        }
    }

    @FXML
    void onStopClicked(MouseEvent event) {
        if (this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).getSimulationStatus() == (SimulationStatus.RUN) ||
                (this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).getSimulationStatus() == (SimulationStatus.PAUSE))){

            this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).setSimulationStatus(SimulationStatus.ENDED);
            this.mainController.getEngine().getSimulatorById(this.selectedSimulationID).setPaused(true);
            setAllButtons(SimulationStatus.ENDED);

        }

    }

    public void setSelectedSimulationID(Integer selectedSimulationID) {
        this.selectedSimulationID = selectedSimulationID;
    }

    public GridPane getResultsComponent() {
        return resultsComponent;
    }

    public Map<Integer,AllSimulationDetailsDTO> getAllSimulationsDTO() {
        return this.allSimulationsDTO;
    }

    public ObservableList<AllSimulationDetailsDTO> getAllSimulationDetailsDTOObservableList() {
        return this.allSimulationDetailsDTOObservableList;
    }





}
