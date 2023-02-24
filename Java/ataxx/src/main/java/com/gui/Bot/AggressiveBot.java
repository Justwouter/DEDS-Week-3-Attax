package com.gui.Bot;

import com.gui.GameController;
import com.shared.Cord;
import com.shared.Stack;

import javafx.scene.control.Button;

public class AggressiveBot {
    public Cord from;
    public Cord to;
    public GameController controller;

    public AggressiveBot(GameController controller){
        this.controller = controller;
    }

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
        Cord bestMove = null;
        int piecesTaken = 0;
        for(int y =0;y<board.length;y++){
            for(int x =0;x<board[0].length;x++){
                if(board[x][y].getStyle() == CloneId || board[x][y].getStyle() == JumpID){
                    int currentConverter = controller.infectButtonsAmount(board[y][x]);
                    if(currentConverter > piecesTaken){
                        bestMove = new Cord(x, y);
                        piecesTaken = currentConverter;
                    }
                }
            }
        }
        if(bestMove == null){
            RandomBot bot = new RandomBot();
            bot.from = this.from;
            bot.pickRandomTo(board, CloneId, JumpID);
            this.to = bot.to;
        }
        else{
            this.to = bestMove;
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    
    
}
