package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Settings {
    private static final String path = "assets/testdata/";

    private Set<Item> weapons;
    private Set<Item> boots;
    private Set<Item> helmets;
    private Set<Item> gloves;
    private Set<Item> armor;



    public Settings(){
        this.weapons = new HashSet<>();
        this.armor = new HashSet<>();
        this.boots = new HashSet<>();
        this.helmets = new HashSet<>();
        this.gloves = new HashSet<>();
    }

    public void loadSettings() throws IOException {
        extractData(path+"armas.tsv", weapons);
        extractData(path+"botas.tsv",armor);
        extractData(path+"cascos.tsv",boots);
        extractData(path+"guantes.tsv",helmets);
        extractData(path+"pecheras.tsv",gloves);
    }

    private void extractData(String filepath, Set<Item> pool) throws IOException {

        StringTokenizer st ;
        BufferedReader TSVFile = new BufferedReader(new FileReader(filepath));
        String dataRow = TSVFile.readLine();
        st = new StringTokenizer(dataRow,"\t");
        if(!st.nextElement().toString().equals("id")){
            System.out.println("Wrong input format detected");
            System.exit(1);
        }

        dataRow = TSVFile.readLine();

        while (dataRow != null){
            st = new StringTokenizer(dataRow,"\t");

            int id = Integer.parseInt(st.nextElement().toString());
            double strength = Double.parseDouble(st.nextElement().toString());
            double agility = Double.parseDouble(st.nextElement().toString());
            double expertise = Double.parseDouble(st.nextElement().toString());
            double resistance = Double.parseDouble(st.nextElement().toString());
            double life = Double.parseDouble(st.nextElement().toString());

            pool.add(new Item(id,strength,agility,expertise,resistance,life));

            dataRow = TSVFile.readLine();
        }

        TSVFile.close();
    }

}
