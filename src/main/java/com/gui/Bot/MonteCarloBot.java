package com.gui.Bot;

import com.gui.GameController;
import com.shared.Cord;

import javafx.util.Pair;

public class MonteCarloBot extends ARobot{

    public MonteCarloBot(GameController controller) {
        super(controller);
    }

    @Override
    public Pair<Cord, Cord> getMoveCords() {
        return null;
    }
    
}
