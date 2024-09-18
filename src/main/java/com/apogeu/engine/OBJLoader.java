/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine;

/**
 *
 * @author operador
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {
    public static float[] loadOBJ(String filename) throws IOException {
        List<Float> vertices = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] parts = line.split(" ");
                    vertices.add(Float.parseFloat(parts[1]));
                    vertices.add(Float.parseFloat(parts[2]));
                    vertices.add(Float.parseFloat(parts[3]));
                }
            }
        }
        float[] vertexArray = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            vertexArray[i] = vertices.get(i);
        }
        return vertexArray;
    }
        public static int[] loadIndices(String filename) throws IOException {
        List<Integer> indices = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("f ")) {
                    String[] parts = line.split(" ");
                    for (int i = 1; i < parts.length; i++) {
                        String[] vertexIndices = parts[i].split("/");
                        indices.add(Integer.parseInt(vertexIndices[0]) - 1); // OBJ indices are 1-based
                    }
                }
            }
        }
        int[] indexArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indexArray[i] = indices.get(i);
        }
        return indexArray;
    }
}
