package com.basegame;

public final class Helpers {

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void enableUTF8InPowershell(){
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("powershell", "-command", "$OutputEncoding = [console]::InputEncoding = [console]::OutputEncoding = New-Object System.Text.UTF8Encoding")
                .inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (Exception e) {
        }
        
    }

    public static void sleep(int value, boolean seconds){
        if(seconds){
            try{
                Thread.sleep(value*1000);
            }
            catch( Exception e){}
        }
        else{
            try{
                Thread.sleep(value);
            }
            catch( Exception e){}
        }
    }

}
