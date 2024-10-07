/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.graphics.text;

/**
 *
 * @author Victor J.B
 */
public class Glyph {
    
    public final int width;
    public final int height;
    public final int x;
    public final int y;
    public final float advence;
    
    public Glyph(int width, int height, int x, int y, float advence)
    {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.advence = advence;
    }
    
}
