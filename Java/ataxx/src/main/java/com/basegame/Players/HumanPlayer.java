package com.basegame.Players;

import com.basegame.Board;
import com.basegame.Move;
import com.basegame.ScannerV3;
import com.basegame.interfaces.IPiece;
import com.basegame.interfaces.IPlayer;
import com.basegame.interfaces.IPrinter;
import com.basegame.interfaces.IScanner;

//TODO Implement screen cleaning after bad input. Maybe possibly 
//TODO Move move validation to board instead of individual users
public class HumanPlayer implements IPlayer {
    IScanner scanner = new ScannerV3();
    IPiece character;
    int[] from;

    public HumanPlayer(IPiece character) {
        this.character = character;
    }

    @Override
    public void playTurn(Board gameBoard) {
        IPrinter printer = gameBoard.printer;
        from = null;


        printer.println("Your character is: " + character.getChar());
        printer.println("Select your piece with the format \"Xaxis number,Yaxis number\": ");
        this.from = handleCordsInput(gameBoard);

        gameBoard.drawBoard();

        printer.println("Your character is: " + character.getChar());
        printer.println("Select your target with the format \"Xaxis number,Yaxis number\": ");
        int[] to = handleCordsInput(gameBoard);

        gameBoard.movePiece(new Move(this, from[0], from[1], to[0], to[1]));
    }

    protected int[] handleCordsInput(Board gameBoard) {
        
        String input = scanner.nextLine();
        String[] numbers = input.split(",");
        if (numbers.length == 2) {
            try {
                int Xaxis = Integer.parseInt(numbers[0].trim())-1;
                int Yaxis = Integer.parseInt(numbers[1].trim())-1;
                System.out.println("Out:"+Xaxis + ","+Yaxis);

                if (!playerMoveValid(Yaxis, Xaxis, gameBoard)) {
                    System.out.println("Please make a valid move");
                    return handleCordsInput(gameBoard);

                }

                return new int[] { Xaxis, Yaxis };


            } catch (NumberFormatException e) {}
        }
        System.out.println("Please use a valid format");
        return handleCordsInput(gameBoard);
    }

    protected boolean playerMoveValid(int vertical, int horizontal, Board gameBoard) {
        if(gameBoard.isCordWithinBoard(vertical, horizontal)){
            if(from != null && moveWithinRadius(vertical,horizontal)){
                return gameBoard.getCords(vertical, horizontal) == gameBoard.defaultPiece;
            }
            return gameBoard.getCords(vertical, horizontal) == this.character;
        }
        
        return false;
    }

    protected boolean moveWithinRadius(int y, int x){
        return Math.abs(from[0] - x) <= 2 && (Math.abs(from[1] - y) ) <=2;
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