package com.company;

public enum SquareType {
    VOID(' '), WALL('#'), BOX('$'), BALL('@'), TILE(' '), GOAL('.');

    private char icon;

    private SquareType(char icon){
        this.icon = icon;
    }

    public char getIcon() {
        return icon;
    }
}
