package me.smonn.crayon.renderer;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class VAOManager {
    private int vaoID;
    private int textureID;

    public void setup(){
        this.vaoID = GL30.glGenVertexArrays();
        this.bind();
    }

    public void bindIndices(IntBuffer indices){
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
    }

    public void bindIndirects(IntBuffer indirects){
        int indirectID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL40.GL_DRAW_INDIRECT_BUFFER, indirectID);
        GL15.glBufferData(GL40.GL_DRAW_INDIRECT_BUFFER, indirects, GL15.GL_STATIC_DRAW);
    }

    public void enableTexture(){
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void bindVertices(FloatBuffer data){
        bindFloats(0, 3, data);
    }

    public void bindTexUVs(FloatBuffer data){
        bindFloats(1, 2, data);
    }

    public void bindNormals(FloatBuffer data){
        bindFloats(2, 3, data);
    }

    public void enableAttributes(){
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(5);

    }

    public void disableAttributes(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(5);
    }

    private void bindFloats(int attributeIndex, int batchSize, FloatBuffer data){
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeIndex, batchSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void bindTexture(String fileName) {
        File file = new File("./res/" + fileName + ".png");
        PNGDecoder decoder = null;
        InputStream in;
        ByteBuffer buffer = null;
        try {
            in = new FileInputStream(file);
            decoder = new PNGDecoder(in);
            buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        // WRAP OR NOT TO WRAP, THAT IS THE QUESTION
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // ME DOEST SMOOTH OR SHARP? SHARP!
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // MIP MAP!
        glGenerateMipmap(textureID);

        // MAKE!
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glBindTexture(GL_TEXTURE_2D, 0);

        this.textureID = textureID;
    }

    public void bind(){
        GL30.glBindVertexArray(vaoID);
    }

    public void unbind(){
        GL30.glBindVertexArray(0);
    }
}
