package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Settings settings = new Settings();
        try {
            settings.loadSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
