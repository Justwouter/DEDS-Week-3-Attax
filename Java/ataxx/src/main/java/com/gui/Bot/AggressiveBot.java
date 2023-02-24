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

    public void pickRandomMove(Button[][] board, String identifier){
        Cord bestMoveFrom = null;
        Cord bestMoveTo = null;
        int piecesTaken = 0;
        for(int y =0;y<board.length;y++){
            for(int x =0;x<board[0].length;x++){
                if(board[y][x].isVisible() && board[y][x].getStyle() == identifier){
                    controller.ColorButtonsInner(new Cord(x, y));
                    controller.ColorButtonsOuter(new Cord(x, y));
                    for(int vertical = y-2; vertical <=y+2;vertical++){
                        for(int horizontal = x-2; horizontal <=x+2;horizontal++){
                            if(controller.isIndexInBoard(vertical, horizontal)){
                                int infections = controller.infectButtonsAmount(board[y][x]);
                                if(infections > piecesTaken){
                                    piecesTaken = infections;
                                    bestMoveFrom = new Cord(x, y);
                                    bestMoveTo = new Cord(horizontal, vertical);
                                }
                            }
                        
                        }
                    }
                    

                }
            }
        }
        if(bestMoveFrom == null && bestMoveTo == null){
            RandomBot bot = new RandomBot();
            bot.pickRandomFrom(board, identifier);
            this.from = bot.from;
            bot.pickRandomTo(board, controller.CloneRadius, controller.JumpRadius);
            this.to = bot.to;
        }
        else{
            this.from = bestMoveFrom;
            this.to = bestMoveTo;
        }
        controller.CloseMoveMenu();

    }

    



    public void pickRandomTo(Button[][] board, String CloneId, String JumpID){}

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    
    
}
