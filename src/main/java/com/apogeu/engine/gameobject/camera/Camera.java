/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.gameobject.camera;

import com.apogeu.engine.gameobject.GameObject;
import com.apogeu.engine.geometry.tridimensional.Vector3;
import com.apogeu.engine.graphics.OpenGL;
import com.apogeu.engine.input.Mouse;
import lombok.Getter;
import org.lwjgl.opengl.*;
/**
 *
 * @author Victor J.B
 */
public class Camera extends GameObject {
    
    private int id;
    
    //public enum cameraType {Main,Secondary};
    
    private Vector3 viewDirection;
    
    private float minViewDistance;
    
    private int maxViewDistance;
    

    private static float cameraPositionX;
    private static float cameraPositionY;
    private static float cameraPositionZ;
    private static float movimentSpeed = .5f;
    
    private void renderView(){
    
    }
    public Camera()
    {
        viewDirection = new Vector3(0,0,-1);
        minViewDistance = -0.2f;
        maxViewDistance = 1000;
        OpenGL.setCamera(this);
        //cameraType.Main;
    }
    
    
    public void handleTransform()
    {
        GL11.glTranslatef(0,0, 0); //ajust position to rotate itself
        GL11.glRotatef(Mouse.getRotationX(), 1, 0, 0);
        GL11.glRotatef(Mouse.getRotationY(), 0, 1, 0);
        GL11.glTranslatef(cameraPositionX,cameraPositionY, cameraPositionZ);// return to position
    }
    
    
    
    
    
    
    public Camera getCamera()
    {
        return this;
    }
    
    public Vector3 getViewDirection()
    {
        return this.viewDirection;
    }
}
