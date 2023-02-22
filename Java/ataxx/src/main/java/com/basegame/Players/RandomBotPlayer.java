package com.basegame.Players;

import com.basegame.Board;
import com.basegame.interfaces.IPiece;
import com.basegame.interfaces.IPlayer;

public class RandomBotPlayer implements IPlayer{
    IPiece character;
    int[] from;


    @Override
    public void playTurn(Board gameBoard) {
        
    }

    @Override
    public IPiece getChar() {
        return character;
    }

    @Override
    public void setChar(IPiece character) {
        this.character = character;
    }
    
}
