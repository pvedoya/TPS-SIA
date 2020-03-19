package com.company;

import java.util.Objects;

public class Square {
    private SquareType type;
    private boolean blocked;
    private int i;
    private int j;


    public Square(int i, int j, boolean blocked, SquareType type){
        this.i = i;
        this.j = j;
        this.blocked = ( type == SquareType.WALL || type == SquareType.VOID)? true : false;
        this.type = type;
    }

    public SquareType getType() {
        return type;
    }

    public boolean isBlocked() {
        return blocked;
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

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return blocked == square.blocked &&
                i == square.i &&
                j == square.j &&
                type == square.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, blocked, i, j);
    }
}
