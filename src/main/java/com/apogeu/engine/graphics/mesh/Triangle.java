/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.graphics.mesh;
import com.apogeu.engine.gameobject.transform.Transform;
import com.apogeu.engine.geometry.tridimensional.Vector3;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
/**
 *
 * @author derek maso
 */
public class Triangle{
    
    private int id;
    
    private Vector3 normal;
    
    private ArrayList<Point> points;
            
    private boolean isVisible;
    
    public Triangle(Vector3 d,Point... point)
    {
        this.normal = d;
        this.points = new ArrayList<>();
        for(Point p : point)
        {
            this.points.add(p);
        }
    }
    
    public void Draw()
    {
        
         GL11.glColor3f(1.0f, 0.0f, 0.0f); // Red color
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2f( -.5f, -0.5f); // Bottom-left
            GL11.glVertex2f(0.5f, 0.5f);   // Top-right
            GL11.glVertex2f(-0.5f, 0.5f);  // Top-left
            GL11.glEnd();
    }
    
    public Vector3 getNormal()
    {
        return this.normal;
    }
    public void setRendable(boolean b)
    {
        this.isVisible = b;
    }
}
