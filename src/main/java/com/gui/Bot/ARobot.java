package com.gui.Bot;

import com.gui.GameController;
import com.shared.Cord;

import javafx.util.Pair;

public abstract class ARobot {
    GameController controller;
    public ARobot(GameController controller){
        this.controller = controller;
    }
    public abstract Pair<Cord,Cord> getMoveCords();
}
