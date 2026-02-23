package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        var resource = getClass().getResource("/main_view.fxml");

        if (resource == null) {
            System.out.println("HATA: main_view.fxml bulunamadı!");
        } else {
            Parent root = FXMLLoader.load(resource);
            primaryStage.setTitle("Medical Management System");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}