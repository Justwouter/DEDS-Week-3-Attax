package com.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.shared.Stack;
import com.gui.Support.Player;
import com.gui.Support.SmartButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

public class GameController extends AController implements Initializable{

    @FXML
    private GridPane GameBoard;

    @FXML
    private Ellipse Player1Indicator;

    @FXML
    private Ellipse Player2Indicator;

    private Boolean PlayerTurn = true; //True is player 1, false is player 2

    public Shape[][] Board;

    public final int boardsize = 7;
    public final Shape buttonShape = new Ellipse(53.0, 283.0, 19.0, 18.0);
    public final Paint player1Color =  Color.rgb(0,0,255);
    public final Paint player2Color = Color.rgb(255,0,0);
    public final Paint CloneRadius = Color.rgb(255, 255, 0);
    public final Paint JumpRadius = Color.rgb(255, 165, 0);


    public final Player player1 = new Player(Player1Indicator, "Charlie", player1Color);
    public final Player player2 = new Player(Player2Indicator, "JAck", player2Color);


    public boolean moveMenuOpen = false;
    public Button fromButton;
    public Button toButton;



    public GameController(){}

    //===============Setup the board==========================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillBoard();
        setStartingPositions();
    }

    public void fillBoard(){
        clearBoard();
        Board = new Shape[boardsize][boardsize];
        for(int horizontalIndex = 0; horizontalIndex < boardsize; horizontalIndex++){

            Label verticalLabel = new Label(""+(horizontalIndex));
            GridPane.setHalignment(verticalLabel, HPos.LEFT);
            GridPane.setValignment(verticalLabel, VPos.TOP);
            GameBoard.add(verticalLabel, 0, horizontalIndex);

            for(int verticalIndex = 0; verticalIndex<boardsize; verticalIndex++){
                
                //Add numbers in corners on top row
                if(verticalIndex == 0){
                    Label horizontalLabel = new Label(""+(horizontalIndex));
                    GridPane.setHalignment(horizontalLabel, HPos.LEFT);
                    GridPane.setValignment(horizontalLabel, VPos.TOP);
                    GameBoard.add(horizontalLabel, horizontalIndex, verticalIndex);
                }

                Shape myButton = new Ellipse(53.0, 283.0, 19.0, 18.0);
                //Button rules
                myButton.setVisible(false);
                myButton.setOnMouseClicked(e ->  myButton.setFill(Color.rgb(255,0,255)));

                GridPane.setHalignment(myButton, HPos.CENTER);
                Board[verticalIndex][horizontalIndex] = myButton;
                GameBoard.add(myButton, verticalIndex, horizontalIndex);
            }
        }       
    }

    public void setStartingPositions(){
        for(int horizontalIndex = Board.length-2; horizontalIndex <= Board.length-1; horizontalIndex++){
            for(int verticalIndex = 0; verticalIndex <= 1; verticalIndex++){
                Board[verticalIndex][horizontalIndex].setVisible(true);
                Board[verticalIndex][horizontalIndex].setFill(player1Color);

                Board[horizontalIndex][verticalIndex].setVisible(true);
                Board[horizontalIndex][verticalIndex].setFill(player2Color);
                System.out.println(verticalIndex + ":"+ horizontalIndex);
                System.out.println(horizontalIndex + ":"+ verticalIndex);
                System.out.println();
            }
        }
    }
   
   
    
    
    
    
    //Non game buttons
    public void ResetGame() throws IOException{
        Main.show("game","",player2IsBot);
    }

    
    //Helpers
    public void clearBoard(){
        for(int i = 1; i< GameBoard.getChildren().size(); i++){
            GameBoard.getChildren().remove(i);
        }
    }
    

}
