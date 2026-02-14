package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.OrderDetails;
import model.tm.OrderDetailsTM;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private TableColumn colUnitPrice;

    @FXML
    private TableColumn colTotalCost;

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
    void btnAddOrderCartOnAction(ActionEvent event) {
        if (cmbCustomerIDs.getValue()==null||txtDescription.getText().isEmpty()||
                txtQty.getText().isEmpty()){

            new Alert(Alert.AlertType.WARNING,"Please Fill The All Fields !!").show();

        }else{

            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            int qty = Integer.parseInt(txtQty.getText());
            double discount = Double.parseDouble(txtDiscount.getText());

            double subTotal=unitPrice*qty;
            double discountPrice=subTotal*discount/100;
            double netTotal=subTotal-discountPrice;

            lblSubTotal.setText(subTotal+"");
            lblDiscount.setText(discountPrice+"");
            lblNetTotal.setText(netTotal+"");
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields(){
        cmbCustomerIDs.setValue("");
        cmbItemCodes.setValue("");
        txtDescription.clear();
        txtUnitPrice.clear();
        txtStockOnHand.clear();
        txtQty.clear();
        txtDiscount.clear();
        txtSearchOrderID.clear();
        lblSubTotal.setText("0.00");
        lblDiscount.setText("0.00");
        lblNetTotal.setText("0.00");
    }

    @FXML
    void btnCustDeleteOrderOnAction(ActionEvent event) {

    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        if(Double.parseDouble(lblNetTotal.getText())>0){
            //for order tbl details
            String orderId=txtOrderID.getText();
            String orderDate=txtDate.getText();
            String userId=cmbCustomerIDs.getValue().toString();
            String customerId=
                    userId.charAt(0)+
                            ""+userId.charAt(1)+
                            ""+ userId.charAt(2)+
                            ""+userId.charAt(3);

            //for order details tbl
            String itemFilter=cmbItemCodes.getValue().toString();
            String itemCode=itemFilter.charAt(0)+
                    ""+itemFilter.charAt(1)+
                    ""+itemFilter.charAt(2)+
                    ""+itemFilter.charAt(3);
            int orderQty = Integer.parseInt(txtQty.getText());
            double discount = Double.parseDouble(txtDiscount.getText());

            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement pstmOrder = connection.prepareStatement("INSERT INTO orders VALUES(?,?,?)");

                pstmOrder.setString(1,orderId);
                pstmOrder.setString(2,orderDate);
                pstmOrder.setString(3,customerId);

                if(pstmOrder.executeUpdate()>0){

                    PreparedStatement pstmOrderDetail = connection.prepareStatement("INSERT INTO " +
                            "OrderDetail VALUES (?,?,?,?)");

                    pstmOrderDetail.setString(1,orderId);
                    pstmOrderDetail.setString(2,itemCode);
                    pstmOrderDetail.setInt(3,orderQty);
                    pstmOrderDetail.setDouble(4,discount);

                    if (pstmOrderDetail.executeUpdate()>0){

                        PreparedStatement pstmUpdateItem = connection.prepareStatement("UPDATE Item " +
                                "SET QtyOnHand=QtyOnHand-? Where ItemCode=?");
                        pstmUpdateItem.setInt(1,orderQty);
                        pstmUpdateItem.setString(2,itemCode);

                        if (pstmUpdateItem.executeUpdate()>0) {
                            new Alert(Alert.AlertType.INFORMATION, "Order Added Success !!").show();
                            generateOrderID();
                            clearFields();
                        }

                    }else{
                        new Alert(Alert.AlertType.WARNING,"Please Check All Order Cannot Save !!").show();
                    }


                }else{
                    new Alert(Alert.AlertType.WARNING,"Please Check All Order Cannot Save !!").show();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }else {
            new Alert(Alert.AlertType.WARNING,"Please Enter All Fields And Then Try Again !!").show();
        }
    }

    @FXML
    void btnReloadTableOnAction(ActionEvent event) {
        loadOrderDetailsTable();
        new Alert(Alert.AlertType.INFORMATION,"Table Refreshed !!").show();
    }

    @FXML
    void btnSearchOrderOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOrderOnAction(ActionEvent event) {

    }


    private void loadOrderDetailsTable(){
        ArrayList<OrderDetailsTM> orderDetailsTMSArray=new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(
                    "SELECT od.OrderID,c.CustID,c.CustName,o.OrderDate,i.Description,"+
                            "i.UnitPrice,od.OrderQTY,(i.UnitPrice * od.OrderQTY) AS Tot_Cost "+
                            "FROM OrderDetail od JOIN Item i ON i.ItemCode = od.ItemCode "+
                            "JOIN Orders o ON o.OrderID = od.OrderID "+
                            "JOIN Customer c ON c.CustID = o.CustID"
            );

            while (resultSet.next()){
                orderDetailsTMSArray.add(
                        new OrderDetailsTM(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDate(4).toLocalDate(),
                            resultSet.getString(5),
                            resultSet.getDouble(6),
                            resultSet.getInt(7),
                            resultSet.getDouble(8)
                        )
                );
            }

            colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderID"));
            colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
            colOrderItems.setCellValueFactory(new PropertyValueFactory<>("orderItems"));
            colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            colItemQty.setCellValueFactory(new PropertyValueFactory<>("orderQty"));
            colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));



            tblOrderDetails.setItems(FXCollections.observableList(orderDetailsTMSArray));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        if (cmbItemCodes.getValue()!=null) {
            String itemAllValue = cmbItemCodes.getValue().toString();
            String cmbSelectedItemId =
                    itemAllValue.charAt(0) +
                            "" + itemAllValue.charAt(1) +
                            "" + itemAllValue.charAt(2) +
                            "" + itemAllValue.charAt(3);

            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement psTM = connection.prepareStatement("SELECT * FROM Item WHERE ItemCode=?");

                psTM.setString(1, cmbSelectedItemId);
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

    private void generateOrderID(){
        ArrayList<String> orderIDList=new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSetForOrderIDs = statement.executeQuery("SELECT * from Orders");

            while (resultSetForOrderIDs.next()){
                orderIDList.add(resultSetForOrderIDs.getString(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        String id = orderIDList.get(orderIDList.size()-1);
        int lastOrderID = Integer.parseInt(id.substring(1));
        txtOrderID.setText(lastOrderID>9 ? "D0"+(lastOrderID+2) : "D00"+(lastOrderID+2));

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtDate.setText(LocalDate.now()+"");
        txtDate.setEditable(false);
        setComboBoxValues();
        generateOrderID();
        loadOrderDetailsTable();


        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {

            assert t1 != null;

            OrderDetailsTM orderDetailsTM = (OrderDetailsTM) t1;

            OrderDetails orderDetails = new OrderDetails(
                    orderDetailsTM.getOrderID(),
                    orderDetailsTM.getCustomerID(),
                    orderDetailsTM.getCustomerName(),
                    orderDetailsTM.getOrderDate(),
                    orderDetailsTM.getOrderItems(),
                    orderDetailsTM.getUnitPrice(),
                    orderDetailsTM.getOrderQty(),
                    orderDetailsTM.getTotalCost()
            );

            setTextValues(orderDetails);
        });
    }

    private void setTextValues(OrderDetails orderDetails) {
        txtOrderID.setText(orderDetails.getOrderID());
        txtDate.setText(orderDetails.getOrderDate().toString());
        cmbCustomerIDs.setValue(orderDetails.getCustomerID()+" - "+orderDetails.getCustomerName());

        txtUnitPrice.setText(orderDetails.getUnitPrice()+"");
        txtQty.setText(orderDetails.getOrderQty()+"");
        txtDescription.setText(orderDetails.getOrderItems());


    }
}
