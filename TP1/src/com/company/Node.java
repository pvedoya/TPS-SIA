package com.company;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Node {
    private Board board;
    private String direction;
    private LinkedList<Node> outcomes;
    private String stringBoard;

    public Node(Board board, String direction){
        this.board = board.cloneBoard();
        this.direction = direction;
        this.outcomes = new LinkedList<>();
        stringBoard = board.stringifyBoard();
    }

    public void generateOutcomes(){
        Board aux = this.board.cloneBoard();
        if(aux.makeMove("UP") && !aux.hasBlocked()){
            outcomes.add(new Node(aux,"UP"));
        }
        aux = this.board.cloneBoard();
        if(aux.makeMove("DOWN")&& !aux.hasBlocked()){
            outcomes.add(new Node(aux,"DOWN"));
        }
        aux = this.board.cloneBoard();
        if(aux.makeMove("LEFT")&& !aux.hasBlocked()){
            outcomes.add(new Node(aux,"LEFT"));
        }
        aux = this.board.cloneBoard();
        if(aux.makeMove("RIGHT")&& !aux.hasBlocked()){
            outcomes.add(new Node(aux,"RIGHT"));
        }
    }

    public boolean isGoal(){
        if(this.board.hasWon()){
            return true;
        }
        return false;
    }

    public Board getBoard(){
        return this.board;
    }

    public LinkedList<Node> getOutcomes(){
        return outcomes;
    }

    public String getDirection() {
        return direction;
    }

    public String getStringBoard() {
        return stringBoard;
    }
}
