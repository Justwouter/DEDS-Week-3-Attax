package com.basegame;

// import com.basegame.interfaces.IBoard;
import com.basegame.interfaces.IPlayer;

public class Game {
    public Board GameBoard;
    public IPlayer Player1;
    public IPlayer Player2;
    public boolean CurrentPlayer;

    public Game() {

        this.GameBoard = new Board(7);
        this.Player1 = new HumanPlayer(new AtaxxPiece('\u27D0'));
        this.Player2 = new HumanPlayer(new AtaxxPiece('\u25EF'));
        startGame();
    }

    public void startGame() {
        GameBoard.fillStartingCorners(Player1, Player2);
        while (!isGameDone()) {
            GameBoard.drawBoard(Player1, Player2);
            Player1.playTurn(GameBoard);
            GameBoard.drawBoard(Player1,Player2);
            Player2.playTurn(GameBoard);

        }
    }

    

    public boolean isGameDone(){
        return false;
    }
}
