package com.company;

import java.io.*;
import java.util.*;

/*
* La clase Settings contiene todos los metodos y variables utilizados para leer y analizar el archivo de entrada,
* para luego poder darle esa informacion a las clases que encontraran la solucion
* */

public class Settings {
    private String algorithm;
    private String heuristic;
    private int width;
    private int height;
    private char[][] board;
    private int goals;
//    maxTime?? TODO

    public Settings(){
        this.width = 0;
        this.height = 0;
    }

    public void loadSettings(File file){
        try {
            readFile(file);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Could not find configuration .txt file in TP1/maps");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("ERROR: failed with I/O operation");
            System.exit(1);
        }
    }

    /*
    * Metodo que escanea linea a linea la informacion del archivo de entrada, donde la primera linea es el algoritmo a utilizarse,
    * seguido por la heuristica(si el algoritmo lo permite), un tiempo maximo permitido para la ejecucion,
    * y el tablero en multiples lineas, respetando los simbolos de SquareTypes. Cuando termina la lectura llama a
    * readLines para hacer analisis del contenido.
    * */

    private void readFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(new FileReader(file));
        String str = "";

        int maxLength = 0;
        int lineCount = 0;

        if(!checkAlgorithm(scanner)){
            System.out.println("Incomplete configuration file: Search algorithm missing or wrong algorithm input");
            System.exit(0);
        }

        while(scanner.hasNext()){
            str = scanner.nextLine();
            lineCount++;

            if(str.length() > maxLength){
                maxLength = str.length();
            }
            lines.add(str);
        }

        this.width = maxLength;
        this.height = lineCount;
        this.board = new char[this.height][this.width];

        readLines(lines);
    }

    /*
    * Checks for the algorithm and the heuristic name(onlu for informed algorithms)
    * */

    private boolean checkAlgorithm(Scanner scanner){
        String str1 = "";
        String str2 = "";
        str1 = scanner.nextLine();

        if(str1.equals("DFS") || str1.equals("BFS") || str1.equals("IDDFS") ){
            this.algorithm = str1;

            return true;
        }else if (str1.equals("GGS") || str1.equals("A*") || str1.equals("IDA*")){
            this.algorithm = str1;
            str2 = scanner.nextLine();

            if(str2.equals("MANHATTAN") || str2.equals("SLB") || str2.equals("MMLB")){
                this.heuristic = str2;
                return true;
            }

        }
        return false;
    }

    /*
    * Linea por linea, este metodo se encarga de asignar al tablero los simbolos correspongientes, y llenar los vacios dentro y fuera del tablero
    * */

    private void readLines(List<String> lines){
        int i = 0;
        boolean foundWall = false;

        while(i < lines.size()){
            String str = lines.get(i);
            foundWall = false;
            int j;
            for (j = 0; j < str.length(); j++){
                if(str.charAt(j) == '#' ){
                    if(!foundWall){
                        foundWall = true;
                    }
                    board[i][j] = '#';
                }else if(str.charAt(j) == ' ' && foundWall){
                    board[i][j] = ' ';
                }else if(str.charAt(j) == '.'){
                    board[i][j] = '.';
                    goals++;
                }else if(str.charAt(j) == '$'){
                    board[i][j] = '$';
                }else if(str.charAt(j) == '@'){
                    board[i][j] = '@';
                }else if(str.charAt(j) == '+'){
                    board[i][j] = '+';
                }else if(str.charAt(j) == '*'){
                    board[i][j] = '*';
                }
                else{
                    board[i][j] = ' ';
                }
            }
            while(j < this.width){
                board[i][j++] = ' ';
            }
            i++;
        }
        if(!foundWall){
            System.out.println("Wrong board format detected");
            System.exit(0);
        }
    }

    //Getters & Setters

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char[][] getBoard() {
        return board;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getHeuristic() {
        return heuristic;
    }
}