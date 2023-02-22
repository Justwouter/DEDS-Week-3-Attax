package com.basegame;

import com.basegame.Players.HumanPlayer;
// import com.basegame.interfaces.IBoard;
import com.basegame.interfaces.IPlayer;

public class Game {
    public Board GameBoard;
    public IPlayer Player1;
    public IPlayer Player2;
    public IPlayer lastPlayer;

    public Game() {

        this.GameBoard = new Board(7);
        this.Player1 = new HumanPlayer(new AtaxxPiece('\u27D0'));
        this.Player2 = new HumanPlayer(new AtaxxPiece('\u25EF'));
        startGame();
    }

    public void startGame() {
        GameBoard.fillStartingCorners(Player1, Player2);
        while (true) {
            if(isGameDone(Player1)){break;}
            GameLoop(Player1);
            if(isGameDone(Player2)){break;}
            GameLoop(Player2);
        }
        handleWinner();
        
    }
    public void GameLoop(IPlayer player){
        GameBoard.drawBoard(Player1,Player2);
        player.playTurn(GameBoard);
        lastPlayer = player;

    }

    public void handleWinner(){
        if(lastPlayer != null)
            GameBoard.printer.print("Player " +lastPlayer.getChar().getChar()+" is the winner!");
        else
            GameBoard.printer.print("Player " +Player1.getChar().getChar()+" and Player " + Player2.getChar().getChar()+" have drawn!");
        
    }

    public void setBestScoringPlayer(){
        int score1 = GameBoard.getScore(Player1);
        int score2 = GameBoard.getScore(Player2);

        lastPlayer = (score1 > score2) ? Player2 : Player1;
    }

    public boolean isGameDone(IPlayer player){
        //Helpers.clearScreen();
        if(!GameBoard.isPieceOnBoard(GameBoard.defaultPiece)){
            setBestScoringPlayer();
            return true;
        };
        if(!GameBoard.validMovesLeft(player)){
            return true;
        }
        return false;
    }
}
