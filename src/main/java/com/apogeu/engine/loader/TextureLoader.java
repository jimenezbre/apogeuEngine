package com.apogeu.engine.loader;

import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class TextureLoader {
    public static int loadTexture(String filePath) {
        int textureId;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = STBImage.stbi_load(filePath, width, height, channels, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load texture: " + STBImage.stbi_failure_reason());
            }

            textureId = GL46.glGenTextures();
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureId);

                    // Set texture parameters
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_T, GL46.GL_REPEAT);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);

            GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width.get(0), height.get(0), 0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
            GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);

            STBImage.stbi_image_free(image);
        }

        return textureId;
    }
}
