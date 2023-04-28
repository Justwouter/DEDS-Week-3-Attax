package com.gui.Support;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

public class Player {
    Shape icon;
    String name;
    int id;
    Paint playerColor;
    boolean isBot;
    int totalPointsInCurrentGame;
    //Add statcounters here or smth
    public Player(){}
    public Player(Shape icon, String name, Paint color, Boolean isBot){
        this.icon = icon;
        this.name = name;
        this.playerColor = color;
        this.isBot = isBot;
    }

    public boolean IsBot() {
        return this.isBot;
    }

    public Shape getIcon() {
        return this.icon;
    }

    public void setIcon(Shape icon) {
        this.icon = icon;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paint getPlayerColor() {
        return this.playerColor;
    }

    public void setPlayerColor(Paint playerColor) {
        this.playerColor = playerColor;
    }

    public void setGamePoints(int points){
        this.totalPointsInCurrentGame = points;
    }
    public int getGamePoints(){
        return totalPointsInCurrentGame;
    }

}
