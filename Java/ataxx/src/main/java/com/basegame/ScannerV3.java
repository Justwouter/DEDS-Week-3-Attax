package com.basegame;

import java.util.Scanner;
import com.basegame.interfaces.IScanner;

public class ScannerV3 implements IScanner{
    Scanner scanner = new Scanner(System.in);

    public ScannerV3(){}

    @Override
    public int nextInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } 
            catch (NumberFormatException e) {
                System.out.println("Voer enkel cijfers in.");
            }
        }
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    @Override
    public char nextChar() {
        while (true){
            String input = scanner.nextLine();
            input = input.replace("\n", "");
            if(input.chars().count() > 1){
                System.out.println("Een karakter graag!");
            }
        }
    }
}
