/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.apogeu.engine.gameobject.transform;

import com.apogeu.engine.geometry.tridimensional.Quartenion;
import com.apogeu.engine.geometry.tridimensional.Vector3;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Victor J.B
 */
@Getter
@Setter
public class Transform {
    
    private Vector3 position;
    
    private Quartenion quartenion;
    
    private Vector3 scale;
    
    public Transform()
    {
        position = new Vector3();
        quartenion =  new Quartenion();
        scale =  new Vector3(1);
        
    }

}
