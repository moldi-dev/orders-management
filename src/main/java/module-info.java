module com.moldidev.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.logging;
    requires java.sql;
    requires java.desktop;

    opens controller to javafx.fxml;
    opens model to javafx.base;
    opens utility to javafx.base;

    exports main;
    exports controller;
    exports connection;
    exports dao;
    exports utility;
}