package fxAppliction.detailsTabComponent;

import dto.implement.action.AbstractActionDTO;
import dto.implement.action.ActionDTO;
import fxAppliction.app.AppController;
import dto.implement.*;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DetailsController {

    @FXML
    private ScrollPane details;

    @FXML
    private Pane explainField;

    @FXML
    private SplitPane detailsSplit;

    @FXML
    private TreeView treeView;

    @FXML
    private TextArea detailTextArea;
    @FXML
    private Label infoLabel;
    private StringProperty infoDetails;

    private AppController mainController;

    private List<String> selectionPath;




    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    //TODO add grid to the tree and also to the dto
    public void createDetailsTreeView(WorldInfoDTO worldInfo){
        TreeItem <String> rootItem = new TreeItem<>("World");
        treeView.setRoot(rootItem);
        //treeView = new TreeView<>(rootItem);

        //add environemt variables
        rootItem.getChildren().add(createEnvironmentTreeitem(worldInfo.getEnvDTO()));
        //add entities to tree
        rootItem.getChildren().add(createEntitiesTreeitem(worldInfo.getEntDTO()));
        rootItem.getChildren().add(createRulesTreeitem(worldInfo.getRulesDTO()));

        rootItem.getChildren().add(createTerminationTreeitem(worldInfo.getTerminationDTO()));
        rootItem.getChildren().add(createGridTreeitem(worldInfo.getGridDTO()));


    }

    public TreeItem<String> createRulesTreeitem(List<RuleDTO> rulesDto){
        TreeItem<String> rules = new TreeItem<>("Rules");
        TreeItem<String> actions ;
        for(RuleDTO ruleDTO :rulesDto){
            TreeItem<String> rule = new TreeItem<>(ruleDTO.getName());
            actions = new TreeItem<>("Actions");
            for(ActionDTO actionDTO : ruleDTO.getActionDTOMap().values()){
                TreeItem<String> action = new TreeItem<>(actionDTO.getName());
                actions.getChildren().add(action);
            }
            rule.getChildren().add(actions);
            TreeItem<String> activation = new TreeItem<>("Activation");
            rule.getChildren().add(activation);
            rules.getChildren().add(rule);

        }
        return rules;

    }

    public TreeItem<String> createGridTreeitem(GridDTO gridDTO){
        TreeItem<String> grid = new TreeItem<>("Grid");
        return grid;

    }

    public TreeItem<String> createTerminationTreeitem(TerminationDTO terminationDto){
        TreeItem<String> termination = new TreeItem<>("Termination");
        return termination;

    }

    public TreeItem<String> createEntitiesTreeitem(List<EntityDTO> entitiesDTO){

        TreeItem<String> entities = new TreeItem<>("Entities");
        for(EntityDTO entityDTO: entitiesDTO){
            TreeItem<String> entityItem = new TreeItem<>(entityDTO.getName());
            entities.getChildren().add(entityItem);
        }
        return entities;


    }

    public TreeItem<String> createEnvironmentTreeitem(EnvironmentDTO envDTO){

        TreeItem<String> environment = new TreeItem<>("Environment Variables");
        for(PropertyDTO propertyDTO : envDTO.getEnvProps().values()){
            TreeItem <String> env = new TreeItem<>(propertyDTO.getName());
            environment.getChildren().add(env);
        }
        return environment;


    }

    @FXML
    void selectedItem(MouseEvent event) {

        TreeItem<String> item = (TreeItem<String>) this.treeView.getSelectionModel().getSelectedItem();
        //this.detailTextArea.clear();

        this.infoLabel.setVisible(true);
        this.infoDetails = new SimpleStringProperty();
        infoLabel.textProperty().bind(infoDetails);
        WorldInfoDTO worldInfoDTO = mainController.getWorldInfoDTO();
        if(item != null && item.isLeaf() ){

            setSelectionPath(item);
            Integer indexSelection = item.getParent().getChildren().indexOf(item);
            addDetail(worldInfoDTO,indexSelection);
        }
    }

    public void setSelectionPath(TreeItem<String> selectedItem){
        this.selectionPath = new ArrayList<>();
        TreeItem<String> temp = selectedItem;
        while(temp != null){
            this.selectionPath.add(temp.getValue());
            temp = temp.getParent();
        }
        Collections.reverse(this.selectionPath);
    }

    //TODO add method that get the details per chosen item: will be update for any part of the world to been add to the tree view, add grid and action
    //
    public void addDetail(WorldInfoDTO worldInfoDTO,Integer indexSelection){
        for(int i = 0;i < this.selectionPath.size();i++){
            if(selectionPath.get(i).equals("Entities")){
                String entityType = selectionPath.get(i+1);
                addEntityInfo(entityType);
                break;
            } else if (selectionPath.get(i).equals("Environment Variables")) {
                String enivronmentName = selectionPath.get(i+1);
                addEnvironemtInfo(enivronmentName);
                break;

            } else if (selectionPath.get(i).equals("Rules")) {
                String ruleName = selectionPath.get(i+1);
                if(selectionPath.get(i+2).equals("Actions")){
                    String actionName = selectionPath.get(i+3);
                    addActionInfo(ruleName,actionName,indexSelection);
                    break;
                }
                else {
                    addActivationInfo(ruleName);
                    break;

                }

            } else if (selectionPath.get(i).equals("Termination")) {

                addTerminationInfo();
                break;

            } else if (selectionPath.get(i).equals("Grid")) {
                addGridInfo();
                break;
            }
        }

    }

    public void addGridInfo(){
        WorldInfoDTO worldInfoDTO = mainController.getWorldInfoDTO();
        GridDTO gridDTO= worldInfoDTO.getGridDTO();
        String info = gridDTO.getGridInfo().toString();
        this.infoDetails.set(info);
        this.infoLabel.setVisible(true);

    }

    public void addTerminationInfo(){
        WorldInfoDTO worldInfoDTO = mainController.getWorldInfoDTO();
        TerminationDTO terminationDTO = worldInfoDTO.getTerminationDTO();
        String info = terminationDTO.getTerminationInfo().toString();
        this.infoDetails.set(info);
        this.infoLabel.setVisible(true);


    }

    public void addActionInfo(String ruleName, String actionName,Integer indexSelection){
        WorldInfoDTO worldInfoDTO = mainController.getWorldInfoDTO();
        int i = 0;
        while(!worldInfoDTO.getRulesDTO().get(i).getName().equals(ruleName)){i++;} // get the right rule


        Set<Integer> keySet = worldInfoDTO.getRulesDTO().get(i).getActionDTOMap().keySet();
        List<Integer>listKeys = new ArrayList<>(keySet);
        int j ;
        int actionIndex = 0;
        // get the right action
       for( j = 0;j<listKeys.size();j++){
           if(listKeys.get(j).intValue() == indexSelection.intValue()){
               break;
           }
           else{
               actionIndex++;
           }
       }
        ActionDTO actionDTO = worldInfoDTO.getRulesDTO().get(i).getActionDTOMap().get(actionIndex);

       String info = actionDTO.getActionInfo().toString();

        this.infoDetails.set(info);
        this.infoLabel.setVisible(true);


    }
    public void addActivationInfo(String ruleName){
        WorldInfoDTO worldInfoDTO = mainController.getWorldInfoDTO();
        int i = 0;
        while(!worldInfoDTO.getRulesDTO().get(i).getName().equals(ruleName)){i++;} // get the right rule
        RuleDTO ruleDTO = worldInfoDTO.getRulesDTO().get(i);
        String info = ruleDTO.getActivation().toString();
        this.infoDetails.set(info);
        this.infoLabel.setVisible(true);
    }

    public void addEntityInfo(String entityType){
        WorldInfoDTO worldInfoDTO = mainController.getWorldInfoDTO();
        int i = 0;
        while(!worldInfoDTO.getEntDTO().get(i).getName().equals(entityType)){i++;}
        EntityDTO entityDTO = worldInfoDTO.getEntDTO().get(i);
        String info = entityDTO.getEntityInfo().toString();
        this.infoDetails.set(info);
        this.infoLabel.setVisible(true);
    }

    //TODO check if theres a reason to show the environemt details
    public void addEnvironemtInfo(String environmentName ){
        WorldInfoDTO worldInfoDTO = mainController.getWorldInfoDTO();
        String info = worldInfoDTO.getEnvDTO().getEnvironmentInfo(environmentName).toString();
        this.infoDetails.set(info);
        this.infoLabel.setVisible(true);

    }








}
