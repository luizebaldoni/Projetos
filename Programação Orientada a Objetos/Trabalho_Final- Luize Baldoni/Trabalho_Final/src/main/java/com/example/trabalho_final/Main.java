package com.example.trabalho_final;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage scene) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        scene.setTitle("Gerenciador Financeiro");
        scene.setScene(new Scene(root, 850, 600));
        scene.getIcons().add(new Image("file:src/main/resources/icon.ico"));
        scene.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
