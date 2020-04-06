package com.company;

import java.util.*;

public class  Node implements Comparable<Node> {
    private Board board;

    private String direction;
    private List<Node> outcomes;
//    private String stringBoard;
    private Node parent;
    private int pathCost;
    private double totalCost;

    public Node(Board board, String direction, Node parent){
        this.board = board.cloneBoard();
        this.direction = direction;
        this.outcomes = new ArrayList<>();
        this.parent = parent;
    }

    public Node(Board board, String direction, Node parent, int pathCost){
        this.board = board;
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
        if(aux.makeMove("UP")){
            outcomes.add(new Node(aux,"U", this));
        }
        Board aux2 = this.board.cloneBoard();
        if(aux2.makeMove("LEFT")){
            outcomes.add(new Node(aux2,"L", this));
        }
        Board aux3 = this.board.cloneBoard();
        if(aux3.makeMove("DOWN")){
            outcomes.add(new Node(aux3,"D", this));
        }
        Board aux4 = this.board.cloneBoard();
        if(aux4.makeMove("RIGHT")){
            outcomes.add(new Node(aux4,"R",this));

        }
    }

    public void generateWeightedOutcomes(){
        Board aux1 = this.board.cloneBoard();
        if(aux1.makeMove("UP")){
            outcomes.add(new Node(aux1,"U", this, this.pathCost + 1));
        }
        Board aux2 = this.board.cloneBoard();
        if(aux2.makeMove("DOWN")){
            outcomes.add(new Node(aux2,"D", this, this.pathCost + 1));
        }
        Board aux3 = this.board.cloneBoard();
        if(aux3.makeMove("LEFT")){
            outcomes.add(new Node(aux3,"L", this, this.pathCost + 1));
        }
        Board aux4 = this.board.cloneBoard();
        if(aux4.makeMove("RIGHT")){
            outcomes.add(new Node(aux4,"R",this, this.pathCost + 1));

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
        return board.stringifyBoard();
    }

//    public String getStringBoard(){
//        return stringBoard;
//    }

    public Node getParent() {
        return parent;
    }

    //Utils

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;

        return Objects.equals(board, node.board);
    }


//    @Override
//    public int hashCode() {
//        return Objects.hash( stringBoard);
//    }


    @Override
    public int hashCode() {
        return Objects.hash(board, direction, outcomes, parent);
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