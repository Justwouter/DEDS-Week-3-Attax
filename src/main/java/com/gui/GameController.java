package com.gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import com.shared.Cord;
import com.shared.Stack;
import com.gui.Bot.ARobot;
import com.gui.Support.Player;
import com.gui.Support.AGameloopTimer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

//TODO add onShutdown in Acontroller to better manage events when switching controllers e.g. stopping music
public class GameController extends AController implements Initializable {

    @FXML
    private GridPane GameBoard;

    @FXML
    private Ellipse PlayerIndicator;

    @FXML
    private GridPane ScoreBoard;

    @FXML
    private Button UndoButton;
    private boolean canPressUndoButton = true;
    private Timeline UndoTimer;

    private Boolean PlayerTurn = false; // false is player 0, True is player 1.
    private boolean playerDidMove = false;
    private boolean moveMenuOpen = false;
    private Shape fromButton;

    private Shape[][] Board;
    private Stack<Shape[][]> stateStack = new Stack<>();
    private MediaPlayer musicPlayer;

    private Player player0;
    private Player player1;

    public int boardsize;
    private final Paint CloneRadius = Color.rgb(255, 255, 0);
    private final Paint JumpRadius = Color.rgb(255, 165, 0);

    public GameController(){}

    //#region ===============Setup==========================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Starts the main gameplay loop 
        AGameloopTimer timer = new AGameloopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {
                updatePlayers(this);
            }
        };
        timer.start();
    }

    @Override
    public void loadData(Map<String, Object> dataDict) {
        this.dataDict = dataDict;
        this.boardsize = (int) dataDict.get("BoardSize");
        this.player0 = (Player) dataDict.get("Player0");
        this.player1 = (Player) dataDict.get("Player1");
        this.musicPlayer = startMusic(Main.class.getResource("music/battle.mp3").getFile());
        GameBoard = makeBoard(GameBoard, "");
        GameBoard = colorBoard(GameBoard,boardsize);
        fillBoard();
        setStartingPositions();
        updateScoreboard();
        takeSnapshot();
        setupButtonTimeout();
    }

    private MediaPlayer startMusic(String pathToMusic){
        File file = new File(pathToMusic);
        Media media = new Media(file.toURI().toString()); 
        MediaPlayer musicPlayer = new MediaPlayer(media);
        musicPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                musicPlayer.seek(Duration.ZERO);
            }
        });
        musicPlayer.setAutoPlay(true);
        return musicPlayer;
    }

    private GridPane makeBoard(GridPane gp, String styleArgs){
        gp.getColumnConstraints().clear();
        gp.getRowConstraints().clear();
        gp.setAlignment(Pos.CENTER);

        gp.setStyle(styleArgs);

        ColumnConstraints cc = new ColumnConstraints();
        RowConstraints rc = new RowConstraints();

        for (int i = 0; i < boardsize; i++) {
            cc.setPrefWidth(100);
            rc.setPrefHeight((100));
            gp.getColumnConstraints().add(cc);
            gp.getRowConstraints().add(rc);
        }
        return gp;
    }

    private void fillBoard() {
        clearBoard();

        Board = new Shape[boardsize][boardsize];
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {

            // Add numbers on left row, add colors if background is colored
            Label verticalLabel = new Label("" + (horizontalIndex));
            GridPane.setHalignment(verticalLabel, HPos.LEFT);
            GridPane.setValignment(verticalLabel, VPos.TOP);
            if((horizontalIndex + 0) % 2 == 0){
                verticalLabel.setTextFill(Color.rgb(255, 255, 255));
            }
            GameBoard.add(verticalLabel, 0, horizontalIndex);

            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {

                // Add numbers in corners on top row, add colors if background is colored
                if (verticalIndex == 0) {
                    Label horizontalLabel = new Label("" + (horizontalIndex));
                    GridPane.setHalignment(horizontalLabel, HPos.LEFT);
                    GridPane.setValignment(horizontalLabel, VPos.TOP);
                    if((horizontalIndex + verticalIndex) % 2 == 0){
                        horizontalLabel.setTextFill(Color.rgb(255, 255, 255));
                    }
                    GameBoard.add(horizontalLabel, horizontalIndex, verticalIndex);
                }
                
                // Shape piece = new Ellipse(53.0, 283.0, 19.0, 18.0);
                Shape piece = new Ellipse(53.0, 283.0, 19.0, 18.0);
                piece.setStyle(null);

                // Button rules
                piece.setVisible(false);
                piece.setOnMouseClicked(e -> PlayerTurnClickHandler(piece, e));

                //Centers each piece within the grid.
                GridPane.setHalignment(piece, HPos.CENTER);
                Board[verticalIndex][horizontalIndex] = piece;
                GameBoard.add(piece, verticalIndex, horizontalIndex);
            }
        }
    }

    private GridPane colorBoard(GridPane gp, int boardsize){
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                Pane backgroundPane = new Pane();
                if((horizontalIndex + verticalIndex) % 2 == 0){
                    backgroundPane.setBackground(new Background(new BackgroundFill(Color.rgb(70, 80, 90), CornerRadii.EMPTY, Insets.EMPTY)));
                }
                gp.add(backgroundPane, verticalIndex, horizontalIndex);
            }
        }
        return gp;
    }
    //TODO Once pointer madness is figured out, add support for custom icons <- Should be easy by loading them from the Player class <- No fuck you, do you know how annoying that is with replacing icons on the board instead of just using circles???
    private void setStartingPositions() {
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

    //#endregion

    //#region ===============Gameplay loop==========================
    private void updatePlayers(AGameloopTimer timer) {
        if (getCurrentPlayer().IsBot()) {
            timer.stop();
            pause(0.5);
            doBotMove();
            timer.start();
            afterTurn(timer);
        }
        else{
            //Playermove works w/onclick events so wait till one has finished.
            if(playerDidMove){
                afterTurn(timer);
                playerDidMove = false;

            }
        }
    }

    private void afterTurn(AGameloopTimer timer){
        updateScoreboard(); 
        takeSnapshot(); 
        checkGameEnding(timer);
        switchPlayer();
    }

    //#endregion

    //#region ===============Player Movement code==========================
    /**
     * onClick handler for human players. Is applied to every button on the screen at startup
     * @param button
     * @param e
     */
    private void PlayerTurnClickHandler(Shape button, Event e) {
        Player player = getCurrentPlayer();
        // Player clicking own piece 
        if (button.getFill() == player.getPlayerColor()) {
             //Player pressed same button twice
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
        //Don't tough anything here! Order is important
        else if (isButtonInMovementMenu(button)) {
            if (button.getFill() == JumpRadius) {
                clearButton(fromButton);
            }
            button.setFill(player.getPlayerColor());
            button.setDisable(false);
            button.setVisible(true);

            closePlayerMovementMenu(); // Hide valid move assistent
            infectEnemyButtons(findButtonIndex(button)); // Recolor adjecent pieces
            playerDidMove = true; //Update gameplay loop
        }
    }
    

    // private void activatePlayerButtons(Player player) {
    //     for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
    //         for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
    //             Shape button = Board[verticalIndex][horizontalIndex];
    //             if (button.getFill() == player.getPlayerColor()) {
    //                 button.setOnMouseClicked(e -> openPlayerMovementMenu(button));
    //             }
    //         }
    //     }
    // }

    // private void deactivatePlayerButtons(Player player) {
    //     for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
    //         for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
    //             Shape button = Board[verticalIndex][horizontalIndex];
    //             if (button.getFill() == player.getPlayerColor()) {
    //                 button.setOnMouseClicked(e -> System.out.println("Not your piece!"));
    //             }
    //         }
    //     }
    // }

    private void openPlayerMovementMenu(Shape button) {
        Cord buttonCords = findButtonIndex(button);
        colorButtonsOuterJumpRadius(buttonCords);
        colorButtonsInnerCloneRadius(buttonCords);
        moveMenuOpen = true;
    }

    private Cord findButtonIndex(Shape button) {
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                if (Board[verticalIndex][horizontalIndex] == button) {
                    return new Cord(verticalIndex, horizontalIndex);
                }
            }
        }
        return null;
    }

    private void colorButtonsOuterJumpRadius(Cord index) {
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

    private void colorButtonsInnerCloneRadius(Cord index) {
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

    private void closePlayerMovementMenu() {
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

    /**
     * Loop trough every button arround the selected Cord and convert them to the current players color.
     * @param index
     */
    private void infectEnemyButtons(Cord index) {
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

    //#endregion

    //#region ===============Bot Movement Code==========================
    public void doBotMove(){
        ARobot bot = getCurrentPlayer().getBot();
        bot.setController(this);
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

    //#endregion

    //#region ===============Win conditions==========================
    /**
     * Checks if any of the win coditions are satisfied.<p>
     * Also handles the Gamecontroller shutdown procedures e.g. stopping timers.<p>
     * Should not be used in bots becouse of UI interactions.
     * @param timer the main gameloop timer
     * @return True: Game is done.
     * False: Game is still in progress.
     */
    private boolean checkGameEnding(AGameloopTimer timer) {
        //All spaces are full, win by points
        if (checkBoardFull()) {
            timer.stop();
            musicPlayer.dispose();
            Player player = getPlayerwithMostPoints();
            dataDict.put("VictoryText", player.getName() + " wins with " + player.getGamePoints() + " points!");
            Main.show("WinnerPage", this.dataDict);

            return true;
        
        // Enemy has no more moves left, win by knockout. Arguably the harder achievement
        } else if (!checkNextPlayerHasMoves()) {
            timer.stop();
            musicPlayer.dispose();
            Player player = getCurrentPlayer();
            dataDict.put("VictoryText",player.getName() + " wins by technical knockout!");
            Main.show("WinnerPage", dataDict);

            return true;
        }
        return false;
    }

    /**
     * Loop trough all pieces of the opponent and look for empty spaces within 2 tiles.
     * @return true: possible move found.<p>
     * False: No moves found, triggers in checkGameEnding.
     */
    private boolean checkNextPlayerHasMoves() {
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

    /**
     * Loops trough the whole board to find empty spaces.
     * @return True: Board is full, no empty spaces found.<p>
     * False: There are empty spaces left.
     */
    private boolean checkBoardFull() {
        for (int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++) {
            for (int verticalIndex = 0; verticalIndex < boardsize; verticalIndex++) {
                var button = Board[verticalIndex][horizontalIndex];
                if (!(button.getFill() == player0.getPlayerColor())
                        && !(button.getFill() == player1.getPlayerColor())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Public for use in the bots
     * Searches for pieces of the given player.
     * @param player
     * @return {@link ArrayList} containing found {@link Cord}s 
     */
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
    /**
     * Simply finds the player with the most points on the current board.<p>
     * Used in the scoreboard.
     * @return {@link Player} object of the best player
     */
    private Player getPlayerwithMostPoints() {
        int player0Pieces = findPlayerPieces(player0).size();
        int player1Pieces = findPlayerPieces(player1).size();
        if (player0Pieces > player1Pieces)
            return player0;
        return player1;
    }

    //#endregion

    //#region ===============States & Undo==========================
    /**
     * Hacky way to implement snapshots.<p>
     * Becouse the Shapes in the board are pointers to UI elements, copy them instead of referencing and push everything onto the stack.
     */
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

    /**
     * Equally hacky way to load the snapshots.<p>
     * Get the second to last entry on the stack as entries are made when a turn starts, clear the board and create a replacement. 
     * Afterwards update the player status and the scoreboard to match the new board and take the start of turn snapshot.<p>
     * 
     * WARNING!
     * Be extremely carefull with this. It barley functions as-is and javaFX is really temperamentfull when it comes to pointers.<p>
     * The snapshot logic is weird and annoying but things WILL break if you try changing them!
     */
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
    
    /**
     * Configures the undo button's timeout to avoid messing up the loadSnapshot method and JavaFX's renderer.<p>
     * I'd reccommend at least 1.5 seconds timout between presses as any lower seems to couse desync between the GUI and Realboard.
     */
    private void setupButtonTimeout(){
        //Undo button errors when spammed. To avoid this, add a 1.5 sec delay to undo's
        UndoTimer = new Timeline();
        UndoTimer.getKeyFrames().add(new KeyFrame(Duration.seconds(1.5), event -> {canPressUndoButton = true;}));
        UndoTimer.setCycleCount(Timeline.INDEFINITE);
        UndoTimer.play();
    }

    public void handleUndoButton(){
        if (canPressUndoButton) {
            canPressUndoButton = false;
            loadSnapshot();
        }

    }

    //#endregion

    //#region ===============Scoreboard==========================
    /**
     * Called after every turn, this method updates the scoreboard stack and corresponding GUI elements.<p>
     * Individual scores are also updated in their respective {@link Player} classes.
     */
    private void updateScoreboard() {
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

    private void updatePlayerScores() {
        getCurrentPlayer().setGamePoints(findPlayerPieces(getCurrentPlayer()).size());
        getOtherPlayer().setGamePoints(findPlayerPieces(getOtherPlayer()).size());
    }

    //#endregion

    //#region ===============Helpers==========================
    public Shape[][] getBoard(){
        return this.Board;
    }

    private void clearBoard() {
        for (int i = 1; i < GameBoard.getChildren().size(); i++) {
            GameBoard.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && !(node instanceof Label || node instanceof Pane));
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

    private void switchPlayer() {
        PlayerTurn = !PlayerTurn;
        PlayerIndicator.setFill(getCurrentPlayer().getPlayerColor());
    }

    public boolean isIndexWithinBounds(int i) {
        return i > -1 && i < Board.length;
    }

    private boolean isButtonInMovementMenu(Shape button) {
        return button.isVisible() && (button.getFill() == JumpRadius || button.getFill() == CloneRadius);
    }

    private boolean isJumpMove(Cord from, Cord to){
        return !(Math.abs(from.getHorizontal() - to.getHorizontal()) < 2 && Math.abs(from.getVertical() - to.getVertical()) < 2);
    }

    private void clearButton(Shape button) {
        button.setFill(Color.rgb(0, 0, 0));
        button.setVisible(false);
        button.setDisable(true);
    }

    private void pause(Double timeInSeconds) {
        //JavaFX is annoying  with UI threads so make a new one and pause that
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep((int)(timeInSeconds*1000)); 
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
    //#endregion

    //#region ===============Control button functionality==========================
    public void ResetGame() {
        musicPlayer.dispose();
        Main.show("game",this.dataDict);
    }
    //#endregion
}
