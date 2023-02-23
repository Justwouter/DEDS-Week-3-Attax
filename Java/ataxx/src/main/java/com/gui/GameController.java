package com.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.shared.Cord;
import com.shared.GenericMove;
import com.shared.Stack;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

//TODO Win conditions, AI stuff, Undo
public class GameController extends AController implements Initializable{

    @FXML
    private GridPane GameBoard;

    private Boolean PlayerTurn = true;

    public Button[][] Board;
    public Stack<GenericMove<String>> movementStack = new Stack<>();

    public final int boardsize = 7;
    public final Circle buttonShape = new Circle(89,101,20);
    public final String player1Color = "-fx-background-color: #ff0000; ";
    public final String player2Color = "-fx-background-color: #0000FF; ";
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
                Board[i][j].setVisible(true);
                Board[i][j].setStyle(player1Color);

                Board[j][i].setVisible(true);
                Board[j][i].setStyle(player2Color);

            }
        }
    }
   
   
   
   
    public void OpenMoveMenu(Button button, MouseEvent e) {
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
            infectButtons(button);
            movementStack.push(new GenericMove<String>(findButtonIndex(fromButton), findButtonIndex(button), button.getStyle()));
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
                if(horizontal < boardsize && horizontal>=0 && vertical < boardsize && vertical >= 0){
                    if(!Board[horizontal][vertical].isVisible()){
                        Board[horizontal][vertical].setStyle(JumpRadius);
                        Board[horizontal][vertical].setVisible(true);
                    }
                }

            }
        }

    }
    private void ColorButtonsInner(Cord index){
        for(int vertical = index.getY()-1;vertical<=index.getY()+1;vertical++){
            for(int horizontal = index.getX()-1;horizontal<=index.getX()+1;horizontal++){
                if(horizontal < boardsize && horizontal>=0 && vertical < boardsize && vertical >= 0){
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
                }

            }
        }
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

    private void infectButtons(Button button){
        Cord index = findButtonIndex(button);
        for(int vertical = index.getY()-1;vertical<=index.getY()+1;vertical++){
            for(int horizontal = index.getX()-1;horizontal<=index.getX()+1;horizontal++){
                if(horizontal < boardsize && horizontal>=0 && vertical < boardsize && vertical >= 0){
                    if(Board[horizontal][vertical].isVisible() && !isButtonInMoveMenu(Board[horizontal][vertical])){
                        Board[horizontal][vertical].setStyle(button.getStyle());
                    }
                }
            }
        }
    }

    //Non game buttons
    public void ResetGame() throws IOException{
        Main.show("game");
    }

    
    //Helpers
    public void clearBoard(){
        for(int i = 1; i< GameBoard.getChildren().size(); i++){
            GameBoard.getChildren().remove(i);
        }
    }
}
