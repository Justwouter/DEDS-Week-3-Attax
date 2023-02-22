package com.basegame;

import com.basegame.interfaces.IPiece;

public class AtaxxPiece implements IPiece{
    private char character;

    public AtaxxPiece(char character){
        this.character = character;
    }

    @Override
    public Object getChar() {
        return character;
    }
    
    
}
