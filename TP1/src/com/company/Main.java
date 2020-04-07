package com.company;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        File readFile = new File("TP1/maps/" + args[0]);
        //File readFile = new File("maps/" + args[0]);

        Settings settings = new Settings();
        settings.loadSettings(readFile);

        Board board = new Board(settings.getWidth(),settings.getHeight(),settings.getBoard());
        board.validate();

        Solver solver = new Solver(settings.getAlgorithm(), settings.getHeuristic(), board);

        try {
            Solution solution = solver.generateSolution();
            if(solution != null) {
                solution.dumpToFile();
            }
        } catch (IOException e) {
            System.out.println("ERROR: Failed to open and write in solution.txt file");
            System.exit(1);
        }catch(OutOfMemoryError e){
            System.out.println("ERROR: System could not find enough memory to generate solution");
            System.exit(1);
        }
    }
}
