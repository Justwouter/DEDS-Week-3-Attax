package com.basegame;

public class BaseGameStarter {

    public static void StartNewTerminalGame() {
        // Stack<Object> myStack = new Stack<>();
        // myStack.push("Hello");
        // myStack.push(1);
        // System.out.println(myStack.pop().getData());
        // System.out.println(myStack.pop().getData());
        new Game();
    }

    public static void main(String[] args) {
        Helpers.enableUTF8InPowershell();
        StartNewTerminalGame();
    }
    
}
