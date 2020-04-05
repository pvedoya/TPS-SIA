package com.company;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

public class  Node {
    private Board board;
    private String direction;
    private HashSet<Node> outcomes;
    private String stringBoard;
    private Node parent;

    public Node(Board board, String direction, Node parent){
        this.board = board.cloneBoard();
        this.direction = direction;
        this.outcomes = new HashSet<>();
        stringBoard = board.stringifyBoard();
        this.parent = parent;
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

    public HashSet<Node> getOutcomes(){
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
        return  Objects.equals(direction, node.direction) &&
                Objects.equals(stringBoard, node.stringBoard);
    }

    @Override
    public String toString() {
        return this.board.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, direction, outcomes, stringBoard);
    }

}