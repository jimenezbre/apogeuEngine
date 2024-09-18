/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.apogeu.engine;

/**
 *
 * @author operador
 */
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;

public class App {

    private long window;
    private int vaoId;
    private int vboId;
    private String vertexShaderSource = "src/main/java/com/apogeu/engine/vertex.glsl";
    private String fragmentShaderSource = "src/main/java/com/apogeu/engine/fragment.glsl";
    float[] vertices = null;
    int[] indices = null;
    int shaderProgram;
    private Camera camera;
    private float deltaTime;
    private float lastFrame;
    private float lastX;
    private float lastY;
    private Matrix4f projectionMatrix;

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
        try {
            vertices = OBJLoader.loadOBJ("src/main/java/com/apogeu/engine/Comic.obj");
            indices = OBJLoader.loadIndices("src/main/java/com/apogeu/engine/Comic.obj");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        shaderProgram = ShaderUtils.createShaderProgram(vertexShaderSource, fragmentShaderSource);
        GL46.glUseProgram(shaderProgram);

        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), 800.0f / 600.0f, 0.1f, 100.0f);

        // VAO and VBO setup
        vaoId = GL46.glGenVertexArrays();
        int vboId = GL46.glGenBuffers();
        int eboId = GL46.glGenBuffers();

        GL46.glBindVertexArray(vaoId);

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertices, GL46.GL_STATIC_DRAW);

        GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 3 * Float.BYTES, 0);
        GL46.glEnableVertexAttribArray(0);

        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboId);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indices, GL46.GL_STATIC_DRAW);

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindVertexArray(0);
    }

    private void loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            float currentFrame = (float) GLFW.glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

            // Update the view matrix
            Matrix4f viewMatrix = camera.getViewMatrix();
            int viewLoc = GL46.glGetUniformLocation(shaderProgram, "view");
            GL46.glUniformMatrix4fv(viewLoc, false, viewMatrix.get(new float[16]));

            // Update the projection matrix
            int projectionLoc = GL46.glGetUniformLocation(shaderProgram, "projection");
            GL46.glUniformMatrix4fv(projectionLoc, false, projectionMatrix.get(new float[16]));
            if (viewLoc == -1 || projectionLoc == -1) {
                System.err.println("Error: Uniform not found in shader program" + viewLoc + "/" + projectionLoc);
            }

            GL46.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL46.glBindVertexArray(vaoId);
            GL46.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
            GL46.glBindVertexArray(0);

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new App().run();
    }
}
