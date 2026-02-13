package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class OrderDetailsPageController implements Initializable {

    @FXML
    private ComboBox cmbCustomerIDs;

    @FXML
    private ComboBox cmbItemCodes;

    @FXML
    private TableColumn colCustomerID;

    @FXML
    private TableColumn colCustomerName;

    @FXML
    private TableColumn colItemQty;

    @FXML
    private TableColumn colOrderDate;

    @FXML
    private TableColumn colOrderId;

    @FXML
    private TableColumn colOrderItems;

    @FXML
    private TableColumn colTotal;

    @FXML
    private Text lblDiscount;

    @FXML
    private Text lblNetTotal;

    @FXML
    private Text lblSubTotal;

    @FXML
    private TableView tblOrderDetails;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtDiscount;

    @FXML
    private TextField txtOrderID;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtSearchOrderID;

    @FXML
    private TextField txtStockOnHand;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    void btnAddCustomerOnAction(ActionEvent event) {

    }

    @FXML
    void btnAddOrderCartOnAction(ActionEvent event) {
        System.out.println(cmbCustomerIDs.getValue()+"");//null
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {

    }

    @FXML
    void btnCustDeleteOrderOnAction(ActionEvent event) {

    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {

    }

    @FXML
    void btnReloadTableOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOrderOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOrderOnAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtDate.setText(LocalDate.now()+"");
        txtDate.setEditable(false);
        setComboBoxValues();
    }

    private void setComboBoxValues(){
        ArrayList<String> customerNameAndIdList=new ArrayList<>();

        ArrayList<String> itemNameAndIdList=new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSetForCustomerIDs = statement.executeQuery("SELECT CustID,CustName from customer");

            while (resultSetForCustomerIDs.next()){
                String name= resultSetForCustomerIDs.getString(1)+"  -  "+resultSetForCustomerIDs.getString(2);
                customerNameAndIdList.add(name);
            }

            ResultSet resultSetForItemIds = statement.executeQuery("SELECT ItemCode,Description from item");

            while (resultSetForItemIds.next()){
                String name= resultSetForItemIds.getString(1)+"  -  "+resultSetForItemIds.getString(2);
                itemNameAndIdList.add(name);
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        cmbCustomerIDs.setItems(FXCollections.observableList(customerNameAndIdList));
        cmbItemCodes.setItems(FXCollections.observableList(itemNameAndIdList));
    }


    public void cmbItemCodeOnAction(ActionEvent actionEvent) {
        String itemAllValue=cmbItemCodes.getValue().toString();
        String cmbSelectedItemId=
                itemAllValue.charAt(0)+
                        ""+itemAllValue.charAt(1)+
                        ""+itemAllValue.charAt(2)+
                        ""+itemAllValue.charAt(3);

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTM = connection.prepareStatement("SELECT * FROM Item WHERE ItemCode=?");

            psTM.setString(1,cmbSelectedItemId);
            ResultSet resultSet = psTM.executeQuery();
            resultSet.next();

            txtDescription.setText(resultSet.getString(2));
            txtUnitPrice.setText(resultSet.getString(4));
            txtStockOnHand.setText(resultSet.getString(5));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
