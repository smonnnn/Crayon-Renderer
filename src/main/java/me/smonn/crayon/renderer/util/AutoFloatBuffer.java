package me.smonn.crayon.renderer.util;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class AutoFloatBuffer {
    private float[] buffer;
    private int usedSize = 0;

    public AutoFloatBuffer(int size){
        buffer = new float[size];
    }

    private void increaseCapacity(int size){
        float[] newBuffer = new float[size + buffer.length];
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        this.buffer = newBuffer;
    }

    public void put(float[] arr){
        if(buffer.length < arr.length + usedSize){
            increaseCapacity(arr.length);
        }
        System.arraycopy(arr, 0, buffer, usedSize, arr.length);
        usedSize+=arr.length;
    }

    public FloatBuffer get(){
        FloatBuffer buff = BufferUtils.createFloatBuffer(buffer.length);
        buff.put(buffer);
        buff.flip();
        return buff;
    }
}
