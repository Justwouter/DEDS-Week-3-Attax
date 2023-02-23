package com.basegame;

import com.basegame.Players.HumanPlayer;
import com.basegame.Players.RandomBotPlayer;
import com.basegame.interfaces.IPiece;
// import com.basegame.interfaces.IBoard;
import com.basegame.interfaces.IPlayer;
import com.basegame.interfaces.IPrinter;
import com.basegame.interfaces.IScanner;

public class Game {
    private final long TotalMovesInGame = 9223372036854775807L;
    public Board GameBoard;
    public IPlayer Player1;
    public IPlayer Player2;
    public IPlayer lastPlayer;
    IScanner scanner = new ScannerV3();
    IPrinter printer = new AtaxxPrinter();


    public Game() {

        this.GameBoard = new Board(7);
        this.Player1 = choosePlayer(1, new AtaxxPiece('\u27D0'));
        this.Player2 = choosePlayer(2, new AtaxxPiece('\u25EF'));
        startGame();
    }

    public IPlayer choosePlayer(int playernumber, IPiece character){
        printer.println("Who plays as player "+playernumber+" With character " + character.getChar()+"?");
        printer.println("1: Human Player \n2: Random Bot");
        switch(scanner.nextInt()){
            case 1:
                return new HumanPlayer(character);

            case 2:
                return new RandomBotPlayer(character);

            default:
                Helpers.clearScreen();
                printer.println("Please choose a valid option!");
                return choosePlayer(playernumber, character);
        }
    }

    public void startGame() {
        GameBoard.fillStartingCorners(Player1, Player2);

        for(long i = 0; i<TotalMovesInGame;i++) {
            if(isGameDone(Player1)){handleWinner(false);return;}
            GameLoop(Player1);
            //Helpers.sleep(2, true);
            if(isGameDone(Player2)){handleWinner(false);return;}
            GameLoop(Player2);
            //Helpers.sleep(2, true);
        }
        handleWinner(true);
        
        
    }
    public void GameLoop(IPlayer player){
        GameBoard.drawBoard(Player1,Player2);
        player.playTurn(GameBoard);
        lastPlayer = player;

    }

    public void handleWinner(boolean timeup){
        if(timeup){
            printer.println("Player " +Player1.getChar().getChar()+" and Player " + Player2.getChar().getChar()+" took longer than "+TotalMovesInGame+" moves!");

        }
        else{
            if(lastPlayer != null)
                printer.println("Player " +lastPlayer.getChar().getChar()+" is the winner!");
            else
                printer.println("Player " +Player1.getChar().getChar()+" and Player " + Player2.getChar().getChar()+" have drawn!");
        
        }
        
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
