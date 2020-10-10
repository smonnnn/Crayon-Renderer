package me.smonn.crayon.renderer.util;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

public class AutoIntBuffer {
    private int[] buffer;
    private int usedSize = 0;

    public AutoIntBuffer(int size){
        buffer = new int[size];
    }

    private void increaseCapacity(int size){
        int[] newBuffer = new int[size + buffer.length];
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        this.buffer = newBuffer;
    }

    public void put(int[] arr){
        if(buffer.length < arr.length + usedSize){
            increaseCapacity(arr.length);
        }
        System.arraycopy(arr, 0, buffer, usedSize, arr.length);
        usedSize+=arr.length;
    }

    public IntBuffer get(){
        IntBuffer buff = BufferUtils.createIntBuffer(buffer.length);
        buff.put(buffer);
        buff.flip();
        return buff;
    }

    public int getBufferSize(){
        return buffer.length;
    }
}
