/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.scene;

import com.apogeu.engine.gameobject.GameObject;
import com.apogeu.engine.gameobject.camera.Camera;
import java.util.ArrayList;
import lombok.Getter;

/**
 *
 * @author Victor J.B
 */
@Getter
public class Scene {
    
     private static ArrayList<GameObject> sceneObjects = new ArrayList<>();
     
     private static ArrayList<Camera> sceneCameras = new ArrayList<>();
             
}
