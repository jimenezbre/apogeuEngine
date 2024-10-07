package com.apogeu.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class Window {
    private long windowHandle;
    private int width, height;
    private String title;
    private Camera camera;
    private float lastFrame;
    private float deltaTime;
    private float lastX;
    private float lastY;

    public Window(int width, int height, String title, Camera camera) {
        this.camera = camera;
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // Window hidden until it's created
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        // Create the window
        windowHandle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (windowHandle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Center the window
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(windowHandle, pWidth, pHeight);

            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(windowHandle,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        // Make the window visible
        GLFW.glfwShowWindow(windowHandle);

        // Create OpenGL context
        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1); // Enable v-sync

        // This line is critical for OpenGL to work
        GL.createCapabilities();
        GL46.glEnable(GL46.GL_DEPTH_TEST);

        setupInput();
    }

    public void update() {
        float currentFrame = (float) GLFW.glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;
        GLFW.glfwSwapBuffers(windowHandle);
        GLFW.glfwPollEvents();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(windowHandle);
    }

    public void cleanup() {
        // Free the window callbacks and destroy the window
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
        GLFW.glfwSetWindowTitle(windowHandle, newTitle);
    }

    private void setupInput() {
        GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
                float speedMultiplier = 2.0f;
                if ((mods & GLFW.GLFW_MOD_SHIFT) != 0) {
                    speedMultiplier = 7.0f; // Increase speed when Shift is pressed (you can adjust this multiplier)
                }
                switch (key) {
                    case GLFW.GLFW_KEY_W:
                        camera.processKeyboard(CameraMovement.FORWARD, deltaTime * speedMultiplier);
                        break;
                    case GLFW.GLFW_KEY_S:
                        camera.processKeyboard(CameraMovement.BACKWARD, deltaTime * speedMultiplier);
                        break;
                    case GLFW.GLFW_KEY_A:
                        camera.processKeyboard(CameraMovement.LEFT, deltaTime * speedMultiplier);
                        break;
                    case GLFW.GLFW_KEY_D:
                        camera.processKeyboard(CameraMovement.RIGHT, deltaTime * speedMultiplier);
                        break;
                    case GLFW.GLFW_KEY_Q:
                        camera.processKeyboard(CameraMovement.UP, deltaTime * speedMultiplier); // Add this line
                        break;
                    case GLFW.GLFW_KEY_E:
                        camera.processKeyboard(CameraMovement.DOWN, deltaTime * speedMultiplier); // Add this line
                        break;
                }
            }
        });

        GLFW.glfwSetInputMode(windowHandle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        GLFW.glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos) -> {
            float xOffset = (float) xpos - lastX;
            float yOffset = lastY - (float) ypos;
            lastX = (float) xpos;
            lastY = (float) ypos;
            camera.processMouseMovement(xOffset, yOffset, true);
        });
    }
}
