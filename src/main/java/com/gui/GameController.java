package com.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.shared.Cord;
import com.shared.Stack;
import com.gui.Bot.ARobot;
import com.gui.Bot.AggressiveBot;
import com.gui.Bot.RandomBot;
import com.gui.Support.Player;
import com.gui.Support.AGameloopTimer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

public class GameController extends AController implements Initializable {

    //Collapse all ctr+k ctr+0
    //ctrl+k ctrl+s for settings

    @FXML
    private GridPane GameBoard;

    @FXML
    private Ellipse PlayerIndicator;

    @FXML
    private GridPane ScoreBoard;

    @FXML
    private Button UndoButton;
    private boolean canPressUndoButton = true;
    private Timeline timeline;

    private AGameloopTimer GameplayLoop;
    private Boolean PlayerTurn = false; // false is player 0, True is player 1.
    private boolean playerDidMove = false;
    public boolean moveMenuOpen = false;
    public Shape fromButton;

    public Shape[][] Board;
    Stack<Shape[][]> stateStack = new Stack<>();

    public final int boardsize = 7;
    public final Paint player0Color = Color.rgb(30, 144, 255);
    public final Paint player1Color = Color.rgb(255, 0, 0);
    public final Paint CloneRadius = Color.rgb(255, 255, 0);
    public final Paint JumpRadius = Color.rgb(255, 165, 0);

    // public final Player player0 = new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), "Wouter", player0Color, false);
    // public final Player player1 = new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), "Marieke", player1Color, player2isbot);
    public GameController(){}
    public GameController(Player p0, Player p1) {
        player0 = p0;
        player1 = p1;
    }

