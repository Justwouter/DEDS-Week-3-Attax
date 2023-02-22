package com.basegame.interfaces;

import com.basegame.Board;

public interface IPlayer {
    public void playTurn(Board gameBoard);
    public IPiece getChar();
    public void setChar(IPiece character);
    
}
