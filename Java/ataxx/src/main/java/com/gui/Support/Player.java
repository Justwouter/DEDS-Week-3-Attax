package com.gui.Support;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

public class Player {
    Shape icon;
    String name;
    int id;
    Paint playerColor;
    //Add statcounters here or smth

    public Player(Shape icon, String name, Paint color){
        this.icon = icon;
        this.name = name;
        this.playerColor = color;
    }
}
