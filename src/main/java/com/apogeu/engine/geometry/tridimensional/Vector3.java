/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.geometry.tridimensional;

import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author Victor J.B
 */

@Getter
@Setter
public class Vector3 {
    private float x;
    
    private float y;
    
    private float z;
    
    public Vector3()
    {
        x = 0; y = 0; z =0 ;
    }
    
    public Vector3 (float identityValue)
    {
        this.x = identityValue;
        this.y = identityValue;
        this.z = identityValue;
    }
    
    public Vector3(float x,float y,float z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
    }
    
    public void setVector3(float x,float y,float z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
    }
    
    public static Vector3 subVector3(Vector3 a,Vector3 b)
    {
       return new Vector3(a.x-b.x,a.y-b.y,a.z-b.z);
    }
    
    public static float sumVector3(Vector3 a)
    {
        float temp = a.x+a.y+a.z;
        return temp;
    }
}
