module com.example.yplatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires com.almasb.fxgl.all;
    requires java.validation;
    requires java.sql;
    requires jBCrypt;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    opens Client to javafx.fxml;
    exports Client;
}