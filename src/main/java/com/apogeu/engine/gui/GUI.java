/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.gui;

import com.apogeu.engine.Component;
import com.apogeu.engine.gameobject.GameObject;
import com.apogeu.engine.gameobject.transform.Transform;
import com.apogeu.engine.geometry.bidimensional.Rect;
import com.apogeu.engine.utils.AutoIncrement;
import java.util.ArrayList;
import lombok.Getter;

/**
 *
 * @author Victor J.B
 */


@Getter
public class GUI implements Component{//implements GUIinterface{
    
    private static ArrayList<GUI> GuiElements = new ArrayList<>();
    
    private int Id;
    
    private String name;
    
    private Rect rect;
    
    private int layer;

    public GUI()
    {
        Id = AutoIncrement.getNextGuiId();
        rect = new Rect();
        name = "Empity GUI";
        layer = 0;
        GuiElements.add(this);
    }
//    public String getText(){
//            return "";
//    }
}
