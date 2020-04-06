package com.company;

import java.util.*;

/*
* Clase que recibe un nombre de algoritmo (y uno de heuristica de ser necesaria) y un tablero, y encuentra la solucion correspondiente
* */

public class Solver {
    private String algorithm;
    private String heuristic;
    private Board board;
    private ArrayList<Node> moves;

    private int moveQ;
    private int exploredQ;
    private int frontierQ;
    private long time;
    private int cost;


    public Solver(String algorithm,String heuristic,Board board){
        this.algorithm = algorithm;
        this.heuristic = heuristic;
        this.board = board;
        this.moves = new ArrayList<>();
    }

    /*
    * LLama al metodo correspondiente al algoritmo, y si encuentra una solucion, crea y retorna una solucion con esa informacion
    * */

    public Solution generateSolution() throws OutOfMemoryError {
        boolean found = false;
        switch(this.algorithm){
            case "DFS": found = solveDFS(); this.cost = 0; break;
            case "BFS": found = solveBFS();this.cost = 0; break;
            case "IDDFS": found = solveIDDFS();this.cost = 0; break;
            case "GGS": found = solveGGS();break;
            case "A*": found = solveAstar();break;
            case "IDA*": found = solveIDAstar();break;
            default: break;
        }

        Solution solution = null;

        if(found){
            this.moveQ = 0;
            findPath();
            solution = new Solution(moves, moveQ, cost, exploredQ, frontierQ, time, algorithm, heuristic);
        }
        return solution;
    }

    /*
    * Usa una lista con solamente el ultimo nodo para encotrar un camino a la raiz, agregando los nodos intermedio
    * a la lista. Tambien suma al contador de movimientos
    * */

    private void findPath(){
        Node n = moves.get(0);

        while(n.getParent() != null) {
            this.moveQ++;
            moves.add(n.getParent());
            n = n.getParent();
        }
        Collections.reverse(moves);
    }

    private boolean solveIDAstar() {
        Node startNode = new Node(this.board, null, null);
        RouteNode startRouteNode = new RouteNode(startNode, null, 0, heuristic(startNode.getBoard()));

        int threshold = (int) heuristic(startNode.getBoard());
        while(true) {
            RetIDAstar ret = searchIDAstar(startRouteNode,  threshold);
            switch (ret.getSearchReturn()) {
                case BOUND:
                    threshold = ret.getHeuristic();
                    break;
                case FOUND:
                    return true;
                case NOT_FOUND:
                    return false;
            }
        }
    }

    private RetIDAstar searchIDAstar(RouteNode routeNode, int threshold) {
        RetIDAstar ret = new RetIDAstar();

        if(routeNode.getNode().isGoal()){
            moves.add(routeNode.getNode());
            ret.setSearchReturn(SEARCHRETURN.FOUND);
            return ret;
        }

        int f = (int) routeNode.getTotalCost();
        //si el f es mas grande que el threshold, return y poner el nuevo threshold
        if(f > threshold) {
            ret.setSearchReturn(SEARCHRETURN.BOUND);
            ret.setHeuristic(f);
            return ret;
        }

        int min = Integer.MAX_VALUE;
        routeNode.getNode().generateOutcomes();

        for(Node childNode : routeNode.getNode().getOutcomes()){
            RouteNode childRouteNode = new RouteNode(childNode, routeNode.getNode(), routeNode.getRouteScore() + 1, heuristic(childNode.getBoard()));
            RetIDAstar r = searchIDAstar(childRouteNode, threshold);

            switch (r.getSearchReturn()) {
                case BOUND:
                    if(r.getHeuristic() < min) {
                        min = r.getHeuristic();
                    }
                    break;
                case FOUND:
                    return r;
                case NOT_FOUND:
                    continue;
            }

        }

        if(min == Integer.MAX_VALUE) {
            ret.setSearchReturn(SEARCHRETURN.NOT_FOUND);
        } else {
            ret.setHeuristic(min);
            ret.setSearchReturn(SEARCHRETURN.BOUND);
        }
        return ret;
    }

    private enum SEARCHRETURN { BOUND, FOUND, NOT_FOUND };

