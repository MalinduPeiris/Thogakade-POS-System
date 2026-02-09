package controller;

import com.jfoenix.controls.JFXButton;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {


    @FXML
    private AnchorPane pagesAnchorPane;

    public JFXButton btnCustManage;
    public JFXButton btnItemManage;
    public JFXButton btnOrderManage;
    public JFXButton btnHome;

    public Text lblMainDate;
    public Text lblMainTime;

    @FXML
    void ItemManageOnAction(ActionEvent event) {
        btnItemManage.setStyle(
                "-fx-background-color: #3742fa; " +
                        "-fx-text-fill: black;"
        );
        btnHome.setStyle(
                "-fx-background-color:  #1e90ff; " +
                        "-fx-text-fill: white;"
        );
        btnOrderManage.setStyle(
                "-fx-background-color:  #1e90ff; " +
                        "-fx-text-fill: white;"
        );
        btnCustManage.setStyle(
                "-fx-background-color:  #1e90ff; " +
                        "-fx-text-fill: white;"
        );


        try {
            URL resource = this.getClass().getResource("/view/itemPage.fxml");

            assert resource!=null;

            Parent parent = FXMLLoader.load(resource);
            pagesAnchorPane.getChildren().clear();
            pagesAnchorPane.getChildren().add(parent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void OrderManageOnAction(ActionEvent event) {
        btnOrderManage.setStyle(
                "-fx-background-color:  #3742fa; " +
                        "-fx-text-fill: black;"
        );
        btnItemManage.setStyle(
                "-fx-background-color: #1e90ff; " +
                        "-fx-text-fill: white;"
        );
        btnHome.setStyle(
                "-fx-background-color:  #1e90ff; " +
                        "-fx-text-fill: white;"
        );
        btnCustManage.setStyle(
                "-fx-background-color:  #1e90ff; " +
                        "-fx-text-fill: white;"
        );
    }

    @FXML
    void customerManageOnAction(ActionEvent event) {
        btnCustManage.setStyle(
                "-fx-background-color:  #3742fa; " +
                        "-fx-text-fill: black;"
        );
        btnItemManage.setStyle(
                "-fx-background-color: #1e90ff; " +
                        "-fx-text-fill: white;"
        );
        btnHome.setStyle(
                "-fx-background-color:  #1e90ff; " +
                        "-fx-text-fill: white;"
        );
        btnOrderManage.setStyle(
                "-fx-background-color:  #1e90ff; " +
                        "-fx-text-fill: white;"
        );


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
        btnHome.setStyle(
                "-fx-background-color:  #3742fa; " +
                        "-fx-text-fill: black;"
        );
        btnItemManage.setStyle(
                "-fx-background-color: #1e90ff; " +
                        "-fx-text-fill: white;"
        );
        btnOrderManage.setStyle(
                "-fx-background-color:  #1e90ff; " +
                        "-fx-text-fill: white;"
        );
        btnCustManage.setStyle(
                "-fx-background-color:  #1e90ff; " +
                        "-fx-text-fill: white;"
        );

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
        btnHome.setStyle(
                "-fx-background-color:  #3742fa; " +
                        "-fx-text-fill: black;"
        );


        String time = LocalTime.now()+"";

        String customizeTime=time.charAt(0)+""+time.charAt(1)+""+time.charAt(2)+""+time.charAt(3)+""+time.charAt(4);

        System.out.println(customizeTime);

        lblMainDate.setText(LocalDate.now()+"");
        lblMainTime.setText(customizeTime);

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
