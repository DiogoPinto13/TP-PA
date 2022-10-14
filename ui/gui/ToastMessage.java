package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;
import pt.isec.pa.apoio_poe.model.errors.ErrorType;
import pt.isec.pa.apoio_poe.ui.gui.resources.ImageManager;

import java.util.Timer;
import java.util.TimerTask;

public class ToastMessage {
    private ToastMessage() {}

    public static void show(Window owner, ErrorLog message) {
        final Dialog popup = new Dialog();
        //popup.setAutoHide(true);
        //popup.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_LEFT);
        popup.setTitle("Error message");
        double x = owner.getX();
        double y = owner.getY();
        double w = owner.getWidth();
        double h = owner.getHeight();
        Image icon = ImageManager.getImage("checked.png");
        Label[] lbMessage = new Label[message.getErrorType().size()];
        DialogPane dialogPane = popup.getDialogPane();
        
        if(message.getErrorType().size() != 1 || !message.getErrorType().get(0).equals(ErrorType.SUCESS)){
            icon = ImageManager.getImage("cancel.png");
        }

        ImageView imageView = new ImageView(icon);

        
        for(int i=0;i<message.getErrorType().size();i++){
            
            lbMessage[i] = new Label(message.getErrorType().get(i).toString());
            //lbMessage[i].setTextFill(Color.WHITE);
            
        }
        
        dialogPane.setContent(new VBox(8,lbMessage));
        dialogPane.getButtonTypes().addAll(ButtonType.OK);
        dialogPane.setGraphic(imageView);
        popup.show();
        //popup.show(owner,x+w/2-message.length()/2.0*lbMessage.getFont().getSize(),y+0.80*h);
    }
}
