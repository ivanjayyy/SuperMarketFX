package lk.ijse.supermarketfx.model;

import lk.ijse.supermarketfx.db.DBConnection;
import lk.ijse.supermarketfx.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {

    public boolean saveCustomer(CustomerDTO customerDTO) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        PreparedStatement pst = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?,?)");

        pst.setString(1,customerDTO.getCustomerId());
        pst.setString(2,customerDTO.getName());
        pst.setString(3,customerDTO.getNic());
        pst.setString(4,customerDTO.getEmail());
        pst.setString(5,customerDTO.getPhone());

        int i = pst.executeUpdate();

        boolean isSaved = i > 0;
        return isSaved;
    }

    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement("select * from customer");
        ResultSet resultSet = pst.executeQuery();

        ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();

        while (resultSet.next()) {
            CustomerDTO customerDTO = new CustomerDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );

            customerDTOS.add(customerDTO);
        }
        return customerDTOS;
    }

    public String getNextCustomerId() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement("select customer_id from customer order by customer_id desc limit 1");

        ResultSet resultSet = pst.executeQuery();

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format("C%03d", nextIdNumber);
            return nextIdString;
        }

        return "C001";
    }
}
