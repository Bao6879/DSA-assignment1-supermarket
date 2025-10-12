module main {
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
    requires xstream;

    opens main to javafx.fxml;
    opens main.supermarketComponents to xstream;
    opens main.linkedList to xstream, javafx.fxml;
    exports main.supermarketComponents;
    exports main;
    exports main.linkedList;
}