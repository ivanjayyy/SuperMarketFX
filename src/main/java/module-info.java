module lk.ijse.supermarketfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;

    opens lk.ijse.supermarketfx.dto.tm to javafx.base;
    opens lk.ijse.supermarketfx.controller to javafx.fxml;
    exports lk.ijse.supermarketfx;
}