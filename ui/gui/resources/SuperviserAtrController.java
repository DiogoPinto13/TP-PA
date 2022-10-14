package pt.isec.pa.apoio_poe.ui.gui.resources;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.main;
import pt.isec.pa.apoio_poe.model.PhaseManager;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.data.Teacher;
import pt.isec.pa.apoio_poe.model.data.Phase.Filters;
import pt.isec.pa.apoio_poe.model.fsm.ImportTypes;
import pt.isec.pa.apoio_poe.model.fsm.LookupState;
import pt.isec.pa.apoio_poe.model.fsm.Options;
import pt.isec.pa.apoio_poe.ui.gui.ToastMessage;

public class SuperviserAtrController implements Initializable{
    PhaseManager model;
    
    @FXML
    public Pane root;
    @FXML
    public VBox buttonVbox;
    @FXML
    public HBox radioButtonHBox ;
    @FXML
    public Button lookupConfig;
    @FXML
    public Button nextConfig;
    @FXML
    public Button lockConfig;
    @FXML
    public Button exportcsv;
    @FXML
    public Button manageSuperviser;
    @FXML
    public Button automaticSuperviser;
    @FXML
    public Button previousConfig;
    @FXML
    public Button exportCSVConfig;
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

    Scene scene;
    ImportTypes importID;


    public SuperviserAtrController(PhaseManager model){
        this.model = model;

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        scene = root.getScene();
        registerHandlers();
        update();
        updateState();
        lockEvent();
    }
    
