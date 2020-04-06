package com.company;

import java.util.*;

/*
 * La clase board guarda una instancia del tablero en matriz de char, para tener una rapida lectura e impresion, guarda las dimensiones del tablero
 * la ubicacion de la pelota y de los objetivos
 * */

public class Board implements Cloneable{
    private int width;
    private int height;

    private char[][] board;

    private int ballX;
    private int ballY;

    private ArrayList<Integer[]> goals;
    private ArrayList<Integer[]> boxes;

    public Board(int width, int height, char[][] board) {
        this.width = width;
        this.height = height;
        this.board = board;
        this.goals = new ArrayList<>();
        this.boxes = new ArrayList<>();
    }

    public Board(){

    }

    //Getters & Setters

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char[][] getBoard(){
        return board;
    }

    public ArrayList<Integer[]> getGoals() {
        return goals;
    }

    public void setBall(int x, int y){
        this.ballX = x;
        this.ballY = y;
    }

    /*
     * Metodo que llama a los metodos para detectar si las piezas y las paredes son correctas, en caso de no ser correctas corta la ejecucion del programa
     * */

    public void validate(){
        boolean valid = true;

        if(!validWalls()){
            valid = false;
        }
        if(!validPieces()){
            valid = false;
        }

        if(!valid){
            System.out.println("Wrong Board Format Detected");
            System.exit(0);
        }else{
            System.out.println("Correct board format detected, starting to find a solution...");
        }
    }

    /*
     * Medodo que asegura que haya la misma cantidad de objetivos que cajas, y que solo haya una pelota, cambia los iconos que no sean pared,
     * ademas se encarga de agregar las coordenadas de las cajas al set correspondiente, y marca las coordenadas de la pelota
     * */

