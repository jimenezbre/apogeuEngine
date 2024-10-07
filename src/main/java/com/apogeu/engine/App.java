/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.apogeu.engine;

import java.io.File;
/**
 *
 * @author operador
 */
import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import org.lwjgl.opengl.GL46;


import com.apogeu.engine.graphics.mesh.Mesh;
import com.apogeu.engine.graphics.text.Font;
import com.apogeu.engine.graphics.texture.Texture;
import com.apogeu.engine.loader.OBJLoader;
import com.apogeu.engine.loader.TextureLoader;
import com.apogeu.engine.scene.Grid;
import com.apogeu.engine.utils.FPSCounter;
import com.apogeu.engine.utils.FileChooser;



public class App {

    private Window window;
    static int windowWidth = 1200;
    static int windowHeight = 800;
    private String vertexShaderSource = "src/main/java/com/apogeu/engine/vertex.glsl";
    private String fragmentShaderSource = "src/main/java/com/apogeu/engine/fragment.glsl";
    float[] vertices = null;
    int[] indices = null;
    float[] textureCoords = null;
    float[] normals = null;
    int shaderProgram;
    private Camera camera;
    private float deltaTime;
    private float lastFrame;
    private float lastX;
    private float lastY;
    private Matrix4f projectionMatrix;
    Mesh model;
    Mesh model2;
    Texture texture1;
    Texture texture2;
    Font font;

    public void run() {
        camera = new Camera(new Vector3f(0.0f, 0.0f, 3.0f), new Vector3f(0.0f, 1.0f, 0.0f), -90.0f, 0.0f);

        window = new Window(windowWidth, windowHeight, "Apogeu", camera);
        window.init();

        init();
        loop();
        window.cleanup();
    }

    private void init() {

        setupOpenGL();

        // Initialize the camera
        // Set up input callbacks  
    }

    public void setupOpenGL() {
        // Load OBJ file
        OBJLoader.OBJData objData;
        OBJLoader.OBJData objData2;
        final File filechosen;
        try {
            // filechosen = FileChooser.selectFile();
            // objData = OBJLoader.loadOBJ(filechosen.getAbsolutePath());
            objData = OBJLoader.loadOBJ("src/main/java/com/apogeu/engine/Comic.obj");
            model = new Mesh(objData.vertices, objData.indices, objData.textureCoords, objData.normals);            
            texture1 = Texture.loadTexture("src/main/java/com/apogeu/engine/Comic.png");
            objData2 = OBJLoader.loadOBJ("src/main/java/com/apogeu/engine/Cube.obj");
            texture2 = Texture.loadTexture("src/main/java/com/apogeu/engine/Cube.png");
            model2 = new Mesh(objData2.vertices, objData2.indices, objData2.textureCoords, objData2.normals);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        font = new Font();
        shaderProgram = ShaderUtils.createShaderProgram(vertexShaderSource, fragmentShaderSource);
        GL46.glUseProgram(shaderProgram);

        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) windowWidth / (float) windowHeight , 0.1f, 200.0f);

        Grid.initializeGrid();
    }

private void loop() {
    FPSCounter fpsCounter = new FPSCounter(font);
    while (!window.shouldClose()) {

      fpsCounter.update();


        // Update the view matrix
        Matrix4f viewMatrix = camera.getViewMatrix();
        int viewLoc = GL46.glGetUniformLocation(shaderProgram, "view");
        if (viewLoc != -1) {
            GL46.glUniformMatrix4fv(viewLoc, false, viewMatrix.get(new float[16]));
        } else {
            System.err.println("Error: 'view' uniform not found in shader program.");
        }

        // Update the projection matrix
        int projectionLoc = GL46.glGetUniformLocation(shaderProgram, "projection");
        if (projectionLoc != -1) {
            GL46.glUniformMatrix4fv(projectionLoc, false, projectionMatrix.get(new float[16]));
        } else {
            System.err.println("Error: 'projection' uniform not found in shader program.");
        }

        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);

        // Set the light position
        int lightPosLoc = GL46.glGetUniformLocation(shaderProgram, "lightPos");
        if (lightPosLoc != -1) {
            GL46.glUniform3f(lightPosLoc, 1.2f, 5.0f, 2.0f);  // Example light position
        } else {
            System.err.println("Error: 'lightPos' uniform not found in shader program.");
        }

        // Set the view (camera) position
        int viewPosLoc = GL46.glGetUniformLocation(shaderProgram, "viewPos");
        if (viewPosLoc != -1) {
            GL46.glUniform3f(viewPosLoc, camera.getCameraX(), camera.getCameraY(), camera.getCameraZ());  // Example camera position
        } else {
            System.err.println("Error: 'viewPos' uniform not found in shader program.");
        }

        // Set the light color
        int lightColorLoc = GL46.glGetUniformLocation(shaderProgram, "lightColor");
        if (lightColorLoc != -1) {
            GL46.glUniform3f(lightColorLoc, 1.0f, 1.0f, 1.0f);  // White light
        } else {
            System.err.println("Error: 'lightColor' uniform not found in shader program.");
        }

        // Set the model matrix
        Matrix4f modelMatrix = new Matrix4f().identity();

        int modelLoc = GL46.glGetUniformLocation(shaderProgram, "model");
        if (modelLoc != -1) {
            GL46.glUniformMatrix4fv(modelLoc, false, modelMatrix.get(new float[16]));
        } else {
            System.err.println("Error: 'model' uniform not found in shader program.");
        }

        Grid.drawGrid();
        model.draw(texture1);
        model2.draw(texture2);

        fpsCounter.render( 100.0f, 100.0f, 1.0f);

        window.update();
    }
  //  font.saveTextAsPng("hello World!!", "teste.png", 16);
}

    public static void main(String[] args) {
        new App().run();
    }
}
