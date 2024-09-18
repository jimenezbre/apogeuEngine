/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author operador
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.lwjgl.opengl.GL46;

public class ShaderUtils {
    public static int createShaderProgram(String vertexPath, String fragmentPath) {
        String vertexCode = readShaderSource(vertexPath);
        String fragmentCode = readShaderSource(fragmentPath);
        
        int vertexShader = compileShader(GL46.GL_VERTEX_SHADER, vertexCode);
        int fragmentShader = compileShader(GL46.GL_FRAGMENT_SHADER, fragmentCode);
        
        int shaderProgram = GL46.glCreateProgram();
        GL46.glAttachShader(shaderProgram, vertexShader);
        GL46.glAttachShader(shaderProgram, fragmentShader);
        GL46.glLinkProgram(shaderProgram);
        
        checkProgramLinkErrors(shaderProgram);
        
        GL46.glDeleteShader(vertexShader);
        GL46.glDeleteShader(fragmentShader);
        
        return shaderProgram;
    }
    
    private static int compileShader(int type, String source) {
        int shader = GL46.glCreateShader(type);
        GL46.glShaderSource(shader, source);
        GL46.glCompileShader(shader);
        
        checkShaderCompileErrors(shader, type);
        
        return shader;
    }
    
    private static void checkShaderCompileErrors(int shader, int type) {
        int success = GL46.glGetShaderi(shader, GL46.GL_COMPILE_STATUS);
        if (success == GL46.GL_FALSE) {
            int len = GL46.glGetShaderi(shader, GL46.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + (type == GL46.GL_VERTEX_SHADER ? "VERTEX" : "FRAGMENT") + " shader compilation failed.");
            System.out.println(GL46.glGetShaderInfoLog(shader, len));
        }
    }
    
    private static void checkProgramLinkErrors(int program) {
        int success = GL46.glGetProgrami(program, GL46.GL_LINK_STATUS);
        if (success == GL46.GL_FALSE) {
            int len = GL46.glGetProgrami(program, GL46.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Shader program linking failed.");
            System.out.println(GL46.glGetProgramInfoLog(program, len));
        }
    }
    
    private static String readShaderSource(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
