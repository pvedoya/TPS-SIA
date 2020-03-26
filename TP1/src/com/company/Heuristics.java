package com.company;

import java.util.ArrayList;

public class Heuristics {

    private long INFINITE_COST = 1000000000;
    private Board board;
    private ArrayList<Integer[]> goalCoordinates = new ArrayList<>();
    private ArrayList<Integer[]> boxCoordinates = new ArrayList<>();

    public Heuristics (Board board) {
        this.board = board;
    }

    /*
    función que retorna la mínima cantidad de movimientos que hay que realizar para terminar el juego
     */
    public long getMovementCost (Node node, long movesFromStart) {

        /*
        si el nodo está en la posición ganadora, retornamos la cantidad de movimientos
        realizados hasta llegar a la posición ganadora
        */
        if (node.getBoard().hasWon()) {
            return movesFromStart;
        }
        /*
        si el nodo no tiene más movimientos para hacer porque las cajas están en esquinas
        retornamos una cantidad de movimientos infinita pues de esta manera el juego está perdido
         */
        if (node.getBoard().hasBlocked()) {
            return INFINITE_COST;
        }

        long minMovements = INFINITE_COST;
        node.generateOutcomes();
        for (Node n : node.getOutcomes()) {
            long aux = getMovementCost(n, movesFromStart + 1);
            minMovements = Math.min(minMovements, aux);
        }

        return minMovements + movesFromStart;
    }

    private int manhattanDistance (Integer[] from, Integer[] to) {

        return Math.abs(from[0] - to [0]) + Math.abs(from[1] - to[1]);
    }

    private int getBoxesCost () {

        getCoordinates();
        int cost = 0;
        int count = 0;

        for (Integer[] goalCoordinate : goalCoordinates) {
            Integer[] distances = new Integer[boxCoordinates.size() - count];
            for (int j = 0; j < boxCoordinates.size(); j++) {

                if (boxCoordinates.get(j)[0] != -1 && boxCoordinates.get(j)[1] != -1) {
                    distances[j - count] = manhattanDistance(boxCoordinates.get(j), goalCoordinate);
                }
            }

            int minDistance = 1000000000;
            int pos = -1;
            for (int k = 0; k < distances.length; k++) {
                if (minDistance >= distances[k]) {
                    minDistance = distances[k];
                    pos = k;
                }
            }

            boxCoordinates.set(pos, new Integer[]{-1, -1});

            cost += minDistance;
            count++;
        }

        return cost;
    }

    private void getCoordinates() {
        char[][] board = this.board.getBoard();
        int height = this.board.getHeight();
        int width = this.board.getWidth();

        for (int i = 0; i < width; i ++) {
            for (int j = 0; j < height; j++) {

                if (board[i][j] == SquareType.BOX.getIcon()){
                    boxCoordinates.add(new Integer[]{i, j});
                }
                if (board[i][j] == SquareType.GOAL.getIcon()){
                    goalCoordinates.add(new Integer[]{i, j});
                }
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
