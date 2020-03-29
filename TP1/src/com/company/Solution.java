package com.company;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

    public void printSolution(){
        System.out.println("");
        if (moves.isEmpty()) {
            System.out.println("Could not find a solution using " + algorithm);
        }else{
            System.out.println("Found a solution in " + depth + " moves, time taken: " + time + " ms");
            System.out.println("The cost for the algorithm used(" + algorithm + ") was " + cost);
            System.out.println("Nodes explored: " + exploredQ + ", nodes in frontier: " + frontierQ);
            System.out.println("");
            System.out.println("Printing steps...");
            for(Node node: moves){
                node.getBoard().printBoard();
                System.out.println("");
            }
        }
    }

    public void dumpToFile() throws IOException {

        System.out.println("");

        if (moves.isEmpty()) {
            System.out.println("Could not find a solution using " + algorithm);
        }else{
            System.out.println("Solution found, copying data to solution.txt file on root folder");
            BufferedWriter writer = new BufferedWriter(new FileWriter("solution.txt"));
            writer.write("Found a solution in " + depth + " moves, time taken: " + time + " ms\n");
            writer.write("The cost for the algorithm used(" + algorithm + ") was " + cost + "\n");
            writer.write("Nodes explored: " + exploredQ + ", nodes in frontier: " + frontierQ + "\n");
            writer.write("\n");
            writer.write("Printing steps...\n");

            for(Node node: moves){
                writeBoard(node.getBoard(), writer);
                writer.write("\n");
            }

            writer.close();
        }
    }

    private void writeBoard(Board board, BufferedWriter writer) throws IOException {
        for(int i = 0; i < board.getHeight(); i++){
            writer.write(board.getBoard()[i]);
            writer.write("\n");
        }
    }
}
