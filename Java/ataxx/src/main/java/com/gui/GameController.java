package com.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class GameController extends AController implements Initializable{

    @FXML
    private Circle BlueBall;

    @FXML
    private Circle RedBall;

    @FXML
    private GridPane GameBoard;

    


    private AnchorPane Player1 = new AnchorPane();
    private AnchorPane Player2 = new AnchorPane();
    private Boolean PlayerTurn = true;

    public GameController(){
        Player1.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(0), Insets.EMPTY)));
        Player2.setBackground(new Background(new BackgroundFill(Color.BLUE, new CornerRadii(0), Insets.EMPTY)));


    }

    

    public void SetupGameBoard(){
        clearBoard();
        int boardsize = 7;
        
        for(int x =0;x<=boardsize-1;x++){
            for(int y =0;y<=boardsize-1;y++){
                Circle myNewCircle = getPlayCircle();
                GameBoard.add(myNewCircle, y, x);
                GridPane.setHalignment(myNewCircle, HPos.CENTER);

            } 
        }
        // GameBoard.setOnMouseClicked(event ->{
        //     System.out.println(event.getX()+" : "+event.getY());
        //     Node intersect = event.getPickResult().getIntersectedNode();
        //     Integer cIndex = GridPane.getColumnIndex(intersect);
        //     Integer rIndex = GridPane.getRowIndex(intersect);
        //     int x = cIndex == null ? 0 : cIndex;
        //     int y = rIndex == null ? 0 : rIndex;
        //     System.out.println(x + " : "+y);
        //     GameBoard.getChildren().remove(intersect);
        //     if(PlayerTurn){
        //         GameBoard.add(Player1, x, y);
        //     }
        //     else{
        //         GameBoard.add(Player2, x, y);
        //     };
        // });
    }



    // public void SetStartingPositions() {
    //     GameBoard.getChildren().clear();
    //     for (int i = 0; i < GameBoard.getColumnCount(); i++) {
    //         for (int j = 0; j < GameBoard.getRowCount(); j++) {

    //             AnchorPane AnchorThing = new AnchorPane();
    //             AnchorThing.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));
    //             AnchorThing.setOnMouseClicked(event -> {
    //                 System.out.println(event.getX()+" : "+event.getY());
    //                 if(PlayerTurn){
    //                     AnchorThing.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(0), Insets.EMPTY)));
    //                 }
    //                 else{
    //                     AnchorThing.setBackground(new Background(new BackgroundFill(Color.BLUE, new CornerRadii(0), Insets.EMPTY)));
    //                 };
    //                 PlayerTurn = !PlayerTurn;
    //             });
    //             GameBoard.add(AnchorThing, i, j);

    //         }
    //     }
        
    // }

    public void ResetGame() throws IOException{
        Main.show("game");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetupGameBoard();
    }









    //Helpers
    public Circle getPlayCircle(){
        Circle myCircle = new Circle(89,101,20);
        if(PlayerTurn){
            myCircle.setFill(Color.RED);
        }
        else{
            myCircle.setFill(Color.BLUE);
        }
        PlayerTurn = !PlayerTurn;
        return myCircle;
    }
    
    public void clearBoard(){
        for(int i = 1; i< GameBoard.getChildren().size(); i++){
            GameBoard.getChildren().remove(i);
        }
    }
}
