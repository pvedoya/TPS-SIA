package com.company;

import java.util.*;


public class Solver {
    private String algorithm;
    private Board board;
    private ArrayList<Board> moves;

    public Solver(String algorithm, Board board){
        this.algorithm = algorithm;
        this.board = board;
        this.moves = new ArrayList<>();
    }

    public boolean generateSolution(){
        switch(this.algorithm){
            case "DFS": return solveDFS();
            case "BFS": return solveBFS();
            case "IDDFS": return solveIDDFS();
            case "GGS": return solveGGS();
            case "A*": return solveA();
            case "IDA*": return solveIDA();
            default: break;
        }
        return false;
    }

    private boolean solveIDA() {
        return true;
    }

    private boolean solveA() {
        return true;
    }

    private boolean solveGGS() {
        return true;
    }

    private boolean solveIDDFS() {
        return true;
    }

    private boolean solveBFS() {
        return true;
    }

    private boolean solveDFS(){
        Node node = new Node(this.board, null);
        HashSet<String> explored = new HashSet<>();
        Stack<Node> frontier = new Stack<>();
        frontier.push(node);

        while(!frontier.empty()){
            if(frontier.peek() == null){
                System.out.println("Could not find answer");
                return false;
            }

            node = frontier.pop();

            if( !explored.contains(node.getStringBoard())){
                explored.add(node.getStringBoard());
                if(node.isGoal()){
                    System.out.println("Si");
                    return true;
                }
                node.generateOutcomes();
                for(Node n : node.getOutcomes()){
                    if(!explored.contains(n.getStringBoard())){
                        Node aux = new Node(n.getBoard(),n.getDirection());
                        frontier.add(aux);
                    }
                }
            }

        }
        System.out.println("No");
        return false;
    }


}
