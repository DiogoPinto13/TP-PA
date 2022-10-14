package pt.isec.pa.apoio_poe.ui.gui.resources;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
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
import pt.isec.pa.apoio_poe.model.data.Branches;
import pt.isec.pa.apoio_poe.model.data.Internship;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.data.Teacher;
import pt.isec.pa.apoio_poe.model.fsm.Options;


public class LookupController implements Initializable {
    

    @FXML
    public Button btnStudentsAssignProp;
    @FXML
    public Button btnStdCandWithoutProp;
    @FXML
    public Button exportCSVConfig;
    @FXML
    public Button btnAvProp;
    @FXML
    public Button btnAssignProp;
    @FXML
    public Button btnNumSupeForSuper;
    @FXML
    public Button btnMaxNumSup;
    @FXML
    public Button btnAvrNumSup;
    @FXML
    public Button btnMinNumSup;
    @FXML
    public Button btnNumSuperSpecSup;

    @FXML
    public ListView<String> lookupView;

    @FXML
    public Pane root;

    @FXML
    public PieChart pieGraph;

    @FXML
    public BarChart barGraph;

    @FXML
    public BarChart companyBarGraph;

    @FXML
    public PieChart percentagePie;

    private PhaseManager model;



    public LookupController(PhaseManager model) {
        this.model = model;

    }

