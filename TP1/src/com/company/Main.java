package com.company;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        File readFile = new File("maps/" + args[0]);

        Settings settings = new Settings();
        settings.loadSettings(readFile);

        Board board = new Board(settings.getWidth(),settings.getHeight(),settings.getBoard());
        board.validate();


        Solver solver = new Solver(settings.getAlgorithm(), board);
        solver.generateSolution();

        List<Node> moves = solver.getMoves();
        for (Node n: moves) {
            n.getBoard().printBoard();
        }
    }
}