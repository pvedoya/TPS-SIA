package com.company;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Heuristics {

    private AtomicInteger INFINITE_COST = new AtomicInteger(1000000000);
    private ArrayList<Integer[]> goalCoordinates = new ArrayList<>();
    private ArrayList<Integer[]> boxCoordinates = new ArrayList<>();
    private Integer[][] hungarianMatrix;

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
        hungarianMatrix = new Integer[boxCoordinates.size()][goalCoordinates.size()];
        for (int i = 0; i < boxCoordinates.size(); i++){
            for (int j = 0; j < goalCoordinates.size(); j++) {
                hungarianMatrix[i][j] = movesCounter(board, boxCoordinates.get(i), goalCoordinates.get(j));
            }
        }

        Integer[] combinations = new Integer[(int) Math.pow(boxCoordinates.size(), goalCoordinates.size())];
        for (int i = 0; i < boxCoordinates.size(); i++) {
            for (int j = 0; j < goalCoordinates.size(); j++) {

                int range = (int) Math.pow(boxCoordinates.size(), goalCoordinates.size() - (i+1));
                int space;
                if (i == 0) space = 0;
                else {
                    space = (int) Math.pow(boxCoordinates.size(), goalCoordinates.size() - i);
                }
                int rep = 0;

                do {
                    for (int k = j * range + rep * space; k < (j+1) * range + rep * space ; k++) {
                        combinations[k] += hungarianMatrix[i][j];
                    }
                    rep++;
                } while (rep < i*boxCoordinates.size());

            }
        }

        int resp = INFINITE_COST.get();

        for (int i = 0; i < boxCoordinates.size() * goalCoordinates.size(); i++) {
            resp = Math.min(resp, combinations[i]);
        }

        return resp;
    }

    private int movesCounter (Board board, Integer[] from, Integer[] to) {

        char[][] b = board.getBoard();
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        int directionIndex;
        boolean[] deadlocks = {false, false, false, false};
        Integer[] currentPosition = from;
        ArrayList<Integer[]> possiblePositions;
        boolean moved;

        Map<Integer, Set<Integer[]>> paths = new HashMap<>();
        for (int j = 0; j < dx.length; j++) {
            paths.put(j, new HashSet<>());
        }

        /* index que nos indica en que dirección arrancó el recorrido */
        directionIndex = 0;
        while (directionIndex < 4 && !deadlocks[directionIndex]) {

            possiblePositions = new ArrayList<>();
            moved = false;
            for (int i = 0; i < dx.length; i++){

                /* primero chequeamos que la caja se pueda empujar en la dirección i */
                Integer[] pushFrom = new Integer[]{-1, -1};
                switch (i)  {
                    case 0: pushFrom = new Integer[]{currentPosition[0] + dx[1], currentPosition[1] + dy[1]}; break;
                    case 1: pushFrom = new Integer[]{currentPosition[0] + dx[0], currentPosition[1] + dy[0]}; break;
                    case 2: pushFrom = new Integer[]{currentPosition[0] + dx[3], currentPosition[1] + dy[3]}; break;
                    case 3: pushFrom = new Integer[]{currentPosition[0] + dx[2], currentPosition[1] + dy[2]}; break;
                    default:break;
                }
                if (pushFrom[0] != -1 && b[pushFrom[0]][pushFrom[1]] != SquareType.TILE.getIcon()) {
                    break;
                }

                /* despues nos fijamos si nos podemos posicionar en la nueva posición */
                Integer[] nextPostion = {currentPosition[0] + dx[i], currentPosition[1] + dy[i]};

                if (b[nextPostion[0]][nextPostion[1]] == SquareType.GOAL.getIcon()) {

                    /* conseguimos un recorrido hacia el goal*/
                    moved = true;
                    currentPosition = nextPostion;
                    Set<Integer[]> positions = paths.get(directionIndex);
                    positions.add(currentPosition);
                    paths.replace(directionIndex, positions);

                    /* forma de decir que puede parar de buscar un recorrido en esta dirección */
                    deadlocks[directionIndex] = true;
                    currentPosition = from;
                    directionIndex++;
                    break;
                }

                if (b[nextPostion[0]][nextPostion[1]] == SquareType.TILE.getIcon()) {
                    if (!paths.get(directionIndex).contains(nextPostion)) possiblePositions.add(nextPostion);
                }

            }

            if (!moved && !possiblePositions.isEmpty()) {

                int minDistanceIndex = -1;
                int minDistance = INFINITE_COST.get();
                int aux;
                for (int k = 0; k < possiblePositions.size(); k++) {
                    aux = manhattanDistance(possiblePositions.get(k), to);
                    if (aux <= minDistance) {
                        minDistance = aux;
                        minDistanceIndex = k;
                    }
                }

                currentPosition = possiblePositions.remove(minDistanceIndex);
                moved = true;
                Set<Integer[]> positions = paths.get(directionIndex);
                positions.add(currentPosition);
                paths.replace(directionIndex, positions);
            }

            if (!moved) {
                paths.replace(directionIndex, new HashSet<>()); /* -> de esta manera indicamos que hubo un deadlock */
                deadlocks[directionIndex] = true;
                currentPosition = from;
                directionIndex++;
            }

        }

        AtomicInteger minMoves = INFINITE_COST;

        paths.forEach((direction, positions) -> {
            minMoves.set(Math.min(minMoves.get(), positions.size() == 0 ? INFINITE_COST.get() : positions.size()));
        });

        return minMoves.get();
    }

}
