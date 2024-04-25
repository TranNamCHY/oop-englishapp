module org.englishapp.englishapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.web;
    requires java.sql;
    requires jlayer;
    requires freetts;
    requires org.controlsfx.controls;
    requires java.ocr.api;
    //requires com.dlsc.formsfx;
    //requires net.synedra.validatorfx;
    //requires org.kordamp.ikonli.javafx;
    //requires org.kordamp.bootstrapfx.core;

    opens org.englishapp.englishapp to javafx.fxml;
    exports org.englishapp.englishapp;
    exports org.englishapp.englishapp.Controller;
    opens org.englishapp.englishapp.Controller to javafx.fxml;
    exports org.englishapp.englishapp.Management;
    opens org.englishapp.englishapp.Management to javafx.fxml;
}