package com.basegame;

import com.basegame.interfaces.IPlayer;

public class Move {
    public IPlayer player;
    public int toX;
    public int toY;
    public int fromX;
    public int fromY;

    public Move(int x1,int y1, int x2, int y2){
        this.fromX = x1;
        this.fromY = y1;
        this.toX = x2;
        this.toY = y2;
    }
    
    public Move(IPlayer player, int x1,int y1, int x2, int y2){
        this.player = player;
        this.fromX = x1;
        this.fromY = y1;
        this.toX = x2;
        this.toY = y2;
    }
}
