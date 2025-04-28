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

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String customerid = lblCustomerid.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        // Data Transfer Object - dto

        CustomerDTO customerDTO = new CustomerDTO(
                customerid,name,nic,email,phone
        );

        try {
            boolean isSaved = customerModel.saveCustomer(customerDTO);

            if(isSaved) {
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
