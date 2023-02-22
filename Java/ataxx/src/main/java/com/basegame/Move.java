package com.basegame;

import com.basegame.interfaces.IPlayer;

public class Move {
    public IPlayer player;
    public int fromX;
    public int fromY;
    public int toX;
    public int toY;
    public boolean jump;
    
    public Move(IPlayer player, int x1,int y1, int x2, int y2, boolean jump){
        this.player = player;
        this.fromX = x1;
        this.fromY = y1;
        this.toX = x2;
        this.toY = y2;
        this.jump = jump;
    }
}
