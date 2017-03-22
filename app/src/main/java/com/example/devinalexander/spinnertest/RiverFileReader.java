package com.example.devinalexander.spinnertest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class RiverFileReader {

    /*
    public static void main(String[] args) {
        ArrayList<RiverLocation> array = readRiverFile("/Users/DevinAlexander/Desktop/riverURLs.txt");
        for (RiverLocation rl : array) {
            System.out.println(rl);
        }

    }
    */

    public static ArrayList<RiverLocation> readRiverFile(InputStream stream) {
        ArrayList<RiverLocation> array = new ArrayList<RiverLocation>();
        try {

            //File file = new File(filepath);
            //Scanner scan = new Scanner(file);



            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader buffRead = new BufferedReader(reader);

            String line = buffRead.readLine();
            while (line != null) {
                //String line = scan.nextLine();
                if (!line.isEmpty()) {
                    String[] split = line.split(",");

                    if (split.length == 4) {
                        array.add(new RiverLocation(split[0].trim() + " River", split[1].trim().replaceAll("[()]", ""),
                                new URL(split[2].trim()), split[3].trim()));
                    } else if (split.length == 5) {
                        array.add(new RiverLocation(split[0].trim() + " River", split[1].trim(),
                                split[2].trim().replaceAll("[()]", ""), new URL(split[3].trim()), split[4].trim()));

                    }
                    else{
                        RiverLocation river = new RiverLocation(split[0].trim(), split[1].trim().replaceAll("\"", ""), split[2].trim().replaceAll("\"", ""));
                        array.add(river);
                    }
                }
                line = buffRead.readLine();
            }

        } catch (Exception e) {
            System.out.println("File not Found!");
        }
        Collections.sort(array);
        return array;
    }
}
