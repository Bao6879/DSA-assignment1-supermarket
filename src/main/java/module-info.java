module com.example.dsaassignment1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires annotations;

    opens com.example.dsaassignment1 to javafx.fxml;
    exports com.example.dsaassignment1.supermarketComponents;
    exports com.example.dsaassignment1;
    exports com.example.dsaassignment1.linkedList;
    opens com.example.dsaassignment1.linkedList to javafx.fxml;
}