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
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import com.apogeu.engine.graphics.mesh.Mesh;
import com.apogeu.engine.graphics.texture.Texture;
import com.apogeu.engine.loader.OBJLoader;
import com.apogeu.engine.loader.TextureLoader;
import com.apogeu.engine.scene.Grid;
import com.apogeu.engine.utils.FileChooser;



public class App {

    private long window;
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

    public void run() {
        init();
        loop();

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    private void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = GLFW.glfwCreateWindow(800, 600, "OBJ Viewer", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();
        GL46.glEnable(GL46.GL_DEPTH_TEST);

        setupOpenGL();

        // Initialize the camera
        camera = new Camera(new Vector3f(0.0f, 0.0f, 3.0f), new Vector3f(0.0f, 1.0f, 0.0f), -90.0f, 0.0f);

        // Set up input callbacks
        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
                switch (key) {
                    case GLFW.GLFW_KEY_W:
                        camera.processKeyboard(CameraMovement.FORWARD, deltaTime);
                        break;
                    case GLFW.GLFW_KEY_S:
                        camera.processKeyboard(CameraMovement.BACKWARD, deltaTime);
                        break;
                    case GLFW.GLFW_KEY_A:
                        camera.processKeyboard(CameraMovement.LEFT, deltaTime);
                        break;
                    case GLFW.GLFW_KEY_D:
                        camera.processKeyboard(CameraMovement.RIGHT, deltaTime);
                        break;
                }
            }
        });

        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        GLFW.glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            float xOffset = (float) xpos - lastX;
            float yOffset = lastY - (float) ypos;
            lastX = (float) xpos;
            lastY = (float) ypos;
            camera.processMouseMovement(xOffset, yOffset, true);
        });
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

        shaderProgram = ShaderUtils.createShaderProgram(vertexShaderSource, fragmentShaderSource);
        GL46.glUseProgram(shaderProgram);

        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), 800.0f / 600.0f, 0.1f, 100.0f);

        Grid.initializeGrid();
    }

private void loop() {
    while (!GLFW.glfwWindowShouldClose(window)) {
        float currentFrame = (float) GLFW.glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

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

        GL46.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

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

        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }
}


    public static void main(String[] args) {
        new App().run();
    }
}
