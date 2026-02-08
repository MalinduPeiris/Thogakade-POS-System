package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class LogingController {

    private final String username="malindu";
    private final String password="1234";

    public AnchorPane changingAnchorPane;

    @FXML
    private AnchorPane logingPageAnchorPane;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    void btnLogingOnAction(ActionEvent event) {
        System.out.println("username : "+txtUserName.getText()+" , sample : "+username);
        System.out.println("pass : "+txtPassword.getText()+" , sample : "+password+"\n");

        if (txtUserName.getLength()>0 && txtPassword.getLength()>0){
            if(txtUserName.getText().equals(username)){
                System.out.println("yes");
                if (txtPassword.getText().equals(password)){
                    new Alert(Alert.AlertType.INFORMATION,"Logging Success !!").show();

                    try {
                        URL resource = this.getClass().getResource(("/view/mainPage.fxml"));

                        assert resource!=null;
                        Parent parent=FXMLLoader.load(resource);

                        logingPageAnchorPane.getChildren().clear();
                        logingPageAnchorPane.getChildren().add(parent);
                        return;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    new Alert(Alert.AlertType.ERROR,"Incorrect Password !!").show();
                }
            }else {
                new Alert(Alert.AlertType.ERROR, "Not Matching Username !!").show();
            }

        }else {
            new Alert(Alert.AlertType.WARNING,"Please Fill The All Fields!!").show();
        }
    }

}
