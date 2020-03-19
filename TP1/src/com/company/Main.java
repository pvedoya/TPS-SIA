package com.company;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File readFile = new File("maps/" + args[0]);

        Settings settings = new Settings();
        settings.loadSettings(readFile);

        Board board = new Board();
        board.applySettings(settings);

        Solver solver = new Solver(settings.getAlgorithm(), board);
        solver.generateSolution();

        System.out.println("Solved");
    }
}
