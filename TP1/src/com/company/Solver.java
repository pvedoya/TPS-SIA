package com.company;

import java.util.ArrayList;

public class Solver {
    private String algorithm;
    private Board board;
    private ArrayList<Board> moves;

    public Solver(String algorithm, Board board){
        this.algorithm = algorithm;
        this.board = board;
        this.moves = new ArrayList<>();
    }

    public void generateSolution(){
        switch(this.algorithm){
            case "DFS": solveDFS();
            case "BFS": solveBFS();
            case "IDDFS": solveIDDFS();
            case "GGS": solveGGS();
            case "A*": solveA();
            case "IDA*": solveIDA();
            default: ;
        }
        System.out.println("Solution generation ended");
    }

    private void solveIDA() {
    }

    private void solveA() {
    }

    private void solveGGS() {
    }

    private void solveIDDFS() {
    }

    private void solveBFS() {
    }

    private void solveDFS() {
    }


}
