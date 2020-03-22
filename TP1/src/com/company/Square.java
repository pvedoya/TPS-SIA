package com.company;

import java.util.Objects;

public class Square {
    private SquareType type;
    private boolean isGoal;
    private int i;
    private int j;


    public Square(int i, int j, boolean blocked, SquareType type){
        this.i = i;
        this.j = j;
        this.isGoal = false;
        this.type = type;
    }

    public boolean isGoal(){
        return this.isGoal;
    }

    public void setGoal(){
        this.isGoal = true;
    }

    public SquareType getType() {
        return type;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void setType(SquareType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return i == square.i &&
                j == square.j &&
                type == square.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type,i, j);
    }
}
