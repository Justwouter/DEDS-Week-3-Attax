package com.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameController {

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
        // GameBoard.add(GameBoard, 0, 0);

    }

    public void drawMove(Text text) {
        text.setText("O");
        text.setFill(Color.BLACK);
    }
}