    @FXML
    public void studentsAssignProp(){
        for(Student s : model.getStudentsWithProposal())
            lookupView.getItems().add(s.toString());

    }
    @FXML
    public void stdCandWithoutProp(){
        for(Student s : model.getStudentsWithCandidacyAndWithoutProposals()){
            lookupView.getItems().add(s.toString());
        }
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
            //model.exportToCSV();
        }
    }
    @FXML
    public void avProp(){
        for(Proposal p : model.getAvailable()){
            lookupView.getItems().add(p.toString());
        }
    }
    @FXML
    public void assignProp(){
        for(Proposal p : model.getAssignedProposals()){
            lookupView.getItems().add(p.toString());
        }
    }
    @FXML
    public void numSupeForSuper(){
        HashMap<String ,Long> map = model.getSupervisionNumberBySuperviser();
        for(String t : model.getSupervisionNumberBySuperviser().keySet())
            lookupView.getItems().add(t.toString() + ":" + map.get(t));
    }
    @FXML
    public void maxNumSup(){
        lookupView.getItems().add(String.valueOf(model.getSuperviserWithMaxSupervisions()));

    }
    @FXML
    public void avrNumSup(){
        lookupView.getItems().add(String.valueOf(model.getSuperviserWithAvegareSupervisions()));
    }
    @FXML
    public void minNumSup(){
        lookupView.getItems().add(String.valueOf(model.getSuperviserWithMinSupervisions()));
    }
    @FXML
    public void numSuperSpecSup(){
        Dialog dialog = new Dialog();
        dialog.setTitle("Search superviser");
        dialog.setHeaderText("Choose a superviser to see his number of supervisions");
        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType assignButtonType = new ButtonType("Lookup", ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        

        ObservableList<Teacher> options =
        FXCollections.observableArrayList(model.getTeachers());

        ComboBox<Teacher> comboBoxTeachers = new ComboBox<Teacher>(options);
        comboBoxTeachers.getSelectionModel().selectFirst();

        dialogPane.setContent(new VBox(8,comboBoxTeachers));
        Optional<ButtonType> optionalResult = dialog.showAndWait();
        if(optionalResult.get() == assignButtonType){
            if(comboBoxTeachers.getValue() == null){
                return;
            }
            lookupView.getItems().add(String.valueOf(model.getSupervisionsFromSuperviser(comboBoxTeachers.getValue()) ));

        }
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




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        registerHandlers();
        update();
        root.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if(newScene != null){
                root.getScene().getWindow().sizeToScene();
            }
        });
        updateState();
    }

    private void registerHandlers(){

        model.addObserver(PhaseManager.LOAD_STRING, (evt) -> {
            update();
            updateState();
        });
        model.addObserver(PhaseManager.CHANGE_STATE, (evt)->{
            updateState();
        });

        
    }

    private void update(){

        
        // update do lookUpView e graphs
        PieChart.Data DA = new PieChart.Data("DA", 0);
        PieChart.Data RAS = new PieChart.Data("RAS"  , 0);
        PieChart.Data SI = new PieChart.Data("SI" , 0);
        XYChart.Series<String, Number> companytops = new XYChart.Series<String, Number>();


        PieChart.Data proposalAssigned = new PieChart.Data("Assigned", model.getAssignedProposals().size());
        PieChart.Data proposalNotAssigned = new PieChart.Data("Not assigned"  , model.getAvailable().size());

        
        List<Proposal> p = model.getProposals();
        for(Proposal proposal : p){
            
            for(String b : proposal.getBranch()){
                switch (b) {
                    case "DA":
                        DA.setPieValue(DA.getPieValue()+1);
                        
                        
                        break;
                    case "RAS":
                        RAS.setPieValue(RAS.getPieValue()+1);
                    
                    break;
                    case "SI":
                        SI .setPieValue(SI.getPieValue()+1);
                    
                    break;
                
                    default:
                        break;
                }
            }
            
        }




        companyBarGraph.setTitle("Top 5 companies with most internships");

        boolean firstTime = true;


        for(Internship i : model.getInternships()){
            companytops.getData().add(new XYChart.Data<>(i.getCollege(),0));
            
        }


        for(Internship i : model.getInternships()){
            for(int j=0; j < companytops.getData().size(); j++ ){
                if(companytops.getData().get(j).getXValue().equals(i.getCollege())){
                    if(companytops.getData().indexOf(companytops.getData().get(j)) != -1){
                        companytops.getData().get(j).setYValue(companytops.getData().get(j).getYValue().intValue() + 1);
                    }
                } 
            }
            
        }
        

        companyBarGraph.getData().add(companytops);

        barGraph.setTitle("Top 5 teachers with the most supervisions");

       // ArrayList<XYChart.Series<String, Number>> tops = new ArrayList<>();


        XYChart.Series<String, Number> tops = new XYChart.Series<String, Number>();
        //XYChart.Series<String, Number> top2 = new XYChart.Series<String, Number>();
        //XYChart.Series<String, Number> top3 = new XYChart.Series<String, Number>();
        //XYChart.Series<String, Number> top4 = new XYChart.Series<String, Number>();
        //XYChart.Series<String, Number> top5 = new XYChart.Series<String, Number>();
        
        
       // tops.add(top2);
       // tops.add(top3);
       // tops.add(top4);
       // tops.add(top5);

        //XYChart.Series series1 = new XYChart.Series();
        //series1.setName("Teachers with most p");

         // 4 2 1

        HashMap<String ,Long> map = model.getSupervisionNumberBySuperviser();
        List<Long> values = new ArrayList<Long>(map.values());
        Collections.reverse(values);

        tops.setName("Number of supervisions");
        for(String t : model.getSupervisionNumberBySuperviser().keySet()){
            int index = values.indexOf(map.get(t));
            if(index < 5){
                tops.getData().add(new XYChart.Data<>(t, map.get(t))); 

            }
        }


        barGraph.getData().add(tops);
        
        //barGraph.getData().sort( (Series<String, Number> o1, Series<String, Number> o2) -> o1.getData().size() <= o2.getData().size() ? 1:0  );

        pieGraph.getData().add(DA);
        pieGraph.getData().add(RAS);
        pieGraph.getData().add(SI);

        if(percentagePie != null){
            percentagePie.getData().add(proposalAssigned);
            percentagePie.getData().add(proposalNotAssigned);
        }


        percentagePie.getData().forEach((data) -> {
            data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), " ", data.pieValueProperty(), " proposals"
                )
            );
        });

        pieGraph.getData().forEach((data) ->{
            data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), " ", data.pieValueProperty(), " proposals"
                )
            );

        });
    }


    private void updateState(){
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
            case APPLICATON_OPT:
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
            case LOOKUP:
                break;
            default:
                break;
        }
        
    }
    
}
