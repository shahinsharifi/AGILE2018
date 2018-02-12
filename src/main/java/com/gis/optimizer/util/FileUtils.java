package com.gis.optimizer.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileUtils {

    public static void printAlgorithmProgress(Map<Integer,Long> input, String fileName){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String result = gson.toJson(input);
        try (FileWriter file = new FileWriter("/home/shahin/Documents/PhD/GISience/Experiments/"+fileName+".json")) {
            file.write(result);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
