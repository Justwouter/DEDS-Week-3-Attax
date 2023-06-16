package com.gui;

import java.io.File;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class WinnerController extends AController{

    @FXML
    public Label WinnerLabel;

    @FXML
    private Button HomePageEnter;

    @FXML
    private BorderPane MainPane;

    private MediaPlayer musicPlayer;



    public void switchToGame() throws Exception{
        musicPlayer.dispose();
        Main.show("homepage",this.dataDict);
    }
    
    @Override
    public void loadData(Map<String, Object> dataDict) {
        this.dataDict = dataDict;

        Label winLabel = new Label((String) dataDict.get("VictoryText"));
        MainPane.setTop(winLabel);
        BorderPane.setAlignment(winLabel, Pos.CENTER);
        this.musicPlayer = startMusic(Main.class.getResource("music/YAY.mp3").getFile());
    }

    private MediaPlayer startMusic(String pathToMusic){
        File file = new File(pathToMusic);
        Media media = new Media(file.toURI().toString()); 
        MediaPlayer musicPlayer = new MediaPlayer(media);
        musicPlayer.setAutoPlay(true);
        return musicPlayer;
    }

}