    private boolean validPieces(){
        int goalCounter = 0;
        int boxCounter = 0;
        int ballCounter = 0;


        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width;j++){
                if(this.board[i][j] == SquareType.BALL.getIcon() ){
                    ballCounter++;
                    this.ballX = i;
                    this.ballY = j;
                    this.board[i][j] = ' ';
                }else if(this.board[i][j] == SquareType.BALLGOAL.getIcon()){
                    this.ballX = i;
                    this.ballY = j;
                    this.board[i][j] = ' ';
                    Integer[] aux = {i, j};
                    this.goals.add(aux);
                    ballCounter++;
                    goalCounter++;
                }else if(this.board[i][j] == SquareType.BOX.getIcon()){
                    boxCounter++;
                    this.board[i][j] = ' ';
                    Integer[] aux = {i, j};
                    this.boxes.add(aux);
                }else if(this.board[i][j] == SquareType.GOAL.getIcon() ){
                    Integer[] aux = {i, j};
                    this.goals.add(aux);
                    goalCounter++;
                    this.board[i][j] = ' ';
                }else if(this.board[i][j] == SquareType.BOXGOAL.getIcon()){
                    this.board[i][j] = ' ';
                    Integer[] aux = {i, j};
                    this.goals.add(aux);
                    this.boxes.add(aux);
                    goalCounter++;
                    boxCounter++;
                }
            }
        }
        if(goalCounter != boxCounter || ballCounter != 1){
            return false;
        }
        return true;
    }

    /*
     * Utiliza DFS para asegurar que existe una pared que rodea al tablero, arrancando del primer segmento que encuentra,
     * verificando para arriba, abajo, izquierda y derecha si hay segmentos continuos, hasta volver al origen
     * */

    private boolean validWalls(){
        Set<Integer[]> visited = new TreeSet<>((array1, array2) -> Math.abs(Integer.compare(array1[0], array2[0])) + Math.abs(Integer.compare(array1[1], array2[1])));
        Stack<Integer[]> stack = new Stack<>();

        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                if(this.board[i][j] == SquareType.WALL.getIcon() && stack.empty() ){
                    Integer[] first= {i, j};
                    stack.push(first);
                }
            }
        }

        Integer[] first = stack.pop();
        Integer[] prev = first.clone();
        if(first == null){
            return false;
        }

        Integer[] current = first.clone();
        int minI, minJ, maxI, maxJ;

        minI = Math.max(0, current[0]-1);
        minJ = Math.max(0, current[1]-1);
        maxI = Math.min(this.height-1, current[0]+1);
        maxJ = Math.min(this.width-1, current[1]+1);

        visited.add(current);

        if(current[0] - 1 >= minI){
            if(this.board[current[0]-1][current[1]] == SquareType.WALL.getIcon()){
                Integer[] aux = {current[0]-1, current[1]};
                stack.push(aux);
            }
        }else if(current[0] + 1 <= maxI) {
            if (this.board[current[0] + 1][current[1]] == SquareType.WALL.getIcon()) {
                Integer[] aux = {current[0] + 1, current[1]};
                stack.push(aux);
            }
        }else if(current[1] - 1 >= minJ){
            if(this.board[current[0]][current[1]-1] == SquareType.WALL.getIcon()){
                Integer[] aux = {current[0], current[1]-1};
                stack.push(aux);
            }
        }else if(current[1] + 1 <= maxJ){
            if(this.board[current[0]][current[1]+1] == SquareType.WALL.getIcon()){
                Integer[] aux = {current[0], current[1]+1};
                stack.push(aux);
            }
        }

        while(!stack.empty()){

            current = stack.pop();
            visited.add(current);

            minI = Math.max(0, current[0]-1);
            minJ = Math.max(0, current[1]-1);
            maxI = Math.min(this.height-1, current[0]+1);
            maxJ = Math.min(this.width-1, current[1]+1);


            if(current[0] - 1 >= minI){
                if(this.board[current[0]-1][current[1]] == SquareType.WALL.getIcon()){
                    Integer[] aux = {current[0]-1, current[1]};
                    if(Arrays.equals(aux,first) && !Arrays.equals(first, prev)){
                        return true;
                    }
                    if(!visited.contains(aux)){
                        stack.push(aux);
                    }
                }
            }
            if(current[0] + 1 <= maxI){
                if(this.board[current[0]+1][current[1]] == SquareType.WALL.getIcon()){
                    Integer[] aux = {current[0]+1, current[1]};
                    if(Arrays.equals(aux,first) && !Arrays.equals(first, prev)){
                        return true;
                    }
                    if(!visited.contains(aux)){
                        stack.push(aux);
                    }
                }
            }
            if(current[1] - 1 >= minJ){
                if(this.board[current[0]][current[1]-1] == SquareType.WALL.getIcon()){
                    Integer[] aux = {current[0], current[1]-1};
                    if(Arrays.equals(aux,first) && !Arrays.equals(first, prev)){
                        return true;
                    }
                    if(!visited.contains(aux)){
                        stack.push(aux);
                    }
                }
            }
            if(current[1] + 1 <= maxJ){
                if(this.board[current[0]][current[1]+1] == SquareType.WALL.getIcon()){
                    Integer[] aux = {current[0], current[1]+1};
                    if(Arrays.equals(aux,first) && !Arrays.equals(first, prev)){
                        return true;
                    }
                    if(!visited.contains(aux)){
                        stack.push(aux);
                    }
                }
            }
            prev = current;

        }
        return false;
    }

    /*
     * Agrega los BOXGOALS y BALLGOALS al tablero, reemplazandolos donde corresponde, metodo hecho para preparar el tablero para mostrar al usuario
     * */

    public char[][] fullBoard(){
        char[][] auxBoard = new char[this.height][this.width];

        for (int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++) {
                auxBoard[i][j] = this.board[i][j];
            }
        }
        auxBoard[this.ballX][this.ballY] = SquareType.BALL.getIcon();

        for(Integer[] i : this.boxes){
            auxBoard[i[0]][i[1]] = SquareType.BOX.getIcon();
        }
        for(Integer[] i : this.goals){
            if(auxBoard[i[0]][i[1]] == SquareType.BOX.getIcon()){
                auxBoard[i[0]][i[1]] = SquareType.BOXGOAL.getIcon();
            }else if (auxBoard[i[0]][i[1]] == SquareType.BALL.getIcon()){
                auxBoard[i[0]][i[1]] = SquareType.BALLGOAL.getIcon();
            }else{
                auxBoard[i[0]][i[1]] = SquareType.GOAL.getIcon();
            }
        }
        return auxBoard;
    }

    /*
     * Metodo que verifica que todas las cajas hayan encontrado un objetivo
     * */

    public boolean hasWon(){
        int freeBoxes = 0;
        for (Integer[] i : this.boxes){
            freeBoxes++;
            for(Integer[] j : this.goals){
                if(j[0] == i[0] && j[1] == i[1]){
                    freeBoxes--;
                }
            }
        }
        return freeBoxes == 0;
    }

    public Board cloneBoard() {
        Board clone = new Board();

        clone.board = this.board;
        clone.width = this.width;
        clone.height = this.height;
        clone.goals = this.goals;
        clone.boxes = new ArrayList<>();
        for(Integer[] i : this.boxes){
            Integer[] aux = {i[0], i[1]};
            clone.boxes.add(aux);
        }
        clone.setBall(this.ballX,this.ballY);
        return clone;
    }

    private boolean isGoal(int i, int j){
        for (Integer[] pair: this.goals) {
            if(pair[0] == i && pair[1] == j){
                return true;
            }
        }
        return false;
    }

    /*
     * Metodo que verifica que haya por lo menos una caja libre, buscando caja por caja y fijandose que no este en una esquina, una esquina es la situacion
     * donde para dos direcciones continuas a una caja(ARRIBA y DERECHA, ARRIBA e IZQUIERDA, IZQUIERDA y ABAJO, ABAJO y DERECHA) hay un objeto no movible,
     * que puede ser una pared, o una caja, ya que una caja no puede empujar a otra.
     * */

    public boolean hasBlocked() {
        char[][] aux = fullBoard();

        int blocked = 0;
        for(int i = 0; i < this.height; i++){
            for(int j = 0;j < this.width; j++){
                if(aux[i][j] == SquareType.BOX.getIcon() && !isGoal(i,j)){
                    if((aux[i-1][j] == SquareType.WALL.getIcon() || aux[i-1][j] == SquareType.BOX.getIcon()) && (aux[i][j-1] == SquareType.WALL.getIcon() || aux[i][j-1] == SquareType.BOX.getIcon())){
                        blocked++;
                    }else if((aux[i-1][j] == SquareType.WALL.getIcon() || aux[i-1][j] == SquareType.BOX.getIcon()) && aux[i][j+1] == SquareType.WALL.getIcon() || aux[i][j+1] == SquareType.BOX.getIcon()){
                        blocked++;
                    }else if((aux[i+1][j] == SquareType.WALL.getIcon() || aux[i+1][j] == SquareType.BOX.getIcon()) && (aux[i][j+1] == SquareType.WALL.getIcon() || aux[i][j+1] == SquareType.BOX.getIcon())){
                        blocked++;
                    }else if((aux[i+1][j] == SquareType.WALL.getIcon() || aux[i+1][j] == SquareType.BOX.getIcon()) && (aux[i][j-1] == SquareType.WALL.getIcon() | aux[i][j-1] == SquareType.BOX.getIcon())){
                        blocked++;
                    }
                }
            }
        }
        return blocked == this.goals.size();
    }

    /*
     * Metodo que recibe una direccion, y verifica que desde la actual ubicacion de la bola se pueda hacer la jugada, si se puede hacer se realiza.
     * Para la verificacion, evalua si puede moverse al casillero contiguo, y si hay una caja se fija si puede moverla al espacio siguiente. Una vez que realiza
     * el movimiento, actualiza los casilleros utilizados con las figuras correspondientes y retorna verdadero.
     * */

    public boolean makeMove(String direction){
        char[][] auxBoard = fullBoard();

        if(direction.equals("UP")){
            if(this.ballX > 0 && !(auxBoard[this.ballX-1][this.ballY] == SquareType.WALL.getIcon())){
                if(auxBoard[this.ballX-1][this.ballY] == SquareType.BOX.getIcon() || auxBoard[this.ballX-1][this.ballY] == SquareType.BOXGOAL.getIcon()){
                    if(auxBoard[this.ballX-2][this.ballY] == SquareType.BOX.getIcon() || auxBoard[this.ballX-2][this.ballY] == SquareType.BOXGOAL.getIcon() || auxBoard[this.ballX-2][this.ballY] == SquareType.WALL.getIcon()){
                        return false;
                    }else if(auxBoard[this.ballX-2][this.ballY] == SquareType.TILE.getIcon() || auxBoard[this.ballX-2][this.ballY] == SquareType.GOAL.getIcon() ){
                        Integer[] aux = null;
                        for(Integer[] i : this.boxes){
                            if(i[0] == this.ballX-1 && i[1] == this.ballY){
                                aux = i;
                            }
                        }
                        aux[0] = this.ballX-2;
                    }
                }
                this.ballX = this.ballX - 1;
                return true;
            }
        }else if(direction.equals("DOWN")){
            if(this.ballX < this.height+1 && !(auxBoard[this.ballX+1][this.ballY] == SquareType.WALL.getIcon())) {
                if (auxBoard[this.ballX + 1][this.ballY] == SquareType.BOX.getIcon() || auxBoard[this.ballX+1][this.ballY] == SquareType.BOXGOAL.getIcon()) {
                    if (auxBoard[this.ballX + 2][this.ballY] == SquareType.BOX.getIcon() || auxBoard[this.ballX + 2][this.ballY] == SquareType.WALL.getIcon() || auxBoard[this.ballX+2][this.ballY] == SquareType.BOXGOAL.getIcon()) {
                        return false;
                    } else if (auxBoard[this.ballX + 2][this.ballY] == SquareType.TILE.getIcon() || auxBoard[this.ballX + 2][this.ballY] == SquareType.GOAL.getIcon()) {
                        Integer[] aux = null;
                        for (Integer[] i : this.boxes) {
                            if (i[0] == this.ballX + 1 && i[1] == this.ballY) {
                                aux = i;
                            }
                        }
                        aux[0] = this.ballX + 2;
                    }
                }
                this.ballX = this.ballX + 1;
                return true;
            }
        }else if(direction.equals("LEFT")){
            if(this.ballY > 0 && !(auxBoard[this.ballX][this.ballY-1] == SquareType.WALL.getIcon())){
                if(auxBoard[this.ballX][this.ballY-1] == SquareType.BOX.getIcon() || auxBoard[this.ballX][this.ballY-1] == SquareType.BOXGOAL.getIcon()){
                    if(auxBoard[this.ballX][this.ballY-2] == SquareType.BOX.getIcon() || auxBoard[this.ballX][this.ballY-2] == SquareType.WALL.getIcon() || auxBoard[this.ballX][this.ballY-2] == SquareType.BOXGOAL.getIcon()){
                        return false;
                    }else if(auxBoard[this.ballX][this.ballY-2] == SquareType.TILE.getIcon() || auxBoard[this.ballX][this.ballY-2] == SquareType.GOAL.getIcon()){
                        auxBoard[this.ballX][this.ballY-2] = SquareType.BOX.getIcon();
                        Integer[] aux = null;
                        for(Integer[] i : this.boxes){
                            if(i[1] == this.ballY-1 && i[0] == this.ballX){
                                aux = i;
                            }
                        }
                        aux[1] = this.ballY-2;
                    }
                }
                this.ballY = this.ballY - 1;
                return true;
            }
        }else if(direction.equals("RIGHT")){
            if(this.ballY < this.width-1 && !(auxBoard[this.ballX][this.ballY+1] == SquareType.WALL.getIcon())){
                if(auxBoard[this.ballX][this.ballY+1] == SquareType.BOX.getIcon() || auxBoard[this.ballX][this.ballY+1] == SquareType.BOXGOAL.getIcon()){
                    if(auxBoard[this.ballX][this.ballY+2] == SquareType.BOX.getIcon() || auxBoard[this.ballX][this.ballY+2] == SquareType.WALL.getIcon() || auxBoard[this.ballX][this.ballY+2] == SquareType.BOXGOAL.getIcon()){
                        return false;
                    }else if(auxBoard[this.ballX][this.ballY+2] == SquareType.TILE.getIcon() || auxBoard[this.ballX][this.ballY+2] == SquareType.GOAL.getIcon()){
                        auxBoard[this.ballX][this.ballY+2] = SquareType.BOX.getIcon();
                        Integer[] aux = null;
                        for(Integer[] i : this.boxes){
                            if(i[1] == this.ballY+1 && i[0] == this.ballX){
                                aux = i;
                            }
                        }
                        aux[1] = this.ballY+2;
                    }
                }
                this.ballY = this.ballY + 1;
                return true;
            }
        }else{
            System.out.println("Invalid movement argument");
            return false;
        }
        return false;

    }

    /*
     * Retorna un String conteniendo todas las piezas del tablero en su orden correspondiente, esto es una representacion del estado en el cual esta el tablero,
     * ya que al mover una pieza cambia este String
     * */


    public String stringifyBoard(){
        String ret = "";
        char[][] auxBoard = fullBoard();

        for(int i = 0; i < this.height ; i++){
            for(int j =0 ; j < this.width; j++){
                ret += auxBoard[i][j];
            }
        }
        return ret;
    }



    //Utils

    @Override
    public String toString(){
        String ret = "";

        for(int i = 0; i< this.height ; i++){
            for(int j =0 ; j < this.width; j++){
                ret += this.board[i][j];
            }
            ret+="\n";
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;

        boolean equalBoards = true;
        char[][] fullBoard = fullBoard();
        char[][] fullBoard1 = board1.fullBoard();

        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                if(fullBoard[i][j] != fullBoard1[i][j]){
                    equalBoards = false;
                }
            }
        }

        return  equalBoards;

    }

    @Override
    public int hashCode() {
        int result = Objects.hash(width, height, ballX, ballY, goals, boxes);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
