/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.geometry.bidimensional;

import lombok.Getter;
/**
 *
 * @author Victor J.B
 */
@Getter
public class Rect {

    private Vector2 position;
   
    private Vector2 dimension;
    
    public Rect(Vector2 position, Vector2 dimension)
    {
        this.position = position;
        this.dimension = dimension;
    }
    
    public Rect(float x, float y, float width, float height)
    {
        position = new Vector2(x,y);
        dimension = new Vector2(width,height);
    }
    
    public Rect()
    {
        this.position = new Vector2();
        this.dimension = new Vector2();
    }
}
