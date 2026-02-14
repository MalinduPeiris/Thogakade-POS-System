package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.tm.CustomerTM;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CustomerPageController implements Initializable {

    @FXML
    private ComboBox cmbCustomerSearchBy;

    @FXML
    private ComboBox cmbProvince;

    @FXML
    private ComboBox cmbTitle;

    @FXML
    private TableColumn colCity;

    @FXML
    private TableColumn colDOB;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colPostalCode;

    @FXML
    private TableColumn colSalary;

    @FXML
    public TableColumn colProvince;

    @FXML
    public TableColumn colAddress;

    @FXML
    private DatePicker dateDOB;

    @FXML
    private TableView tblCustomerDetails;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtCustId;

    @FXML
    private TextField txtCustName;

    @FXML
    private TextField txtCustsearchValue;

    @FXML
    private TextField txtPostalCode;

    @FXML
    private TextField txtSalary;

    @FXML
    void btnCustAddOnAction(ActionEvent event) {
        String id=txtCustId.getText();
        String name=txtCustName.getText();
        String address=txtAddress.getText();
        String city=txtCity.getText();
        String postalCode=txtPostalCode.getText();

        if(!id.isEmpty() && !name.isEmpty() && !address.isEmpty() && !city.isEmpty() &&
                !postalCode.isEmpty()) {

            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement psTM = connection.prepareStatement(
                        "INSERT INTO customer VALUES (?,?,?,?,?,?,?,?,?)"
                );


                LocalDate date=dateDOB.getValue();
                double salary=Double.parseDouble(txtSalary.getText());
                String title=cmbTitle.getValue().toString();
                String province=cmbProvince.getValue().toString();

                Customer customer = new Customer(
                        id, title, name, date, salary, address, city, province, postalCode
                );

                psTM.setString(1, customer.getId());
                psTM.setString(2, customer.getTitle());
                psTM.setString(3, customer.getName());
                psTM.setObject(4, customer.getDate());
                psTM.setDouble(5, customer.getSalary());
                psTM.setString(6, customer.getAddress());
                psTM.setString(7, customer.getCity());
                psTM.setString(8, customer.getProvince());
                psTM.setString(9, customer.getPostalCode());


                if (psTM.executeUpdate() > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Added Success !!").show();
                    clearFields();
                    loadAllCustomerToTable();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Customer Added Denied !!").show();
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }else {
            new Alert(Alert.AlertType.ERROR,"Please Fill The All Fields !!").show();
        }


    }

    public void btnCustomerSearchToDeleteAndUpdateOnAction(ActionEvent actionEvent) {
        String id = txtCustId.getText().trim();
        if(!id.isEmpty()){
            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement psTM = connection.prepareStatement("SELECT * FROM customer WHERE CustID=?");
                psTM.setString(1,id);

//                System.out.println(psTM.executeQuery());
                ResultSet resultSet=psTM.executeQuery();
                if(resultSet!=null) {
                    resultSet.next();


                    Customer customer = new Customer(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDate(4).toLocalDate(),
                            resultSet.getDouble(5),
                            resultSet.getString(6),
                            resultSet.getString(7),
                            resultSet.getString(8),
                            resultSet.getString(9)
                    );

                    setTableValuesForSearch(resultSet);
                    setTextValuesforSearch(customer);
                }else{
                    new Alert(Alert.AlertType.WARNING,"Customer Not Found !!").show();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }else{
            new Alert(Alert.AlertType.WARNING,"Please Enter First Customer ID !!").show();
        }
    }

    @FXML
    void btnCustDeleteOnAction(ActionEvent event) {
        String id = txtCustId.getText().trim();
        if(!id.isEmpty()) {
            String name=txtCustName.getText();

            if(name.isEmpty()){
                new Alert(Alert.AlertType.WARNING,"Please Enter First Customer ID and Then Search That After That Delete Is Available For Related Customer... ").show();
            }else {
                try {
                    Connection connection = DBConnection.getInstance().getConnection();

                    PreparedStatement psTM = connection.prepareStatement("DELETE From customer WHERE CustId=?");

                    psTM.setString(1,id);

                    if(psTM.executeUpdate()>0){
                        new Alert(Alert.AlertType.INFORMATION,"Customer "+name+" Delete Successfully !!").show();
                        clearFields();
                        loadAllCustomerToTable();
                    }else {
                        new Alert(Alert.AlertType.WARNING,"Customer "+name+" Delete Denied !!").show();
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Please Enter First Customer ID and Then Search That After That Delete Is Available For Related Customer... ").show();
        }
    }

    @FXML
    void btnCustUpdateOnAction(ActionEvent event) {
        String id=txtCustId.getText();
        String name=txtCustName.getText();
        String address=txtAddress.getText();
        String city=txtCity.getText();
        LocalDate date=dateDOB.getValue();
        String postalCode=txtPostalCode.getText();

        if(!id.isEmpty() && !name.isEmpty() && !address.isEmpty() &&
                !city.isEmpty() && !postalCode.isEmpty() && date != null) {

            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement psTM = connection.prepareStatement(
                        "UPDATE Customer SET CustTitle=?, CustName=?, DOB=?, salary=?, CustAddress=?" +
                                ", City=?, Province=?, PostalCode=? WHERE CustID=?"
                );

                double salary=Double.parseDouble(txtSalary.getText());
                String title=cmbTitle.getValue().toString();
                String province=cmbProvince.getValue().toString();

                psTM.setString(1, title);
                psTM.setString(2, name);
                psTM.setObject(3, date);
                psTM.setDouble(4, salary);
                psTM.setString(5, address);
                psTM.setString(6, city);
                psTM.setString(7, province);
                psTM.setString(8, postalCode);
                psTM.setString(9, id);


                if (psTM.executeUpdate() > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Update Success !!").show();
                    clearFields();
                    loadAllCustomerToTable();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Customer Update Denied !!").show();
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }else {
            new Alert(Alert.AlertType.ERROR,"Please Fill The All Fields !!").show();
        }

    }

    @FXML
    void btnCustomerSearchOnAction(ActionEvent event) {
        String searchOp=cmbCustomerSearchBy.getValue().toString();
        if(searchOp.equals("Id")||searchOp.equals("Name")||searchOp.equals("City")){
            try {
                Connection connection = DBConnection.getInstance().getConnection();

                if(searchOp.equals("Id")){
                    String id=txtCustsearchValue.getText();

                    PreparedStatement psTM = connection.prepareStatement("SELECT * FROM customer WHERE CustID=?");
                    psTM.setString(1,id);

                    ResultSet resultSet=psTM.executeQuery();
                    if(resultSet!=null) {
                        resultSet.next();


                        Customer customer = new Customer(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getDate(4).toLocalDate(),
                                resultSet.getDouble(5),
                                resultSet.getString(6),
                                resultSet.getString(7),
                                resultSet.getString(8),
                                resultSet.getString(9)
                        );

                        setTableValuesForSearch(resultSet);
                        setTextValuesforSearch(customer);
                    }else{
                        new Alert(Alert.AlertType.WARNING,"Customer Not Found !!").show();
                    }

                }else if(searchOp.equals("Name")){
                    String name=txtCustsearchValue.getText();

                    PreparedStatement psTM = connection.prepareStatement("SELECT * FROM customer WHERE CustName=?");
                    psTM.setString(1,name);

                    ResultSet resultSet=psTM.executeQuery();
                    if(resultSet!=null) {
                        resultSet.next();


                        Customer customer = new Customer(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getDate(4).toLocalDate(),
                                resultSet.getDouble(5),
                                resultSet.getString(6),
                                resultSet.getString(7),
                                resultSet.getString(8),
                                resultSet.getString(9)
                        );

                        setTableValuesForSearch(resultSet);
                        setTextValuesforSearch(customer);
                    }else{
                        new Alert(Alert.AlertType.WARNING,"Customer Not Found !!").show();
                    }

                }else if(searchOp.equals("City")){
                    String city=txtCustsearchValue.getText();

                    PreparedStatement psTM = connection.prepareStatement("SELECT * FROM customer WHERE City=?");
                    psTM.setString(1,city);

                    ResultSet resultSet=psTM.executeQuery();

                    ArrayList<CustomerTM> customerTMArrayList=new ArrayList<>();
                    while (resultSet.next()){
                        customerTMArrayList.add(
                                new CustomerTM(
                                        resultSet.getString(1),
                                        resultSet.getString(2),
                                        resultSet.getString(3),
                                        resultSet.getDate(4).toLocalDate(),
                                        resultSet.getDouble(5),
                                        resultSet.getString(6),
                                        resultSet.getString(7),
                                        resultSet.getString(8),
                                        resultSet.getString(9)
                                )
                        );
                    }

                    colId.setCellValueFactory(new PropertyValueFactory<>("id"));
                    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
                    colDOB.setCellValueFactory(new PropertyValueFactory<>("date"));
                    colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
                    colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
                    colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
                    colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
                    colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));

                    tblCustomerDetails.setItems(FXCollections.observableList(customerTMArrayList));

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }else {
            new Alert(Alert.AlertType.ERROR,"Please First Select find By option then enter related thing and then click search !!").show();
        }
    }

    public void btnReloadTableOnAction(ActionEvent actionEvent) {
        loadAllCustomerToTable();
        new Alert(Alert.AlertType.INFORMATION, "Table Refreshed !!").show();
        clearFields();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setComboBoxes();
        loadAllCustomerToTable();

        tblCustomerDetails.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {

            assert t1 != null;

            CustomerTM customerTM = (CustomerTM) t1;

            if (customerTM!=null) {

                Customer customer = new Customer(
                        customerTM.getId(),
                        customerTM.getName(),
                        customerTM.getName(),
                        customerTM.getDate(),
                        customerTM.getSalary(),
                        customerTM.getAddress(),
                        customerTM.getCity(),
                        customerTM.getProvince(),
                        customerTM.getPostalCode()
                );

                setTextValuesforSearch(customer);
            }
        });

        generateCustomerID();


    }

    private void setComboBoxes(){
        cmbTitle.setItems(
                FXCollections.observableList(
                        Arrays.asList("Mr","Miss","Ms","Dr")
                )
        );

        cmbProvince.setItems(
                FXCollections.observableList(
                        Arrays.asList(
                                "Western", "Central", "Southern", "Northern", "Eastern",
                                "North Western", "North Central", "Uva", "Sabaragamuwa"
                        )
                )
        );

        cmbCustomerSearchBy.setItems(
                FXCollections.observableList(
                        Arrays.asList("Id","Name","City")
                )
        );
    }

    private void clearFields(){
        txtCustId.clear();
        txtCustName.clear();
        cmbTitle.setValue(null);
        dateDOB.getEditor().clear();
        txtAddress.clear();
        txtSalary.clear();
        txtCity.clear();
        txtPostalCode.clear();
        cmbProvince.setValue(null);
    }



    private void setTableValuesForSearch(ResultSet resultSet){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("date"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));

        try {
            Customer customer=new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4).toLocalDate(),
                    resultSet.getDouble(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9)
            );

            CustomerTM customerTM=new CustomerTM(
                    customer.getId(),
                    customer.getTitle(),
                    customer.getName(),
                    customer.getDate(),
                    customer.getSalary(),
                    customer.getAddress(),
                    customer.getCity(),
                    customer.getProvince(),
                    customer.getPostalCode()
            );

            tblCustomerDetails.setItems(
                    FXCollections.observableArrayList(
                            Arrays.asList(customerTM)
                    )
            );




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void setTextValuesforSearch(Customer customer){
        txtCustId.setText(customer.getId());
        cmbTitle.setValue(customer.getTitle());
        txtCustName.setText(customer.getName());
        System.out.println(customer.getDate()+" ekakk");
        dateDOB.setValue(customer.getDate());
        txtSalary.setText(customer.getSalary()+"");
        txtAddress.setText(customer.getAddress());
        txtCity.setText(customer.getCity());
        txtPostalCode.setText(customer.getPostalCode());
        cmbProvince.setValue(customer.getProvince());
    }

    private void loadAllCustomerToTable(){
        try {
            Connection connection=DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from customer");

            ArrayList<CustomerTM> customerTMArrayList=new ArrayList<>();
            while (resultSet.next()){
                customerTMArrayList.add(
                        new CustomerTM(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getDate(4).toLocalDate(),
                                resultSet.getDouble(5),
                                resultSet.getString(6),
                                resultSet.getString(7),
                                resultSet.getString(8),
                                resultSet.getString(9)
                        )
                );
            }

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colDOB.setCellValueFactory(new PropertyValueFactory<>("date"));
            colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
            colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));

            tblCustomerDetails.setItems(FXCollections.observableList(customerTMArrayList));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateCustomerID(){
        ArrayList<String> customerIDList=new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSetForCustomerIDs = statement.executeQuery("SELECT * from customer");

            while (resultSetForCustomerIDs.next()){
                customerIDList.add(resultSetForCustomerIDs.getString(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        String id = customerIDList.get(customerIDList.size()-1);
        int lastOrderID = Integer.parseInt(id.substring(1));
        txtCustId.setText(lastOrderID>9 ? "C0"+(lastOrderID+1) : "C00"+(lastOrderID+1));

    }


}
