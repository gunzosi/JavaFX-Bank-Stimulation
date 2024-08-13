package com.jmc.mazebank;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage)  {
        Model.getInstance().getViewFactory().showClientWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
