package com.apogeu.engine.graphics.mesh;

import org.lwjgl.opengl.GL46;

import com.apogeu.engine.graphics.texture.Texture;

public class Mesh {
    private int vaoId;
    private int vboId;
    private int eboId;
    private int indexCount;

    public Mesh(float[] vertices, int[] indices, float[] textureCoords, float[] normals) {
        vaoId = GL46.glGenVertexArrays();
        vboId = GL46.glGenBuffers();
        eboId = GL46.glGenBuffers();
        indexCount = indices.length;

        GL46.glBindVertexArray(vaoId);

        // Vertex data
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertices, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);
        GL46.glEnableVertexAttribArray(0);

        // Texture coordinates
        int texCoordVboId = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, texCoordVboId);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, textureCoords, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, 0, 0);
        GL46.glEnableVertexAttribArray(1);

        // Normals
        int normalVboId = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, normalVboId);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, normals, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(2, 3, GL46.GL_FLOAT, false, 0, 0);
        GL46.glEnableVertexAttribArray(2);

        // Index data
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboId);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indices, GL46.GL_STATIC_DRAW);

        GL46.glBindVertexArray(0);
    }

    public void draw(Texture texture) {
        texture.bind();
        texture.applyParameters();
        GL46.glBindVertexArray(vaoId);
        GL46.glDrawElements(GL46.GL_TRIANGLES, indexCount, GL46.GL_UNSIGNED_INT, 0);
        GL46.glBindVertexArray(0);
        texture.unbind();
    }

    public void cleanup() {
        GL46.glDeleteVertexArrays(vaoId);
        GL46.glDeleteBuffers(vboId);
        GL46.glDeleteBuffers(eboId);
    }
}
