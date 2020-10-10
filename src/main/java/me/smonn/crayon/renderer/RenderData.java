package me.smonn.crayon.renderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import me.smonn.crayon.renderer.util.AutoFloatBuffer;
import me.smonn.crayon.renderer.util.AutoIntBuffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class RenderData {
    private AutoFloatBuffer vertices;
    private AutoFloatBuffer texUVs;
    private AutoIntBuffer indices;
    private AutoFloatBuffer normals;
    private AutoIntBuffer indirects;
    private AutoFloatBuffer transforms;

    private int gl_InstanceID = 0;
    private int indicesOffset = 0;

    public RenderData(){
        this.vertices = new AutoFloatBuffer(60);
        this.texUVs = new AutoFloatBuffer(40);
        this.indices = new AutoIntBuffer(30);
        this.normals = new AutoFloatBuffer(60);
        this.indirects = new AutoIntBuffer(50);
        this.transforms = new AutoFloatBuffer(160);
    }

    public void addDrawCall(float[] vertices, int[] indices, float[] texUVs, float[] normals, Matrix4f[] transforms, int renderCount){
        this.vertices.put(vertices);
        this.texUVs.put(texUVs);
        this.indices.put(indices);
        this.normals.put(normals);
        this.addTransforms(transforms);
        this.addIndirect(vertices.length, renderCount, indices.length);
    }

    private void addIndirect(int vertexCount, int renderCount, int indicesCount){
        indirects.put(new int[]{vertexCount, renderCount, 0, indicesOffset, gl_InstanceID});
        gl_InstanceID+=renderCount;
        indicesOffset+=indicesCount;
    }

    private float[] tempBuffer = new float[16];
    private void addTransforms(Matrix4f[] matrices){
        for (Matrix4f matrix : matrices) {
            tempBuffer[0] = matrix.m00;
            tempBuffer[1] = matrix.m01;
            tempBuffer[2] = matrix.m02;
            tempBuffer[3] = matrix.m03;
            tempBuffer[4] = matrix.m10;
            tempBuffer[5] = matrix.m11;
            tempBuffer[6] = matrix.m12;
            tempBuffer[7] = matrix.m13;
            tempBuffer[8] = matrix.m20;
            tempBuffer[9] = matrix.m21;
            tempBuffer[10] = matrix.m22;
            tempBuffer[11] = matrix.m23;
            tempBuffer[12] = matrix.m30;
            tempBuffer[13] = matrix.m31;
            tempBuffer[14] = matrix.m32;
            tempBuffer[15] = matrix.m33;
            transforms.put(tempBuffer);
        }
    }

    public FloatBuffer getVertices(){
        return vertices.get();
    }

    public FloatBuffer getTexUVs(){
        return texUVs.get();
    }

    public IntBuffer getIndices(){
        return indices.get();
    }

    public IntBuffer getIndirects(){
        return indirects.get();
    }

    public FloatBuffer getTransforms(){
        return transforms.get();
    }

    public FloatBuffer getNormals(){
        return normals.get();
    }

    public int getDrawCallCount(){
        return this.indirects.getBufferSize() / 5;
    }
}


