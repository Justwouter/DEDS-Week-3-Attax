package com.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameController extends AController implements Initializable{

    @FXML
    private GridPane GameBoard;

    public void SetStartingPositions() {
        GameBoard.getChildren().clear();
        for (int i = 0; i < GameBoard.getColumnCount(); i++) {
            for (int j = 0; j < GameBoard.getRowCount(); j++) {

                AnchorPane AnchorThing = new AnchorPane();
                AnchorThing.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(0), Insets.EMPTY)));
                AnchorThing.setOnMouseClicked(event -> AnchorThing.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), Insets.EMPTY))));
                GameBoard.add(AnchorThing, i, j);

            }
        }
    }

    public void ResetGame() throws IOException{
        Main.show("game");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetStartingPositions();
    }
}
