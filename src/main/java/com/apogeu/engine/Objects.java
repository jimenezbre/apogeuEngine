/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 package com.apogeu.engine;
        

import com.apogeu.engine.utils.AutoIncrement;
import com.apogeu.engine.gameobject.GameObject;
import java.util.List;

/**
 *
 * @author Victor J.B
 */
public class Objects {
    
    private int InstanceID;
    
    private String name;
    
    private boolean hideFlag;
    
    private static List<GameObject> listedObjects;
    
    public Objects()
    {
       this.InstanceID = AutoIncrement.getNextId();
       this.hideFlag = false;
       this.name = "Empity Object";
    }
    
    public Integer getInstanceID()
    {
        return this.InstanceID;
    }
    
    public String toString()
    {
        return this.name;
    }
    
    
    public static void Destroy(int id)
    {
        //TODO
    }
    public Objects instantiate(Objects obj)
    {
        //TODO
        return new Objects();
    }
    // Sets n Gets
    public void setName(String s)
    {
        this.name = s;
    }
    public String getName()
    {
        return this.name;
    }
    
    public void hitFlag()
    {
        this.hideFlag = !this.hideFlag;
    }
    
    public boolean getHideFlag()
    {
        return this.hideFlag;
    }
    
    public static List<GameObject> getAllObjects()
    {
        return listedObjects;
    }
    
}
