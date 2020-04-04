package com.company;

import java.util.*;

public class Solver {
    private ArrayList<Node> moves;
    private Board board;

    //solution data
    private String algorithm;
    private int exploredQ;
    private int frontierQ;
    private int moveQ;
    private long time;
    private int cost;


    public Solver(String algorithm,Board board){
        this.algorithm = algorithm;
        this.board = board;
        this.moves = new ArrayList<>();
    }

    public Solution generateSolution(){
        boolean found = false;
        switch(this.algorithm){
            case "DFS": found = solveDFS(); this.cost = 0; break;
            case "BFS": found = solveBFS(); this.cost = 0; break;
            case "IDDFS": found = solveIDDFS(); this.cost = 0; break;
            case "GGS": found = solveGGS(); break;
            case "A*": found = solveAstar(); break;
            case "IDA*": found = solveIDAstar(); break;
            default: break;
        }
        Solution solution = null;
        if(found){
            this.moveQ = 0;
            findPath();
            solution = new Solution(moves, moveQ, cost, exploredQ, frontierQ, time, algorithm);
        }
        return solution;
    }

    private void findPath(){
        Node n = moves.get(0);
        while(n.getParent() != null) {
            this.moveQ++;
            moves.add(n.getParent());
            n = n.getParent();
        }
        System.out.println("Solved in " + this.moveQ + " moves");
        Collections.reverse(moves);
    }

    private boolean solveIDAstar() {
        System.out.println("IDAstrar");
        return false;
    }

    private boolean solveAstar() {
        System.out.println("AStar");
        Node startNode = new Node(this.board, null, null);
        RouteNode startRouteNode = new RouteNode(startNode, null, 0, heuristic(startNode));

        Queue<RouteNode> frontier = new PriorityQueue<>();
        Set<String> explored = new HashSet<>();

        frontier.add(startRouteNode);

        while (!frontier.isEmpty()) {
            RouteNode currentRouteNode = frontier.poll(); //devuelve el nodo con menor costo por ser una priority queue

            if (currentRouteNode.getNode().isGoal()) {
                moves.add(currentRouteNode.getNode());
                return true;
            }

            explored.add(currentRouteNode.getNode().getStringBoard());
            currentRouteNode.getNode().generateOutcomes();

            for(Node childNode : currentRouteNode.getNode().getOutcomes()){
                RouteNode childRouteNode = new RouteNode(childNode, currentRouteNode.getParent(), currentRouteNode.getRouteScore() + 1, heuristic(childNode));
                if(!explored.contains(childNode.getStringBoard()) && !frontier.contains(childRouteNode)){
                    frontier.add(childRouteNode);
                } else if (frontier.contains(childRouteNode)) {
                    frontier.remove(childRouteNode);
                    frontier.add(childRouteNode);
                }
            }
        }
        return false;
    }

    //todo
    private double heuristic(Node node) {
        return 1;
    }

    private boolean solveGGS() {
        System.out.println("GGS");
        return false;
    }


    private boolean solveIDDFS() {
        long startTime = System.nanoTime();
        boolean found = false;
        Stack<Node> frontier = new Stack<>();
        HashSet<String> explored = new HashSet<>();

        Node node = new Node(this.board, null, null);
        frontier.push(node);

        while(!found){
            found = solveDLS(explored, frontier);
        }

        long endTime = System.nanoTime();
        this.time = (endTime - startTime)/1000000;

        this.frontierQ = frontier.size();
        this.exploredQ = explored.size();

        return true;
    }

    private boolean solveDLS( HashSet<String> explored, Stack<Node> previous){

        Stack<Node> frontier = new Stack<>();

        while(!previous.empty()){
            Node aux = previous.pop();

            if( !explored.contains(aux.getStringBoard())){
                explored.add(aux.getStringBoard());

                if(aux.isGoal()){
                    moves.add(aux);
                    return true;
                }

                aux.generateOutcomes();
                for(Node n : aux.getOutcomes()){
                    if(!explored.contains(n.getStringBoard()) && !n.getBoard().hasBlocked()){
                        frontier.add(n);
                    }
                }
            }

        }
        for (Node node : frontier) {
            previous.add(node);
        }

        return false;
    }

    private boolean solveDFS(){
        long startTime = System.nanoTime();

        Node node = new Node(this.board, null, null);
        HashSet<String> explored = new HashSet<>();
        Stack<Node> frontier = new Stack<>();
        frontier.push(node);

        while(!frontier.empty()){
            node = frontier.pop();

            if( !explored.contains(node.getStringBoard()) ){
                explored.add(node.getStringBoard());

                if(node.isGoal()){
                    long endTime = System.nanoTime();
                    this.time = (endTime - startTime)/1000000;

                    this.frontierQ = frontier.size();
                    this.exploredQ = explored.size();
                    moves.add(node);
                    return true;
                }
                node.generateOutcomes();
                for(Node n : node.getOutcomes()){

                    if(!explored.contains(n.getStringBoard()) && !n.getBoard().hasBlocked()){
                        frontier.add(n);
                    }
                }
            }

        }

        return false;
    }

    private boolean solveBFS() {
        long startTime = System.nanoTime();

        Node node = new Node(this.board, null, null);
        HashSet<String> explored = new HashSet<>();
        Queue<Node> frontier = new LinkedList<>();
        frontier.add(node);

        if(node.isGoal()){
            long endTime = System.nanoTime();
            this.time = (endTime - startTime)/1000000;

            this.frontierQ = frontier.size();
            this.exploredQ = explored.size();

            moves.add(node);

            return true;
        }

        while(!frontier.isEmpty()){
            node = frontier.poll();
            node.getBoard().printBoard();
            explored.add(node.getStringBoard());
            node.generateOutcomes();

            for(Node n : node.getOutcomes()){
                if(!explored.contains(n.getStringBoard()) && !frontier.contains(n) && !n.getBoard().hasBlocked()){

                    if(n.isGoal()){
                        long endTime = System.nanoTime();
                        this.time = (endTime - startTime)/1000000;

                        this.frontierQ = frontier.size();
                        this.exploredQ = explored.size();

                        moves.add(n);

                        return true;
                    }
                    frontier.add(n);
                }
            }
        }
        return false;
    }

    public ArrayList<Node> getMoves() {
        return moves;
    }
}