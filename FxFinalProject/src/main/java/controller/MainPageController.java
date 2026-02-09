package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

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

    public void mainPageOnAction(ActionEvent actionEvent) {
        try {
            URL resource = this.getClass().getResource("/view/mainPageComponents.fxml");

            assert resource!=null;

            Parent parent = FXMLLoader.load(resource);
            pagesAnchorPane.getChildren().clear();
            pagesAnchorPane.getChildren().add(parent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            URL resource = this.getClass().getResource("/view/mainPageComponents.fxml");

            assert resource!=null;

            Parent parent = FXMLLoader.load(resource);
            pagesAnchorPane.getChildren().clear();
            pagesAnchorPane.getChildren().add(parent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
