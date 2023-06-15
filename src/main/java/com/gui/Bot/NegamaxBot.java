package com.gui.Bot;

import com.gui.GameController;
import com.gui.Support.Player;
import com.shared.Cord;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class NegamaxBot extends ARobot {

    private static final int MAX_DEPTH = 6;

    public NegamaxBot(){super();}
    public NegamaxBot(GameController controller) {
        super(controller);
    }

    @Override
    public Pair<Cord, Cord> getMoveCords() {
        int bestScore = Integer.MIN_VALUE;
        ArrayList<Pair<Cord, Cord>> bestTargets = new ArrayList<>();

        ArrayList<Cord> possibleStarts = controller.findPlayerPieces(controller.getCurrentPlayer());
        for (Cord from : possibleStarts) {
            for (Cord to : getTargets(from)) {
                int score = negamax(to, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, -1);
                if (score > bestScore) {
                    bestScore = score;
                    bestTargets.clear();
                    bestTargets.add(new Pair<>(from, to));
                } else if (score == bestScore) {
                    bestTargets.add(new Pair<>(from, to));
                }
            }
        }

        return bestTargets.get(new Random().nextInt(bestTargets.size()));
    }

    private int negamax(Cord index, int depth, int alpha, int beta, int color) {
        if (depth == 0 || isTerminalNode()) {
            return evaluateBoard();
        }

        ArrayList<Cord> possibleMoves = getTargets(index);
        for (Cord move : possibleMoves) {
            Shape[][] copiedBoard = copyBoard(controller.getBoard());

            Cord from = index;
            Cord to = move;
            Shape temp = copiedBoard[to.getVertical()][to.getHorizontal()];
            copiedBoard[to.getVertical()][to.getHorizontal()] = copiedBoard[from.getVertical()][from.getHorizontal()];
            copiedBoard[from.getVertical()][from.getHorizontal()] = temp;

            int score = -negamax(move, depth - 1, -beta, -alpha, -color);

            alpha = Math.max(alpha, score);
            if (alpha >= beta) {
                break;
            }
        }

        return alpha;
    }

    private boolean isTerminalNode() {
        ArrayList<Cord> cords = controller.findPlayerPieces(controller.getCurrentPlayer());
        int boardSize = controller.boardsize;

        for (Cord index : cords) {
            for (int horizontalIndex = index.getHorizontal() - 2; horizontalIndex <= index.getHorizontal()
                    + 2; horizontalIndex++) {
                for (int verticalIndex = index.getVertical() - 2; verticalIndex <= index.getVertical()
                        + 2; verticalIndex++) {
                    if (controller.isIndexWithinBounds(horizontalIndex) && controller.isIndexWithinBounds(verticalIndex)) {
                        if (!controller.Board[verticalIndex][horizontalIndex].isVisible()) {
                            return false;
                        }
                    }
                }
            }
        }

        for (int horizontalIndex = 0; horizontalIndex < boardSize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardSize; verticalIndex++) {
                var button = controller.Board[verticalIndex][horizontalIndex];
                if (!(button.getFill() == controller.getCurrentPlayer().getPlayerColor())
                        || !(button.getFill() == controller.getOtherPlayer().getPlayerColor())) {
                    return false;
                }
            }
        }
        return true;
    }

    private int evaluateBoard() {
        Player currentPlayer = controller.getCurrentPlayer();
        int currentPlayerPieces = controller.findPlayerPieces(currentPlayer).size();
        return currentPlayerPieces;
    }

    private ArrayList<Cord> getTargets(Cord index) {
        ArrayList<Cord> possibleMoves = new ArrayList<>();

        int horizontal = index.getHorizontal();
        int vertical = index.getVertical();

        for (int i = horizontal - 2; i <= horizontal + 2; i++) {
            for (int j = vertical - 2; j <= vertical + 2; j++) {
                if (controller.isIndexWithinBounds(i) && controller.isIndexWithinBounds(j) && !controller.getBoard()[j][i].isVisible()) {
                    possibleMoves.add(new Cord(j, i));
                }
            }
        }

        return possibleMoves;
    }

    private Shape[][] copyBoard(Shape[][] originalBoard) {
        Shape[][] copy = new Shape[originalBoard.length][originalBoard[0].length];
        for (int i = 0; i < originalBoard.length; i++) {
            for (int j = 0; j < originalBoard[i].length; j++) {
                copy[i][j] = originalBoard[i][j];
            }
        }
        return copy;
    }
}