    private void updateState(){
        if(root.getScene() != null)
            switch (model.getState()) {
                case CONFIG:     
                    FXMLLoader loader0 = new FXMLLoader(getClass().getResource("fxml/config_menu.fxml"));
                    loader0.setController(new ConfigController(model));
                    try {
                        root.getScene().setRoot(loader0.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }      
                    break;
                case CONFIG_CLOSED:
                    FXMLLoader loader01 = new FXMLLoader(getClass().getResource("fxml/config_menu.fxml"));
                    loader01.setController(new ConfigController(model));
                    try {
                        root.getScene().setRoot(loader01.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }      
                    break;
                case APPLICATON_OPT:
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/applicationOpt.fxml"));
                    loader.setController(new ApplicationOptController(model));
                    try {
                        root.getScene().setRoot(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case APPLICATON_OPT_CLOSED:
                    FXMLLoader loader02 = new FXMLLoader(getClass().getResource("fxml/applicationOpt.fxml"));
                    loader02.setController(new ApplicationOptController(model));
                    try {
                        root.getScene().setRoot(loader02.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case TIESTATE:
                    FXMLLoader loader1 = new FXMLLoader(getClass().getResource("fxml/tie.fxml"));
                    loader1.setController(new tieController(model));
                    try {
                        root.getScene().setRoot(loader1.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case SUPREVISER_ATR:
                    break;
                case SUPREVISER_ATR_CLOSED:
                    lockEvent();
                    break;
                case PROPOSAL_ATR:
                    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("fxml/proposalAtr.fxml"));
                    loader2.setController(new ProposalAtrController(model));
                    try {
                        root.getScene().setRoot(loader2.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                break;
                case PROPOSAL_ATR_CLOSED:
                    FXMLLoader loader21 = new FXMLLoader(getClass().getResource("fxml/proposalAtr.fxml"));
                    loader21.setController(new ProposalAtrController(model));
                    try {
                        root.getScene().setRoot(loader21.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
    
    private void registerHandlers(){
        model.addObserver(PhaseManager.AUTOMATIC_ASSIGN_SUPERVISER_STRING, (evt) -> {
            update();
        });
        model.addObserver(PhaseManager.MANAGE_SUPERVISERS_STRING, (evt) -> {
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
            case AUTOMATIC_SUPERVISERS:
                automaticSuperviser.setDisable(false);
                break;
            case MANAGE_SUPERVISERS:
                manageSuperviser.setDisable(false);
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
            case EXPORT:
                exportCSVConfig.setDisable(false);
                break;
            default:
                break;
        }
        }

    }
    
    private void update(){
        TreeItem<String> root = new TreeItem<>("Superviser Atribution Phase");
        TreeItem<String> teachers = new TreeItem<>("Teachers");
        TreeItem<String> students = new TreeItem<>("Students");


        TreeItem<String> proposalAndSuperviser= new TreeItem<>("With assigned proposal and superviser");
        for(Student s : model.getStudentsWithProposalAndSuperviser()){
            TreeItem<String> student = new TreeItem<>(s.toString());
            proposalAndSuperviser.getChildren().add(student);
        }
        TreeItem<String> proposalAndNoSuperviser= new TreeItem<>("With assigned proposal and without superviser");
        for(Student s : model.getStudentsWithProposalAndWithoutSuperviser()){
            TreeItem<String> student = new TreeItem<>(s.toString());
            proposalAndNoSuperviser.getChildren().add(student);
        }


        root.getChildren().add(students);
        for(Student s : model.getStudents()){
            TreeItem<String> student = new TreeItem<>(s.toString());
            students.getChildren().add(student);
        }
        TreeItem<String> studentsNumber = new TreeItem<>("Number of students");
        students.getChildren().add(studentsNumber);
        students.getChildren().add(proposalAndNoSuperviser);
        students.getChildren().add(proposalAndSuperviser);

        TreeItem<String> totalStudents = new TreeItem<>("Total: " + model.getStudents().size());
        studentsNumber.getChildren().add(totalStudents);
      


        TreeItem<String> maxSupervisions = new TreeItem<>("Max supervisions: " + model.getSuperviserWithMaxSupervisions());
        TreeItem<String> averageSupervisions = new TreeItem<>("Average supervisions: " + model.getSuperviserWithAvegareSupervisions());
        TreeItem<String> minSupervisions = new TreeItem<>("Min supervisions: " + model.getSuperviserWithMinSupervisions());
        

        root.getChildren().add(teachers);
        for(Teacher t : model.getTeachers()){
            TreeItem<String> teacher = new TreeItem<>(t.toString());
            teachers.getChildren().add(teacher);
        }
        TreeItem<String> teachersNumber = new TreeItem<>("Number of teachers");
        teachers.getChildren().add(teachersNumber);
        teachers.getChildren().add(maxSupervisions);
        teachers.getChildren().add(averageSupervisions);
        teachers.getChildren().add(minSupervisions);
        
        TreeItem<String> totalTeachers = new TreeItem<>("Total: " + model.getTeachers().size());
        teachersNumber.getChildren().add(totalTeachers);




        root.setExpanded(true);
        phaseViewPanel.setRoot(root);
    }
    @FXML
    public void export(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File save...");
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSVs (*.csv)","*.csv"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if(file != null){
            model.exportToCSVFile(file);
        }
    }
    @FXML
    public void autoAtr(ActionEvent event){

       ToastMessage.show(root.getScene().getWindow(), model.automaticSuperviserStudentProposalAssociation());


    }
    @FXML
    public void manualAtr(ActionEvent event){
        Dialog dialog = new Dialog();
        dialog.setTitle("Manual Attribution");
        dialog.setHeaderText("Choose a proposal and a student");
        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType assignButtonType = new ButtonType("Assign", ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        

        ObservableList<Options> options =
        FXCollections.observableArrayList(model.manageSupervisers());
        //List<ImportTypes> options = model.getImports();
        ComboBox<Options> comboBoxOptions = new ComboBox<Options>(options);
        ComboBox<Student> lookUpSuperviser = new ComboBox<Student>();
        ComboBox<Teacher> supervisers = new ComboBox<Teacher>();

        comboBoxOptions.getSelectionModel().selectFirst();

        
        
        //options.add(Options.MANUAL_ASSIGN_SUPERVISER);
        //options.add(Options.MANUAL_REMOVE_SUPERVISER);
        //options.add(Options.LOOKUP_SUPERVISER);
        //options.add(Options.EDIT_SUPERVISER);

        comboBoxOptions.setOnAction((evt->{
            switch (comboBoxOptions.getValue()) {
                case MANUAL_ASSIGN_SUPERVISER:{
                //dialogPane.setContent(new VBox(8,comboBoxOptions));
                    if(model.getStudentsWithProposalAndWithoutSuperviser() == null){
                        dialog.setHeaderText("No students to assign proposal to");
                        Optional<ButtonType> optionalResult = dialog.showAndWait();
                        if(optionalResult.get() == assignButtonType){
                            
                        }
                        return;
                    }
                    lookUpSuperviser.getItems().clear();
                    ObservableList<Student> studentsWithoutSuperviser =
                    FXCollections.observableArrayList(model.getStudentsWithProposalAndWithoutSuperviser());

                    lookUpSuperviser.getItems().addAll(studentsWithoutSuperviser);
                    lookUpSuperviser.getSelectionModel().selectFirst();

                    lookUpSuperviser.fireEvent(new ActionEvent());
                    dialogPane.setContent(new VBox(8,comboBoxOptions,lookUpSuperviser,supervisers));

                    break;
                }
                case MANUAL_REMOVE_SUPERVISER:{
                    if(model.getStudentsWithProposalAndSuperviser() == null){
                        dialog.setHeaderText("No students to remove supervisers");
                        Optional<ButtonType> optionalResult = dialog.showAndWait();
                        if(optionalResult.get() == assignButtonType){
                            
                        }
                        return;
                    }
                    lookUpSuperviser.getItems().clear();
                    ObservableList<Student> studentsWithSuperviser =
                    FXCollections.observableArrayList(model.getStudentsWithProposalAndSuperviser());

                    lookUpSuperviser.getItems().addAll(studentsWithSuperviser);
                    lookUpSuperviser.getSelectionModel().selectFirst();

                    dialogPane.setContent(new VBox(8,comboBoxOptions,lookUpSuperviser));
                    
                    break; 
                }
                case LOOKUP_SUPERVISER:{
                    if(model.getStudentsWithProposalAndSuperviser() == null){
                        dialog.setHeaderText("No students to do the lookup");
                        Optional<ButtonType> optionalResult = dialog.showAndWait();
                        if(optionalResult.get() == assignButtonType){
                            
                        }
                        return;
                    }
                    lookUpSuperviser.getItems().clear();
                    ObservableList<Student> studentsWithSuperviser =
                    FXCollections.observableArrayList(model.getStudentsWithProposalAndSuperviser());

                    lookUpSuperviser.getItems().addAll(studentsWithSuperviser);
                    lookUpSuperviser.getSelectionModel().selectFirst();

                    dialogPane.setContent(new VBox(8,comboBoxOptions,lookUpSuperviser));
                

                    break;
                }
                case EDIT_SUPERVISER:
                    if(model.getStudentsWithProposalAndSuperviser() == null){
                        dialog.setHeaderText("No superviser to edit");
                        Optional<ButtonType> optionalResult = dialog.showAndWait();
                        if(optionalResult.get() == assignButtonType){
                            
                        }
                        return;
                    }
                    lookUpSuperviser.getItems().clear();
                    ObservableList<Student> studentsWithSuperviser =
                    FXCollections.observableArrayList(model.getStudentsWithProposalAndSuperviser());

                    lookUpSuperviser.getItems().addAll(studentsWithSuperviser);
                    lookUpSuperviser.getSelectionModel().selectFirst();

                    lookUpSuperviser.fireEvent(new ActionEvent());
                    dialogPane.setContent(new VBox(8,comboBoxOptions,lookUpSuperviser,supervisers));

                    break;
 
                default:
                    break;
            }

        }));


        lookUpSuperviser.setOnAction((evt)->{
            if(comboBoxOptions.getValue() == Options.MANUAL_ASSIGN_SUPERVISER){
                if(lookUpSuperviser.getValue() != null){
                    supervisers.getItems().clear();
                    
                    ObservableList<Teacher> availableSupervisers =
                    FXCollections.observableArrayList(model.getUnassignedSupervisers(lookUpSuperviser.getValue()));

                    supervisers.getItems().addAll(availableSupervisers);

                    supervisers.getSelectionModel().selectFirst();
                }
            } else if(comboBoxOptions.getValue() == Options.EDIT_SUPERVISER){
                if(lookUpSuperviser.getValue() != null){
                    supervisers.getItems().clear();
                    
                    ObservableList<Teacher> availableSupervisers =
                    FXCollections.observableArrayList(model.getUnassignedSupervisers(lookUpSuperviser.getValue()));

                    supervisers.getItems().addAll(availableSupervisers);

                    supervisers.getSelectionModel().selectFirst();
                }
            }


        });
    
        comboBoxOptions.fireEvent(new ActionEvent());
        dialogPane.getScene().getWindow().sizeToScene();
        //comboBoxStudents.setOnAction((evt)->{
        //    comboBoxProposals.getItems().clear();
        //    comboBoxProposals.getItems().addAll(model.getAvailableProposals(comboBoxStudents.getValue()));
        //    comboBoxProposals.getSelectionModel().selectFirst();
        //});

        //comboBoxStudents.fireEvent(new ActionEvent());
    
        //comboBoxStudents.getSelectionModel().selectFirst();
        //System.out.println(comboBoxStudents.getValue());
        
        // ComboBox<String> comboBoxProposal = new ComboBox<String>(options);

        //comboBox.getSelectionModel().selectFirst();

        

        Optional<ButtonType> optionalResult = dialog.showAndWait();
        if(optionalResult.get() == assignButtonType){
            //ToastMessage.show(root.getScene().getWindow(), model.manualAssignProposal(comboBoxStudents.getValue(),comboBoxProposals.getValue()));
            switch (comboBoxOptions.getValue()) {
                case MANUAL_ASSIGN_SUPERVISER:
                    ToastMessage.show(root.getScene().getWindow(),model.manualAssignSuperviser(lookUpSuperviser.getValue(),supervisers.getValue().getEmail()));
                    break;
                case MANUAL_REMOVE_SUPERVISER:
                    ToastMessage.show(root.getScene().getWindow(),model.removeSuperviser(lookUpSuperviser.getValue()));
                    break; 
                case LOOKUP_SUPERVISER:
                    lookupView.getItems().add(model.getSuperviser(lookUpSuperviser.getValue()).toString());
                    break;
                case EDIT_SUPERVISER:
                    ToastMessage.show(root.getScene().getWindow(),model.manualAssignSuperviser(lookUpSuperviser.getValue(),supervisers.getValue().getEmail()));
                    break;
                default:
                    break;
            }
            
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
        ComboBox<Teacher> comboBoxTeacher = new ComboBox<Teacher>();
        //TextField textField = new TextField();

        //dialogPane.setContent(new VBox(8,comboBox));



        comboBox.setOnAction((evt) ->{
            if(comboBox.getValue() == null){
                return;
            }
            switch (comboBox.getValue()) {
                case GET_STUDENTS_WITH_ASSIGNED_PROPOSAL_AND_SUPERVISER:
                    dialogPane.setContent(new VBox(8,comboBox));
                    break;
                case GET_STUDENTS_WITH_ASSIGNED_PROPOSAL_AND_WITHOUT_SUPERVISER:
                    dialogPane.setContent(new VBox(8,comboBox));
                    break;
                case GET_NUMBER_OF_SUPERVISIONS_FOREACH_SUPERVISOR:
                    dialogPane.setContent(new VBox(8,comboBox));
                    break;
                case GET_MAX_SUPERVISIONS:
                    dialogPane.setContent(new VBox(8,comboBox));
                    break;
                case GET_AVERAGE_SUPERVISIONS:
                    dialogPane.setContent(new VBox(8,comboBox));
                    break;
                case GET_MIN_SUPERVISIONS:
                    dialogPane.setContent(new VBox(8,comboBox));
                    break;
                case GET_SUPERVISIONS_BY_SUPERVISER:
                    
                    ObservableList<Teacher> teachers =
                    FXCollections.observableArrayList(model.getTeachers());

                    comboBoxTeacher.getItems().clear();
                    comboBoxTeacher.getItems().addAll(teachers);
                    dialogPane.setContent(new VBox(8,comboBox,comboBoxTeacher));
                    break;
                default:
                    break;
            }
            dialogPane.getScene().getWindow().sizeToScene();
        });


        comboBox.getSelectionModel().selectFirst();
        comboBox.fireEvent(new ActionEvent());

        Optional<ButtonType> optionalResult = dialog.showAndWait();

        if(optionalResult.get() == importButtonType){
            if(comboBox.getValue() == null){
                return;
            }
            switch (comboBox.getValue()) {
                case GET_STUDENTS_WITH_ASSIGNED_PROPOSAL_AND_SUPERVISER:
                    for(Student s : model.getStudentsWithProposalAndSuperviser())
                        lookupView.getItems().add(s.toString());
                    break;
                case GET_STUDENTS_WITH_ASSIGNED_PROPOSAL_AND_WITHOUT_SUPERVISER:
                    for(Student s : model.getStudentsWithProposalAndWithoutSuperviser())
                        lookupView.getItems().add(s.toString());
                    break;
                case GET_NUMBER_OF_SUPERVISIONS_FOREACH_SUPERVISOR:
                    HashMap<String ,Long> map = model.getSupervisionNumberBySuperviser();
                    for(String t : model.getSupervisionNumberBySuperviser().keySet())
                        lookupView.getItems().add(t.toString() + ":" + map.get(t));
                    break;
                case GET_MAX_SUPERVISIONS:
                    lookupView.getItems().add(String.valueOf(model.getSuperviserWithMaxSupervisions()));
                    break;
                case GET_AVERAGE_SUPERVISIONS:
                    lookupView.getItems().add(String.valueOf(model.getSuperviserWithAvegareSupervisions()));
                    break;
                case GET_MIN_SUPERVISIONS:
                    lookupView.getItems().add(String.valueOf(model.getSuperviserWithMinSupervisions()));
                    break;
                case GET_SUPERVISIONS_BY_SUPERVISER:
                    lookupView.getItems().add(String.valueOf(model.getSupervisionsFromSuperviser(comboBoxTeacher.getValue())));
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

