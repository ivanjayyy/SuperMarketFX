package lk.ijse.supermarketfx.model;

import lk.ijse.supermarketfx.dto.CustomerDTO;
import lk.ijse.supermarketfx.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {

    public boolean saveCustomer(CustomerDTO customerDTO) throws SQLException {
//        Connection connection = DBConnection.getInstance().getConnection();
//
//        PreparedStatement pst = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?,?)");
//
//        pst.setString(1,customerDTO.getCustomerId());
//        pst.setString(2,customerDTO.getName());
//        pst.setString(3,customerDTO.getNic());
//        pst.setString(4,customerDTO.getEmail());
//        pst.setString(5,customerDTO.getPhone());
//
//        int i = pst.executeUpdate();
//
//        boolean isSaved = i > 0;
//        return isSaved;

        return CrudUtil.execute("INSERT INTO customer VALUES (?,?,?,?,?)", customerDTO.getCustomerId(),customerDTO.getName(),customerDTO.getNic(),customerDTO.getEmail(),customerDTO.getPhone());
    }

    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException {
//        Connection connection = DBConnection.getInstance().getConnection();
//        PreparedStatement pst = connection.prepareStatement("select * from customer");
//        ResultSet resultSet = pst.executeQuery();

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");

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
//        Connection connection = DBConnection.getInstance().getConnection();
//        PreparedStatement pst = connection.prepareStatement("select customer_id from customer order by customer_id desc limit 1");
//
//        ResultSet resultSet = pst.executeQuery();

        ResultSet resultSet = CrudUtil.execute("SELECT customer_id FROM customer ORDER BY customer_id DESC LIMIT 1");

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

    public boolean updateCustomer(CustomerDTO customerDTO) throws SQLException {
        return CrudUtil.execute("UPDATE customer SET name = ?, nic = ?, email = ?, phone = ? WHERE customer_id = ?", customerDTO.getName(),customerDTO.getNic(),customerDTO.getEmail(),customerDTO.getPhone(),customerDTO.getCustomerId());
    }

    public boolean deleteCustomer(String customerId) throws SQLException {
        return CrudUtil.execute("DELETE FROM customer WHERE customer_id = ?", customerId);
    }
}
