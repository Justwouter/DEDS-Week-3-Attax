package com.basegame;

import com.basegame.interfaces.IPiece;
import com.basegame.interfaces.IPlayer;
import com.basegame.interfaces.IPrinter;
import com.basegame.interfaces.IScanner;

//TODO Implement screen cleaning after bad input.
//TODO Unit takeover & check voor queen style move & disallow knight style move.
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
        this.from = handleCordsInput(0, gameBoard);

        gameBoard.drawBoard();

        printer.println("Your character is: " + character.getChar());
        printer.println("Select your target with the format \"Xaxis number,Yaxis number\": ");
        int[] to = handleCordsInput(0, gameBoard);

        gameBoard.movePiece(this, from[0], from[1], to[0], to[1]);
    }

    protected int[] handleCordsInput(int depth, Board gameBoard) {
        if (depth <= 5) {
            String input = scanner.nextLine();
            String[] numbers = input.split(",");
            if (numbers.length == 2) {
                try {
                    int Xaxis = Integer.parseInt(numbers[0].trim());
                    int Yaxis = Integer.parseInt(numbers[1].trim());

                    if (!playerMoveValid(Xaxis, Yaxis, gameBoard)) {
                        System.out.println("Please make a valid move");
                        return handleCordsInput(depth + 1, gameBoard);

                    }

                    return new int[] { Xaxis, Yaxis };


                } catch (NumberFormatException e) {}
            }
            System.out.println("Please use a valid format");
            return handleCordsInput(depth + 1, gameBoard);
        }
        return null;

    }

    protected boolean playerMoveValid(int x, int y, Board gameBoard) {
        if(gameBoard.isCordWithinBoard(x, y)){
            if(from != null && moveWithinRadius(x,y)){
                return gameBoard.getCords(x, y) == gameBoard.defaultPiece;
            }
            return gameBoard.getCords(x, y) == this.character;
        }
        
        return false;

    }

    protected boolean moveWithinRadius(int x, int y){
        return ((Math.abs(from[0] - x) <= 2) && (Math.abs(from[1] - y) <= 2));
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
