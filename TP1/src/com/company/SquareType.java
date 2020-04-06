package com.company;

public enum SquareType {
    WALL('#'),BOX('$'),BALL('@'),TILE(' '),GOAL('.'),BOXGOAL('*'),BALLGOAL('+');
    /*

    * Enum que contiene las distintas piezas de el tablero, utilizando como referencia los tableros del sitio
    * que se encuentra en el enunciado del TP. "BOXGOAL" y "BALLGOAL" representan, respectivamente, cajas y la
     pelota cuando estan sobre el objetivo.
    *
    * */
    private char icon;

    private SquareType(char icon){
        this.icon = icon;
    }

    public char getIcon(){
        return this.icon;
    }
}