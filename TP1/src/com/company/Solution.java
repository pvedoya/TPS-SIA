package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
* Clase que guarda y exporta la solucion al tablero con el algoritmo seleccionado
* */

public class Solution {
    private ArrayList<Node> moves;
    private int depth;
    private double cost;
    private int exploredQ;
    private int frontierQ;
    private long time;
    private String algorithm;
    private String heuristic;
    private boolean found;

    public Solution(ArrayList<Node> moves, int depth, double cost, int exploredQ, int frontierQ, long time, String algorithm, String heuristic, boolean found) {
        this.moves = moves;
        this.depth = depth;
        this.cost = cost;
        this.exploredQ = exploredQ;
        this.frontierQ = frontierQ;
        this.time = time;
        this.algorithm = algorithm;
        this.heuristic = heuristic;
        this.found = found;
    }

    public void dumpToFile() throws IOException {

        System.out.println("");

        if (moves.isEmpty() || !found) {
            System.out.println("Could not find a solution using " + algorithm);
        }else{
            System.out.println("Solution found, copying data to solution.txt file on root folder");
            BufferedWriter writer = new BufferedWriter(new FileWriter("solution.txt"));
            writer.write("Input board:\n");
            writeBoard(moves.get(0).getBoard(), writer);
            writer.write("\nFound a solution in " + depth + " moves, time taken: " + time + " ms\n");
            if(algorithm.equals("A*") || algorithm.equals("IDA*") || algorithm.equals("GGS")){
                writer.write("The cost for the algorithm " + algorithm + " with heuristic " + heuristic + " was " + cost + "\n");
            }else{
                writer.write("Solved using the " + algorithm + " algorithm\n");
            }
            writer.write("Nodes explored: " + exploredQ + ", nodes in frontier: " + frontierQ + "\n");
            writer.write("\n");
            writer.write("Movements: \n");
            for(Node node: moves){
                if(node.getDirection() != null){
                    writer.write(node.getDirection() + " ");
                }
            }
            writer.write("\n\nGraphic Steps:\n");

            for(Node node: moves){
                writeBoard(node.getBoard(), writer);
                writer.write("\n");
            }

            writer.close();
        }
    }

    /*
    * Exporta el tablero linea a linea
    * */

    private void writeBoard(Board board, BufferedWriter writer) throws IOException {
        board.fullBoard();
        for(int i = 0; i < board.getHeight(); i++){
            writer.write(board.getBoard()[i]);
            writer.write("\n");
        }
    }
}
