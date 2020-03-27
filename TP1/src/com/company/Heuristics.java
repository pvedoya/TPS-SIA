package com.company;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Heuristics {

    private Integer INFINITE_COST = 1000000000;
    private ArrayList<Integer[]> goalCoordinates = new ArrayList<>();
    private ArrayList<Integer[]> boxCoordinates = new ArrayList<>();
    private Map<Integer[], Map<Integer[], Integer>> matrix = new HashMap<>();

    /*
    en esta heurística la evaluación es sobre la cercanía de las cajas a los goals
    se toma un promedio de la cercanía de caja cada a un goal y luego se suman los promedios de cada goal
     */
    public int avrgManhattanDistance (Board board) {

        getCoordinates(board);
        int sum = 0;
        int avg;

        for (Integer[] goalCoordinate : goalCoordinates) {
            avg = 0;
            for (int i = 0 ; i < boxCoordinates.size(); i++) {
                avg += manhattanDistance(goalCoordinate, boxCoordinates.get(i));
            }
            avg = avg / boxCoordinates.size();
            sum += avg;
        }

        return sum;
    }

    /*
    en esta heurística la evaluación es sobre la distancia de las cajas a los goals
    se le asigna una caja a cada goal dependiendo de las que esté más cerca
    función que retorna una suma de las distancias de las cajas a los goals
    si las cajas están en las posiciones de los goals, retorna 0
     */
    public int simpleLowerBound (Board board) {

        getCoordinates(board);
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

    private int manhattanDistance (Integer[] from, Integer[] to) {

        return Math.abs(from[0] - to [0]) + Math.abs(from[1] - to[1]);
    }

    private void getCoordinates(Board b) {
        char[][] board = b.getBoard();
        int height = b.getHeight();
        int width = b.getWidth();

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

    /*
    función heurística que asigna un par (box, goal) con el punto de minimizar las jugadas necesarias
     no importa la posición del jugador
     retorna las coordanadas de cada box a su determinado goal
     */
    public int minimumMatchingLowerBound (Board board) {

        getCoordinates(board);
        int movesCount = INFINITE_COST;
        for (Integer[] boxCoordinate : boxCoordinates ){
            Map<Integer[], Integer> goalColumns = matrix.put(boxCoordinate, new HashMap<>());
            for (Integer[] goalCoordinate : goalCoordinates) {
                movesCount = movesCounter(board, boxCoordinate, goalCoordinate);
                if (goalColumns != null) goalColumns.put(goalCoordinate, movesCount);
            }
        }

        /* elegir los mínimos del mapa y sumar */
        return -1;
    }

    private int movesCounter (Board board, Integer[] from, Integer[] to) {

        char[][] b = board.getBoard();
        String[] directions = new String[] {"UP", "DOWN", "LEFT", "RIGHT"};
        boolean deadLock = false;
        int minMoves = INFINITE_COST;

        while (!deadLock) {
            for (String direction: directions) {
                switch (direction) {
                    case "UP":
                        break;
                    case "DOWN":
                        break;
                    case "LEFT":
                        break;
                    case "RIGHT":
                        break;
                }
            }
        }

        return minMoves;
    }

}
