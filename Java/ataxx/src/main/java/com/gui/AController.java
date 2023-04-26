package com.gui;

import com.gui.Support.Player;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public abstract class AController {
    @FXML
    public Label WinnerLabel = new Label();

    public Player player0;
    public Player player1;

}
//= new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), "Wouter", player0Color, false);
//= new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), "Marieke", player1Color, true);