package com.shared;

import javafx.scene.layout.GridPane;

public class BoardState<T> {
    public T[][] board;
    public boolean playerturn;
    public GridPane displayBoard;
    
    public BoardState(T[][] board, boolean playerturn, GridPane display){
        this.board = board;
        this.playerturn = playerturn;
        this.displayBoard = display;
    }
}
