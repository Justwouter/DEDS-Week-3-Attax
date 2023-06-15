package com.gui;

import java.util.Map;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class WinnerController extends AController{

    @FXML
    public Label WinnerLabel;

    @FXML
    private Button HomePageEnter;

    @FXML
    private BorderPane MainPane;

    public void switchToGame() throws Exception{
        Main.show("homepage",this.dataDict);
    }
    
    @Override
    public void loadData(Map<String, Object> dataDict) {
        this.dataDict = dataDict;


        Label winLabel = new Label((String) dataDict.get("VictoryText"));
        MainPane.setTop(winLabel);
        BorderPane.setAlignment(winLabel, Pos.CENTER);
    }
}
