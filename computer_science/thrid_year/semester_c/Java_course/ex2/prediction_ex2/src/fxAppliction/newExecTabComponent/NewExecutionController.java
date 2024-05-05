package fxAppliction.newExecTabComponent;

import dto.implement.*;
import fxAppliction.app.AppController;
import fxAppliction.task.UiTask;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.util.Callback;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewExecutionController {
    @FXML
    private GridPane newExecGridPane;

    @FXML
    private Label entitiesSetUp;

    @FXML
    private Label EnvironemtSetUpLabel;

    @FXML
    private VBox enironmentVbox;

    @FXML
    private VBox setEntitiesList;

    @FXML
    private Button clearBTN;

    @FXML
    private Button startBTN;

    @FXML
    private Label maxPop;
    @FXML
    private Label currentPopulation;

    private AppController mainController;

    private Integer maxPopulation;

    private InitSimulationDTO initSimulationDTO;

    private Integer currentSimulationId;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setMaxPopulation(Integer current){
        this.maxPopulation = current;
    }

    public Integer getMaxPopulation() {
        return this.maxPopulation;
    }

    public void setEntitiesPopulation(WorldInfoDTO worldInfoDTO){
        this.setEntitiesList.getChildren().clear();
        List<EntityDTO> entitiesDTO = worldInfoDTO.getEntDTO();
        Integer maxPopulation = worldInfoDTO.getGridDTO().getNumOfColumns() * worldInfoDTO.getGridDTO().getNumOfRows();

        this.maxPop.setText("Max Population: "+maxPopulation);
        setMaxPopulation(maxPopulation);

        int[] amounts = new int[entitiesDTO.size()];
        TextField[] amountTextFields = new TextField[entitiesDTO.size()];

        for(int i = 0;i<entitiesDTO.size();i++){

            Label typeLabel = new Label(entitiesDTO.get(i).getName());

            TextField populationTextField = new TextField();
            populationTextField.setPromptText("Enter Value");


            // Add a listener to the text field to validate the input
            int finalI = i;
            populationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) { // Allow only digits
                    populationTextField.setText(oldValue);
                }
                else {
                    int newAmount = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
                    int total = calculateTotalAmount(amounts, finalI, newAmount);
                    if (total > this.getMaxPopulation()) {
                        populationTextField.setText(oldValue);
                    } else {
                        amounts[finalI] = newAmount;
                    }
                    this.currentPopulation.setText("Total Population: "+ Arrays.stream(amounts).sum());
                }
            });
            VBox cell = new VBox(typeLabel,populationTextField);
            setEntitiesList.getChildren().add(cell);
            cell.visibleProperty();

        }

        }
    private int calculateTotalAmount(int[] amounts, int currentIndex, int newValue) {
        int total = newValue;
        for (int i = 0; i < amounts.length; i++) {
            if (i != currentIndex) {
                total += amounts[i];
            }
        }
        return total;
    }

    public void setEnvironemtVariable(WorldInfoDTO worldInfoDTO){
        this.enironmentVbox.getChildren().clear();
        EnvironmentDTO environmentDTO = worldInfoDTO.getEnvDTO();

        HBox cell = null;
        for(PropertyDTO propDTO : environmentDTO.getEnvProps().values()){
            CheckBox setValCheckBox = new CheckBox();
            Label varName  = new Label(propDTO.getName()+" ");
            Label varType = new Label(propDTO.getType()+" ");
            switch(propDTO.getType().toLowerCase()){
                case "string":
                    TextField stringVal = new TextField();
                    stringVal.disableProperty().bind(Bindings.not(setValCheckBox.selectedProperty()));
                    cell  = new HBox(setValCheckBox,varName,varType,stringVal);
                    break;
                case "boolean":
                    CheckBox booleanVal = new CheckBox();
                    Label explain = new Label("v for True");
                    bindControlsDisableProperty(setValCheckBox.selectedProperty(), booleanVal);
                    cell  = new HBox(setValCheckBox,varName,varType,booleanVal,explain);
                    break;
                case "decimal":
                case "float":

                    Float min = propDTO.getFrom();
                    Float max = propDTO.getTo();


                    Slider numericVal = new Slider(min,max,(min + max)/2);
                    Double currentVal = numericVal.getValue();
                    Label valueLabel = new Label(String.format("%.1f", numericVal.getValue()));
                    bindControlsDisableProperty(setValCheckBox.selectedProperty(), numericVal);
                    numericVal.valueProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            valueLabel.setText(String.format("%.1f", numericVal.getValue()));
                        }
                    });

