package com.gui;

import com.gui.Support.Player;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public abstract class AController {
    @FXML
    public Label WinnerLabel = new Label();

    public static Player player0; //Cheating a bit but conventional OOP ways don't seem to work.
    public static Player player1;

    public void setPlayer0(Player p0){
        player0 = p0;
    }
    public void setPlayer1(Player p1){
        player1 = p1;
    }

}
//= new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), "Wouter", player0Color, false);
//= new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), "Marieke", player1Color, true);