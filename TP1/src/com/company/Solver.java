package com.company;

import java.util.*;


public class Solver {
    private String algorithm;
    private Board board;
    private ArrayList<Node> moves;
    private int moveQ;

    public Solver(String algorithm, Board board){
        this.algorithm = algorithm;
        this.board = board;
        this.moves = new ArrayList<>();
    }

    public boolean generateSolution(){
        boolean found = false;
        switch(this.algorithm){
            case "DFS": found = solveDFS(); break;
            case "BFS": found = solveBFS(); break;
            case "IDDFS": found = solveIDDFS();break;
            case "GGS": found = solveGGS();break;
            case "A*": found = solveAstar();break;
            case "IDA*": found = solveIDAstar();break;
            default: break;
        }

        if(found){
            this.moveQ = 0;
            findPath();
            System.out.println("Solved in " + moveQ + " moves");
        }else{

        }
        return found;
    }

    private void findPath(){
        Node n = moves.get(0);
        while(n.getParent() != null){
            this.moveQ++;
            moves.add(n.getParent());
            n=n.getParent();
        }
        Collections.reverse(moves);
    }

    private boolean solveIDAstar() {
        return true;
    }

    private boolean solveAstar() {
        return true;
    }

    private boolean solveGGS() {
        return true;
    }

    private boolean solveIDDFS() {
        return true;
    }

    private boolean solveBFS() {
        Node node = new Node(this.board, null, null);
        HashSet<String> explored = new HashSet<>();
        Queue<Node> frontier = new LinkedList<>();
        frontier.add(node);

        if(node.isGoal()){
            return true;
        }

        while(!frontier.isEmpty()){
            node = frontier.poll();
            explored.add(node.getStringBoard());
            node.generateOutcomes();

            for(Node n : node.getOutcomes()){
                if(!explored.contains(n.getStringBoard()) && !frontier.contains(n)){
                    if(n.isGoal()){
                        moves.add(n);
                        return true;
                    }
                    frontier.add(n);
                }
            }
        }

        return false;
    }

    private boolean solveDFS(){
        Node node = new Node(this.board, null, null);
        HashSet<String> explored = new HashSet<>();
        Stack<Node> frontier = new Stack<>();
        frontier.push(node);

        while(!frontier.empty()){
            node = frontier.pop();

            if( !explored.contains(node.getStringBoard())){
                explored.add(node.getStringBoard());

                if(node.isGoal()){
                    moves.add(node);
                    return true;
                }
                node.generateOutcomes();
                for(Node n : node.getOutcomes()){

                    if(!explored.contains(n.getStringBoard())){
                        frontier.add(n);
                    }
                }
            }

        }

        return false;
    }

    public ArrayList<Node> getMoves() {
        return moves;
    }
}