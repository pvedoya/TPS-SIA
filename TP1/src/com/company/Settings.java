package com.company;

import java.io.*;
import java.util.*;

public class Settings {
    private int width;
    private int height;
    private Set<Square> squares;
    private String algorithm;
//    heuristic??

    public Settings(){
        this.width = 0;
        this.height = 0;
        this.squares = new HashSet<>();
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

        readSquares(lines);
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

    private void readSquares(List<String> lines){
        int i = 0;
        boolean foundWall = false;

        while(i < lines.size()){
            String str = lines.get(i);
            foundWall = false;
            for (int j = 0; j < str.length(); j++){
                if(str.charAt(j) == '#' ){
                    if(!foundWall){
                        foundWall = true;
                    }
                    Square square = new Square(i, j, true, SquareType.WALL);
                    this.squares.add(square);
                }else if(str.charAt(j) == ' ' && foundWall){
                    Square square = new Square(i, j, true, SquareType.TILE);
                    this.squares.add(square);
                }else if(str.charAt(j) == '.'){
                    Square square = new Square(i, j, true, SquareType.GOAL);
                    this.squares.add(square);
                }else if(str.charAt(j) == '$'){
                    Square square = new Square(i, j, true, SquareType.BOX);
                    this.squares.add(square);
                }else if(str.charAt(j) == '@'){
                    Square square = new Square(i, j, true, SquareType.BALL);
                    this.squares.add(square);
                }
            }

            i++;
        }

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

    public Set<Square> getSquares() {
        return squares;
    }

    public void setSquares(Set<Square> squares) {
        this.squares = squares;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
