package com.gui.Bot;

import java.util.ArrayList;
import java.util.Random;

import com.gui.GameController;
import com.shared.Cord;

import javafx.scene.shape.Shape;
import javafx.util.Pair;

public class RandomBot extends ARobot {

    public RandomBot(){super();}
    public RandomBot(GameController controller) {
        super(controller);
    }

    @Override
    public Pair<Cord, Cord> getMoveCords() {
        ArrayList<Cord> from = getRandomFrom();
        return getRandomTo(from);
    }

    private ArrayList<Cord> getRandomFrom() {
        ArrayList<Cord> cords = controller.findPlayerPieces(controller.getCurrentPlayer());
        return cords;
    }

    private Pair<Cord,Cord> getRandomTo(ArrayList<Cord> indexList) {
        Shape[][] Board = controller.getBoard();
        ArrayList<Pair<Cord,Cord>> possibleMoves = new ArrayList<>();
        for(Cord index : indexList){
            for (int horizontalIndex = index.getHorizontal() - 2; horizontalIndex <= index.getHorizontal()
                    + 2; horizontalIndex++) {
                for (int verticalIndex = index.getVertical() - 2; verticalIndex <= index.getVertical()
                        + 2; verticalIndex++) {
                    if (controller.isIndexWithinBounds(horizontalIndex) && controller.isIndexWithinBounds(verticalIndex)) {
                        if (!Board[verticalIndex][horizontalIndex].isVisible()) {
                            possibleMoves.add(new Pair<Cord,Cord>(index, new Cord(verticalIndex, horizontalIndex)));
                        }
                    }
                }
            }
        }
        return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
    }
}
