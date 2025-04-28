package lk.ijse.supermarketfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.supermarketfx.dto.CustomerDTO;
import lk.ijse.supermarketfx.dto.tm.CustomerTM;
import lk.ijse.supermarketfx.model.CustomerModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CustomerPageController implements Initializable {
    public Label lblCustomerid;
    public TextField txtName;
    public TextField txtNic;
    public TextField txtEmail;
    public TextField txtPhone;

    private final CustomerModel customerModel = new CustomerModel();

    // table model

    public TableView<CustomerTM> tblCustomer;
    public TableColumn<CustomerTM,String> colId;
    public TableColumn<CustomerTM,String> colName;
    public TableColumn<CustomerTM,String> colNic;
    public TableColumn<CustomerTM,String> colEmail;
    public TableColumn<CustomerTM,String> colPhone;

    public Button btnGenerateReport;
    public Button btnReset;
    public Button btnDelete;
    public Button btnUpdate;
    public Button btnSave;

    private final String namePattern = "^[a-zA-Z ]$";
    private final String nicPattern = "^[0-9]{9}[vVXx]||[0-9]{12}$";
    private final String emailPattern = "^[\\\\w!#$%&'*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$";
    private final String phonePattern = "^(\\\\d+)||((\\\\d+\\\\.)(\\\\d){2})$";

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String customerid = lblCustomerid.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        // [A-Aa-z ]+
        // 1. Using Pattern object java.util.regex
//        Pattern namePattern = Pattern.compile("^[A-Za-z ]+$");
//        boolean isValidName = namePattern.matcher(name).matches();
//        System.out.println(name + " is valid: " + isValidName);

        // 2. Using String class matches() method
//        boolean isValidNameUsingStringMethod = name.matches("^[A-Za-z ]+$");
//        System.out.println(name + " is valid: " + isValidNameUsingStringMethod);

        boolean isValidName = name.matches(namePattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPhone = phone.matches(phonePattern);

        txtName.setStyle(txtName.getStyle() + ";-fx-text-fill: black;");
        if(!isValidName) {
            txtName.setStyle(txtName.getStyle() + ";-fx-text-fill: red;");
        }

        txtNic.setStyle(txtNic.getStyle() + ";-fx-text-fill: black;");
        if(!isValidNic) {
            txtNic.setStyle(txtNic.getStyle() + ";-fx-text-fill: red;");
        }

        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-text-fill: black;");
        if(!isValidEmail) {
            txtEmail.setStyle(txtEmail.getStyle() + ";-fx-text-fill: red;");
        }

        txtPhone.setStyle(txtPhone.getStyle() + ";-fx-text-fill: black;");
        if(!isValidPhone) {
            txtPhone.setStyle(txtPhone.getStyle() + ";-fx-text-fill: red;");
        }

        // Data Transfer Object - dto

        CustomerDTO customerDTO = new CustomerDTO(
                customerid,name,nic,email,phone
        );

        if(isValidName && isValidNic && isValidEmail && isValidPhone) {
            try {
                boolean isSaved = customerModel.saveCustomer(customerDTO);

                if (isSaved) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Customer Saved Successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Customer Saving Failed.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Customer Saving Failed.").show();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        try {
//            loadTableData();
//            loadNextId();
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Customer Loading Failed.").show();
        }
    }

    private void loadTableData() throws SQLException {
        ArrayList<CustomerDTO> customerDTOS = customerModel.getAllCustomers();

        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();

        for (CustomerDTO customerDTO : customerDTOS) {
            CustomerTM customerTM = new CustomerTM(
                    customerDTO.getCustomerId(),
                    customerDTO.getName(),
                    customerDTO.getNic(),
                    customerDTO.getEmail(),
                    customerDTO.getPhone()
            );
            customerTMS.add(customerTM);
        }

        tblCustomer.setItems(customerTMS);
    }

    public void loadNextId() throws SQLException {
        String nextId = customerModel.getNextCustomerId();
        lblCustomerid.setText(nextId);
    }

    private void resetPage() {
        try {
            loadTableData();
            loadNextId();

            // save button id -> enable
            btnSave.setDisable(false);

            // update, delete button (id) -> disable
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);

            txtEmail.setText("");
            txtPhone.setText("");
            txtNic.setText("");
            txtName.setText("");

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Customer Loading Failed.").show();
            e.printStackTrace();
        }
    }

    public void onClickTable(MouseEvent mouseEvent) {
        CustomerTM selectedItem = tblCustomer.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblCustomerid.setText(selectedItem.getCustomerId());
            txtName.setText(selectedItem.getName());
            txtNic.setText(selectedItem.getNic());
            txtEmail.setText(selectedItem.getEmail());
            txtPhone.setText(selectedItem.getPhone());

            // save button disable
            btnSave.setDisable(true);

            // update, delete button enable
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);

        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if(optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            String customerId = lblCustomerid.getText();

            try {
                boolean isDeleted = customerModel.deleteCustomer(customerId);

                if(isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Deleted Successfully").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Customer Deleting Failed.").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Customer Deleting Failed.").show();
                e.printStackTrace();
            }
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String customerid = lblCustomerid.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        CustomerDTO customerDTO = new CustomerDTO(
                customerid,name,nic,email,phone
        );

        try {
            boolean isUpdated = customerModel.updateCustomer(customerDTO);

            if(isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Update Successfully.").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Customer Update Failed.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Customer Update Failed.").show();
            e.printStackTrace();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }
}
