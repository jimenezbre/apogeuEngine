/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.graphics.mesh;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Victor J.B
 */
public class Line
{
    
    private ArrayList<Point> points;
    
    public Line(Point... point)
    {
        this.points = new ArrayList<>();
        for(Point p : point)
        {
            this.points.add(p);
        }
    }
    
    public void Draw()
    {
    GL11.glColor3f(0.0f, 1.0f, 0.0f); // Red color
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex3f( -.5f, -0.5f, 1f); // Bottom-left
            GL11.glVertex3f(0.5f, 0.5f, 1f);   // Top-right
            GL11.glVertex3f(-0.5f, 2f, 1f);  // Top-left
            GL11.glEnd();
    }
}
