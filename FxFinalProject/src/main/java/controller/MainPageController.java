package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class MainPageController {

    @FXML
    private AnchorPane pagesAnchorPane;

    @FXML
    void ItemManageOnAction(ActionEvent event) {

    }

    @FXML
    void OrderManageOnAction(ActionEvent event) {

    }

    @FXML
    void customerManageOnAction(ActionEvent event) {

        try {
            URL resource = this.getClass().getResource("/view/customerPage.fxml");
            assert resource!=null;

            Parent parent=FXMLLoader.load(resource);

            pagesAnchorPane.getChildren().clear();
            pagesAnchorPane.getChildren().add(parent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
