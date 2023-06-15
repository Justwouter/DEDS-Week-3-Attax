package com.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class Main extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    
    @Override
    public void start(Stage ps) throws Exception {
        primaryStage = ps;
        primaryStage.getIcons().add(new Image("file:src/main/resources/com/gui/images/logo/Tux.png"));
        show("homepage", new HashMap<>());
    }

    public static void show(String fxml, Map<String,Object> dataDict) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(fxml + ".fxml"));
        try{
            Pane mainLayout= loader.load();
            AController ctrl = loader.getController();
            ctrl.loadData(dataDict);
            
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