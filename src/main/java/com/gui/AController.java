package com.gui;

import java.util.Map;

public abstract class AController {
    

    Map<String,Object> dataDict;

    public abstract void loadData(Map<String,Object> dataDict);

}