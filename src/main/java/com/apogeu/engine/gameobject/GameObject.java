/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.gameobject;

import com.apogeu.engine.Component;
import com.apogeu.engine.gui.GUI;
import com.apogeu.engine.gui.TextGui;
//import com.apogeu.engine.MonoBehaviour;
import com.apogeu.engine.gameobject.transform.Transform;
import java.util.ArrayList;
import com.apogeu.engine.utils.AutoIncrement;
/**
 *
 * @author Victor J.B
 */
public class GameObject 
{
        
    private ArrayList<GameObject> children;
    
    private boolean isStatic;
    
    private int layer;
    
    private String tag;
    
    private String scene;
    
    private ArrayList<Component> components;
    
    private Transform transform;
       
    public void addComponent(Component obj)
    {
       components.add(obj);
    }

    public GameObject()
    {
        components = new ArrayList<>();
        transform = new Transform();
        tag = "none";
        layer = 0;
        isStatic = true;
        
    }
    
    public GameObject getGameObject()
    {
        return this;
    }
    
    public Component getComponent(String className) throws ClassNotFoundException
    {
        for(Component each : this.components)
        {
            if(each.getClass().getName().contains(className))
            {
                System.out.print("Found!!");
                return each;
            }
        }
        return null;
    }
}
