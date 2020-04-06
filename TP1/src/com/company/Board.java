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

    public Board(int width, int height, char[][] board) {
        this.width = width;
        this.height = height;
        this.board = board;
        this.goals = new ArrayList<>();
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

    public void setBall(int x, int y){
        this.ballX = x;
        this.ballY = y;
    }

    public ArrayList<Integer[]> getGoals () {
        return goals;
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
    * Medodo que asegura que haya la misma cantidad de objetivos que cajas, y que solo haya una pelota, cambia '*' y '+' por '$' y '@'
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
                }else if(this.board[i][j] == SquareType.BALLGOAL.getIcon()){
                    this.ballX = i;
                    this.ballY = j;
                    this.board[i][j] = '@';
                    Integer[] aux = {i, j};
                    this.goals.add(aux);
                    ballCounter++;
                    goalCounter++;
                }else if(this.board[i][j] == SquareType.BOX.getIcon()){
                    boxCounter++;
                }else if(this.board[i][j] == SquareType.GOAL.getIcon() ){
                    Integer[] aux = {i, j};
                    this.goals.add(aux);
                    goalCounter++;
                }else if(this.board[i][j] == SquareType.BOXGOAL.getIcon()){
                    this.board[i][j] = '$';
                    Integer[] aux = {i, j};
                    this.goals.add(aux);
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

    public void fullboard(){
        for(Integer[] i : this.goals){
            if(board[i[0]][i[1]] == SquareType.BOX.getIcon()){
                board[i[0]][i[1]] = SquareType.BOXGOAL.getIcon();
            }else if(board[i[0]][i[1]] == SquareType.BALL.getIcon()){
                board[i[0]][i[1]] = SquareType.BALLGOAL.getIcon();
            }
        }
    }

    /*
    * Metodo que verifica que todas las cajas hayan encontrado un objetivo
    * */

    public boolean hasWon(){
        int freeBoxes = 0;
        for(int i = 0; i < this.height; i++){
            for(int j = 0;j < this.width; j++){

                if(this.board[i][j] == SquareType.BOX.getIcon()){
                    for (Integer[] pair:this.goals) {
                        if(pair[0] == i && pair[1] == j){
                            freeBoxes--;
                        }
                    }
                    freeBoxes++;
                }
            }
        }
        return freeBoxes == 0;
    }

    public void printBoard() {
        for (int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                System.out.print(board[i][j]);
            }
            System.out.print('\n');
        }
    }

    public Board cloneBoard() {
        Board clone = new Board(this.width, this.height, new char[this.height][this.width]);

        clone.goals = this.goals;

        for(int i = 0; i < this.height; i++){
            for( int j = 0; j < this.width; j++){
                clone.board[i][j] = this.board[i][j];
            }
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
        int blocked = 0;
        for(int i = 0; i < this.height; i++){
            for(int j = 0;j < this.width; j++){
                if(this.board[i][j] == SquareType.BOX.getIcon() && !isGoal(i,j)){
                    if((this.board[i-1][j] == SquareType.WALL.getIcon() || this.board[i-1][j] == SquareType.BOX.getIcon()) && (this.board[i][j-1] == SquareType.WALL.getIcon() || this.board[i][j-1] == SquareType.BOX.getIcon())){
                        blocked++;
                    }else if((this.board[i-1][j] == SquareType.WALL.getIcon() || this.board[i-1][j] == SquareType.BOX.getIcon()) && this.board[i][j+1] == SquareType.WALL.getIcon() || this.board[i][j+1] == SquareType.BOX.getIcon()){
                        blocked++;
                    }else if((this.board[i+1][j] == SquareType.WALL.getIcon() || this.board[i+1][j] == SquareType.BOX.getIcon()) && (this.board[i][j+1] == SquareType.WALL.getIcon() || this.board[i][j+1] == SquareType.BOX.getIcon())){
                        blocked++;
                    }else if((this.board[i+1][j] == SquareType.WALL.getIcon() || this.board[i+1][j] == SquareType.BOX.getIcon()) && (this.board[i][j-1] == SquareType.WALL.getIcon() | this.board[i][j-1] == SquareType.BOX.getIcon())){
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
        if(direction.equals("UP")){
            if(this.ballX > 0 && !(this.board[this.ballX-1][this.ballY] == SquareType.WALL.getIcon())){
                if(this.board[this.ballX-1][this.ballY] == SquareType.GOAL.getIcon() || this.board[this.ballX-1][this.ballY] == SquareType.TILE.getIcon()){
                    this.board[this.ballX-1][this.ballY] = SquareType.BALL.getIcon();
                }else if(this.board[this.ballX-1][this.ballY] == SquareType.BOX.getIcon()){
                    if(this.board[this.ballX-2][this.ballY] == SquareType.BOX.getIcon() || this.board[this.ballX-2][this.ballY] == SquareType.WALL.getIcon()){
                        return false;
                    }else if(this.board[this.ballX-2][this.ballY] == SquareType.TILE.getIcon() || this.board[this.ballX-2][this.ballY] == SquareType.GOAL.getIcon()){
                        this.board[this.ballX-1][this.ballY] = SquareType.BALL.getIcon();
                        this.board[this.ballX-2][this.ballY] = SquareType.BOX.getIcon();
                    }
                }
                this.board[this.ballX][this.ballY] = SquareType.TILE.getIcon();
                for (Integer[] pair: this.goals) {
                    if(pair[0] == this.ballX && pair[1] == this.ballY){
                        this.board[this.ballX][this.ballY] = SquareType.GOAL.getIcon();
                    }
                }
                this.ballX = this.ballX - 1;
                return true;
            }
        }else if(direction.equals("DOWN")){
            if(this.ballX < this.height-1 && !(this.board[this.ballX+1][this.ballY] == SquareType.WALL.getIcon())){
                if(this.board[this.ballX+1][this.ballY] == SquareType.GOAL.getIcon() || this.board[this.ballX+1][this.ballY] == SquareType.TILE.getIcon()){
                    this.board[this.ballX+1][this.ballY] = SquareType.BALL.getIcon();
                }else if(this.board[this.ballX+1][this.ballY] == SquareType.BOX.getIcon()){
                    if(this.board[this.ballX+2][this.ballY] == SquareType.BOX.getIcon() || this.board[this.ballX+2][this.ballY] == SquareType.WALL.getIcon()){
                        return false;
                    }else if(this.board[this.ballX+2][this.ballY] == SquareType.TILE.getIcon() || this.board[this.ballX+2][this.ballY] == SquareType.GOAL.getIcon()){
                        this.board[this.ballX+1][this.ballY] = SquareType.BALL.getIcon();
                        this.board[this.ballX+2][this.ballY] = SquareType.BOX.getIcon();
                    }
                }
                this.board[this.ballX][this.ballY] = SquareType.TILE.getIcon();
                for (Integer[] pair: this.goals) {
                    if(pair[0] == this.ballX && pair[1] == this.ballY){
                        this.board[this.ballX][this.ballY] = SquareType.GOAL.getIcon();
                    }
                }
                this.ballX = this.ballX + 1;
                return true;
            }
        }else if(direction.equals("LEFT")){
            if(this.ballY > 0 && !(this.board[this.ballX][this.ballY-1] == SquareType.WALL.getIcon())){
                if(this.board[this.ballX][this.ballY-1] == SquareType.GOAL.getIcon() || this.board[this.ballX][this.ballY-1] == SquareType.TILE.getIcon()){
                    this.board[this.ballX][this.ballY-1] = SquareType.BALL.getIcon();
                }else if(this.board[this.ballX][this.ballY-1] == SquareType.BOX.getIcon()){
                    if(this.board[this.ballX][this.ballY-2] == SquareType.BOX.getIcon() || this.board[this.ballX][this.ballY-2] == SquareType.WALL.getIcon()){
                        return false;
                    }else if(this.board[this.ballX][this.ballY-2] == SquareType.TILE.getIcon() || this.board[this.ballX][this.ballY-2] == SquareType.GOAL.getIcon()){
                        this.board[this.ballX][this.ballY-1] = SquareType.BALL.getIcon();
                        this.board[this.ballX][this.ballY-2] = SquareType.BOX.getIcon();
                    }
                }
                this.board[this.ballX][this.ballY] = SquareType.TILE.getIcon();
                for (Integer[] pair: this.goals) {
                    if(pair[0] == this.ballX && pair[1] == this.ballY){
                        this.board[this.ballX][this.ballY] = SquareType.GOAL.getIcon();
                    }
                }
                this.ballY = this.ballY - 1;
                return true;
            }
        }else if(direction.equals("RIGHT")){
            if(this.ballY < this.width-1 && !(this.board[this.ballX][this.ballY+1] == SquareType.WALL.getIcon())){
                if(this.board[this.ballX][this.ballY+1] == SquareType.GOAL.getIcon() || this.board[this.ballX][this.ballY+1] == SquareType.TILE.getIcon()){
                    this.board[this.ballX][this.ballY+1] = SquareType.BALL.getIcon();
                }else if(this.board[this.ballX][this.ballY+1] == SquareType.BOX.getIcon()){
                    if(this.board[this.ballX][this.ballY+2] == SquareType.BOX.getIcon() || this.board[this.ballX][this.ballY+2] == SquareType.WALL.getIcon()){
                        return false;
                    }else if(this.board[this.ballX][this.ballY+2] == SquareType.TILE.getIcon() || this.board[this.ballX][this.ballY+2] == SquareType.GOAL.getIcon()){
                        this.board[this.ballX][this.ballY+1] = SquareType.BALL.getIcon();
                        this.board[this.ballX][this.ballY+2] = SquareType.BOX.getIcon();
                    }
                }
                this.board[this.ballX][this.ballY] = SquareType.TILE.getIcon();
                for (Integer[] pair: this.goals) {
                    if(pair[0] == this.ballX && pair[1] == this.ballY){
                        this.board[this.ballX][this.ballY] = SquareType.GOAL.getIcon();
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

        for(int i = 0; i< this.height ; i++){
            for(int j =0 ; j < this.width; j++){
                ret += this.board[i][j];
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

        boolean equalGoals = true;
        if(goals.size() != board1.goals.size()){
            equalGoals = false;
        }else{
            int found = 0;
            for(Integer[] i : this.goals){
                for(Integer[] j : board1.goals){
                    if(i[0] == j[0] && i[1] == j[1]){
                        found++;
                    }
                }
            }
            if(found != this.goals.size()){
                equalGoals = false;
            }
        }

        boolean equalBoards = true;
        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.height; j++){
                if(this.board[i][j] != board1.board[i][j]){
                    equalBoards = false;
                }
            }
        }

        return  width == board1.width &&
                height == board1.height &&
                ballX == board1.ballX &&
                ballY == board1.ballY &&
                equalBoards &&
                equalGoals;

    }

    @Override
    public int hashCode() {
        int result = Objects.hash(width, height, ballX, ballY, goals);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
