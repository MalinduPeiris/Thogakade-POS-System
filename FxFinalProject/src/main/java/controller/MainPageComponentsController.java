package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.tm.ItemQtyTM;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainPageComponentsController implements Initializable {

    @FXML
    private TableColumn colID;

    @FXML
    private TableColumn colProduct;

    @FXML
    private TableColumn colQtyOnHand;

    @FXML
    private TableColumn colSoldQty;

    @FXML
    private TableColumn colUnitPrice;

    @FXML
    private Text lblTotCustomer;

    @FXML
    private Text lblTotItems;

    @FXML
    private Text lblTotOrders;

    @FXML
    private Text lblTotSales;

    @FXML
    private AnchorPane pagesAnchorPane;

    @FXML
    private TableView tblMainPgItem;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLowQtyItems();
        loadOtherCardDetails();
    }

    private void loadLowQtyItems(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(
                    "SELECT i.itemCode, i.description, i.unitPrice, i.qtyOnHand, " +
                            "SUM(o.orderQty) AS totSoldQty FROM Item i " +
                            "JOIN OrderDetail o ON i.itemCode = o.itemCode " +
                            "WHERE i.qtyOnHand < 35 " +
                            "GROUP BY i.itemCode, i.description, i.unitPrice, i.qtyOnHand"
            );

            ArrayList<ItemQtyTM> itemQtyTMS=new ArrayList<>();
            while(resultSet.next()){
                itemQtyTMS.add(
                        new ItemQtyTM(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getDouble(3),
                                resultSet.getInt(4),
                                resultSet.getInt(5)
                        )
                );
            }

            colID.setCellValueFactory(new PropertyValueFactory<>("id"));
            colProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
            colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
            colSoldQty.setCellValueFactory(new PropertyValueFactory<>("totSoldQty"));

            tblMainPgItem.setItems(FXCollections.observableList(itemQtyTMS));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadOtherCardDetails(){
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();

            Statement statement = connection.createStatement();

            ResultSet resultSetForTotCustomer = statement.executeQuery(
                    "SELECT COUNT(*) FROM Customer"
            );
            resultSetForTotCustomer.next();
            lblTotCustomer.setText(resultSetForTotCustomer.getString(1));


            ResultSet resultSetForTotItems = statement.executeQuery(
                    "SELECT COUNT(*) FROM Item"
            );
            resultSetForTotItems.next();
            lblTotItems.setText(resultSetForTotItems.getString(1));


            LocalDate date = LocalDate.now();
            PreparedStatement psTM = connection.prepareStatement("SELECT COUNT(*) From orders WHERE OrderDate=?");
            psTM.setObject(1,date);
            ResultSet resultSetForTodayOrderCount = psTM.executeQuery();
            resultSetForTodayOrderCount.next();
            lblTotOrders.setText(resultSetForTodayOrderCount.getString(1));



            PreparedStatement psTM2 = connection.prepareStatement(
                    "SELECT SUM(od.OrderQTY * i.UnitPrice) AS todayTotalSale FROM Orders o " +
                            "JOIN OrderDetail od ON o.OrderID = od.OrderID " +
                            "JOIN Item i ON od.ItemCode = i.ItemCode WHERE o.OrderDate = ?"
            );
            psTM2.setObject(1,date);
            ResultSet resultSetForTodayTotSale = psTM2.executeQuery();
            resultSetForTodayTotSale.next();
            System.out.println(resultSetForTodayTotSale.getString(1));
            lblTotSales.setText(
                    resultSetForTodayTotSale.getString(1)==null ? "Rs. 0.00"
                            : resultSetForTodayTotSale.getString(1)
            );



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
