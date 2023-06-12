package com.gui.Bot;

import java.util.ArrayList;
import java.util.Random;

import com.gui.GameController;
import com.shared.Cord;

import javafx.util.Pair;

public class RandomBot extends ARobot {

    public RandomBot(GameController controller) {
        super(controller);
    }

    @Override
    public Pair<Cord, Cord> getMoveCords() {
        
        Cord from = getRandomFrom();
        Cord to = getRandomTo(from);
        return new Pair<Cord, Cord>(from, to);
    

    }

    private Cord getRandomFrom() {
        ArrayList<Cord> cords = controller.findPlayerPieces(controller.getCurrentPlayer());
        return cords.get(new Random().nextInt(cords.size()));
    }

    private Cord getRandomTo(Cord index) {
        ArrayList<Cord> possibleMoves = new ArrayList<>();
        for (int horizontalIndex = index.getHorizontal() - 2; horizontalIndex <= index.getHorizontal()
                + 2; horizontalIndex++) {
            for (int verticalIndex = index.getVertical() - 2; verticalIndex <= index.getVertical()
                    + 2; verticalIndex++) {
                if (controller.isIndexWithinBounds(horizontalIndex) && controller.isIndexWithinBounds(verticalIndex)) {
                    if (!controller.Board[verticalIndex][horizontalIndex].isVisible()) {
                        possibleMoves.add(new Cord(verticalIndex, horizontalIndex));
                    }
                }
            }
        }

        return possibleMoves.get(new Random().nextInt(possibleMoves.size()));

    }
}
