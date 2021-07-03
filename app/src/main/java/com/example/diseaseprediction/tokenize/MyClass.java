package com.example.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MyClass {


    public static void main(String[] args) {
        File f = new File("C:\\Users\\Admin\\Desktop\\Disease_prediction\\lib\\src\\main\\java\\com\\example\\lib\\Model.");
        System.out.println(f.getAbsolutePath());
        Scanner myReader = null;
        try {
            myReader = new Scanner(f);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}