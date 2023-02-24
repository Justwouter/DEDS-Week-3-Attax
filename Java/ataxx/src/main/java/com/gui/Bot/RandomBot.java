package com.gui.Bot;

import com.shared.Cord;
import com.shared.Stack;

import javafx.scene.control.Button;

public class RandomBot {
    public Cord from;
    public Cord to;

    public void pickRandomFrom(Button[][] board, String identifier){
        Stack<Cord> possibleStarts = new Stack<>();
        for(int y =0;y<board.length;y++){

            for(int x =0;x<board[0].length;x++){
                if(board[y][x].isVisible() && board[y][x].getStyle() == identifier){
                    possibleStarts.push(new Cord(x, y));
                }
            }
        }
        this.from =  possibleStarts.getIndex(getRandomNumber(0, possibleStarts.length())); //ToFix This errors when the stack is empty and returns null
    }

    public void pickRandomTo(Button[][] board, String CloneId, String JumpID){
        Stack<Cord> possibleMoves = new Stack<>();
        for(int y =0;y<board.length;y++){
            for(int x =0;x<board[0].length;x++){
                if(board[x][y].getStyle() == CloneId || board[x][y].getStyle() == JumpID){
                    possibleMoves.push(new Cord(x, y));
                }
            }
        }
        this.to =  possibleMoves.getIndex(getRandomNumber(0, possibleMoves.length())); //ToFix This errors when the stack is empty and returns null
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    
}
