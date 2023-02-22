package com.basegame;

// import com.basegame.interfaces.IBoard;
import com.basegame.interfaces.IPiece;
import com.basegame.interfaces.IPlayer;
import com.basegame.interfaces.IPrinter;
import com.shared.Stack;

public class Board {
    public IPiece[][] BoardPieces;
    public Stack<Move> movementStack = new Stack<>();
    public IPrinter printer = new AtaxxPrinter();
    public IPiece defaultPiece = new AtaxxPiece('\u25A0');

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
        for(int i =5;i<=6;i++){
            for(int j=0;j<=1;j++){
                BoardPieces[i][j] = player1.getChar();
                BoardPieces[j][i] = player2.getChar();
            }
        }
    }

    public void drawBoard() {
        Helpers.clearScreen();
        drawMainBoard();
    }
    public void drawBoard(IPlayer player1, IPlayer player2) {
        
        this.drawBoard();
        drawScoreBoard(player1, player2);
    }
    private void drawMainBoard(){
        // Legend Top
        for (int j = 0; j <= BoardPieces.length; j++) {
            printer.print(j + " ");
        }
        printer.println();

        // Actual board & Side legend
        for (int i = 0; i < BoardPieces.length; i++) {
            printer.print(i + 1);
            for (int j = 0; j < BoardPieces[i].length; j++) {
                printer.print(" " + this.BoardPieces[i][j].getChar());
            }
            printer.println();
        }
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

    

    public boolean movePiece(IPlayer player, int x1, int y1, int x2, int y2) {
        x1 = x1-1;
        y1 = y1-1;
        x2= x2-1;
        y2=y2-1;
        if (isCordWithinBoard(x1, x1) && isCordWithinBoard(x2, y2)) {
            this.BoardPieces[y2][x2] = player.getChar();
            boolean jumper = false;
            
            if (!checkClone(x1, y1, x2, y2) && checkJump(x1, y1, x2, y2)) {
                this.BoardPieces[y1][x1] = this.defaultPiece;
                jumper = true;
            }
            movementStack.push(new Move(player, x1, y1, x2, y2, jumper));
            return true;
        }
        return false;
    }



    public boolean isCordWithinBoard(int x, int y) {
        return !(x <= -1 | x > getBoardXSize()-1 | y <= -1 | y > getBoardYSize()-1);
    }

    protected boolean checkClone(int x1, int y1, int x2, int y2) {
        return ((Math.abs(x1 - x2) <= 1) && (Math.abs(y1 - y2) <= 1));
    }

    protected boolean checkJump(int x1, int y1, int x2, int y2) {
        return ((Math.abs(x1 - x2) <= 2) && (Math.abs(y1 - y2) <= 2));
    }

    public IPiece getCords(int x, int y) {
        printer.println(BoardPieces[y-1][x-1].getChar());
        return BoardPieces[y-1][x-1];
    }





    public int getBoardXSize() {
        return BoardPieces[0].length;
    }

    public int getBoardYSize() {
        return BoardPieces.length;
    }

}
