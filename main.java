package pt.isec.pa.apoio_poe;

import javafx.application.Application;
import pt.isec.pa.apoio_poe.model.PhaseManager;
import pt.isec.pa.apoio_poe.ui.javaFX;
import pt.isec.pa.apoio_poe.ui.text.ui;

public class main {
    public static void main(String args[]){
        //PhaseManager fsm = new PhaseManager();
        //ui ui = new ui(fsm);
        //ui.start();
        Application.launch(javaFX.class, args);
    }
}
