package com.basegame;

import com.basegame.Players.RandomBotPlayer;
// import com.basegame.interfaces.IBoard;
import com.basegame.interfaces.IPiece;
import com.basegame.interfaces.IPlayer;
import com.basegame.interfaces.IPrinter;
import com.shared.Stack;

public class Board {
    //Index 1 is Y index 2 is X! Yes, It's stupid.
    public IPiece[][] BoardPieces;
    public Stack<Move> movementStack = new Stack<>();
    public IPrinter printer = new AtaxxPrinter();
    public final IPiece defaultPiece = new AtaxxPiece('\u25A0');

    public Board() {
        this(7, 7);
    }

    public Board(int singleDimension) {
        this(singleDimension, singleDimension);
    }

    public Board(int yAmount, int xAmount) {
        this.BoardPieces = new IPiece[yAmount][xAmount];
        fillBoard();
        
    }

    public void fillBoard() {
        for (int i = 0; i < BoardPieces.length; i++) {
            for (int j = 0; j < BoardPieces[i].length; j++) {
                this.BoardPieces[i][j] = defaultPiece;
            }
        }
    }

    public void fillStartingCorners(IPlayer player1, IPlayer player2){
        for(int i = BoardPieces.length-2;i<=BoardPieces.length-1;i++){
            for(int j=0;j<=1;j++){
                BoardPieces[i][j] = player1.getChar();
                BoardPieces[j][i] = player2.getChar();
            }
        }
    }

    public void drawBoard() {
        //Helpers.clearScreen();
        drawMainBoard();
    }
    public void drawBoard(IPlayer player1, IPlayer player2) {
        
        this.drawBoard();
        drawScoreBoard(player1, player2);
    }
    private void drawMainBoard(){

        // Actual board & Side legend
        for (int i = 0; i < BoardPieces.length; i++) {
            printer.print(i + 1);
            for (int j = 0; j < BoardPieces[i].length; j++) {
                printer.print(" " + this.BoardPieces[i][j].getChar());
            }
            printer.println();
        }
        // Legend Bottom
        for (int j = 0; j <= BoardPieces.length; j++) {
            printer.print(j + " ");
        }
        printer.println();
    } 
    public void drawScoreBoard(IPlayer player1, IPlayer player2){
        printer.println("Score: "+player1.getChar().getChar()+" = "+getScore(player1)+" - "+player2.getChar().getChar()+" = "+getScore(player2));
    }


    public int getScore(IPlayer player){
        int counter = 0;
        for (int i = 0; i < BoardPieces.length; i++) {
            for (int j = 0; j < BoardPieces[i].length; j++) {
                if(this.BoardPieces[i][j] == player.getChar()){
                    counter++;
                }
            }
        }
        return counter;
    }

    

    public boolean movePiece(Move move) {
        System.out.println("Movepiece: "+(move.fromX+1)+","+(move.fromY+1) + "->"+(move.toX+1)+","+(move.toY+1));
        if(isCordWithinBoard(move.fromY, move.fromX) && isCordWithinBoard(move.toY, move.toX) && this.BoardPieces[move.toY][move.toX] != move.player.getChar()){
            this.BoardPieces[move.toY][move.toX] = move.player.getChar();
            if(!checkClone(move) && checkJump(move)){
                this.BoardPieces[move.fromX][move.fromY] = this.defaultPiece;
            }
            movementStack.push(move);
            convertAdjecentPieces(move.player, move.toY, move.toX);
            return true;
        }
        return false;











        // if (isCordWithinBoard(x1, x1) && isCordWithinBoard(x2, y2) && this.BoardPieces[y2][x2] != player.getChar()) {
        //     this.BoardPieces[y2][x2] = player.getChar();
        //     boolean jumper = false;
            
        //     if (!checkClone(x1, y1, x2, y2) && checkJump(x1, y1, x2, y2)) {
        //         this.BoardPieces[y1][x1] = this.defaultPiece;
        //         jumper = true;
        //     }
        //     movementStack.push(new Move(player, x1, y1, x2, y2, jumper));
        //     convertAdjecentPieces(player, x2, y2);
        //     return true;
        // }
        // return false;
    }

    public boolean isCordWithinBoard(int y, int x) {
        return !(x < 0 | x > getBoardXSize() | y < 0 | y > getBoardYSize());
    }

    protected void convertAdjecentPieces(IPlayer player, int y, int x){
        for(int i = y-1;i<=y+1;i++){
            if(i >-1 && i < getBoardYSize()){
                for(int j = x-1;j<=x+1;j++){
                    if(j > -1 && j < getBoardXSize() && BoardPieces[i][j] != defaultPiece){
                        BoardPieces[i][j] = player.getChar();
                    }
                }
            }
        }
    }

    protected boolean checkClone(Move move) {
        return ((Math.abs(move.fromX - move.toX) <= 1) && (Math.abs(move.fromY - move.toY) <= 1));
    }

    protected boolean checkJump(Move move) {
        return ((Math.abs(move.fromX - move.toX) <= 2) && (Math.abs(move.fromY - move.toY) <= 2));
    }

    public IPiece getCords(int y, int x) {
        return BoardPieces[y][x];
    }

    public boolean isPieceOnBoard(IPiece piece){
        for (int i = 0; i < BoardPieces.length; i++) {
            for (int j = 0; j < BoardPieces[i].length; j++) {
                if(BoardPieces[i][j] == piece){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean validMovesLeft(IPlayer player){
        for(int vertical = 0;vertical < getBoardYSize(); vertical++){
            for(int horizontal = 0;horizontal< getBoardXSize() ;horizontal++){
                if(BoardPieces[vertical][horizontal] == player.getChar()){
                    return validMovesForPiece(vertical, horizontal).length() > 0;
                }

            }
        }
        return false;
    }
    
    public Stack<Move> validMovesForPiece(int y, int x){
        Stack<Move> possibleMoves = new Stack<>();
        for(int vertical = (y-2);vertical <= (y+2); vertical++){
            for(int horizontal = (x-2);horizontal<=(x+2);horizontal++){
                if(horizontal< getBoardXSize() && vertical < getBoardYSize() && isCordWithinBoard(vertical, horizontal) && BoardPieces[vertical][horizontal] == this.defaultPiece){
                    possibleMoves.push(new Move(new RandomBotPlayer(), x, y, horizontal, vertical));
                    //System.out.println((x+1)+","+(y+1) + "->"+(horizontal+1)+","+(vertical+1) + " True");

                }
                else{
                    //System.out.println((x+1)+","+(y+1) + "->"+(horizontal+1)+","+(vertical+1) + " False");

                }
            }
        }
        //debugMethod(possibleMoves);
        return possibleMoves;
    }

    public void debugMethod(Stack<Move> stack){
        drawBoard();
        int size = stack.length();
        for(int i = 0; i<size;i++){
            Move move = stack.pop().getData();
            System.out.println((move.fromX+1)+","+(move.fromY+1) + "->"+(move.toX+1)+","+(move.toY+1));
        }
        Helpers.sleep(100000, true);
    }


    public int getBoardXSize() {
        return BoardPieces[0].length;
    }
    public int getBoardYSize() {
        return BoardPieces.length;
    }

}
