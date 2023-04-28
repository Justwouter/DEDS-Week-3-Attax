package com.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
//import com.game.Stack;



public class Main extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    
    @Override
    public void start(Stage ps) throws Exception {
        primaryStage = ps;
        primaryStage.getIcons().add(new Image("file:src/main/resources/com/gui/images/logo/Tux.png"));
        show("homepage", new HomeController());
    }

    public static void show(String fxml, AController controller) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(fxml + ".fxml"));
        BorderPane mainLayout = null;
        try{
            mainLayout= loader.load();
            loader.setController(controller);
            
            Scene scene = new Scene(mainLayout, 1280, 720);
            primaryStage.setScene(scene);
            String c = fxml.substring(0, 1).toUpperCase();
            String title = c + fxml.substring(1);
            primaryStage.setTitle(title);
            primaryStage.setResizable(false);
            primaryStage.show();
        }catch(IOException ex){ex.printStackTrace();}


        
    }
}