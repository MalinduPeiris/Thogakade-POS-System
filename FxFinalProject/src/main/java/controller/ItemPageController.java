package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.Item;
import model.tm.CustomerTM;
import model.tm.ItemTM;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ItemPageController implements Initializable {

    @FXML
    private ComboBox cmbItemSearchBy;

    @FXML
    private TableColumn colDescription;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colPackOfSize;

    @FXML
    private TableColumn colQtyOnHand;

    @FXML
    private TableColumn colUnitPrice;

    @FXML
    private TableView tblItemDetails;

    @FXML
    private TextField txtItemCode;

    @FXML
    private TextField txtItemDescription;

    @FXML
    private TextField txtItemSearchValue;

    @FXML
    private TextField txtPackSize;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    void btnItemSearchToDeleteAndUpdateOnAction(ActionEvent event) {
        String id = txtItemCode.getText().trim();
        if(!id.isEmpty()){
            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement psTM = connection.prepareStatement("SELECT * FROM item WHERE ItemCode=?");
                psTM.setString(1,id);

                ResultSet resultSet=psTM.executeQuery();
                if(resultSet!=null) {
                    resultSet.next();


                    Item item = new Item(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDouble(4),
                            resultSet.getInt(5)
                    );

                    setTableValuesForSearch(resultSet);
                    setTextValuesforSearch(item);
                }else{
                    new Alert(Alert.AlertType.WARNING,"Item Not Found !!").show();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }else{
            new Alert(Alert.AlertType.WARNING,"Please Enter First Customer ID !!").show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnItemAddOnAction(ActionEvent event) {
        String id=txtItemCode.getText();
        String description=txtItemDescription.getText();
        String packSize=txtPackSize.getText();
        Double unitPrice=Double.parseDouble(txtUnitPrice.getText());
        Integer qtyOnHand=Integer.parseInt(txtQtyOnHand.getText());

        if(!id.isEmpty() && !description.isEmpty() && !packSize.isEmpty() && unitPrice>0 &&
                qtyOnHand>0) {

            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement psTM = connection.prepareStatement(
                        "INSERT INTO item VALUES (?,?,?,?,?)"
                );

                ItemTM itemTM=new ItemTM(
                        id,description,packSize,unitPrice,qtyOnHand
                );

                psTM.setString(1, itemTM.getId());
                psTM.setString(2, itemTM.getDescription());
                psTM.setString(3, itemTM.getPackSize());
                psTM.setDouble(4, itemTM.getUnitPrice());
                psTM.setInt(5, itemTM.getQtyOnHand());


                if (psTM.executeUpdate() > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Item Added Success !!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Item Added Denied !!").show();
                }

            loadItemTable();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }else {
            new Alert(Alert.AlertType.ERROR,"Please Fill The All Fields !!").show();
        }


    }

    @FXML
    void btnItemDeleteOnAction(ActionEvent event) {
        String id = txtItemCode.getText().trim();
        if(!id.isEmpty()) {
            String name=txtItemDescription.getText();

            if(name.isEmpty()){
                new Alert(Alert.AlertType.WARNING,"Please Enter First Item ID and Then Search That After That Delete Is Available For Related Item... ").show();
            }else {
                try {
                    Connection connection = DBConnection.getInstance().getConnection();

                    PreparedStatement psTM = connection.prepareStatement("DELETE From Item WHERE ItemCode=?");

                    psTM.setString(1,id);

                    if(psTM.executeUpdate()>0){
                        new Alert(Alert.AlertType.INFORMATION,"Item "+name+" Delete Successfully !!").show();
                        clearFields();
                    }else {
                        new Alert(Alert.AlertType.ERROR,"Item "+name+" Delete Denied !!").show();
                    }

                    loadItemTable();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Please Enter First Item ID and Then Search That After That Delete Is Available For Related Item... ").show();
        }
    }

    @FXML
    void btnItemSearchOnAction(ActionEvent event) {
        System.out.println("Searching");
        String searchOp=cmbItemSearchBy.getValue().toString();
        if(searchOp.equals("ID")||searchOp.equals("QTY Less Than")){
            try {
                Connection connection = DBConnection.getInstance().getConnection();

                if(searchOp.equals("ID")){
                    String id=txtItemSearchValue.getText();

                    PreparedStatement psTM = connection.prepareStatement("SELECT * FROM item WHERE ItemCode=?");
                    psTM.setString(1,id);

                    ResultSet resultSet=psTM.executeQuery();
                    if(resultSet!=null) {
                        resultSet.next();

                        Item item = new Item(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getDouble(4),
                                resultSet.getInt(5)
                        );

                        setTableValuesForSearch(resultSet);
                        setTextValuesforSearch(item);
                    }else{
                        new Alert(Alert.AlertType.WARNING,"Item Not Found !!").show();
                    }

                }else if(searchOp.equals("QTY Less Than")){
                    String qty=txtItemSearchValue.getText();

                    PreparedStatement psTM = connection.prepareStatement("SELECT * FROM customer WHERE QtyOnHand<?");
                    psTM.setString(1,qty);

                    ResultSet resultSet=psTM.executeQuery();

                    ArrayList<ItemTM> itemTMArrayList=new ArrayList<>();
                    while (resultSet.next()){
                        itemTMArrayList.add(
                                new ItemTM(
                                        resultSet.getString(1),
                                        resultSet.getString(2),
                                        resultSet.getString(3),
                                        resultSet.getDouble(4),
                                        resultSet.getInt(5)
                                )
                        );
                    }

                    colId.setCellValueFactory(new PropertyValueFactory<>("id"));
                    colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                    colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("packSize"));
                    colPackOfSize.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
                    colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

                    tblItemDetails.setItems(FXCollections.observableList(itemTMArrayList));


                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }else {
            new Alert(Alert.AlertType.ERROR,"Please First Select find By option then enter related thing and then click search !!").show();
        }
    }

    @FXML
    void btnItemUpdateOnAction(ActionEvent event) {
        String id=txtItemCode.getText();
        String description=txtItemDescription.getText();
        String packSize=txtPackSize.getText();
        Double unitPrice=Double.parseDouble(txtUnitPrice.getText());
        Integer qtyOnHand=Integer.parseInt(txtQtyOnHand.getText());

        if(!id.isEmpty() && !description.isEmpty() && !packSize.isEmpty() && unitPrice>0 &&
                qtyOnHand>0) {

            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement psTM = connection.prepareStatement(
                        "UPDATE Item SET Description=?, PackSize=?, UnitPrice=?, QtyOnHand=? WHERE ItemCode=?"
                );


                psTM.setString(1, description);
                psTM.setString(2, packSize);
                psTM.setDouble(3, unitPrice);
                psTM.setInt(4, qtyOnHand);
                psTM.setString(5, id);

                loadItemTable();
                clearFields();
                

                if (psTM.executeUpdate() > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Item Update Success !!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Item Update Denied !!").show();
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }else {
            new Alert(Alert.AlertType.ERROR,"Please Fill The All Fields !!").show();
        }
    }

    @FXML
    void btnReloadTableOnAction(ActionEvent event) {
       loadItemTable();
    }


    private void clearFields(){
        txtItemCode.clear();
        txtItemDescription.clear();
        txtItemSearchValue.clear();
        txtUnitPrice.clear();
        txtPackSize.clear();
        txtQtyOnHand.clear();
    }

    private void loadItemTable(){
        try {
            Connection connection=DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from item");

            ArrayList<ItemTM> itemTMArrayList=new ArrayList<>();
            while (resultSet.next()){
                itemTMArrayList.add(
                        new ItemTM(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getDouble(4),
                                resultSet.getInt(5)
                        )
                );
            }

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("packSize"));
            colPackOfSize.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

            tblItemDetails.setItems(FXCollections.observableList(itemTMArrayList));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setComboBoxes(){
        cmbItemSearchBy.setItems(
                FXCollections.observableList(
                        Arrays.asList("ID","QTY Less Than")
                )
        );
    }


    private void setTableValuesForSearch(ResultSet resultSet){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colPackOfSize.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        try {
            ItemTM itemTM=new ItemTM(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getInt(5)
            );

            tblItemDetails.setItems(
                    FXCollections.observableArrayList(
                            Arrays.asList(itemTM)
                    )
            );

            Item item=new Item(
                    itemTM.getId(),
                    itemTM.getDescription(),
                    itemTM.getPackSize(),
                    itemTM.getUnitPrice(),
                    itemTM.getQtyOnHand()
            );

            setTextValuesforSearch(item);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setTextValuesforSearch(Item item){
        txtItemCode.setText(item.getId());
        txtItemDescription.setText(item.getDescription());
        txtUnitPrice.setText(item.getUnitPrice()+"");
        txtPackSize.setText(item.getPackSize());
        txtQtyOnHand.setText(item.getQtyOnHand()+"");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadItemTable();
        setComboBoxes();
    }
}
