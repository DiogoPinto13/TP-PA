package pt.isec.pa.apoio_poe.ui.gui.resources;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.PhaseManager;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;

import pt.isec.pa.apoio_poe.ui.gui.ToastMessage;

public class tieController implements Initializable{
    PhaseManager model;


    @FXML
    Pane root;

    @FXML
    VBox buttonVbox;
    @FXML
    ListView<String> lookupView;
    @FXML
    Button solveTieButton;
        
    @FXML
    Button lookupConfig;


    public tieController(PhaseManager model){
        this.model = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        registerHandlers();
       // update();
       updateState();
    }

    private void registerHandlers(){
        model.addObserver(PhaseManager.LOAD_STRING, (evt) -> {
            updateState();
        });
        model.addObserver(PhaseManager.CHANGE_STATE, (evt) -> {
            updateState();
        });
    }

    private void updateState(){
        if(root.getScene() != null)
            switch (model.getState()) {
                case CONFIG:     
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/config_menu.fxml"));
                    loader.setController(new ConfigController(model));
                    try {
                        root.getScene().setRoot(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }      
                    break;
                case CONFIG_CLOSED:
                    FXMLLoader loaderc = new FXMLLoader(getClass().getResource("fxml/config_menu.fxml"));
                    loaderc.setController(new ConfigController(model));
                    try {
                        root.getScene().setRoot(loaderc.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }      
                    break;
                case APPLICATON_OPT:
                    FXMLLoader loader0 = new FXMLLoader(getClass().getResource("fxml/applicationOpt.fxml"));
                    loader0.setController(new ApplicationOptController(model));
                    try {
                        root.getScene().setRoot(loader0.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case APPLICATON_OPT_CLOSED:
                    FXMLLoader loader01 = new FXMLLoader(getClass().getResource("fxml/applicationOpt.fxml"));
                    loader01.setController(new ApplicationOptController(model));
                    try {
                        root.getScene().setRoot(loader01.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case PROPOSAL_ATR:
                    FXMLLoader loader1 = new FXMLLoader(getClass().getResource("fxml/proposalAtr.fxml"));
                    loader1.setController(new ProposalAtrController(model));
                    try {
                        root.getScene().setRoot(loader1.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case PROPOSAL_ATR_CLOSED:
                    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("fxml/proposalAtr.fxml"));
                    loader2.setController(new ProposalAtrController(model));
                    try {
                        root.getScene().setRoot(loader2.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break; 
                case TIESTATE:
                    break;
                case SUPREVISER_ATR:
                    FXMLLoader loader3 = new FXMLLoader(getClass().getResource("fxml/superviserAtr.fxml"));
                    loader3.setController(new SuperviserAtrController(model));
                    try {
                        root.getScene().setRoot(loader3.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case SUPREVISER_ATR_CLOSED:
                    FXMLLoader loader34 = new FXMLLoader(getClass().getResource("fxml/superviserAtr.fxml"));
                    loader34.setController(new SuperviserAtrController(model));
                    try {
                        root.getScene().setRoot(loader34.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case LOOKUP:
                    FXMLLoader loader4 = new FXMLLoader(getClass().getResource("fxml/lookup.fxml"));
                    loader4.setController(new LookupController(model));
                    try {
                        root.getScene().setRoot(loader4.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
    }

    @FXML 
    public void solveTie(){

        Dialog dialog = new Dialog();
        dialog.setTitle("Solve tie");
        dialog.setHeaderText("Choose the student to received the tied proposal" );
        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType importButtonType = new ButtonType("Solve", ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(importButtonType, ButtonType.CANCEL);

        ObservableList<Student> options =
        FXCollections.observableArrayList(model.getTiedStudents());
        //List<ImportTypes> options = model.getImports();
        ComboBox<Student> comboBox = new ComboBox<Student>(options);

        comboBox.getSelectionModel().selectFirst();

        dialogPane.setContent(new VBox(8,comboBox));


        
        Optional<ButtonType> optionalResult = dialog.showAndWait();

        if(optionalResult.get() == importButtonType){
            if(comboBox.getValue() == null){
                return;
            }
            if(root.getScene() != null){
                ToastMessage.show(root.getScene().getWindow(), model.solveTie(comboBox.getValue()));
                model.automaticAvailableProposals();
               //FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/proposalAtr.fxml"));
               //loader.setController(new ProposalAtrController(model));
               //try {
               //    root.getScene().setRoot(loader.load());
               //    model.automaticAvailableProposals();
               //} catch (IOException e) {
               //    e.printStackTrace();
               //}
            }
        }
        
    }

    @FXML 
    public void lookup(ActionEvent event){
        
        Dialog dialog = new Dialog();
        dialog.setTitle("Candidacy proposal lookup");
        dialog.setHeaderText("Please choose a student to see their candidacy proposals");
        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType importButtonType = new ButtonType("Lookup", ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(importButtonType, ButtonType.CANCEL);

        ObservableList<Student> options =
        FXCollections.observableArrayList(model.getTiedStudents());
        //List<ImportTypes> options = model.getImports();
        ComboBox<Student> comboBox = new ComboBox<Student>(options);

        comboBox.getSelectionModel().selectFirst();

        dialogPane.setContent(new VBox(8,comboBox));


        
        Optional<ButtonType> optionalResult = dialog.showAndWait();

        if(optionalResult.get() == importButtonType){
            if(comboBox.getValue() == null){
                return;
            }
            for(Proposal p : model.getCandidacyProposals(comboBox.getValue())){
                lookupView.getItems().addAll(p.toString());
            }
            
        }


    }

    @FXML
    public void save(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File save...");
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Data (*.dat)","*.dat"),
            new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if(file != null){
            model.saveFile(file);
        }
    }

    @FXML
    public void load(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File open...");
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Data (*.dat)","*.dat"),
            new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if(file != null){
            model.loadFile(file);
        }
    }

    @FXML
    public void undo(){
        model.undo();
    }
    
    @FXML
    public void redo(){
        model.redo();
    }

}
