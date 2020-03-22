package com.company;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        File readFile = new File("maps/" + args[0]);

        Settings settings = new Settings();
        settings.loadSettings(readFile);

        Board board = new Board();
        board.applySettings(settings);

        Scanner input = new Scanner(System.in);
        String str = input.nextLine();
        while(!str.equals("END")){
            board.makeMove(str);
            board.printBoard();
            str = input.nextLine();
        }

//        Solver solver = new Solver(settings.getAlgorithm(), board);
//        solver.generateSolution();

        System.out.println("Solved");
    }
}
