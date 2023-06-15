package com.gui.Bot;

import java.util.ArrayList;
import java.util.Random;

import com.gui.GameController;
import com.shared.Cord;

import javafx.scene.shape.Shape;
import javafx.util.Pair;

/***
 * Bot that will always try to take the most pieces.
 * If moves take an equal amount of pieces, choose one at random.
 */
public class AggressiveBot extends ARobot {

    public AggressiveBot() {super();}
    public AggressiveBot(GameController controller) {
        super(controller);
    }

    @Override
    public Pair<Cord, Cord> getMoveCords() {
        // Find all pieces of the player currently at turn.
        ArrayList<Cord> possibleStarts = controller.findPlayerPieces(controller.getCurrentPlayer());

        int bestScore = 0;
        ArrayList<Pair<Cord, Cord>> bestTargets = new ArrayList<>();

        // For each found piece, calculate possible moves and evaluate them based on the amount of pieces captured by the move.
        for (Cord from : possibleStarts) {
            for (Cord to : getTargets(from)) {
                int score = evaluateMove(to);
                if (score > bestScore) {
                    bestScore = score;
                    bestTargets.clear();
                    bestTargets.add(new Pair<Cord, Cord>(from, to));
                } 
                else if (score == bestScore) {
                    bestTargets.add(new Pair<Cord, Cord>(from, to));
                }
            }
        }
        //If multiple moves gain the same amount of pieces, pick one at "random".
        Pair<Cord,Cord> bestmove = bestTargets.get(new Random().nextInt(bestTargets.size()));
        return bestmove;
    }

    /**
     * Find all valid moves for a specified piece.
     * @param index Cord object containing the starting piece.
     * @return Arraylist of Cords with every possible move
     */
    private ArrayList<Cord> getTargets(Cord index) {
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
        return possibleMoves;
    }

    /**
     * Calculates the amount of pieces converted by a move.
     * @param index Cord object containing the desired target.
     * @return Amount of pieces captured with move.
     */
    private int evaluateMove(Cord index) {
        int counter = 0;
        for (int horizontalIndex = index.getHorizontal() - 1; horizontalIndex <= index.getHorizontal()
                + 1; horizontalIndex++) {
            for (int verticalIndex = index.getVertical() - 1; verticalIndex <= index.getVertical()+ 1; verticalIndex++) {
                if (controller.isIndexWithinBounds(horizontalIndex) && controller.isIndexWithinBounds(verticalIndex)) {
                    Shape button = controller.Board[verticalIndex][horizontalIndex];
                    if (button.getFill() == controller.getOtherPlayer().getPlayerColor() && button.isVisible()) {
                        counter++;
                    }
                }
            }
        }
        return counter;

    }
}