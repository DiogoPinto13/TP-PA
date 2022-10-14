package pt.isec.pa.apoio_poe.ui.gui.resources;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.PhaseManager;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.data.Phase.Filters;
import pt.isec.pa.apoio_poe.model.fsm.ImportTypes;
import pt.isec.pa.apoio_poe.model.fsm.Options;
import pt.isec.pa.apoio_poe.ui.gui.ToastMessage;

public class ApplicationOptController implements Initializable{
    PhaseManager model;
    
    @FXML
    public Pane root;
    @FXML
    public VBox buttonVbox;
    @FXML
    public HBox radioButtonHBox ;
    @FXML
    public Button importCSVConfig;
    @FXML
    public Button exportCSVConfig;
    @FXML
    public Button lookupConfig;
    @FXML
    public Button nextConfig;
    @FXML
    public Button previousConfig;
    @FXML
    public Button lockConfig;
    @FXML
    public MenuItem saveConfig;
    @FXML
    public MenuItem loadConfig;
    @FXML
    public MenuItem undoConfig;
    @FXML
    public MenuItem redoConfig;
    @FXML
    public ListView<String> lookupView;
    @FXML
    public TreeView<String> phaseViewPanel;


    ImportTypes importID;


    public ApplicationOptController(PhaseManager model){
        this.model = model;


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        registerHandlers();
        update();
        updateState();
        lockEvent();
    }
    
