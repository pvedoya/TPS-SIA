package com.company;

import java.util.*;

public class  Node implements Comparable<Node> {
    private Board board;
    private String direction;
    private List<Node> outcomes;
    private String stringBoard;
    private Node parent;
    private int pathCost;
    private double totalCost;

    public Node(Board board, String direction, Node parent){
        this.board = board.cloneBoard();
        this.direction = direction;
        this.outcomes = new ArrayList<>();
        this.stringBoard = board.stringifyBoard();
        this.parent = parent;
    }

    public Node(Board board, String direction, Node parent, int pathCost){
        this.board = board.cloneBoard();
        this.outcomes = new ArrayList<>();
        this.parent = parent;
        this.pathCost = pathCost;
        this.totalCost = 0;
        this.direction = direction;
    }

    /*
    * Crea los nodos hijos con los movimientos posibles desde el tablero actual, creando nuevas instancias
    * del tablero y aplicando el movimiento, si el tablero se bloquea(no se puede mover ninguna caja), se ingora el hijo
    * */

    public void generateOutcomes(){
        Board aux = this.board.cloneBoard();
        if(aux.makeMove("UP") && !aux.hasBlocked()){
            outcomes.add(new Node(aux,"UP", this));
        }
        aux = this.board.cloneBoard();
        if(aux.makeMove("DOWN") && !aux.hasBlocked()){
            outcomes.add(new Node(aux,"DOWN", this));
        }
        aux = this.board.cloneBoard();
        if(aux.makeMove("LEFT") && !aux.hasBlocked()){
            outcomes.add(new Node(aux,"LEFT", this));
        }
        aux = this.board.cloneBoard();
        if(aux.makeMove("RIGHT") && !aux.hasBlocked()){
            outcomes.add(new Node(aux,"RIGHT",this));

        }
    }

    public void generateWeightedOutcomes(){
        Board aux = this.board.cloneBoard();
        if(aux.makeMove("UP") && !aux.hasBlocked()){
            outcomes.add(new Node(aux,"UP", this, this.pathCost + 1));
        }
        aux = this.board.cloneBoard();
        if(aux.makeMove("DOWN") && !aux.hasBlocked()){
            outcomes.add(new Node(aux,"DOWN", this, this.pathCost + 1));
        }
        aux = this.board.cloneBoard();
        if(aux.makeMove("LEFT") && !aux.hasBlocked()){
            outcomes.add(new Node(aux,"LEFT", this, this.pathCost + 1));
        }
        aux = this.board.cloneBoard();
        if(aux.makeMove("RIGHT") && !aux.hasBlocked()){
            outcomes.add(new Node(aux,"RIGHT",this, this.pathCost + 1));

        }
    }

    public boolean isGoal(){
        if(this.board.hasWon()){
            return true;
        }
        return false;
    }

    //Getters & Setters

    public Board getBoard(){
        return this.board;
    }

    public List<Node> getOutcomes(){
        return outcomes;
    }

    public String getDirection() {
        return direction;
    }

    public String getStringBoard() {
        return stringBoard;
    }

    public Node getParent() {
        return parent;
    }

    //Utils

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return  Objects.equals(stringBoard, node.stringBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash( stringBoard);
    }

    @Override
    public String toString() {
        return this.board.toString();
    }


    @Override
    public int compareTo(Node node) {
        Double d1 = totalCost;
        Double d2 = node.totalCost;
        return d1.compareTo(d2);
    }

    public int getPathCost() {
        return pathCost;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}