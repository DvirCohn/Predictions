package fxAppliction.headerComponent;

import fxAppliction.app.AppController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.FileChooser;

import java.io.File;

public class HeaderController {
    @FXML
    private Label predictionsLabel;

    @FXML
    private Button loadFileBtn;

    @FXML
    private TextField filePathField;

    @FXML
    private ComboBox<String> queueManagment;
    @FXML
  //  private Label queueManagment;
    private String selectedFilepath;
    StringBuffer data = new StringBuffer();
    private AppController mainController;

    @FXML
    public void initialize(){
        this.queueManagment.setItems(FXCollections.observableArrayList("Runing: ", "Waiting: ", "Finished: "));
        this.queueManagment.setCellFactory(param -> new ListCell<String>() {




            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    switch (item){
                        case "Runing: ":
                            item =  "Runing: "+getMainController().getEngine().countRunningSimulations();
                            break;
                        case "Wiating: ":
                            item = "Waiting: "+getMainController().getEngine().countWaitingSimulations();
                            break;

                        case "Finished: ":
                            item = "Finished: "+getMainController().getEngine().countEndedSimulations();
                            break;
                        default: break;
                    }

                    setText(item);
                }
            }
        });

        this.queueManagment.setDisable(true);
    }

    public ComboBox<String> getQueueManagment() {
        return queueManagment;
    }

    public void updateComboBox() {
        // Perform the updates to the ComboBox here
        // You can access the ComboBox and update its items or properties as needed
        queueManagment.getItems().setAll("Runing: "+getMainController().getEngine().countRunningSimulations(), "Waiting: "+getMainController().getEngine().countWaitingSimulations(),
                "Finished: "+getMainController().getEngine().countEndedSimulations());
    }



    public AppController getMainController() {
        return mainController;
    }

    @FXML
    void onLoadFileButtonClicked(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an XML File");
        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(xmlFilter);
        File selectedFile = fileChooser.showOpenDialog(loadFileBtn.getScene().getWindow());

        if (selectedFile != null) {
            selectedFilepath = selectedFile.getAbsolutePath();
        }


        filePathField.setText(selectedFilepath);

        // need to deliver to the engine the selected path to been checked *** ToDo
        mainController.loadFilePressed();

    }
    public String getSelectedFilepath(){
        return this.selectedFilepath;
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

//   public void updateQueueManageData(){
//         data.append("Runing: "+this.mainController.getEngine().countRunningSimulations() +" ");
//         data.append("Waiting: "+this.mainController.getEngine().countWaitingSimulations() +" ");
//         data.append("finished: "+this.mainController.getEngine().countEndedSimulations());
//
//         this.queueManagment.setText(data.toString());
//         this.queueManagment.setVisible(true);
//
//   }
}
