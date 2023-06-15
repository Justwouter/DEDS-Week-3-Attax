package com.gui;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import com.gui.Support.Player;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class HomeController extends AController implements Initializable{

    @FXML
    private Button HomePageEnter;

    @FXML
    private TextField Player0Name;

    @FXML
    private TextField Player1Name;

    @FXML
    private CheckBox Player1BotCheck;

    @FXML
    private CheckBox Player0BotCheck;

    Player player0;
    Player player1;

    @FXML
    private void switchToGame() {
        makeUsers();
        Main.show("game",this.dataDict);
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setupTextUpdateHandlers();
    }

    private void setupTextUpdateHandlers(){
        HomePageEnter.setOnAction(e -> switchToGame());
    }

    public void makeUsers(){
        if(Player0Name.getText().isEmpty()){
            Player0Name.setText("Player1");
        }
        if(Player1Name.getText().isEmpty()){
            Player1Name.setText("Player2");
        }
        player0 = new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), Player0Name.getText(), Color.rgb(30, 144, 255), Player0BotCheck.isSelected());
        player1 = new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), Player1Name.getText(), Color.rgb(255, 0, 0), Player1BotCheck.isSelected());
        
        dataDict.put("Player0", player0);
        dataDict.put("Player1", player1);
    }


    @Override
    public void loadData(Map<String, Object> dataDict) {
        this.dataDict = dataDict;
    }

}
