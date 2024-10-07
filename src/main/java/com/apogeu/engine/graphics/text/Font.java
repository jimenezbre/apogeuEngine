/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apogeu.engine.graphics.text;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;
import java.util.Map;
import com.apogeu.engine.graphics.texture.Texture;
import static java.awt.Font.TRUETYPE_FONT;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import org.lwjgl.system.MemoryUtil;

/**
 *
 * @author Victor J.B
 */
public class Font {
    
    private final Map<Character,Glyph> glyphs;
    
    private final Texture texture;
    
    private int fontHeight;
    
    
    public Font(){
    this(new java.awt.Font(MONOSPACED,PLAIN,16), true);
    }
    
    public Font(boolean antiAlias)
    {
    this(new java.awt.Font(MONOSPACED,PLAIN,16), antiAlias);
    }
    
    public Font(int Size)
    {
    this(new java.awt.Font(MONOSPACED,PLAIN,Size), true);
    }
    
    public Font(java.awt.Font font){
        this(font, true);
    }
    public Font(InputStream in, int size, boolean antiAlias) throws FontFormatException, IOException {
        this(java.awt.Font.createFont(TRUETYPE_FONT, in).deriveFont(PLAIN, size), antiAlias);
    }
    
    public Font(java.awt.Font font,boolean antiAlias){
        glyphs = new HashMap<>();
        texture = createFontTexture(font,antiAlias);
    }
    
    public Texture createFontTexture(java.awt.Font font, boolean antiAlias)
    {
        int imageWidth = 0;
        int imageHeight = 0;
        
        // calculate texture width
         for(int i = 32; i < 256; i++)
         {
             if(i == 127)
             {
                 continue;
             }
             char c = (char) i;
             BufferedImage ch = createCharImage(font, c, antiAlias);
             if(ch == null) 
             {
                 continue;
             }
             imageWidth += ch.getWidth();
             imageHeight = Math.max(imageHeight, ch.getHeight());
         }
        
         fontHeight = imageHeight;
         BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
         Graphics2D g = image.createGraphics();
         
         int x = 0;
         
         //Create reference charTexture
         for(int i =32; i < 256; i++)
         {
             if(i == 127){continue;}
             
             char c = (char) i;
             BufferedImage charImage = createCharImage(font, c, antiAlias);
             
             if(charImage == null){continue;}
             int charWidth = charImage.getWidth();
             int charHeight = charImage.getHeight();
         
             Glyph ch = new Glyph(charWidth, charHeight, x, image.getHeight() - charHeight, 0f);
             g.drawImage(charImage, x, 0 ,null);
             x += ch.width;
             glyphs.put(c,ch);
         }
         
         //gotta to flip to opengl orientation
         AffineTransform transform = AffineTransform.getScaleInstance(1f,-1f);
         transform.translate(0, -image.getHeight());
         AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
         
         image = operation.filter(image, null);
         
         int width = image.getWidth();
         int height = image.getHeight();
         
         int[] pixels = new int[width * height];
         image.getRGB(0, 0, width, height, pixels, 0, width);
         
         ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);
         for(int i = 0; i< height;i++)
         {
            for(int j = 0; j< width;j++)
            {
                /* Pixel as RGBA: 0xAARRGGBB */
                int pixel = pixels[i * width + j];
                /* Red component 0xAARRGGBB >> 16 = 0x0000AARR */
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                /* Green component 0xAARRGGBB >> 8 = 0x00AARRGG */
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                /* Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB */
                buffer.put((byte) (pixel & 0xFF));
                /* Alpha component 0xAARRGGBB >> 24 = 0x000000AA */
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();         
        Texture fontTexture = Texture.createTexture(width, height, buffer);
        MemoryUtil.memFree(buffer);
        return fontTexture;
    }
    
    private BufferedImage createCharImage(java.awt.Font font, char c, boolean antiAlias)
    {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if(antiAlias)
        {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.dispose();
        
        int charWidth = metrics.charWidth(c);
        int charHeight = metrics.getHeight();
        
        if (charWidth == 0)
        {
            return null;
        }
        
        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if(antiAlias)
        {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        g.setPaint(java.awt.Color.WHITE);
        g.drawString(String.valueOf(c),  0, metrics.getAscent());
        g.dispose();
        return image;
    }
    
    public int getHeight(CharSequence text)
    {
        int height = 0;
        int lineHeight = 0;
        for (int i = 0; i<text.length();i++)
        {
            char c = text.charAt(i);
            if (c == '\n')
            {
                height += lineHeight;
                lineHeight = 0;
                continue;
            }
            if (c == '\r'){continue;}
            
            Glyph g = glyphs.get(c);
            lineHeight = Math.max(lineHeight, g.height);
        }
        height += lineHeight;
        return height;
    }
    
       public int getWidth(CharSequence text) {
        int width = 0;
        int lineWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                width = Math.max(width, lineWidth);
                lineWidth = 0;
                continue;
            }
            if (c == '\r') {continue;}
            
            Glyph g = glyphs.get(c);
            lineWidth += g.width;
        }
        width = Math.max(width, lineWidth);
        return width;
    }
    
    
    
    public void loadFont(String fontFormat,String fontFile)
    {
        
    }
    
    public void dispose()
    {
        texture.delete();
    }
}
