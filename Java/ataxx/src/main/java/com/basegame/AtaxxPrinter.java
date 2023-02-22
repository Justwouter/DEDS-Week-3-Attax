package com.basegame;

import java.io.PrintWriter;

import com.basegame.interfaces.IPrinter;

public class AtaxxPrinter implements IPrinter{
    private PrintWriter printer = new PrintWriter(System.out,true);

    @Override
    public void println() {
        printer.println();
    }

    @Override
    public void println(Object data) {
        printer.println(data);
    }

    @Override
    public void print(Object data) {
        printer.print(data);
    }

    
}