//                    numericVal.valueProperty().addListener(new InvalidationListener() {
//                        @Override
//                        public void invalidated(Observable observable) {
//                            valueLabel.setText(String.format("%.1f", numericVal.getValue()));
//                        }
//                    });


                    cell  = new HBox(setValCheckBox,varName,varType,numericVal,valueLabel);
                    break;


            }
            this.enironmentVbox.getChildren().add(cell);

        }
    }
    private void bindControlsDisableProperty(BooleanProperty setValCheckBoxSelected, Control... controls) {
        for (Control control : controls) {
            control.disableProperty().bind(Bindings.not(setValCheckBoxSelected));
        }
    }

    public VBox getEnironmentVbox() {
        return enironmentVbox;
    }

    public VBox getSetEntitiesList() {
        return setEntitiesList;
    }

    public void setDetailsForNewRun(InitSimulationDTO initDTO){
        Integer amountOfEntity = null;
        for(Node cell : this.setEntitiesList.getChildren()){
            if(cell instanceof VBox){
                VBox vbox = (VBox) cell;
                for (Node innerNode : vbox.getChildren()) {
                    if (innerNode instanceof Label) {
                        Label label = (Label) innerNode;
                        if(initDTO.getEntNameToPopulation().containsKey(label.getText())){
                            amountOfEntity = initDTO.getEntNameToPopulation().get(label.getText());
                        }

                    } else if (innerNode instanceof TextField) {
                        TextField textField = (TextField) innerNode;
                        textField.setText(amountOfEntity.toString());

                    }
                }

            }
        }


            for(Node cell : this.enironmentVbox.getChildren()){
                if(cell instanceof HBox){
                    HBox Hbox = (HBox) cell;

                    if(Hbox.getChildren().get(1) instanceof Label) {
                        String envName = ((Label) Hbox.getChildren().get(1)).getText();
                        if(initDTO.getEnvNameToPropertyDTO().containsKey(envName)){
                            if(Hbox.getChildren().get(3) instanceof TextField){
                                TextField text =((TextField) Hbox.getChildren().get(3));
                                text.setText(initDTO.getEnvNameToPropertyDTO().get(envName).getObjectValue());
                            } else if (Hbox.getChildren().get(3) instanceof Slider) {
                                Slider slider =((Slider) Hbox.getChildren().get(3));
                                slider.setValue(new Double(initDTO.getEnvNameToPropertyDTO().get(envName).getObjectValue()));
                            }
                            else{
                                CheckBox checkBox = ((CheckBox) Hbox.getChildren().get(3));
                                String boolResult = initDTO.getEnvNameToPropertyDTO().get(envName).getObjectValue();
                                if(boolResult.equals("false")){
                                    checkBox.setSelected(false);
                                }
                                else {
                                    checkBox.setSelected(true);
                                }
                            }
                        }
                    }

                }

            }




    }

    @FXML
    void onStartButtonClicked(MouseEvent event) {
        Map<String,Integer> entityToPopulation = new HashMap<>();
        Map<String,PropertyDTO>envNameToProp = new HashMap<>();
        String entityName = "", Entityamount ="";
        Integer entityPopulation;
        WorldInfoDTO worldInfoDTO = mainController.getWorldInfoDTO();


        for(Node cell : this.setEntitiesList.getChildren()){
            if(cell instanceof VBox){
                VBox vbox = (VBox) cell;
                for (Node innerNode : vbox.getChildren()) {
                    if (innerNode instanceof Label) {
                        Label label = (Label) innerNode;
                         entityName = label.getText();

                    } else if (innerNode instanceof TextField) {
                        TextField textField = (TextField) innerNode;
                        Entityamount = textField.getText();
                        textField.clear();

                    }
                }
                if(Entityamount.equals("")){
                    entityPopulation = 0;
                }
                else{
                    entityPopulation = new Integer(Entityamount);
                }
                entityToPopulation.put(entityName,entityPopulation);
            }
        }


        for(Node cell : this.enironmentVbox.getChildren()){
            if(cell instanceof HBox){
                HBox Hbox = (HBox) cell;
                String type;
                if(Hbox.getChildren().get(0) instanceof CheckBox){

                    CheckBox changeEnv = (CheckBox) Hbox.getChildren().get(0);
                    if(changeEnv.isSelected()){
                        if(Hbox.getChildren().get(1) instanceof Label && Hbox.getChildren().get(2) instanceof Label){
                            String envName = ((Label) Hbox.getChildren().get(1)).getText();
                            PropertyDTO propertyDTO = worldInfoDTO.getEnvDTO().getEnvProps().get(envName);
                            type = ((Label) Hbox.getChildren().get(2)).getText();
                            switch(type.toLowerCase()){
                                case "boolean":
                                    if(Hbox.getChildren().get(3) instanceof CheckBox){
                                        if(((CheckBox) Hbox.getChildren().get(3)).isSelected()){
                                             propertyDTO.setObjectValue("true");
                                             envNameToProp.put(envName,propertyDTO);
                                             break;
                                        }
                                        else{
                                            propertyDTO.setObjectValue("false");
                                            envNameToProp.put(envName,propertyDTO);
                                            break;
                                        }
                                    }
                                case "string":
                                    if(Hbox.getChildren().get(3) instanceof TextField) {
                                        String newVal = ((TextField) Hbox.getChildren().get(3)).getText();
                                        propertyDTO.setObjectValue(newVal);
                                        envNameToProp.put(envName,propertyDTO);
                                        break;


                                    }
                                case "float":
                                    if(Hbox.getChildren().get(3) instanceof Slider){
                                        Float newVal = new Float(((Slider) Hbox.getChildren().get(3)).getValue());
                                        propertyDTO.setObjectValue(newVal.toString());
                                        envNameToProp.put(envName,propertyDTO);
                                        break;

                                    }
                                case "integer":
                                    if(Hbox.getChildren().get(3) instanceof Slider){
                                        Integer newVal = new Integer((int) ((Slider) Hbox.getChildren().get(3)).getValue());
                                        propertyDTO.setObjectValue(newVal.toString());
                                        envNameToProp.put(envName,propertyDTO);
                                        break;

                                    }
                                default:
                                    break;
                            }

                        }

                    }
                }

            }

        }
        try{
            this.currentSimulationId = this.mainController.getEngine().createNewSimulation();
            this.mainController.getSimulationIds().add(currentSimulationId);
        }
        catch (FileNotFoundException | JAXBException exception){}
        mainController.getResultsComponentController().getAllSimulationsDTO().put(currentSimulationId,mainController.getEngine().getSimulatorAllDataDTO(currentSimulationId));
        mainController.getResultsComponentController().getAllSimulationDetailsDTOObservableList().add(mainController.getEngine().getSimulatorAllDataDTO(currentSimulationId));
        this.initSimulationDTO = new InitSimulationDTO(entityToPopulation,envNameToProp);
        this.mainController.getEngine().executeSimulation(this.currentSimulationId, this.initSimulationDTO);
        UiTask uiTask = new UiTask(currentSimulationId,this.mainController.getEngine(),this.mainController);
        this.mainController.executeUIupdates(uiTask);
        this.mainController.getTabPane().getSelectionModel().select(2);

    }

    @FXML
    void onClearButtonclicked(MouseEvent event) {

        if(this.setEntitiesList.getChildren().size() != 0){//data was load to the app
            for(Node cell : setEntitiesList.getChildren()){
                if(cell instanceof VBox){
                    VBox vBox = (VBox) cell;
                    for(Node innerItem : vBox.getChildren()){
                        if(innerItem instanceof TextField){
                            TextField textField = (TextField) innerItem;
                            textField.clear();
                        }

                    }
                }
            }
        }

        if(this.enironmentVbox.getChildren().size() != 0){
            for(Node cell : enironmentVbox.getChildren()){
                if(cell instanceof  HBox ){
                    HBox hBox = (HBox) cell;
                    if(hBox.getChildren().get(0) instanceof CheckBox ){
                        CheckBox setCheckBox = (CheckBox)hBox.getChildren().get(0);
                        setCheckBox.setSelected(false);
                    }
                    if(hBox.getChildren().get(3) instanceof CheckBox ){
                        CheckBox setCheckBox = (CheckBox)hBox.getChildren().get(3);
                        setCheckBox.setSelected(false);
                    }
                    else if(hBox.getChildren().get(3) instanceof TextField ){
                        TextField textFieldValue = (TextField) hBox.getChildren().get(3);
                        textFieldValue.clear();
                    }
                    else if (hBox.getChildren().get(3) instanceof Slider ){
                        Slider sliderVal = (Slider) hBox.getChildren().get(3);
                        sliderVal.setValue((sliderVal.getMax() + sliderVal.getMin())/2);
                    }
                }
            }



        }
    }

    public InitSimulationDTO getInitSimulationDTO(){
        return this.initSimulationDTO;
    }

    public Integer getCurrentSimulationId() {
        return this.currentSimulationId;
    }
}







