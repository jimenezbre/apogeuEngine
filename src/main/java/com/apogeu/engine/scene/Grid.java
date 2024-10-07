
package com.apogeu.engine.scene;

/** *
 * @author Victor J.B
 */

 import static org.lwjgl.opengl.GL46.*;

 import java.nio.FloatBuffer;
 import org.lwjgl.system.MemoryUtil;
 
 public class Grid {
 
     private static int gridSize = 16;
     private static int vaoId, vboId;
     
     public static void initializeGrid() {
         // Create the VAO and VBO
         vaoId = glGenVertexArrays();
         vboId = glGenBuffers();
 
         // Bind VAO
         glBindVertexArray(vaoId);
 
         // Calculate total grid vertices
         int pointsPerAxis = 2 * gridSize + 1;
         int totalVertices = pointsPerAxis * pointsPerAxis * pointsPerAxis * 6 * 6; // 6 floats per vertex (position + color)
 
         FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(totalVertices);
         
         for (int x = inverseInt(gridSize); x <= gridSize; x++) {
             for (int y = inverseInt(gridSize); y <= gridSize; y++) {
                 for (int z = inverseInt(gridSize); z <= gridSize; z++) {
                     // X-axis lines (red)
                     verticesBuffer.put(new float[]{x, y, z, 1.0f, 0.0f, 0.0f}); // start
                     verticesBuffer.put(new float[]{x, y, z + 1, 1.0f, 0.0f, 0.0f}); // end
                     
                     // Y-axis lines (green)
                     verticesBuffer.put(new float[]{x, y, z, 0.0f, 1.0f, 0.0f}); // start
                     verticesBuffer.put(new float[]{x, y + 1, z, 0.0f, 1.0f, 0.0f}); // end
                     
                     // Z-axis lines (blue)
                     verticesBuffer.put(new float[]{x, y, z, 0.0f, 0.0f, 1.0f}); // start
                     verticesBuffer.put(new float[]{x + 1, y, z, 0.0f, 0.0f, 1.0f}); // end
                 }
             }
         }
 
         verticesBuffer.flip();
 
         // Bind the VBO
         glBindBuffer(GL_ARRAY_BUFFER, vboId);
         glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
 
         // Specify the layout of the vertex data
         glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);         // position
         glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES); // color
         
         // Enable vertex attributes
         glEnableVertexAttribArray(0);
         glEnableVertexAttribArray(1);
 
         // Unbind VAO and VBO
         glBindBuffer(GL_ARRAY_BUFFER, 0);
         glBindVertexArray(0);
 
         // Free the buffer
         MemoryUtil.memFree(verticesBuffer);
     }
 
     public static void drawGrid() {
         glBindVertexArray(vaoId);
         glDrawArrays(GL_LINES, 0, (gridSize * 2 + 1) * (gridSize * 2 + 1) * (gridSize * 2 + 1) * 6); // 6 vertices per cube point
         glBindVertexArray(0);
     }
 
     public static void cleanUp() {
         glDeleteVertexArrays(vaoId);
         glDeleteBuffers(vboId);
     }
 
     private static int inverseInt(int value) {
         return -value;
     }
 }
 