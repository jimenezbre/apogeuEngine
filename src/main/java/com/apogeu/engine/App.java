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

public class App {

    private long window;
    int textureId = 0;
    private int vaoId;
    private int vboId;
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
        try {
            objData = OBJLoader.loadOBJ("src/main/java/com/apogeu/engine/Comic.obj");
            vertices = objData.vertices;
            textureCoords = objData.textureCoords; // Add texture coordinates
            normals = objData.normals; // Add normals
            indices = objData.indices;
            textureId = loadTexture("src/main/java/com/apogeu/engine/Comic.png");
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

        GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);
        GL46.glEnableVertexAttribArray(0);

        int texCoordVboId = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, texCoordVboId);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, textureCoords, GL46.GL_STATIC_DRAW);
        GL46.glEnableVertexAttribArray(1);
        GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, true,0, 0);

        int normalVboId = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, normalVboId);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, normals, GL46.GL_STATIC_DRAW);
        GL46.glEnableVertexAttribArray(2);
        GL46.glVertexAttribPointer(2, 3, GL46.GL_FLOAT, false, 0, 0);

        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboId);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indices, GL46.GL_STATIC_DRAW);        

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindVertexArray(0);
    }

    private int loadTexture(String filePath) {
    int textureId;
    try (MemoryStack stack = MemoryStack.stackPush()) {
        IntBuffer width = stack.mallocInt(1);
        IntBuffer height = stack.mallocInt(1);
        IntBuffer channels = stack.mallocInt(1);

        STBImage.stbi_set_flip_vertically_on_load(true);

        // Load image using STB
        ByteBuffer image = STBImage.stbi_load(filePath, width, height, channels, 4);
        if (image == null) {
            throw new RuntimeException("Failed to load texture: " + STBImage.stbi_failure_reason());
        }
        textureId = GL46.glGenTextures();
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureId);

        // Set texture parameters
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_T, GL46.GL_REPEAT);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);



        // Upload texture data
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width.get(0), height.get(0), 0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);

        // Free the image memory
        STBImage.stbi_image_free(image);
    }
    return textureId;
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

        // Set the object base color (if necessary)
        // int objectColorLoc = GL46.glGetUniformLocation(shaderProgram, "objectColor");
        // if (objectColorLoc != -1) {
        //     GL46.glUniform3f(objectColorLoc, 1.0f, 1.0f, 1.0f);  // White object color
        // } else {
        //     System.err.println("Error: 'objectColor' uniform not found in shader program.");
        // }

        // Set the model matrix
        Matrix4f modelMatrix = new Matrix4f().identity();

        int modelLoc = GL46.glGetUniformLocation(shaderProgram, "model");
        if (modelLoc != -1) {
            GL46.glUniformMatrix4fv(modelLoc, false, modelMatrix.get(new float[16]));
        } else {
            System.err.println("Error: 'model' uniform not found in shader program.");
        }

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
