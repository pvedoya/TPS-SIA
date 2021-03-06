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
    private double cost;


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
            solution = new Solution(moves, moveQ, cost, exploredQ, frontierQ, time, algorithm, heuristic, true);
        } else {
            solution = new Solution(moves, moveQ, cost, exploredQ, frontierQ, time, algorithm, heuristic, false);
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
        long startTime = System.nanoTime();
        Node startNode = new Node(this.board, null, null,  0);
        int threshold = heuristic(startNode.getBoard());

        startNode.setTotalCost(startNode.getPathCost() + threshold);
        List<Node> explored = new ArrayList<>();

        while(true) {
            int ret = searchIDAstar(startNode, 0,  threshold, explored);
            if(ret == -1) { //found
                long endTime = System.nanoTime();
                this.time = (endTime - startTime)/1000000;
                return true;
            } else {
               explored.clear();
               threshold = ret;
            }

        }
    }

    private int searchIDAstar(Node currentNode, int currentDepth, int threshold, List<Node> explored) {
        int f = currentDepth + heuristic(currentNode.getBoard());
        if(f > threshold) {
            return f;
        }

        if(currentNode.isGoal()){
            moves.add(currentNode);
            this.cost = currentNode.getTotalCost();
            this.exploredQ = explored.size();
            return -1;
        }

        int min = Integer.MAX_VALUE;
        currentNode.generateWeightedOutcomes();
        explored.add(currentNode);
        int ret;

        for(Node childNode : currentNode.getOutcomes()) {
            if(!explored.contains(childNode)) {
                ret  = searchIDAstar(childNode, currentDepth + 1, threshold, explored);
                if(ret == -1) {
                    return -1;
                }
                if(ret < min) {
                    min = ret;
                }
            }
        }
        return min;
    }

    private boolean solveAstar() {
        long startTime = System.nanoTime();

        Node startNode = new Node(this.board, null, null,  0);
        int h = heuristic(startNode.getBoard());
        if(h >= 1000000000) {
            return false;
        }
        startNode.setTotalCost(startNode.getPathCost() + h);
        Queue<Node> frontier = new PriorityQueue<>(); //todo chequear q este bien el compareto
        frontier.add(startNode);
        Set<Node> explored = new HashSet<>();

        while (!frontier.isEmpty()) {
            Node currentNode = frontier.poll(); //devuelve el nodo con menor costo por ser una priority queue
            if (currentNode.isGoal()) {
                moves.add(currentNode);
                this.cost = currentNode.getTotalCost();
                this.frontierQ = frontier.size();
                this.exploredQ = explored.size();

                long endTime = System.nanoTime();
                this.time = (endTime - startTime)/1000000;
                return true;
            }
            explored.add(currentNode);
            currentNode.generateWeightedOutcomes();
            for(Node childNode : currentNode.getOutcomes()){
                h = heuristic(childNode.getBoard());
                    childNode.setTotalCost(childNode.getPathCost() + h);
                    if(!explored.contains(childNode) && !frontier.contains(childNode)){
                        frontier.add(childNode);
                    } else if(frontier.contains(childNode)){
                        for(Node n : frontier) {
                            if(childNode.equals(n)) {
                                if(childNode.getTotalCost() < n.getTotalCost()){
                                    frontier.remove(n);
                                    frontier.add(childNode);
                                    break;
                                }
                            }
                        }
                    } else if(explored.contains(childNode)) {
                        for(Node n : explored) {
                            if(childNode.equals(n)) {
                                if(childNode.getTotalCost() < n.getTotalCost()){
                                    frontier.add(childNode);
                                }
                            }
                        }
                    }
                }
           }
        return false;
    }

    private int heuristic(Board b) {
        if(this.heuristic.equals("MANHATTAN")){
            return Heuristics.avrgManhattanDistance(b);
        }else if (this.heuristic.equals("MMLB")){
            return Heuristics.minimumMatchingLowerBound(b);
        }else{
            return Heuristics.simpleLowerBound(b);
        }
    }

    private boolean solveGGS() {
        long startTime = System.nanoTime();

        Node startNode = new Node(this.board, null, null,  0);
        int h = heuristic(startNode.getBoard());
        if(h >= 1000000000) {
            return false;
        }
        startNode.setTotalCost(h);
        Queue<Node> frontier = new PriorityQueue<>(); //todo chequear q este bien el compareto
        frontier.add(startNode);
        Set<Node> explored = new HashSet<>();

        while (!frontier.isEmpty()) {
            Node currentNode = frontier.poll(); //devuelve el nodo con menor costo por ser una priority queue
            if (currentNode.isGoal()) {
                moves.add(currentNode);
                this.cost = currentNode.getTotalCost();
                this.frontierQ = frontier.size();
                this.exploredQ = explored.size();

                long endTime = System.nanoTime();
                this.time = (endTime - startTime)/1000000;
                return true;
            }
            explored.add(currentNode);
            currentNode.generateWeightedOutcomes();
            for(Node childNode : currentNode.getOutcomes()){
                System.out.println(childNode.getBoard().toString());
                h = heuristic(childNode.getBoard());
                childNode.setTotalCost(h);
                if(!explored.contains(childNode) && !frontier.contains(childNode)){
                    frontier.add(childNode);
                } else if(frontier.contains(childNode)){
                    for(Node n : frontier) {
                        if(childNode.equals(n)) {
                            if(childNode.getTotalCost() < n.getTotalCost() ){
                                frontier.remove(n);
                                frontier.add(childNode);
                                break;
                            }
                        }
                    }
                } else if(explored.contains(childNode)) {
                    for(Node n : explored) {
                        if(childNode.equals(n)) {
                            if(childNode.getTotalCost() < n.getTotalCost()){
                                frontier.add(childNode);
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

        if(node.isGoal()){
            long endTime = System.nanoTime();
            this.time = (endTime - startTime)/1000000;

            this.frontierQ = frontier.size();
            this.exploredQ = explored.size();
            moves.add(node);
            return true;
        }

        while(!frontier.empty()){
            node = frontier.pop();
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