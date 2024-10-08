/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.graphics.texture;
import com.apogeu.engine.utils.AutoIncrement;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import lombok.Getter;
import lombok.Setter;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;
import org.lwjgl.system.MemoryStack;

/**
 *
 * @author Victor J.B
 * 
 *         // wraps
//        GL_REPEAT simply repeats the texture
//        GL_MIRRORED_REPEAT will repeat the texture too, but it gets mirrored with odd coordinates
//        GL_CLAMP_TO_EDGE clamps the coordinates between 0.0 and 1.0
//        GL_CLAMP_TO_BORDER will give the coordinates outside of 0.0 and 1.0 a specified border color
        
//        Filtering
//        The next parameter you should set is the texture filtering. This is used if your texture is scaled to a size different to the original image size. For simple texture there are two values.
//
//        GL_NEAREST selects the value that is next to the selected texture coordinate
//        GL_LINEAR calculates weighted average on the four surrounding neighbors
//        GL_NEAREST_MIPMAP_NEAREST takes the closest mipmap that matches the size of the pixel and samples with nearest neighbor interpolation
//        GL_LINEAR_MIPMAP_NEAREST samples the closest mipmaps with bilinear interpolation
//        GL_NEAREST_MIPMAP_LINEAR take the two closest mipmaps that matches the size of the pixel and samples with nearest neighbor interpolation
//        GL_LINEAR_MIPMAP_LINEAR samples the closest mipmaps with trilinear interpolation
 * 
 */
import org.lwjgl.stb.STBImage;
import org.lwjgl.opengl.GL46;

@Getter
public class Texture {

    private final int id;  // Texture ID generated by OpenGL
    private int width;
    private int height;

    @Setter
    private WrapMode selectWrapMode = WrapMode.Repeat;

    @Setter
    private FilteringMode selectFilteringMode = FilteringMode.Linear;

    @Setter
    private boolean isMipMapMode = true;

    @Setter
    private MipMapMode selectMipMapMode = MipMapMode.LinearLinear;

    // Enum for wrapping mode
    public enum WrapMode {
        Repeat(GL46.GL_REPEAT), MirrorRepeat(GL46.GL_MIRRORED_REPEAT), 
        ClampEdges(GL46.GL_CLAMP_TO_EDGE), ClampBorder(GL46.GL_CLAMP_TO_BORDER);

        private final int mode;
        WrapMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }
    }

    // Enum for filtering mode
    public enum FilteringMode {
        Nearest(GL46.GL_NEAREST), Linear(GL46.GL_LINEAR);

        private final int mode;
        FilteringMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }
    }

    // Enum for mipmap modes
    public enum MipMapMode {
        NearestNearest(GL46.GL_NEAREST_MIPMAP_NEAREST), NearestLinear(GL46.GL_NEAREST_MIPMAP_LINEAR), 
        LinearNearest(GL46.GL_LINEAR_MIPMAP_NEAREST), LinearLinear(GL46.GL_LINEAR_MIPMAP_LINEAR);

        private final int mode;
        MipMapMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }
    }

    // Constructor that generates the texture ID
    public Texture() {
        id = GL46.glGenTextures();
    }

    // Set texture parameters
    public void setParameter(int name, int value) {
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, name, value);
    }

    // Bind the texture
    public void bind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, id);
    }

    // Unbind the texture
    public void unbind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, id);
    }

    // Delete the texture when no longer needed
    public void delete() {
        GL46.glDeleteTextures(id);
    }

    // Method to set width and height, validating positive values
    public void setWidth(int width) {
        if (width > 0) {
            this.width = width;
        }
    }

    public void setHeight(int height) {
        if (height > 0) {
            this.height = height;
        }
    }

    // Method to apply the selected wrap mode and filtering mode
    public void applyParameters() {
        setParameter(GL46.GL_TEXTURE_WRAP_S, selectWrapMode.getMode());
        setParameter(GL46.GL_TEXTURE_WRAP_T, selectWrapMode.getMode());
        setParameter(GL46.GL_TEXTURE_MIN_FILTER, isMipMapMode ? selectMipMapMode.getMode() : selectFilteringMode.getMode());
        setParameter(GL46.GL_TEXTURE_MAG_FILTER, selectFilteringMode.getMode());
    }

    // Static method to create a new texture
    public static Texture createTexture(int width, int height, ByteBuffer data) {
        Texture texture = new Texture();
        texture.setWidth(width);
        texture.setHeight(height);

        texture.bind();

        // Set default parameters for wrap and filtering modes
        texture.applyParameters();

        // Upload texture data
        texture.uploadData(GL46.GL_RGBA8, width, height, GL46.GL_RGBA, data);
        
        if (texture.isMipMapMode) {
            GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);  // Generate mipmaps if enabled
        }

        texture.unbind();
        return texture;
    }

    // Method to load a texture from a file
    public static Texture loadTexture(String path) {
        ByteBuffer image;
        int width, height;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            // Load the image using STB
            STBImage.stbi_set_flip_vertically_on_load(true);
            image = STBImage.stbi_load(path, w, h, comp, 4);  // Force 4 channels (RGBA)
            if (image == null) {
                throw new RuntimeException("Failed to load a texture file: " + STBImage.stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        }

        return createTexture(width, height, image);
    }

    // Method to upload the texture data to the GPU
    public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL46.GL_UNSIGNED_BYTE, data);
    }
}
