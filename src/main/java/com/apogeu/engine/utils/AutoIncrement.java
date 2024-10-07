/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.utils;

/**
 *
 * @author Victor J.B
 */
public class AutoIncrement {
    private static int lastObjectId;
    private static int lastPointId;
    private static int lastGuiId;
    private static int lastTextureId;
    
    private AutoIncrement()
    {
        lastObjectId = 0;
        lastPointId = 0;
        lastGuiId = 0;
    }
    
    public static Integer getNextId()
    {
        lastObjectId++;
        return lastObjectId;
    }
    
    public static Integer getNextGuiId()
    {
        lastObjectId++;
        return lastObjectId;
    }
    
    public static Integer getNextPointId()
    {
        lastPointId++;
        return lastPointId;
    }
    
    public static Integer getNextTextureId()
    {
        lastTextureId++;
        return lastTextureId;
    }
}