    private static class RetIDAstar {
        private SEARCHRETURN sr;
        private int newHeuristic;
        public SEARCHRETURN getSearchReturn() { return sr; }
        public void setSearchReturn(SEARCHRETURN sr) { this.sr=sr; }
        public void setHeuristic(int newHeuristic) { this.newHeuristic=newHeuristic; }
        public int getHeuristic() { return newHeuristic; }
    }

    private boolean solveAstar() {
        Node startNode = new Node(this.board, null, null,  0);
        startNode.setTotalCost(startNode.getPathCost() + heuristic(startNode.getBoard()));
        Queue<Node> frontier = new PriorityQueue<>(); //todo chequear q este bien el compareto
        frontier.add(startNode);
        Set<Board> explored = new HashSet<>();

        while (!frontier.isEmpty()) {
            Node currentNode = frontier.poll(); //devuelve el nodo con menor costo por ser una priority queue
            if (currentNode.isGoal()) {
                moves.add(currentNode);
                return true;
            }
            explored.add(currentNode.getBoard());
            currentNode.generateWeightedOutcomes();
//            List<Node> outcomes = currentNode.getOutcomes();
            for(Node childNode : currentNode.getOutcomes()){
                childNode.setTotalCost(childNode.getPathCost() + heuristic(childNode.getBoard()));
                if(!explored.contains(childNode.getBoard())){
                    boolean exists = false;
                    for(Node n : frontier) {
                        if(childNode.getBoard().equals(n.getBoard())) {
                            exists = true;
                            break;
                        }
                    }
                    if(!exists) {
                        frontier.add(childNode);
                    }
                } else {
                    for(Node n : frontier) {
                        if(childNode.getBoard().equals(n.getBoard())) {
                            if(n.getTotalCost() > childNode.getTotalCost()) {
                                frontier.remove(n);
                                frontier.add(childNode);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private double heuristic(Board b) {
        //return 1;
        if(this.heuristic.equals("MANHATTAN")){
            return Heuristics.avrgManhattanDistance(b);
        }else if (this.heuristic.equals("MMLB")){
            return Heuristics.minimumMatchingLowerBound(b);
        }else{
            return Heuristics.simpleLowerBound(b);
        }
    }

    private boolean solveGGS() {
        Node startNode = new Node(this.board, null, null,  0);
        startNode.setTotalCost(startNode.getPathCost() + heuristic(startNode.getBoard()));
        Queue<Node> frontier = new PriorityQueue<>(); //todo chequear q este bien el compareto
        frontier.add(startNode);
        Set<Board> explored = new HashSet<>();

        while (!frontier.isEmpty()) {
            Node currentNode = frontier.poll(); //devuelve el nodo con menor costo por ser una priority queue
            if (currentNode.isGoal()) {
                moves.add(currentNode);
                return true;
            }
            explored.add(currentNode.getBoard());
            currentNode.generateWeightedOutcomes();
//            List<Node> outcomes = currentNode.getOutcomes();
            for(Node childNode : currentNode.getOutcomes()){
                childNode.setTotalCost(childNode.getPathCost() + heuristic(childNode.getBoard()));
                if(!explored.contains(childNode.getBoard())){
                    boolean exists = false;
                    for(Node n : frontier) {
                        if(childNode.getBoard().equals(n.getBoard())) {
                            exists = true;
                            break;
                        }
                    }
                    if(!exists) {
                        frontier.add(childNode);
                    }
                } else {
                    for(Node n : frontier) {
                        if(childNode.getBoard().equals(n.getBoard())) {
                            if(n.getTotalCost() > childNode.getTotalCost()) {
                                frontier.remove(n);
                                frontier.add(childNode);
                                break;
                            }
                        }
                    }
                }
            }
        }
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
                    if(!explored.contains(n.getStringBoard()) && !frontier.contains(n) && !n.getBoard().hasBlocked()){
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

                    if(!explored.contains(n.getStringBoard()) && !frontier.contains(n) && !n.getBoard().hasBlocked()){
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

        if(node.isGoal()){
            long endTime = System.nanoTime();
            this.time = (endTime - startTime)/1000000;

            this.frontierQ = frontier.size();
            this.exploredQ = explored.size();

            moves.add(node);

            return true;
        }
        frontier.add(node);

        while(!frontier.isEmpty()){
            node = frontier.poll();

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
}