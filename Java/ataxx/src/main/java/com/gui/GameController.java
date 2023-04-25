package com.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.shared.Cord;
import com.shared.Stack;
import com.gui.Support.Player;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

public class GameController extends AController implements Initializable {

    @FXML
    private GridPane GameBoard;

    @FXML
    private Ellipse PlayerIndicator;

    private Boolean PlayerTurn = false; // false is player 0, True is player 1.

    public Shape[][] Board;

    public final int boardsize = 7;
    public final Paint player0Color = Color.rgb(0, 0, 255);
    public final Paint player1Color = Color.rgb(255, 0, 0);
    public final Paint CloneRadius = Color.rgb(255, 255, 0);
    public final Paint JumpRadius = Color.rgb(255, 165, 0);

    public final Player player0 = new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), "Charlie", player0Color, false);
    public final Player player1 = new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), "JAck", player1Color, false);

    public boolean moveMenuOpen = false;
    public Shape fromButton;
    public Shape toButton;

    public GameController() {
    }

    // ===============Setup board==========================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillBoard();
        setStartingPositions();
    }

    public void fillBoard() {
        clearBoard();
        Board = new Shape[boardsize][boardsize];
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {

            // Add numbers on left row
            Label verticalLabel = new Label("" + (horizontalIndex));
            GridPane.setHalignment(verticalLabel, HPos.LEFT);
            GridPane.setValignment(verticalLabel, VPos.TOP);
            GameBoard.add(verticalLabel, 0, horizontalIndex);

            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {

                // Add numbers in corners on top row
                if (verticalIndex == 0) {
                    Label horizontalLabel = new Label("" + (horizontalIndex));
                    GridPane.setHalignment(horizontalLabel, HPos.LEFT);
                    GridPane.setValignment(horizontalLabel, VPos.TOP);
                    GameBoard.add(horizontalLabel, horizontalIndex, verticalIndex);
                }

                Shape myButton = new Ellipse(53.0, 283.0, 19.0, 18.0);
                // Button rules
                myButton.setVisible(false);
                myButton.setOnMouseClicked(e -> ClickHandler(myButton, e));

                GridPane.setHalignment(myButton, HPos.CENTER);
                Board[verticalIndex][horizontalIndex] = myButton;
                GameBoard.add(myButton, verticalIndex, horizontalIndex);
            }
        }
    }

    // Once pointer madness is figured out, add support for custom icons
    public void setStartingPositions() {
        for (int horizontalIndex = Board.length - 2; horizontalIndex <= Board.length - 1; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex <= 1; verticalIndex++) {
                // Player0 in left under corner
                // Board[verticalIndex][horizontalIndex] = player0.getIcon();
                Board[verticalIndex][horizontalIndex].setVisible(true);
                Board[verticalIndex][horizontalIndex].setFill(player0.getPlayerColor());

                // Player1 in top right corner
                // Board[horizontalIndex][verticalIndex] = player1.getIcon();
                Board[horizontalIndex][verticalIndex].setVisible(true);
                Board[horizontalIndex][verticalIndex].setFill(player1.getPlayerColor());
            }
        }
    }

    // ===============Gameplay loop==========================
    // Don't know how to implement an actual gameplay loop in jfx
    // So janky onclicks will have to do.
    // Downside is that you can't play bot vs bot & the player needs to start.
    public void ClickHandler(Shape button, Event e) {
        Player player = getCurrentPlayer();
        // Player clicking own piece
        if (button.getFill() == player.getPlayerColor()) {
            if(moveMenuOpen){
                closePlayerMovementMenu();
            }   
            if (!player.IsBot()) {
                openPlayerMovementMenu(button);
                fromButton = button;
            }
        }

        // Player clicking piece in movement menu
        else if(isButtonInMovementMenu(button)){
            if(button.getFill() == JumpRadius){
                clearButton(fromButton);
            }
            button.setFill(player.getPlayerColor());
            button.setDisable(false);
            button.setVisible(true);

            closePlayerMovementMenu();
            infectEnemyButtons(findButtonIndex(button));

            switchPlayer();
            if (getCurrentPlayer().IsBot()) {
                //do bot stuff
                switchPlayer();
            }
        }
    }



    // ===============Player Movement code==========================
    public void activatePlayerButtons(Player player) {
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                Shape button = Board[verticalIndex][horizontalIndex];
                if (button.getFill() == player.getPlayerColor()) {
                    button.setOnMouseClicked(e -> openPlayerMovementMenu(button));
                }
            }
        }
    }

    public void deactivatePlayerButtons(Player player) {
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                Shape button = Board[verticalIndex][horizontalIndex];
                if (button.getFill() == player.getPlayerColor()) {
                    button.setOnMouseClicked(e -> System.out.println("Not your piece!"));
                }
            }
        }
    }

    public void openPlayerMovementMenu(Shape button) {
        //button.setFill(Color.rgb(255, 0, 255));
        Cord buttonCords = findButtonIndex(button);
        colorButtonsOuterJumpRadius(buttonCords);
        colorButtonsInnerCloneRadius(buttonCords);
        moveMenuOpen = true;
    }

    public Cord findButtonIndex(Shape button){
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                if(Board[verticalIndex][horizontalIndex] == button){
                    return new Cord(verticalIndex, horizontalIndex);
                }
            }
        }
        return null;
    }

    public void colorButtonsOuterJumpRadius(Cord index){
        for (int horizontalIndex = index.getHorizontal()-2; horizontalIndex <= index.getHorizontal()+2; horizontalIndex++) {
            for (int verticalIndex = index.getVertical()-2; verticalIndex <= index.getVertical()+2; verticalIndex++) {
                if(isIndexWithinBounds(horizontalIndex) && isIndexWithinBounds(verticalIndex)){
                    if(!Board[verticalIndex][horizontalIndex].isVisible()){
                        Board[verticalIndex][horizontalIndex].setFill(JumpRadius);
                        Board[verticalIndex][horizontalIndex].setVisible(true);
                        Board[verticalIndex][horizontalIndex].setDisable(false);
                    }
                }
            }
        }
    }

    public void colorButtonsInnerCloneRadius(Cord index){
        for (int horizontalIndex = index.getHorizontal()-1; horizontalIndex <= index.getHorizontal()+1; horizontalIndex++) {
            for (int verticalIndex = index.getVertical()-1; verticalIndex <= index.getVertical()+1; verticalIndex++) {
                if(isIndexWithinBounds(horizontalIndex) && isIndexWithinBounds(verticalIndex)){
                    if(isButtonInMovementMenu(Board[verticalIndex][horizontalIndex])){
                        Board[verticalIndex][horizontalIndex].setFill(CloneRadius);
                        Board[verticalIndex][horizontalIndex].setVisible(true);
                        Board[verticalIndex][horizontalIndex].setDisable(false);
                    }
                }
            }
        }
    }

    public void closePlayerMovementMenu() {
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                Shape button = Board[verticalIndex][horizontalIndex];
                if(isButtonInMovementMenu(button)){
                    clearButton(button);
                }
            }
        }
        moveMenuOpen = false;
    }

    public void infectEnemyButtons(Cord index){
        for (int horizontalIndex = index.getHorizontal()-1; horizontalIndex <= index.getHorizontal()+1; horizontalIndex++) {
            for (int verticalIndex = index.getVertical()-1; verticalIndex <= index.getVertical()+1; verticalIndex++) {
                if(isIndexWithinBounds(horizontalIndex) && isIndexWithinBounds(verticalIndex)){
                    Shape button = Board[verticalIndex][horizontalIndex];
                    if(button.getFill() == getOtherPlayer().getPlayerColor() && button.isVisible()){
                        Board[verticalIndex][horizontalIndex].setFill(getCurrentPlayer().getPlayerColor());
                    }
                }
            }
        }

    }

    // ===============Bot Movement Code==========================

    // ===============Win conditions==========================
    public boolean isGameDone() {
        return false;
    }

    // ===============Helpers==========================
    public void clearBoard() {
        for (int i = 1; i < GameBoard.getChildren().size(); i++) {
            GameBoard.getChildren().remove(i);
        }
    }

    public Player getCurrentPlayer() {
        if (PlayerTurn) {
            return player1;
        }
        return player0;
    }
    public Player getOtherPlayer(){
        if (PlayerTurn) {
            return player0;
        }
        return player1;
    }

    public void switchPlayer() {
        PlayerTurn = !PlayerTurn;
        PlayerIndicator.setFill(getCurrentPlayer().getPlayerColor());
    }

    public boolean isIndexWithinBounds(int i) {
        return i > -1 && i < Board.length;
    }

    public boolean isButtonInMovementMenu(Shape button){
        return button.isVisible() && (button.getFill() == JumpRadius || button.getFill() == CloneRadius);
    }

    public void clearButton(Shape button){
        button.setFill(Color.rgb(0, 0, 0));
        button.setVisible(false);
        button.setDisable(true);
    }

    // ===============Control button functionality==========================
    public void ResetGame() throws IOException {
        Main.show("game", "");
    }
}
