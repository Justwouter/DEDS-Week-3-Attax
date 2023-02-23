package com.basegame.Players;

import java.util.concurrent.ThreadLocalRandom;

import com.basegame.Board;
import com.basegame.Cord;
import com.basegame.Move;
import com.basegame.interfaces.IPiece;
import com.basegame.interfaces.IPlayer;
import com.shared.Stack;

public class RandomBotPlayer implements IPlayer{
    IPiece character;
    int[] from;

    public RandomBotPlayer(){}
    public RandomBotPlayer(IPiece character){
        this.character = character;
    }

    @Override
    public void playTurn(Board gameBoard) {
        Move myMove = pickRandomMove(FindMoves(gameBoard, findPieces(gameBoard)));
        gameBoard.movePiece(this, myMove.fromX, myMove.fromY,myMove.toX , myMove.toY);
        gameBoard.printer.println("Player "+character.getChar() + ": "+(myMove.fromX)+","+(myMove.fromY)+" - "+(myMove.toX)+","+(myMove.toY));
    }

    protected Stack<Cord> findPieces(Board gameBoard){
        Stack<Cord> cordStack = new Stack<>();
        for (int i = 0; i < gameBoard.BoardPieces.length; i++) {
            for (int j = 0; j < gameBoard.BoardPieces[i].length; j++) {
                if(gameBoard.BoardPieces[i][j] == this.character){
                    cordStack.push(new Cord(i, j));
                }
            }
        }
        System.out.println(cordStack.length());//DEBUG
        return cordStack;
    }

    protected Stack<Move> FindMoves(Board gameBoard, Stack<Cord> pieces){
        Stack<Move> totalMovesStack = new Stack<>();
        for(int i = 0; i< pieces.length();i++){
            Cord cords = pieces.getIndex(i);
            Stack<Move> tempStack =  gameBoard.validMovesForPiece(cords.getY(), cords.getX());
            for(int j = 0; j < tempStack.length();j++){
                totalMovesStack.push(tempStack.getIndex(j));
            }
        }
        System.out.println(totalMovesStack.length()); //DEBUG
        return totalMovesStack;
    }

    protected Move pickRandomMove(Stack<Move> possibleMoves){
        return possibleMoves.getIndex(ThreadLocalRandom.current().nextInt(0, possibleMoves.length()));
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
