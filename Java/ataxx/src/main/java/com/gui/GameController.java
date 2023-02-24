package com.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.basegame.Board;
import com.shared.BoardState;
import com.shared.Cord;
import com.shared.GenericMove;
import com.shared.Stack;
import com.shared.Stack.StackItem;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

//TODO AI stuff, Undo
public class GameController extends AController implements Initializable{

    @FXML
    private GridPane GameBoard;

    @FXML
    private Ellipse Player1Indicator;

    @FXML
    private Ellipse Player2Indicator;

    private Boolean PlayerTurn = true; //True is player 1, false is player 2

    public Button[][] Board;
    public Stack<BoardState<Button>> movementStack = new Stack<>();

    public final int boardsize = 7;
    public final Circle buttonShape = new Circle(89,101,20);
    public final String player1Color = "-fx-background-color: #0000FF; ";
    public final String player2Color = "-fx-background-color: #ff0000; ";
    public final String CloneRadius = "-fx-background-color: #FFFF00; ";
    public final String JumpRadius = "-fx-background-color: #FFA500; ";

    public boolean moveMenuOpen = false;
    public Button fromButton;
    public Button toButton;



    public GameController(){}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillBoard();
        setStartingPositions();
        movementStack.push(generateBoardState());
    }

    public void fillBoard(){
        clearBoard();
        Board = new Button[boardsize][boardsize];
        for(int x =0;x<boardsize;x++){
            for(int y =0;y<boardsize;y++){
                Button myButton = new Button();
                //Button rules
                myButton.setShape(buttonShape);
                myButton.setVisible(false);
                myButton.setOnMouseClicked(e ->  OpenMoveMenu(myButton, e));

                GridPane.setHalignment(myButton, HPos.CENTER);
                Board[y][x] = myButton;
                GameBoard.add(myButton, y, x);

            }
        }       
    }

    public void setStartingPositions(){
        for(int i = Board.length-2;i<=Board.length-1;i++){
            for(int j=0;j<=1;j++){
                Board[j][i].setVisible(true);
                Board[j][i].setStyle(player1Color);

                Board[i][j].setVisible(true);
                Board[i][j].setStyle(player2Color);
            }
        }
    }
   
   
    public void OpenMoveMenu(Button button, MouseEvent e) {
        if(isPlayerTurn(button) || isButtonInMoveMenu(button)){
            if(isButtonInMoveMenu(button)){
                moveMenuOpen = false;
                toButton = button;
                if(isJumpButton(button)){
                    buttonJump(fromButton, toButton);
                }
                else{
                    buttonClone(fromButton, button);
                }
                CloseMoveMenu();
                movementStack.push(generateBoardState());
                infectButtons(button);
                switchPlayer();
                checkPlayerWin();
            }
            else{
                if(moveMenuOpen){
                    moveMenuOpen = false;
                    CloseMoveMenu();
                    OpenMoveMenu(button, e);
                }
                else{
                    moveMenuOpen = true;
                    fromButton = button;
                    Cord buttonIndex = findButtonIndex(button);
                    ColorButtonsOuter(buttonIndex);
                    ColorButtonsInner(buttonIndex);
                }

            }
            
        }
    }
    //MoveMenu Stuff
    private Cord findButtonIndex(Button button){
        for(int vertical =0;vertical<boardsize;vertical++){
            for(int horizontal =0;horizontal<boardsize;horizontal++){
                if(Board[horizontal][vertical] == button){
                    return new Cord(horizontal, vertical);
                }

            }
        }
        return null;
    }

    private void ColorButtonsOuter(Cord index){
        for(int vertical = index.getY()-2;vertical<=index.getY()+2;vertical++){
            for(int horizontal = index.getX()-2;horizontal<=index.getX()+2;horizontal++){
                if(isIndexInBoard(vertical,horizontal)){
                    if(!Board[horizontal][vertical].isVisible()){
                        Board[horizontal][vertical].setStyle(JumpRadius);
                        Board[horizontal][vertical].setVisible(true);
                        Board[horizontal][vertical].setDisable(false);;
                    }
                }

            }
        }

    }
    private void ColorButtonsInner(Cord index){
        for(int vertical = index.getY()-1;vertical<=index.getY()+1;vertical++){
            for(int horizontal = index.getX()-1;horizontal<=index.getX()+1;horizontal++){
                if(isIndexInBoard(vertical,horizontal)){
                    if(!Board[horizontal][vertical].isVisible() || Board[horizontal][vertical].getStyle() == JumpRadius){
                        Board[horizontal][vertical].setStyle(CloneRadius);
                        Board[horizontal][vertical].setVisible(true);
                    }
                    
                }

            }
        }

    }
    private void CloseMoveMenu(){

        for(int vertical = 0;vertical < boardsize;vertical++){
            for(int horizontal = 0;horizontal<  boardsize;horizontal++){
                if(isButtonInMoveMenu(Board[horizontal][vertical])){
                    Board[horizontal][vertical].setStyle(null);
                    Board[horizontal][vertical].setVisible(false);
                    Board[horizontal][vertical].setDisable(true);;

                }

            }
        }
    }

    private boolean isIndexInBoard(int vertical, int horizontal){
        return horizontal < boardsize && horizontal>=0 && vertical < boardsize && vertical >= 0;
    }
    private boolean isButtonInMoveMenu(Button button){
        return button.getStyle() == JumpRadius || button.getStyle() == CloneRadius;
    }
    private boolean isJumpButton(Button button){
        return button.getStyle() == JumpRadius;
    }
    private boolean isCloneButton(Button button){
        return button.getStyle() == CloneRadius;
    }
    

    private void buttonJump(Button from, Button to){
        to.setStyle(from.getStyle());
        to.setVisible(true);
        from.setVisible(false);
        from.setStyle(null);
    }
    private void buttonClone(Button from, Button to){
        to.setStyle(from.getStyle());
        to.setVisible(true);
    }

    private Stack<Cord> infectButtons(Button button){
        Cord index = findButtonIndex(button);
        Stack<Cord> convertStack = new Stack<>();
        for(int vertical = index.getY()-1;vertical<=index.getY()+1;vertical++){
            for(int horizontal = index.getX()-1;horizontal<=index.getX()+1;horizontal++){
                if(horizontal < boardsize && horizontal>=0 && vertical < boardsize && vertical >= 0){
                    if(Board[horizontal][vertical].isVisible() && !isButtonInMoveMenu(Board[horizontal][vertical])){
                        Board[horizontal][vertical].setStyle(button.getStyle());
                        convertStack.push(new Cord(horizontal, vertical));
                    }
                }
            }
        }
        return convertStack;
    }

    public boolean isPlayerTurn(Button button){
        if(PlayerTurn){
            return button.getStyle() == player1Color;
        }
        return button.getStyle() == player2Color;
    }
    public void switchPlayer(){
        if(PlayerTurn){
            Player2Indicator.setVisible(false);
            Player1Indicator.setVisible(true);
            
        }
        else{
            Player1Indicator.setVisible(false);
            Player2Indicator.setVisible(true);
        }
       PlayerTurn = !PlayerTurn;
    }
    

    //Win Conditions checking
    private void checkPlayerWin(){
        if(hasUserValidMovesLeft(player1Color) == 0 && hasUserValidMovesLeft(player2Color) == 0){
            try{
                Main.show("WinnerPage", "Users have drawn!");
            }catch (Exception e){}

        }
       if(hasUserValidMovesLeft(player1Color) == 0){
            try{
                Main.show("WinnerPage", "User 1 Has Won!");
            }catch (Exception e){}

        }
        if(hasUserValidMovesLeft(player2Color) == 0){
            try{
                Main.show("WinnerPage", "User 2 Has Won!");
            }catch (Exception e){}
        }
        
    }
   
    private int hasUserValidMovesLeft(String identifier) {
        int count = 0;
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                if (Board[i][j].getStyle() == identifier) {
                    count++;
                }
            }
        }
        return count;
    }


    private boolean emptySpacesLeft(){
        for(int y =0;y<boardsize;y++){
            for(int x =0;x<boardsize;x++){
                if(Board[y][x].isVisible()){
                    return true;
                }
            }
        }
        return false;
    }
    private String mostPiecesOnBoard(){
        int player1 = 0;
        int player2 = 0;
        for(int y =0;y<boardsize;y++){
            for(int x =0;x<boardsize;x++){
                if(Board[x][y].getStyle() == player1Color){player1++;}
                if(Board[x][y].getStyle() == player2Color){player2++;}
            }
        }
        if(player1 > player2){
            return "PLayer 1 wins by count!";
        }
        else{
            return "Player 2 wins by count!";
        }
    }
    
    //Undo Handling
    public void undoMove(){
        if(movementStack.length() > 0){
            System.out.println(whichPlayerTurn(false) +" is cheating!");
            CloseMoveMenu();
            loadPreviousBoard();
        }
    }
    public void loadPreviousBoard(){
        BoardState<Button> previouState= movementStack.pop().getData();
        for(int vertical =0;vertical<boardsize;vertical++){
            for(int horizontal =0;horizontal<boardsize;horizontal++){
                Board[horizontal][vertical].setShape(previouState.board[horizontal][vertical].getShape());
                Board[horizontal][vertical].setVisible(previouState.board[horizontal][vertical].isVisible());
                Board[horizontal][vertical].setStyle(previouState.board[horizontal][vertical].getStyle());
            }
        }  
    }

    public BoardState<Button> generateBoardState(){
        Button[][] colorMap = new Button[boardsize][boardsize];
        for(int vertical =0;vertical<boardsize;vertical++){
            for(int horizontal =0;horizontal<boardsize;horizontal++){
                Button myButton = new Button();
                myButton.setShape(Board[horizontal][vertical].getShape());
                myButton.setVisible(Board[horizontal][vertical].isVisible());
                myButton.setStyle(Board[horizontal][vertical].getStyle());
                colorMap[horizontal][vertical] = myButton;
            }
        }
        return new BoardState<>(colorMap);
    }
    
    
    
    //Non game buttons
    public void ResetGame() throws IOException{
        Main.show("game","");
    }

    
    //Helpers
    public void clearBoard(){
        for(int i = 1; i< GameBoard.getChildren().size(); i++){
            GameBoard.getChildren().remove(i);
        }
    }
    private String whichPlayerTurn(boolean code){
        if(code){
            if(PlayerTurn){
                return player1Color;
            }
            else{
                return player2Color;
            }
        }
        if(PlayerTurn){
            return "Player 1";
        }
        else{
            return "Player 2";
        }
    }

}
