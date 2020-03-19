package com.company;

public class Board {
    private int width;
    private int height;
    private Square[][] board;

    public Board() {
        this.width = 0;
        this.height = 0;
    }


     public void applySettings(Settings settings){
         this.width = settings.getWidth();
         this.height = settings.getHeight();
         this.board = new Square[this.height][this.width];

         for(int i = 0; i < this.height; i++){
             for(int j = 0; j < this.width; j++){
                 if(this.board[i][j] == null){
                     this.board[i][j] = new Square(i,j,true, SquareType.VOID);
                 }
             }
         }
         for (Square sq:settings.getSquares()) {
             this.board[sq.getI()][sq.getJ()] = sq;
         }


         if(!isValidBoard()){
             System.out.println("Wrong Board Format Detected");
             System.exit(0);
         }else{
             System.out.println("Correct board format detected, starting to find a solution...");
         }

     }

    public void printBoard() {
        for (int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                System.out.print(board[i][j].getType().getIcon());
            }
            System.out.print('\n');
        }
    }

    private boolean isValidBoard(){
        if(!validWalls()){
            return false;
        }
        if(!validPieces()){
            return false;
        }

        return true;
    }

    private boolean validWalls(){
        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                if(this.board[i][j].getType().equals(SquareType.WALL)){
                    Square first = this.board[i][j];
                    return checkPath(null, null, first);
                }
            }
        }

        return false;
    }

    private boolean checkPath(Square first, Square prev, Square next){
        int minI, minJ, maxI, maxJ;
        minI = Math.max(0, next.getI()-1);
        minJ = Math.max(0, next.getJ()-1);
        maxI = Math.min(this.height-1,next.getI()+1);
        maxJ = Math.min(this.width-1,next.getJ()+1);


        if(next.getI() - 1 >= minI){
            if(this.board[next.getI()-1][next.getJ()].getType().equals(SquareType.WALL) && first == null){
                return checkPath(next, next, this.board[next.getI()-1][next.getJ()]);
            }else if(this.board[next.getI()-1][next.getJ()].equals(first) && !prev.equals(first)){
                return true;
            }else if(this.board[next.getI()-1][next.getJ()].getType().equals(SquareType.WALL) && !prev.equals(this.board[next.getI()-1][next.getJ()])){
                return checkPath(first, next, this.board[next.getI()-1][next.getJ()]);
            }
        }
        if(next.getI() + 1 <= maxI){
            if(this.board[next.getI()+1][next.getJ()].getType().equals(SquareType.WALL) && first == null){
                return checkPath(next, next, this.board[next.getI()+1][next.getJ()]);
            }else if(this.board[next.getI()+1][next.getJ()].equals(first) && !prev.equals(first)){
                return true;
            }else if(this.board[next.getI()+1][next.getJ()].getType().equals(SquareType.WALL) && !prev.equals(this.board[next.getI()+1][next.getJ()])){
                return checkPath(first, next, this.board[next.getI()+1][next.getJ()]);
            }
        }
        if(next.getJ() -1 >= minJ){
            if(this.board[next.getI()][next.getJ()-1].getType().equals(SquareType.WALL) && first == null){
                return checkPath(next, next, this.board[next.getI()][next.getJ()-1]);
            }else if(this.board[next.getI()][next.getJ()-1].equals(first) && !prev.equals(first)){
                return true;
            }else if(this.board[next.getI()][next.getJ()-1].getType().equals(SquareType.WALL) && !prev.equals(this.board[next.getI()][next.getJ()-1])){
                return checkPath(first, next, this.board[next.getI()][next.getJ()-1]);
            }
        }
        if(next.getJ() + 1 <= maxJ){
            if(this.board[next.getI()][next.getJ()+1].getType().equals(SquareType.WALL) && first == null){
                return checkPath(next, next, this.board[next.getI()][next.getJ()+1]);
            }else if(this.board[next.getI()][next.getJ()+1].equals(first) && !prev.equals(first)){
                return true;
            }else if(this.board[next.getI()][next.getJ()+1].getType().equals(SquareType.WALL) && !prev.equals(this.board[next.getI()][next.getJ()+1])){
                return checkPath(first, next, this.board[next.getI()][next.getJ()+1]);
            }
        }

        return false;
    }

    private boolean validPieces(){
        int goalCounter = 0;
        int boxCounter = 0;
        int ballCounter = 0;

        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width;j++){
                if(this.board[i][j].getType() == SquareType.BALL ){
                    ballCounter++;
                }else if(this.board[i][j].getType() == SquareType.BOX ){
                    boxCounter++;
                }else if(this.board[i][j].getType() == SquareType.GOAL ){
                    goalCounter++;
                }
            }
        }

        if(goalCounter != boxCounter || ballCounter != 1){
            return false;
        }
        return true;
    }

}


/*
System.out.println(minI + " " + minJ + " " + maxI + " " + maxJ);


        for(int i = minI; i <= maxI; i++){
            for(int j = minJ; j <= maxJ; j++){
                if(next.getJ() != j || next.getI() != i){
                    if(this.board[i][j].getType().equals(SquareType.WALL) && first == null){
                        return checkPath(next, next, this.board[i][j]);
                    }else if(this.board[i][j].getType().equals(SquareType.WALL) && !prev.equals(this.board[i][j])){
                    System.out.println("i:" + i + " j:" + j + " currI:" + next.getI() + " currJ:" + next.getJ());
                        return checkPath(first, next, this.board[i][j]);
                    }else if(this.board[i][j].equals(first) && !prev.equals(first)){
                        return true;
                    }
                }

            }
        }
* */