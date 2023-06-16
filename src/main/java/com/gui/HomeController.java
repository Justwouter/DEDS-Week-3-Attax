package com.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.gui.Bot.ARobot;
import com.gui.Support.Player;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

//TODO Load settings from datadict when play again is used
public class HomeController extends AController implements Initializable {

    @FXML
    private BorderPane MainPane;

    @FXML
    private Spinner<Integer> BoardSize;

    @FXML
    private Button HomePageEnter;

    @FXML
    private TextField Player0Name;

    @FXML
    private TextField Player1Name;

    @FXML
    private CheckBox Player0BotCheck;

    @FXML
    private CheckBox Player1BotCheck;

    @FXML
    private ComboBox<String> Player0BotTypeSelector;

    @FXML
    private ComboBox<String> Player1BotTypeSelector;

    private HashMap<String,ARobot> Botlist = ARobot.botList;

    
    ARobot player0Bottype;
    ARobot player1Bottype;
    int boardsize = 7;

    @FXML
    private void switchToGame() {
        makeUsers();
        Main.show("game", this.dataDict);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setupHandlers();
        BoardSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 16,7,1));
    }

    private void setupHandlers() {
        HomePageEnter.setOnAction(e -> switchToGame());
        Player0BotCheck.setOnAction(e -> handleBotCheck(Player0BotCheck));
        Player1BotCheck.setOnAction(e -> handleBotCheck(Player1BotCheck));
        Player0BotTypeSelector.setOnAction(e -> handleBotChoice(Player0BotTypeSelector));
        Player1BotTypeSelector.setOnAction(e -> handleBotChoice(Player1BotTypeSelector));
        BoardSize.valueProperty().addListener((obs, oldValue, newValue) -> handleBoardsizeChange(newValue));
    }

    /**
     * Toggles the bot selection boxes when the bot activation boxes are ticked.<p>
     * Should fire every time the activation box selection changes.
     * @param source
     */
    private void handleBotCheck(CheckBox source) {
        if(source.getId().contains("0")){
            Player0BotTypeSelector.setDisable(!Player0BotTypeSelector.disableProperty().get());
        }
        else if(source.getId().contains("1")){
            Player1BotTypeSelector.setDisable(!Player1BotTypeSelector.disableProperty().get());
        }
    }

    /**
     * Updates internal variables with the current bot selection.
     * @param source
     */
    private void handleBotChoice(ComboBox<String> source){
        if(source.getId().contains("0")){
            player0Bottype = Botlist.get(source.getSelectionModel().getSelectedItem());
        }
        else if(source.getId().contains("1")){
            player1Bottype = Botlist.get(source.getSelectionModel().getSelectedItem());
        }
    }

    private void handleBoardsizeChange(Integer value){
        this.boardsize = value;
    }

    
    /**
     * Creates users and loads them to the shared 'database' when the startgame button is clicked.
     */
    private void makeUsers() {
        Player player0;
        Player player1;

        if (Player0Name.getText().isEmpty()) {
            Player0Name.setText("Player1");
        }
        if (Player1Name.getText().isEmpty()) {
            Player1Name.setText("Player2");
        }
        if (!Player0BotCheck.isSelected()) {
            player0Bottype = null;
        }
        if (!Player1BotCheck.isSelected()) {
            player1Bottype = null;
        }

        player0 = new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), Player0Name.getText(), Color.rgb(30, 144, 255),
                player0Bottype);
        player1 = new Player(new Ellipse(53.0, 283.0, 19.0, 18.0), Player1Name.getText(), Color.rgb(255, 0, 0),
                player1Bottype);

        dataDict.put("Player0", player0);
        dataDict.put("Player1", player1);
        dataDict.put("BoardSize", this.boardsize);
    }


    @Override
    public void loadData(Map<String, Object> dataDict) {
        this.dataDict = dataDict;
        seedChoiceBoxes();
    }

    private void seedChoiceBoxes(){
        Botlist.forEach((key, value) ->{
            Player0BotTypeSelector.getItems().add(key);
            Player1BotTypeSelector.getItems().add(key);
        });
    }

}
