module com.smartcare.smartcaredesktop {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires jbcrypt;

    // FXML controllers
    opens com.smartcare.smartcaredesktop.controller to javafx.fxml;

    // Main package (if needed for FXML)
    opens com.smartcare.smartcaredesktop to javafx.fxml;

    // 🔥 VERY IMPORTANT — FIXES TABLEVIEW REFLECTION
    opens com.smartcare.smartcaredesktop.model to javafx.base;

    exports com.smartcare.smartcaredesktop;
}