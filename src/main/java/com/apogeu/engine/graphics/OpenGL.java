/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.graphics;

import com.apogeu.engine.Objects;
import com.apogeu.engine.gameobject.camera.Camera;
import static com.apogeu.engine.geometry.tridimensional.Vector3.subVector3;
import static com.apogeu.engine.geometry.tridimensional.Vector3.sumVector3;
import com.apogeu.engine.graphics.mesh.Line;
import com.apogeu.engine.graphics.mesh.Mesh;
import com.apogeu.engine.graphics.mesh.Point;
import com.apogeu.engine.graphics.mesh.Triangle;
import com.apogeu.engine.scene.Scene;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor J.B
 */
public class OpenGL {
    
    private static ArrayList<Mesh> allObjects = new ArrayList<>();
    
    private static ArrayList<Line> lines = new ArrayList<>();

    private static Camera mainCamera;
    
    public static void render()
    {/*
        for(Line li : lines)
        {
            li.Draw(); 
        }
        
        for(Mesh obj : allObjects)
        {
            for (Triangle tri : obj.getTriangles())
            {
                if(sumVector(mainCamera.getViewDirection()) -  sumVector(tri.getNormal()) >= 0)
                {
                    tri.setRendable(true);
                    tri.Draw();
                }
            }
        }*/
    }
    
    public static void setCamera(Camera aThis)
    {
        mainCamera = aThis;
    }
    
    public static void addMesh(Mesh m)
    {
        allObjects.add(m);
    }
    
    public static void addLine(Line n)
    {
        lines.add(n);
    }
}
