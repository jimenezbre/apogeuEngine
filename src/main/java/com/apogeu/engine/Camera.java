/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author operador
 */
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;


public class Camera {
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private Vector3f worldUp;
    private float yaw;
    private float pitch;

    private float movementSpeed;
    private float mouseSensitivity;

    public Camera(Vector3f startPosition, Vector3f startUp, float startYaw, float startPitch) {
        position = startPosition;
        worldUp = startUp;
        yaw = startYaw;
        pitch = startPitch;
        front = new Vector3f(0.0f, 0.0f, -1.0f);
        right = new Vector3f();
        up = new Vector3f();
        movementSpeed = 2.5f;
        mouseSensitivity = 0.1f;
        updateCameraVectors();
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, position.add(front, new Vector3f()), up);
    }

    public void processKeyboard(CameraMovement direction, float deltaTime) {
        float velocity = movementSpeed * deltaTime;
        if (direction == CameraMovement.FORWARD) position.add(front.mul(velocity, new Vector3f()));
        if (direction == CameraMovement.BACKWARD) position.sub(front.mul(velocity, new Vector3f()));
        if (direction == CameraMovement.LEFT) position.sub(right.mul(velocity, new Vector3f()));
        if (direction == CameraMovement.RIGHT) position.add(right.mul(velocity, new Vector3f()));
    }

    public void processMouseMovement(float xOffset, float yOffset, boolean constrainPitch) {
        xOffset *= mouseSensitivity;
        yOffset *= mouseSensitivity *-1;

        yaw += xOffset;
        pitch -= yOffset;

        if (constrainPitch) {
            if (pitch > 89.0f) pitch = 89.0f;
            if (pitch < -89.0f) pitch = -89.0f;
        }

        updateCameraVectors();
    }

    private void updateCameraVectors() {
        Vector3f front = new Vector3f();
        front.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        this.front = front.normalize();
        right = this.front.cross(worldUp, new Vector3f()).normalize();
        up = right.cross(this.front, new Vector3f()).normalize();
    }

    public String getFront() {
        return String.format("Front: [%.2f, %.2f, %.2f]", front.x, front.y, front.z);
    }

    public String getPosition() {
        return String.format("Position: [%.2f, %.2f, %.2f]", position.x, position.y, position.z);
    }
}
