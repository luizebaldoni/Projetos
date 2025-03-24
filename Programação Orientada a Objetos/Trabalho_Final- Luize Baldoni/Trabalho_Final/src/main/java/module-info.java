module com.example.trabalho_final {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;

    opens com.example.trabalho_final to javafx.fxml;
    exports com.example.trabalho_final;
    exports com.example.trabalho_final.Auxiliares;
    opens com.example.trabalho_final.Auxiliares to javafx.fxml;
}