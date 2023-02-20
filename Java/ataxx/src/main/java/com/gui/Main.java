package com.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

import com.game.Stack;

public class Main extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
        // Stack<Object> myStack = new Stack<>();
        // myStack.push("Hello");
        // myStack.push(1);
        // System.out.println(myStack.pop().getData());
        // System.out.println(myStack.pop().getData());
        
    }

    @Override
    public void start(Stage ps) throws Exception {
        primaryStage = ps;
        primaryStage.getIcons().add(new Image("file:src/main/resources/com/gui/images/logo/Tux.png"));
        show("homepage");
    }

    public static void show(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(fxml + ".fxml"));
        BorderPane mainLayout = loader.load();

        Scene scene = new Scene(mainLayout, 640, 480);
        primaryStage.setScene(scene);
        String c = fxml.substring(0, 1).toUpperCase();
        String title = c + fxml.substring(1);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
