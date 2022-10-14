package pt.isec.pa.apoio_poe.ui;
 
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.PhaseManager;
import pt.isec.pa.apoio_poe.ui.gui.resources.ConfigController;;

public class javaFX extends Application {
    PhaseManager model;

@Override
public void init() throws Exception {
    super.init();
    model = new PhaseManager();
}

 @Override
 public void start(Stage stage) throws Exception {
    //String fxmlResource = "/config_menu.fxml";
    //Parent panel;
    //panel = FXMLLoader.load(getClass().getResource(fxmlResource));
    //Scene scene = new Scene(panel);
    //Stage stage = new Stage();
    //stage.setScene(scene);
    //stage.show();

    configureStage(stage);
    //configureStage(new Stage());
    
    stage.setOnCloseRequest(evt ->{  //pra quando fechar uma janela fechar as outras
        Platform.exit();
    });



    
 }
    private void configureStage(Stage stage) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("gui/resources/fxml/config_menu.fxml"));
        ConfigController controller = new ConfigController(model);
        loader.setController(controller);
        Pane root = loader.load();
        root.setUserData(model);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("TP de PA");
        stage.show();
    }

}
