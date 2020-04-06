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
    private int cost;
    private int exploredQ;
    private int frontierQ;
    private long time;
    private String algorithm;


    public Solution(ArrayList<Node> moves, int depth, int cost, int exploredQ, int frontierQ, long time, String algorithm) {
        this.moves = moves;
        this.depth = depth;
        this.cost = cost;
        this.exploredQ = exploredQ;
        this.frontierQ = frontierQ;
        this.time = time;
        this.algorithm = algorithm;
    }

    public void dumpToFile() throws IOException {

        System.out.println("");

        if (moves.isEmpty()) {
            System.out.println("Could not find a solution using " + algorithm);
        }else{
            System.out.println("Solution found, copying data to solution.txt file on root folder");
            BufferedWriter writer = new BufferedWriter(new FileWriter("solution.txt"));
            writer.write("Input board:\n");
            writeBoard(moves.get(0).getBoard(), writer);
            writer.write("\nFound a solution in " + depth + " moves, time taken: " + time + " ms\n");
            if(!algorithm.equals("DFS") && !algorithm.equals("BFS") && !algorithm.equals("IDDFS")){
                writer.write("The cost for the algorithm used(" + algorithm + ") was " + cost + "\n");
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
        char[][] full = board.fullBoard();
        for(int i = 0; i < board.getHeight(); i++){
            writer.write(full[i]);
            writer.write("\n");
        }
    }
}