package com.company;

import java.util.*;

public class Board implements Cloneable{
    private int width;
    private int height;

    private char[][] board;

    private int ballX;
    private int ballY;

    private Set<Integer[]> goals;

    public Board(int width, int height, char[][] board) {
        this.width = width;
        this.height = height;
        this.board = board;
        this.goals = new HashSet<>();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBoard(char[][] board){
        this.board = board;
    }

    public char[][] getBoard(){
        return board;
    }

    public void setBall(int x, int y){
        this.ballX = x;
        this.ballY = y;
    }

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

    private boolean validWalls(){
        Set<Integer[]> visited = new TreeSet<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] array1, Integer[] array2) {
                return Integer.compare(array1[0],array2[0]) + Integer.compare(array1[1], array2[1]);
            }
        });
        Stack<Integer[]> stack = new Stack<>();
        boolean closed = false;

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
        stack.push(current);
        int minI, minJ, maxI, maxJ;

        while(!closed){
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

    private boolean isGoal(int i, int j){
        for (Integer[] pair: this.goals) {
            if(pair[0] == i && pair[1] == j){
                return true;
            }
        }
        return false;
    }


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

    public String stringifyBoard(){
        String ret = "";

        for(int i = 0; i< this.height ; i++){
            for(int j =0 ; j < this.width; j++){
                ret += this.board[i][j];
            }
        }
        return ret;
    }

}
