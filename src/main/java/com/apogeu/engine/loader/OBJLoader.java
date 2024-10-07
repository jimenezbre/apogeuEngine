package com.apogeu.engine.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {
    public static class OBJData {
        public float[] vertices;
        public float[] textureCoords;
        public float[] normals;
        public int[] indices;
    }

    public static OBJData loadOBJ(String filename) throws IOException {
        List<Float> verticesTemp = new ArrayList<>();
        List<Float> textureCoordsTemp = new ArrayList<>();
        List<Float> normalsTemp = new ArrayList<>();
        List<Integer> indicesTemp = new ArrayList<>();

        List<Float> vertices = new ArrayList<>();
        List<Float> textureCoords = new ArrayList<>();
        List<Float> normals = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                switch (parts[0]) {
                    case "v":
                        verticesTemp.add(Float.parseFloat(parts[1]));
                        verticesTemp.add(Float.parseFloat(parts[2]));
                        verticesTemp.add(Float.parseFloat(parts[3]));
                        break;
                    case "vt":
                        textureCoordsTemp.add(Float.parseFloat(parts[1]));
                        textureCoordsTemp.add(Float.parseFloat(parts[2]));
                        break;
                    case "vn":
                        normalsTemp.add(Float.parseFloat(parts[1]));
                        normalsTemp.add(Float.parseFloat(parts[2]));
                        normalsTemp.add(Float.parseFloat(parts[3]));
                        break;
                    case "f":
                        processFace(parts, indicesTemp, vertices, textureCoords, normals,
                                    verticesTemp, textureCoordsTemp, normalsTemp);
                        break;
                }
            }
        }

        OBJData data = new OBJData();
        data.vertices = toFloatArray(vertices);
        data.textureCoords = toFloatArray(textureCoords);
        data.normals = toFloatArray(normals);
        data.indices = toIntArray(indicesTemp);

        return data;
    }

    private static void processFace(String[] parts, List<Integer> indices,
                                    List<Float> vertices, List<Float> textureCoords, List<Float> normals,
                                    List<Float> verticesTemp, List<Float> textureCoordsTemp, List<Float> normalsTemp) {
        for (int i = 1; i < parts.length; i++) {
            String[] vertexData = parts[i].split("/");
            int vertexIndex = Integer.parseInt(vertexData[0]) - 1;
            int textureIndex = vertexData.length > 1 && !vertexData[1].isEmpty() ? Integer.parseInt(vertexData[1]) - 1 : -1;
            int normalIndex = vertexData.length > 2 ? Integer.parseInt(vertexData[2]) - 1 : -1;

            indices.add(vertices.size() / 3);

            // Add vertex data
            vertices.add(verticesTemp.get(vertexIndex * 3));
            vertices.add(verticesTemp.get(vertexIndex * 3 + 1));
            vertices.add(verticesTemp.get(vertexIndex * 3 + 2));

            // Add texture coordinate data if available
            if (textureIndex >= 0) {
                textureCoords.add(textureCoordsTemp.get(textureIndex * 2));
                textureCoords.add(textureCoordsTemp.get(textureIndex * 2 + 1));
            }

            // Add normal data if available
            if (normalIndex >= 0) {
                normals.add(normalsTemp.get(normalIndex * 3));
                normals.add(normalsTemp.get(normalIndex * 3 + 1));
                normals.add(normalsTemp.get(normalIndex * 3 + 2));
            }
        }
    }

    private static float[] toFloatArray(List<Float> list) {
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    private static int[] toIntArray(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).toArray();
    }
}