    // ===============Setup==========================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillBoard();
        setStartingPositions();
        updateScoreboard();
        takeSnapshot();
        setupButtonTimeout();
        AGameloopTimer timer = new AGameloopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {
                updatePlayers(this);
            }
        };
        timer.start();
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

                Shape piece = new Ellipse(53.0, 283.0, 19.0, 18.0);
                // Button rules
                piece.setVisible(false);
                piece.setOnMouseClicked(e -> PlayerTurnClickHandler(piece, e));

                GridPane.setHalignment(piece, HPos.CENTER);
                Board[verticalIndex][horizontalIndex] = piece;
                GameBoard.add(piece, verticalIndex, horizontalIndex);
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
    public void updatePlayers(AGameloopTimer timer) {
        if (getCurrentPlayer().IsBot()) {
            timer.stop();
            pause(0.5);
            doBotMove();
            timer.start();
            afterTurn(timer);
        }
        else{
            if(playerDidMove){
                afterTurn(timer);
                playerDidMove = false;

            }
        }
    }

    public void afterTurn(AGameloopTimer timer){
        updateScoreboard(); 
        takeSnapshot(); 
        checkGameEnding(timer);
        switchPlayer();
    }


    public void PlayerTurnClickHandler(Shape button, Event e) {
        Player player = getCurrentPlayer();
        // Player clicking own piece 
        if (button.getFill() == player.getPlayerColor()) {
            if(moveMenuOpen && button == fromButton){
                closePlayerMovementMenu();
            }
            else{
                if (moveMenuOpen) {
                    closePlayerMovementMenu();
                }
                if (!player.IsBot()) {
                    openPlayerMovementMenu(button);
                    fromButton = button;
                }
            }
           
        }

        // Player clicking piece in movement menu
        else if (isButtonInMovementMenu(button)) {
            if (button.getFill() == JumpRadius) {
                clearButton(fromButton);
            }
            button.setFill(player.getPlayerColor());
            button.setDisable(false);
            button.setVisible(true);

            closePlayerMovementMenu(); // Hide valid move assistent
            infectEnemyButtons(findButtonIndex(button)); // Recolor adjecent pieces
            playerDidMove = true;
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
        // button.setFill(Color.rgb(255, 0, 255));
        Cord buttonCords = findButtonIndex(button);
        colorButtonsOuterJumpRadius(buttonCords);
        colorButtonsInnerCloneRadius(buttonCords);
        moveMenuOpen = true;
    }

    public Cord findButtonIndex(Shape button) {
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                if (Board[verticalIndex][horizontalIndex] == button) {
                    return new Cord(verticalIndex, horizontalIndex);
                }
            }
        }
        return null;
    }

    public void colorButtonsOuterJumpRadius(Cord index) {
        for (int horizontalIndex = index.getHorizontal() - 2; horizontalIndex <= index.getHorizontal()
                + 2; horizontalIndex++) {
            for (int verticalIndex = index.getVertical() - 2; verticalIndex <= index.getVertical()
                    + 2; verticalIndex++) {
                if (isIndexWithinBounds(horizontalIndex) && isIndexWithinBounds(verticalIndex)) {
                    if (!Board[verticalIndex][horizontalIndex].isVisible()) {
                        Board[verticalIndex][horizontalIndex].setFill(JumpRadius);
                        Board[verticalIndex][horizontalIndex].setVisible(true);
                        Board[verticalIndex][horizontalIndex].setDisable(false);
                    }
                }
            }
        }
    }

    public void colorButtonsInnerCloneRadius(Cord index) {
        for (int horizontalIndex = index.getHorizontal() - 1; horizontalIndex <= index.getHorizontal()
                + 1; horizontalIndex++) {
            for (int verticalIndex = index.getVertical() - 1; verticalIndex <= index.getVertical()
                    + 1; verticalIndex++) {
                if (isIndexWithinBounds(horizontalIndex) && isIndexWithinBounds(verticalIndex)) {
                    if (isButtonInMovementMenu(Board[verticalIndex][horizontalIndex])) {
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
                if (isButtonInMovementMenu(button)) {
                    clearButton(button);
                }
            }
        }
        moveMenuOpen = false;
    }

    public void infectEnemyButtons(Cord index) {
        for (int horizontalIndex = index.getHorizontal() - 1; horizontalIndex <= index.getHorizontal()
                + 1; horizontalIndex++) {
            for (int verticalIndex = index.getVertical() - 1; verticalIndex <= index.getVertical()
                    + 1; verticalIndex++) {
                if (isIndexWithinBounds(horizontalIndex) && isIndexWithinBounds(verticalIndex)) {
                    Shape button = Board[verticalIndex][horizontalIndex];
                    if (button.getFill() == getOtherPlayer().getPlayerColor() && button.isVisible()) {
                        Board[verticalIndex][horizontalIndex].setFill(getCurrentPlayer().getPlayerColor());
                    }
                }
            }
        }

    }

    // ===============Bot Movement Code==========================
    public void doBotMove(){
        ARobot bot = new AggressiveBot(this);
        Pair<Cord,Cord> move= bot.getMoveCords();
        Cord from = move.getKey();
        Cord to = move.getValue();
        
        if(isJumpMove(from, to)){
            fromButton = Board[from.getVertical()][from.getHorizontal()];
            clearButton(fromButton);
        }
        Shape toButton =  Board[to.getVertical()][to.getHorizontal()];
        toButton.setFill(getCurrentPlayer().getPlayerColor());
        toButton.setDisable(false);
        toButton.setVisible(true);

        infectEnemyButtons(to);
        playerDidMove = false;

    }

    // ===============Win conditions==========================
    public boolean checkGameEnding(AGameloopTimer timer) {
        if (checkBoardFull()) {
            timer.stop();
            Player player = getPlayerwithMostPoints();
            WinnerController controller = new WinnerController(player.getName() + " Wins with " + player.getGamePoints() + " points!");
            Main.show("WinnerPage", controller);
            return true;
        } else if (!checkNextPlayerHasMoves()) {
            timer.stop();
            Player player = getCurrentPlayer();
            WinnerController controller = new WinnerController(player.getName() + " Wins by technical knockout!");
            Main.show("WinnerPage", controller);
            return true;
        }
        return false;

    }

    public boolean checkNextPlayerHasMoves() {
        Player player = getOtherPlayer();
        ArrayList<Cord> cords = findPlayerPieces(player);

        for (Cord index : cords) {
            for (int horizontalIndex = index.getHorizontal() - 2; horizontalIndex <= index.getHorizontal()
                    + 2; horizontalIndex++) {
                for (int verticalIndex = index.getVertical() - 2; verticalIndex <= index.getVertical()
                        + 2; verticalIndex++) {
                    if (isIndexWithinBounds(horizontalIndex) && isIndexWithinBounds(verticalIndex)) {
                        if (!Board[verticalIndex][horizontalIndex].isVisible()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkBoardFull() {
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                var button = Board[verticalIndex][horizontalIndex];
                if (!(button.getFill() == player0.getPlayerColor())
                        || !(button.getFill() == player1.getPlayerColor())) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Cord> findPlayerPieces(Player player) {
        ArrayList<Cord> cords = new ArrayList<>();
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                var button = Board[verticalIndex][horizontalIndex];
                if (button.getFill() == player.getPlayerColor()) {
                    cords.add(new Cord(verticalIndex, horizontalIndex));
                }
            }
        }
        return cords;
    }

    public Player getPlayerwithMostPoints() {
        int player0Pieces = findPlayerPieces(player0).size();
        int player1Pieces = findPlayerPieces(player1).size();
        if (player0Pieces > player1Pieces)
            return player0;
        return player1;
    }

    // ===============States & Undo==========================
    public void takeSnapshot() {
        Shape[][] storage = new Shape[boardsize][boardsize];
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                var button = Board[verticalIndex][horizontalIndex];
                storage[verticalIndex][horizontalIndex] = new Ellipse(53.0, 283.0, 19.0, 18.0);
                storage[verticalIndex][horizontalIndex].setFill(button.getFill());
                storage[verticalIndex][horizontalIndex].setVisible(button.isVisible());
                storage[verticalIndex][horizontalIndex].setDisable(button.isDisabled());
            }
        }
        stateStack.push(storage);
    }

    public void loadSnapshot() {
        Shape[][] storage = null;
        try {
            storage = stateStack.pop().getData();
            //Skip the first savepoint b\c it's worthless
            storage = stateStack.pop().getData();
            
        } catch (StackOverflowError e) {
            System.out.println("Stack Empty!");
            return;
        }
        if (storage != null) {
            clearBoard();
            for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
                for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                    var button = storage[verticalIndex][horizontalIndex];
                    var newButton = new Ellipse(53.0, 283.0, 19.0, 18.0);
                    newButton.setFill(button.getFill());
                    newButton.setVisible(button.isVisible());
                    newButton.setDisable(button.isDisabled());
                    newButton.setOnMouseClicked(e -> PlayerTurnClickHandler(newButton, e));
                    Board[verticalIndex][horizontalIndex] = newButton;
                    GridPane.setHalignment(newButton, HPos.CENTER);
                    GameBoard.add(newButton, verticalIndex, horizontalIndex);
                }
            }
            switchPlayer();
            updateScoreboard();
            takeSnapshot(); // Retake the snapshot as if the turn has already happend
        }
    }
    
    public void setupButtonTimeout(){
        //Undo button errors when spammed. To avoid this, add a 1.5 sec delay to undo's
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.5), event -> {canPressUndoButton = true;}));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void handleUndoButton(){
        if (canPressUndoButton) {
            canPressUndoButton = false;
            loadSnapshot();
        }

    }
    // ===============Scoreboard==========================
    public void updateScoreboard() {
        updatePlayerScores();

        ScoreBoard.getChildren().removeAll(ScoreBoard.getChildren());
        Stack<Player> ScoreStack = new Stack<>();

        int player0Pieces = findPlayerPieces(player0).size();
        int player1Pieces = findPlayerPieces(player1).size();
        if (player0Pieces > player1Pieces) {
            ScoreStack.push(player1);
            ScoreStack.push(player0);
        } else {
            ScoreStack.push(player0);
            ScoreStack.push(player1);
        }
        for (int i = 0; i <= ScoreStack.length(); i++) {
            Player player = ScoreStack.pop().getData();
            Text playername = new Text(player.getName());
            playername.setFill(player.getPlayerColor());
            ScoreBoard.add(playername, 1, i);
            ScoreBoard.add(new Text("" + player.getGamePoints()), 2, i);
        }

    }

    public void updatePlayerScores() {
        getCurrentPlayer().setGamePoints(findPlayerPieces(getCurrentPlayer()).size());
        getOtherPlayer().setGamePoints(findPlayerPieces(getOtherPlayer()).size());
    }

    // ===============Helpers==========================
    public void clearBoard() {
        for (int i = 1; i < GameBoard.getChildren().size(); i++) {
            GameBoard.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && !(node instanceof Label));
        }
    }

    public Player getCurrentPlayer() {
        if (PlayerTurn) {
            return player1;
        }
        return player0;
    }

    public Player getOtherPlayer() {
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

    public boolean isButtonInMovementMenu(Shape button) {
        return button.isVisible() && (button.getFill() == JumpRadius || button.getFill() == CloneRadius);
    }

    public boolean isJumpMove(Cord from, Cord to){
        return !(Math.abs(from.getHorizontal() - to.getHorizontal()) < 2 && Math.abs(from.getVertical() - to.getVertical()) < 2);
    }

    public void clearButton(Shape button) {
        button.setFill(Color.rgb(0, 0, 0));
        button.setVisible(false);
        button.setDisable(true);
    }

    public void pause(Double timeInSeconds) {
        //JavaFX is annoying  with UI threads so make a new one and pause that
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep((int)timeInSeconds*1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // ===============Control button functionality==========================
    public void ResetGame() {
        Main.show("game",new GameController(player0, player1));
    }
}
