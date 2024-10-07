/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import lombok.Getter;
/**
 *
 * @2author Victor J.B
 */
        
public class Mouse {
    @Getter
    private static float rotationY;
    @Getter
    private static float rotationX;
    private static float rotationSpeedY = 2;
    private static float rotationSpeedX = 2;
    private static double lastMouseX;
    private static double lastMouseY;
    private static float mouseSensibilityY = .1f;
    private static float mouseSensibilityX = .1f;
    private static boolean mouseButtonDown = false;
    
    public static void handleMouse(long window){
                 
         glfwSetCursorPosCallback(window, (windowHandle, xpos, ypos) -> {
            if (mouseButtonDown) {
                double deltaX = xpos - lastMouseX;
                double deltaY = ypos - lastMouseY;
                rotationY += deltaX * mouseSensibilityY;
                rotationX += deltaY * mouseSensibilityX;
                if(rotationX > 90) rotationX = 90;
                if(rotationX < -90) rotationX =-90;
            }

            lastMouseX = xpos;
            lastMouseY = ypos;
        });

        glfwSetMouseButtonCallback(window, (windowHandle, button, action, mods) -> {
            mouseButtonDown = (action == GLFW_PRESS);
        });
    }
}
