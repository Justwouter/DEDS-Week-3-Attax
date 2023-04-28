package com.gui.Bot;

import com.gui.GameController;
import com.shared.Cord;

import javafx.util.Pair;

public abstract class ARobot {
    GameController controller;
    public abstract Pair<Cord,Cord> getMoveCords();
}
