package com.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WinnerController extends AController{

    @FXML
    private Button HomePageEnter;

    

    public void switchToGame() throws Exception{
        Main.show("homepage","", false);
    }
    
}
