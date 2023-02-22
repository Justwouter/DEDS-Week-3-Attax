package com.basegame.interfaces;

public interface IBoard {
    public void drawboard();
    public void movePiece(IPlayer player,int x1,int y1, int x2,int y2);
}
