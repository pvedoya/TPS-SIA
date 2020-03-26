package com.company;

public enum SquareType {
    WALL('#'),BOX('$'),BALL('@'),TILE(' '),GOAL('.'),BOXGOAL('*'),BALLGOAL('+');

    private char icon;

    private SquareType(char icon){
        this.icon = icon;
    }

    public char getIcon(){
        return this.icon;
    }
}