    private void updateState(){
        
        if(root.getScene() != null){
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
                    FXMLLoader loader0 = new FXMLLoader(getClass().getResource("fxml/config_menu.fxml"));
                    loader0.setController(new ConfigController(model));
                    try {
                        root.getScene().setRoot(loader0.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }  
                    break;
                case APPLICATON_OPT:
                    break;
                case APPLICATON_OPT_CLOSED:
                    lockEvent();
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
                    FXMLLoader loader01 = new FXMLLoader(getClass().getResource("fxml/proposalAtr.fxml"));
                    loader01.setController(new ProposalAtrController(model));
                    try {
                        root.getScene().setRoot(loader01.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case TIESTATE:
                    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("fxml/tie.fxml"));
                    loader2.setController(new tieController(model));
                    try {
                        root.getScene().setRoot(loader2.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    }
    
    private void registerHandlers(){

        model.addObserver(PhaseManager.CANDIDACY_STRING, (evt) -> {
            update();
        });
        model.addObserver(PhaseManager.UNDO_REDO_STRING, (evt) -> {
            undoConfig.setDisable(false);
            redoConfig.setDisable(false);
        });
        model.addObserver(PhaseManager.LOCK_STRING, (evt) -> {
            //importCSVConfig.setDisable(true);
            //importStudentsType.setDisable(true);
            //importTeachersType.setDisable(true);
            //importProposalsType.setDisable(true);
            lockEvent();
        });
        model.addObserver(PhaseManager.LOAD_STRING, (evt) -> {
            update();
            lockEvent();
            updateState();
        });
        model.addObserver(PhaseManager.CHANGE_STATE, (evt)->{
            updateState();
        });
    }   

    private void lockEvent(){

       for(Node n : buttonVbox.getChildren()){
        n.setDisable(true);
       }

       Options[] options = model.getOptions();
       for(Options option : options){
        switch (option) {
            case IMPORT:
                importCSVConfig.setDisable(false);
                break;
            case EXPORT:
                exportCSVConfig.setDisable(false);
                break;
            case LOOKUP:
                lookupConfig.setDisable(false);
                break;
            case LOCK:
                lockConfig.setDisable(false);
                break;
            case NEXT:
                nextConfig.setDisable(false);
                break;
            case PREVIOUS:
                previousConfig.setDisable(false);
                break;
            default:
                break;
        }
    }

    }
    
    private void update(){
        if(this.root.getScene() != null){
            TreeItem<String> root = new TreeItem<>("Application Option Phase");
            TreeItem<String> students = new TreeItem<>("Students");
            TreeItem<String> proposals = new TreeItem<>("Proposals");
            TreeItem<String> studentsWithCandidacy = new TreeItem<>("Students with candidacy");

            root.getChildren().add(students);
            root.getChildren().add(proposals);
            
            students.getChildren().add(studentsWithCandidacy);
            for(Student s : model.getStudentsWithCandidacy()){
                TreeItem<String> student = new TreeItem<>(s.toString());
                studentsWithCandidacy.getChildren().add(student);
            }
            
            TreeItem<String> studentsWithoutCandidacy = new TreeItem<>("Students without candidacy");
            students.getChildren().add(studentsWithoutCandidacy);
            for(Student s : model.getStudentsWithoutCandidacy()){
                TreeItem<String> student = new TreeItem<>(s.toString());
                studentsWithoutCandidacy.getChildren().add(student);
            }

            

            TreeItem<String> proposalsWithCandidacy = new TreeItem<>("Proposals with candidacy");
            proposals.getChildren().add(proposalsWithCandidacy);
            for(Proposal p : model.getProposalsWithCandidacy()){
                TreeItem<String> proposal = new TreeItem<>(p.toString());
                proposalsWithCandidacy.getChildren().add(proposal);
            }

            TreeItem<String> proposalsWithoutCandidacy = new TreeItem<>("Proposals without candidacy");
            proposals.getChildren().add(proposalsWithoutCandidacy);
            for(Proposal p : model.getProposalsWithoutCandidacy()){
                TreeItem<String> proposal = new TreeItem<>(p.toString());
                proposalsWithoutCandidacy.getChildren().add(proposal);
            }

            
            TreeItem<String> proposalsNumber = new TreeItem<>("Number of proposals");
            proposals.getChildren().add(proposalsNumber);
            TreeItem<String> totalProposals = new TreeItem<>("Total: " + model.getStudents().size());
            proposalsNumber.getChildren().add(totalProposals);

            TreeItem<String> studentsNumber = new TreeItem<>("Number of students");
            students.getChildren().add(studentsNumber);
            TreeItem<String> totalStudents = new TreeItem<>("Total: " + model.getStudents().size());
            students.getChildren().add(totalStudents);
            

            root.setExpanded(true);
            phaseViewPanel.setRoot(root);
        }

    }

    @FXML
    public void importCSV(ActionEvent event){

        Dialog dialog = new Dialog();
        dialog.setTitle("Choose import");
        dialog.setHeaderText("import type");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ObservableList<ImportTypes> options =
        FXCollections.observableArrayList(model.getImports());
        //List<ImportTypes> options = model.getImports();
        ComboBox<ImportTypes> comboBox = new ComboBox<ImportTypes>(options);
        
        comboBox.getSelectionModel().selectFirst();

        dialogPane.setContent(new VBox(8,comboBox));


        Optional<ButtonType> optionalResult = dialog.showAndWait();
        if(optionalResult.get() == ButtonType.OK){
            importID = comboBox.getValue();
            if(importID == null)
                return;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSVs (*.csv)","*.csv"),
                new FileChooser.ExtensionFilter("All files", "*.*")
            );
            File file = fileChooser.showOpenDialog(root.getScene().getWindow());
            if(file != null){
                ToastMessage.show(root.getScene().getWindow(),model.importStudent(file.getPath(), importID));
            }
        } else{
            
        }


    }
    @FXML
    public void exportCSV(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File save...");
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSVs (*.csv)","*.csv"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if(file != null){
            //model.exportToCSV();
            model.exportToCSVFile(file);
        }
    }
    @FXML 
    public void lookup(ActionEvent event){
        
        Dialog dialog = new Dialog();
        dialog.setTitle("What do you wish to lookup");
        dialog.setHeaderText("Please choose one and insert the required info in the text field");
        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType importButtonType = new ButtonType("Lookup", ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(importButtonType, ButtonType.CANCEL);

        ObservableList<Options> options =
        FXCollections.observableArrayList(model.query());
        //List<ImportTypes> options = model.getImports();
        ComboBox<Options> comboBox = new ComboBox<Options>(options);
        ArrayList<Filters> selectedFilters = new ArrayList<Filters>();
        //TextField textField = new TextField();

        //dialogPane.setContent(new VBox(8,comboBox));

        comboBox.setOnAction((evt) ->{
            if(comboBox.getValue() == null){
                return;
            }
            switch (comboBox.getValue()) {
                case GET_STUDENT_AUTOPROPOSAL:
                    dialogPane.setContent(new VBox(8,comboBox));
                    break;
                case GET_STUDENTS_WITH_CANDIDACY:
                    dialogPane.setContent(new VBox(8,comboBox));
                    break;
                case GET_STUDENTS_WITHOUT_CANDIDACY:
                    dialogPane.setContent(new VBox(8,comboBox));
                    break;
                case GET_PROPOSALS_FILTERED:
                    final MenuButton  choices = new MenuButton("Select filters");

                    List<CheckMenuItem> itemList = Arrays.asList(new CheckMenuItem(Filters.AUTOPROPOSALS.toString())
                    ,new CheckMenuItem(Filters.PROPOSALS_WITHOUT_CANDIDACY.toString()),new CheckMenuItem(Filters.PROPOSALS_WITH_CANDIDACY.toString()),
                    new CheckMenuItem(Filters.TEACHERPROPOSALS.toString()));

                    choices.getItems().addAll(itemList);

                    for (CheckMenuItem item : itemList) {
                        item.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
                            if (newValue) {
                                selectedFilters.add(Filters.getEnum(item.getText()));
                            } else {
                                selectedFilters.remove(Filters.getEnum(item.getText()));
                            }
                        });
                    }

                    dialogPane.setContent(new VBox(8,comboBox,choices));
                    
                    

                    break;
                
                default:
                    
                    break;
            }
            dialogPane.getScene().getWindow().sizeToScene();
        });

        comboBox.getSelectionModel().selectFirst();

        comboBox.fireEvent(new ActionEvent());
        
        
            

       // dialogPane.setContent(new VBox(8,comboBox));



        

        
        final Button btImport = (Button) dialog.getDialogPane().lookupButton(importButtonType);
        btImport.addEventFilter(ActionEvent.ACTION, evt -> {

        });



        
        Optional<ButtonType> optionalResult = dialog.showAndWait();

        if(optionalResult.get() == importButtonType){
            if(comboBox.getValue() == null){
                return;
            }
            switch (comboBox.getValue()) {
                case GET_STUDENT_AUTOPROPOSAL:
                    for(Student s : model.getStudentsAutoProposal()){
                        lookupView.getItems().add(s.toString());
                    }
                    break;
                case GET_STUDENTS_WITHOUT_CANDIDACY:
                    for(Student s : model.getStudentsWithoutCandidacy()){
                        lookupView.getItems().add(s.toString());
                    }
                    break;
                case GET_STUDENTS_WITH_CANDIDACY:
                    for(Student s : model.getStudentsWithCandidacy()){
                        lookupView.getItems().add(s.toString());
                    }
                    break;
                case GET_PROPOSALS_FILTERED:
                    for(Proposal s : model.getProposalsFiltered(selectedFilters.toArray(new Filters[0]))){
                        lookupView.getItems().add(s.toString());
                    }
                break;
                default:
                    break;
            }
            
        } else{

        }


    }
    @FXML 
    public void next(ActionEvent event){
        model.next();
    }
    @FXML
    public void previous(ActionEvent event){
        model.previous();
    }

    @FXML 
    public void lock(ActionEvent event){
        ToastMessage.show(root.getScene().getWindow(),model.lock());
    }

    @FXML 
    public void save(ActionEvent event){
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
    public void load(ActionEvent event){
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
    public void undo(ActionEvent event){
        model.undo();
    }

    @FXML 
    public void redo(ActionEvent event){
        model.redo();
    }


}

