module com.moldidev.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.logging;
    requires java.sql;

    opens com.moldidev.main to javafx.fxml;
    exports com.moldidev.main;
    exports controller;
    exports connection;
    opens controller to javafx.fxml;
}