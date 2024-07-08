module com.fyodor.booleanframe {
    requires javafx.controls;
    requires javafx.fxml;
    requires bsh;

    opens com.fyodor.booleanframe to javafx.fxml;
    exports com.fyodor.booleanframe;
}