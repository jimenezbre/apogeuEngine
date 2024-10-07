/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.physics;

import lombok.Getter;
/**
 *
 * @author Victor J.B
 */
@Getter
public final class Time {
    private static long frameTime;
    private static int timeSpeed = 1;
    
    public static void updateTime()
    {
        frameTime = System.currentTimeMillis();
    }
    
}
