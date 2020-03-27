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
            System.out.println("Could not find solution to map using " + algorithm + " algorithm");
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

//    private boolean solveIDDFS() {
//        int counter = 0;
//        boolean found = false;
//
//        while(!found){
//            Node node = new Node(this.board, null, null);
//            found = solveDLS(node, counter);
//            counter++;
//        }
//
//        return true;
//    }
//
//    private boolean solveDLS(Node node, int limit){
//        HashSet<String> explored = new HashSet<>();
//        Stack<Node> frontier = new Stack<>();
//        int counter = 0;
//
//        if(node.isGoal()){
//            moves.add(node);
//            return true;
//        }
//
//        frontier.push(node);
//
//        while(!frontier.empty() && counter < limit){
//            counter++;
//            Node aux = frontier.pop();
//
//            if( !explored.contains(aux.getStringBoard())){
//                explored.add(aux.getStringBoard());
//
//                if(aux.isGoal()){
//                    moves.add(aux);
//                    return true;
//                }
//
//                aux.generateOutcomes();
//                for(Node n : aux.getOutcomes()){
//                    if(!explored.contains(n.getStringBoard()) && !n.getBoard().hasBlocked()){
//                        frontier.add(n);
//                    }
//                }
//            }
//
//        }
//
//        return false;
//    }

    private boolean solveIDDFS() {
        boolean found = false;
        Stack<Node> frontier = new Stack<>();
        HashSet<String> explored = new HashSet<>();

        Node node = new Node(this.board, null, null);
        frontier.push(node);

        while(!found){
            found = solveDLS(explored, frontier);
        }

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
        Node node = new Node(this.board, null, null);
        HashSet<String> explored = new HashSet<>();
        Stack<Node> frontier = new Stack<>();
        frontier.push(node);

        while(!frontier.empty()){
            node = frontier.pop();

            if( !explored.contains(node.getStringBoard()) ){
                explored.add(node.getStringBoard());

                if(node.isGoal()){
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
        Node node = new Node(this.board, null, null);
        HashSet<String> explored = new HashSet<>();
        Queue<Node> frontier = new LinkedList<>();
        frontier.add(node);

        if(node.isGoal()){
            moves.add(node);
            return true;
        }

        while(!frontier.isEmpty()){
            node = frontier.poll();

            explored.add(node.getStringBoard());
            node.generateOutcomes();

            for(Node n : node.getOutcomes()){
                if(!explored.contains(n.getStringBoard()) && !frontier.contains(n) && !n.getBoard().hasBlocked()){
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

    public ArrayList<Node> getMoves() {
        return moves;
    }
}