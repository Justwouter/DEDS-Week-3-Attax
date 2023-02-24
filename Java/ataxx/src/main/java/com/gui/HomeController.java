package com.gui;

import java.io.IOException;
import javafx.fxml.FXML;

public class HomeController extends AController{

    @FXML
    private void switchToGame() throws IOException {
        Main.show("game", "");
    }
}
