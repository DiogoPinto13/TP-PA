package pt.isec.pa.apoio_poe.ui.gui.resources;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.PhaseManager;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.data.Teacher;
import pt.isec.pa.apoio_poe.model.fsm.ImportTypes;
import pt.isec.pa.apoio_poe.model.fsm.Options;
import pt.isec.pa.apoio_poe.ui.gui.ToastMessage;

public class ConfigController implements Initializable{
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

    private Pane rootSaved;

    ImportTypes importID;
    Integer choosenLookup;


    public ConfigController(PhaseManager model){
        this.model = model;
        rootSaved = root;
    } 

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        registerHandlers();
        update();
        lockEvent();
    }
    
    private void updateState(){
        if(root.getScene() != null){
            switch (model.getState()) {
                case CONFIG:           
                    break;
                case CONFIG_CLOSED:
                    lockEvent();
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
                    FXMLLoader loader0 = new FXMLLoader(getClass().getResource("fxml/applicationOpt.fxml"));
                    loader0.setController(new ApplicationOptController(model));
                    try {
                        root.getScene().setRoot(loader0.load());
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
                    FXMLLoader loader12 = new FXMLLoader(getClass().getResource("fxml/proposalAtr.fxml"));
                    loader12.setController(new ProposalAtrController(model));
                    try {
                        root.getScene().setRoot(loader12.load());
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
                        FXMLLoader loader5 = new FXMLLoader(getClass().getResource("fxml/superviserAtr.fxml"));
                        loader5.setController(new SuperviserAtrController(model));
                        try {
                            root.getScene().setRoot(loader5.load());
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
        model.addObserver(PhaseManager.TEACHER_STRING, (evt) -> {
            update();
        });
        model.addObserver(PhaseManager.STUDENT_STRING, (evt) -> {
            update();
        });
        model.addObserver(PhaseManager.PROPOSAL_STRING, (evt) -> {
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
            case LOCK:
                lockConfig.setDisable(false);
            case NEXT:
                nextConfig.setDisable(false);
            default:
                break;
        }
    }

    }
    
    private void update(){
        TreeItem<String> root = new TreeItem<>("Config Phase");
        TreeItem<String> teachers = new TreeItem<>("Teachers");
        TreeItem<String> students = new TreeItem<>("Students");
        TreeItem<String> proposals = new TreeItem<>("Proposals");

        //root is the parent of itemChild
        root.getChildren().add(students);
        for(Student s : model.getStudents()){
            TreeItem<String> student = new TreeItem<>(s.toString());
            students.getChildren().add(student);
        }
        TreeItem<String> studentsNumber = new TreeItem<>("Number of students");
        students.getChildren().add(studentsNumber);
        TreeItem<String> totalStudents = new TreeItem<>("Total: " + model.getStudents().size());
        studentsNumber.getChildren().add(totalStudents);
      


        root.getChildren().add(teachers);
        for(Teacher t : model.getTeachers()){
            TreeItem<String> teacher = new TreeItem<>(t.toString());
            teachers.getChildren().add(teacher);
        }
        TreeItem<String> teachersNumber = new TreeItem<>("Number of teachers");
        teachers.getChildren().add(teachersNumber);
        TreeItem<String> totalTeachers = new TreeItem<>("Total: " + model.getTeachers().size());
        teachersNumber.getChildren().add(totalTeachers);

        root.getChildren().add(proposals);
        for(Proposal p : model.getProposals()){
            TreeItem<String> proposal = new TreeItem<>(p.toString());
            proposals.getChildren().add(proposal);
        }
        TreeItem<String> proposalsNumber = new TreeItem<>("Number of proposals");
        proposals.getChildren().add(proposalsNumber);
        TreeItem<String> totalProposals = new TreeItem<>("Total: " + model.getStudents().size());
        proposalsNumber.getChildren().add(totalProposals);



        root.setExpanded(true);
        phaseViewPanel.setRoot(root);
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
            if(comboBox.getValue() == null){
                return;
            }
            importID = comboBox.getValue();
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
        model.exportToCSV();
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
        TextField textField = new TextField();


        textField.setOnKeyTyped((evt)->{
            textField.getStyleClass().remove("invalid");
            }
        );

        comboBox.setOnAction((evt) ->{
            if(comboBox.getValue() == null){
                return;
            }
            switch (comboBox.getValue()) {
                case GET_STUDENT_BY_ID:
                    textField.setPromptText("Insert a student ID here");
                    break;
                case GET_TEACHER_BY_EMAIL:
                    textField.setPromptText("Insert teacher email here");
                    break;
                case GET_PROPOSALS_BY_ID:
                    textField.setPromptText("Insert proposal ID here");
                    break;
                
                default:
                    
                    break;
            }
        });

        comboBox.getSelectionModel().selectFirst();

        comboBox.fireEvent(new ActionEvent());
        
            

        dialogPane.setContent(new VBox(8,comboBox,textField));



        

        
        final Button btImport = (Button) dialog.getDialogPane().lookupButton(importButtonType);
        btImport.addEventFilter(ActionEvent.ACTION, evt -> {
            if (textField.getText().equals("")) {
                textField.getStyleClass().add("invalid");
                CSSManager.applyCSS(textField,"buttons.css");
                evt.consume();
            }
        });




        Optional<ButtonType> optionalResult = dialog.showAndWait();

        if(optionalResult.get() == importButtonType){
            if(comboBox.getValue() == null){
                return;
            }
            switch (comboBox.getValue()) {
                case GET_STUDENT_BY_ID:
                    String str = textField.getText();
                    String strNew = str.replaceAll("([a-zA-Z])", "");
                    if(model.getStudentByID(Long.valueOf(strNew))!= null)
                        lookupView.getItems().add(model.getStudentByID(Long.valueOf(strNew)).toString());
                    break;
                case GET_TEACHER_BY_EMAIL:
                    if(model.getTeacherByEmail(textField.getText()) != null)
                        lookupView.getItems().add(model.getTeacherByEmail(textField.getText()).toString());
                    break;
                case GET_PROPOSALS_BY_ID:
                        if(model.getProposalByID(textField.getText()) != null)
                    lookupView.getItems().add(model.getProposalByID(textField.getText()).toString());
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

