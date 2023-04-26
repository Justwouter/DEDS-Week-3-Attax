package com.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WinnerController extends AController{

    @FXML
    private Button HomePageEnter;

    public WinnerController(){}
    public WinnerController(String text){
        WinnerLabel.setText(text);
    }

    public void switchToGame() throws Exception{
        Main.show("homepage",new HomeController());
    }
    
}
