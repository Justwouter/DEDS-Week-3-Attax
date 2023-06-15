package com.gui;

import java.util.Map;

public abstract class AController {
    /**
     * Main 'database' to share objects across controllers.<p>
     * Absolutly no type checking involved so prepare for some compiler warnings.
     */
    Map<String,Object> dataDict;

    /**
     * Method used to load the database accross controllers.<p>
     * Should contain at least {@code this.dataDict = dataDict}
     * @param dataDict
     */
    public abstract void loadData(Map<String,Object> dataDict);
}