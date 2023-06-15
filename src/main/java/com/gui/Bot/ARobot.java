package com.gui.Bot;

import java.util.HashMap;

import com.gui.GameController;
import com.shared.Cord;

import javafx.util.Pair;

public abstract class ARobot {
    GameController controller;

    public ARobot(){}
    public ARobot(GameController controller){
        this.controller = controller;
    }

    public void setController(GameController controller){
        this.controller = controller;
    }

    public abstract Pair<Cord,Cord> getMoveCords();
    
    public static HashMap<String,ARobot> botList = new HashMap<>(){{
        put("RandomBot", new RandomBot());
        put("AggressiveBot",new AggressiveBot());
        put("Negamax",new NegamaxBot());
    }};
}
