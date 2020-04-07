package com.company;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Heuristics {

    private static int INFINITE_COST = 1000000000;
    private static ArrayList<Integer[]> goalCoordinates;
    private static ArrayList<Integer[]> boxCoordinates;

    public static void main (String[] args ) {
        File readFile = new File("maps/" + args[0]);

        Settings settings = new Settings();
        settings.loadSettings(readFile);

        Board board = new Board(settings.getWidth(),settings.getHeight(),settings.getBoard());
        board.validate();

        getCoordinates(board);
        System.out.println(goalCoordinates.size());
        System.out.println(boxCoordinates.size());

        System.out.println("MANHATTAN DISTANCE 1: " + avrgManhattanDistance(board));
        //board.printBoard();
        System.out.println("MANHATTAN DISTANCE 2: " + simpleLowerBound(board));
        //board.printBoard();
        System.out.println("HUNGARIAN MATRIX: " + minimumMatchingLowerBound(board));
        //board.printBoard();
    }

    /*
    en esta heurística la evaluación es sobre la cercanía de las cajas a los goals
    se toma un promedio de la cercanía de caja cada a un goal y luego se suman los promedios de cada goal
     */
    public static int avrgManhattanDistance(Board board) {

        getCoordinates(board);
        int sum = 0;
        int avg;

        for (Integer[] goalCoordinate : goalCoordinates) {
            avg = 0;
            for (Integer[] boxCoordinate : boxCoordinates) {
                avg += manhattanDistance(goalCoordinate, boxCoordinate);
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
    public static int simpleLowerBound(Board board) {

        getCoordinates(board);
        int cost = 0;

        for (Integer[] goalCoordinate : goalCoordinates) {
            ArrayList<Integer> distances = new ArrayList<>();
            for (Integer[] coord: boxCoordinates) {
                if (coord[0] != -1 && coord[1] != -1) {
                    distances.add(manhattanDistance(coord, goalCoordinate));
                }
            }

            int minDistance = 1000000000;
            int pos = -1;
            for (int k = 0; k < distances.size(); k++) {
                if (minDistance >= distances.get(k)) {
                    minDistance = distances.get(k);
                    pos = k;
                }
            }

            boxCoordinates.set(pos, new Integer[]{-1, -1});

            cost += minDistance;
        }

        return cost;
    }

    private static int manhattanDistance (Integer[] from, Integer[] to) {

        return Math.abs(from[0] - to [0]) + Math.abs(from[1] - to[1]);
    }

    private static int optimizedManhattanDistance (Board b, Integer[] from, Integer[] to) {

        int manhattan = manhattanDistance(from, to);

        if (manhattan == 0) {
            return manhattan;
        }

        char[][] board = b.getBoard();
        boolean cantMoveH = false, cantMoveV = false;

        /* me fijo en las 4 direcciones si el jugador se puede mover desde from */
        if ( from[0] - 1 >= 0 && from[0] + 1 < b.getHeight()) {
            if (board[from[0] - 1][from[1]] == SquareType.WALL.getIcon() || board[from[0] - 1][from[1]] == SquareType.BOX.getIcon() || board[from[0] + 1][from[1]] == SquareType.WALL.getIcon() || board[from[0] + 1][from[1]] == SquareType.BOX.getIcon()) {
                cantMoveV = true;
            }
        }
        if ( from[1] - 1 >= 0 && from[1] + 1 < b.getWidth()) {
            if (board[from[0]][from[1] - 1] == SquareType.WALL.getIcon() || board[from[0]][from[1] - 1] == SquareType.BOX.getIcon() || board[from[0]][from[1] + 1] == SquareType.WALL.getIcon() || board[from[0]][from[1] + 1] == SquareType.BOX.getIcon()) {
                cantMoveH = true;
            }
        }

        // se encuentra en una esquina
        if (cantMoveH && cantMoveV) {
            manhattan += INFINITE_COST;
        }

        return manhattan;
    }

    private static void getCoordinates(Board b) {
        char[][] board = b.getBoard();
        int height = b.getHeight();
        int width = b.getWidth();
        goalCoordinates = b.getGoals();
        boxCoordinates  = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (board[i][j] == SquareType.BOX.getIcon()){
                    boxCoordinates.add(new Integer[]{i, j});
                }

            }
        }
    }

    /*
    función heurística que asigna un par (box, goal) con el punto de minimizar las jugadas necesarias
     no importa la posición del jugador
     retorna las coordanadas de cada box a su determinado goal
     */
    public static int minimumMatchingLowerBound (Board board) {

        getCoordinates(board);
        Integer[][] hungarianMatrix = new Integer[boxCoordinates.size()][goalCoordinates.size()];

        for (int i = 0; i < boxCoordinates.size(); i++){
            for (int j = 0; j < goalCoordinates.size(); j++) {
                hungarianMatrix[i][j] = countMoves(board, boxCoordinates.get(i), goalCoordinates.get(j));
            }
        }

        int[] combinations = new int[(int) Math.pow(boxCoordinates.size(), goalCoordinates.size())];
        Arrays.fill(combinations, 0, (int) Math.pow(boxCoordinates.size(), goalCoordinates.size()), 0);
        for (int i = 0; i < boxCoordinates.size(); i++) {
            for (int j = 0; j < goalCoordinates.size(); j++) {

                int range = (int) Math.pow(boxCoordinates.size(), goalCoordinates.size() - (i+1));
                int space, limit;
                if (i == 0) {
                    space = 0;
                    limit = 0;
                }
                else {
                    space = (int) Math.pow(boxCoordinates.size(), goalCoordinates.size() - i);
                    limit = (int) Math.pow(boxCoordinates.size(), i);
                }
                int rep = 0;

                do {
                    for (int k = j * range + rep * space; k < (j+1) * range + rep * space ; k++) {
                        if (i > 0 && rep == j) {
                            combinations[k] += INFINITE_COST;
                        } else {
                            combinations[k] += hungarianMatrix[i][j];
                        }
                    }
                    rep++;
                } while (rep < limit);

            }
        }

        int resp = INFINITE_COST;

        for (int i = 0; i < combinations.length; i++) {
            resp = Math.min(resp, Math.abs(combinations[i]));
        }

        return resp;
    }

    private static int countMoves(Board board, Integer[] from, Integer[] to) {

        if (from[0].equals(to[0]) && from[1].equals(to[1])) return 0;

        char[][] b = board.getBoard();
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        int directionIndex;
        boolean[] deadlocks = {false, false, false, false};
        Integer[] currentPosition = from;
        boolean moved, firstMove = true;
        ArrayList<Integer[]> positions;

        Map<Integer, ArrayList<Integer[]>> paths = new HashMap<>();
        Map<Integer, PriorityQueue<Integer[]>> possiblePositions = new HashMap<>();
        for (int j = 0; j < dx.length; j++) {
            paths.put(j, new ArrayList<>());
            possiblePositions.put(j, new PriorityQueue<>(Comparator.comparingInt(o -> optimizedManhattanDistance(board, o, to))));
        }


        /* index que nos indica en que dirección arrancó el recorrido */
        directionIndex = 0;
        while (directionIndex < 4 && !deadlocks[directionIndex]) {

            moved = false;
            Integer[] pushFrom = new Integer[]{-1, -1};

            if (firstMove) {

                positions = paths.get(directionIndex);
                positions.add(currentPosition);
                paths.replace(directionIndex, positions);

                /* primero chequeamos que la caja se pueda empujar en la dirección direction index */
                switch (directionIndex)  {
                    case 0: pushFrom = new Integer[]{currentPosition[0] + dx[1], currentPosition[1] + dy[1]}; break;
                    case 1: pushFrom = new Integer[]{currentPosition[0] + dx[0], currentPosition[1] + dy[0]}; break;
                    case 2: pushFrom = new Integer[]{currentPosition[0] + dx[3], currentPosition[1] + dy[3]}; break;
                    case 3: pushFrom = new Integer[]{currentPosition[0] + dx[2], currentPosition[1] + dy[2]}; break;
                    default:break;
                }
                if ((b[pushFrom[0]][pushFrom[1]] != SquareType.BOX.getIcon() || (pushFrom[0].equals(from[0]) && pushFrom[1].equals(from[1]))) && b[pushFrom[0]][pushFrom[1]] != SquareType.WALL.getIcon()) {

                    /* despues nos fijamos si nos podemos posicionar en la nueva posición */
                    Integer[] nextPosition = {currentPosition[0] + dx[directionIndex], currentPosition[1] + dy[directionIndex]};

                    if (b[nextPosition[0]][nextPosition[1]] == SquareType.GOAL.getIcon() && nextPosition[0].equals(to[0]) && nextPosition[1].equals(to[1])) {

                        /* conseguimos un recorrido hacia el goal*/
                        moved = true;
                        currentPosition = nextPosition;
                        positions = paths.get(directionIndex);
                        positions.add(currentPosition);
                        paths.replace(directionIndex, positions);

                        /* forma de decir que puede parar de buscar un recorrido en esta dirección */
                        deadlocks[directionIndex] = true;
                        currentPosition = from;
                        directionIndex++;
                    }

                    if (b[nextPosition[0]][nextPosition[1]] == SquareType.TILE.getIcon()
                            || b[nextPosition[0]][nextPosition[1]] == SquareType.BALL.getIcon()
                            || (b[nextPosition[0]][nextPosition[1]] == SquareType.GOAL.getIcon() && !(nextPosition[0].equals(to[0]) && nextPosition[1].equals(to[1])))) {

                        moved = true;
                        currentPosition = nextPosition;
                        positions = paths.get(directionIndex);
                        positions.add(currentPosition);
                        paths.replace(directionIndex, positions);
                    }

                }

            }

            for (int i = 0; i < dx.length && directionIndex < 4 && !deadlocks[directionIndex]; i++){

                /* primero chequeamos que la caja se pueda empujar en la dirección i */
                switch (i)  {
                    case 0: pushFrom = new Integer[]{currentPosition[0] + dx[1], currentPosition[1] + dy[1]}; break;
                    case 1: pushFrom = new Integer[]{currentPosition[0] + dx[0], currentPosition[1] + dy[0]}; break;
                    case 2: pushFrom = new Integer[]{currentPosition[0] + dx[3], currentPosition[1] + dy[3]}; break;
                    case 3: pushFrom = new Integer[]{currentPosition[0] + dx[2], currentPosition[1] + dy[2]}; break;
                    default:break;
                }
                if ((b[pushFrom[0]][pushFrom[1]] != SquareType.BOX.getIcon() || (pushFrom[0].equals(from[0]) && pushFrom[1].equals(from[1]))) && b[pushFrom[0]][pushFrom[1]] != SquareType.WALL.getIcon()) {

                    /* despues nos fijamos si nos podemos posicionar en la nueva posición */
                    Integer[] nextPosition = {currentPosition[0] + dx[i], currentPosition[1] + dy[i]};

                    if (b[nextPosition[0]][nextPosition[1]] == SquareType.GOAL.getIcon() && nextPosition[0].equals(to[0]) && nextPosition[1].equals(to[1])) {

                        /* conseguimos un recorrido hacia el goal*/
                        moved = true;
                        currentPosition = nextPosition;
                        positions = paths.get(directionIndex);
                        positions.add(currentPosition);
                        paths.replace(directionIndex, positions);

                        /* forma de decir que puede parar de buscar un recorrido en esta dirección */
                        deadlocks[directionIndex] = true;
                        currentPosition = from;
                        directionIndex++;
                        firstMove = true;
                        break;
                    }

                    if (b[nextPosition[0]][nextPosition[1]] == SquareType.TILE.getIcon()
                            || b[nextPosition[0]][nextPosition[1]] == SquareType.BALL.getIcon()
                            || (b[nextPosition[0]][nextPosition[1]] == SquareType.GOAL.getIcon() && !(nextPosition[0].equals(to[0]) && nextPosition[1].equals(to[1])))) {
                        /*if (!paths.get(directionIndex).contains(nextPosition)) possiblePositions.add(nextPosition);*/
                        positions = paths.get(directionIndex);
                        AtomicBoolean repeat = new AtomicBoolean(false);
                        positions.forEach( pos -> {
                            if (pos[0].equals(nextPosition[0]) && pos[1].equals(nextPosition[1])) repeat.set(true);
                        });
                        if (!repeat.get()) {
                            possiblePositions.get(directionIndex).add(nextPosition);
                            //System.out.println("Adding pos " + nextPosition[0] + ", " + nextPosition[1] + " with distance " + optimizedManhattanDistance(board, nextPosition, to));
                        }
                    }
                }

            }

            if ((!moved || firstMove) && directionIndex < 4 && !possiblePositions.get(directionIndex).isEmpty()) {

                currentPosition = possiblePositions.get(directionIndex).poll();
                moved = true;
                positions = paths.get(directionIndex);
                positions.add(currentPosition);
                paths.replace(directionIndex, positions);
            }

            if (!moved) {
                paths.replace(directionIndex, new ArrayList<>()); /* -> de esta manera indicamos que hubo un deadlock */
                deadlocks[directionIndex] = true;
                currentPosition = from;
                directionIndex++;
                firstMove = true;
            }

            if (firstMove && moved) {
                firstMove = false;
            }

        }

        final int[] minMoves = {INFINITE_COST};

        paths.forEach((direction, path) -> minMoves[0] = (Math.min(minMoves[0], path.size() == 0 ? INFINITE_COST : path.size())));

        return minMoves[0];
    }

}
