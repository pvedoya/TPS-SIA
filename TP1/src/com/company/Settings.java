package com.company;

import java.io.*;
import java.util.*;

public class Settings {
    private int width;
    private int height;
    private String algorithm;
    private char[][] board;
    private int goals;
//    heuristic??

    public Settings(){
        this.width = 0;
        this.height = 0;
    }

    public void loadSettings(File file){
        try {
            readFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private boolean checkAlgorithm(Scanner scanner){
        String str = "";

        str = scanner.nextLine();

        if(str.equals("DFS") || str.equals("BFS") || str.equals("IDDFS") || str.equals("GGS") || str.equals("A*") || str.equals("IDA*")){
            this.algorithm = str;
            return true;
        }
        return false;
    }

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
                }else{
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

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getGoals() {
        return goals;
    }
}
