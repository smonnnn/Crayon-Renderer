package me.smonn.crayon.renderer.program;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class BaseProgram {
    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    /**
     * A shader program is a set of a vertex and fragment shader that are used to render an object.
     * In this constructor we load in both the vertex shader and fragment shader,
     * create a new program, bind them and their attributes to it,
     * link and validate the program.
     *
     * @param vertexFile
     * File location of the vertex shader.
     *
     * @param fragmentFile
     * File location of the Fragment shader
     */
    public BaseProgram(String vertexFile, String fragmentFile){
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        matbuf1 = BufferUtils.createFloatBuffer(16);
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }

    /**
     * Starts the program.
     */
    public void start(){
        GL20.glUseProgram(programID);
    }

    /**
     * Stops the program.
     */
    public void stop(){
        GL20.glUseProgram(0);
    }

    /**
     * Deletes and detaches this program and its shaders.
     */
    public void exit(){
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    /**
     * The attribute is used inside of the vertex shader.
     * With this function we bind the data of each Vertex Buffer Object on each Vertex Array Object to the variables used inside of the shaders.
     *
     * @param attribute
     * The index of the VBO inside of the VAO getting used by the program.
     * @param name
     * The name used inside of the shader's code.
     */
    protected void bindAttribute(int attribute, String name){
        GL20.glBindAttribLocation(programID, attribute, name);
    }

    protected void loadFloat(int location, float value){
        GL20.glUniform1f(location, value);
    }

    protected void loadVec3(int location, Vector3f value){
        GL20.glUniform3f(location, value.x, value.y, value.z);
    }

    protected void loadBool(int location, boolean value){
        int i = 0;
        if(value){
            i = 1;
        }
        GL20.glUniform1f(location, i);
    }

    public static FloatBuffer matbuf1;
    protected void loadMatrix(int location, Matrix4f value){
        value.store(matbuf1);
        matbuf1.flip();
        GL20.glUniformMatrix4(location, false, matbuf1);
    }

    protected void loadMatrices(int location, FloatBuffer matrices){
        GL20.glUniformMatrix4(location, false, matrices);
    }

    /**
     * Abstract function that should be used for ShaderProgram objects to bind their VAO's VBOs to the shader's variables.
     * Example: super.bindAttribute(0, "pos");
     */
    protected abstract void bindAttributes();

    /**
     * Loads the shader file from disk, creates a new shader object, compiles them and returns the shader's ID.
     *
     * @param filename
     * Shader's file name
     * @param type
     * Vertex or Fragment shader
     * @return
     * ID of the shader loaded.
     */
    private static int loadShader(String filename, int type){
        StringBuilder shaderSource = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while((line = reader.readLine()) !=null){
                shaderSource.append(line).append("\n");

            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error getting shader from file " + filename);
            e.printStackTrace();
            System.exit(-1);
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.out.println("Error compiling shader " + filename);
            System.exit(-1);
        }
        return shaderID;
    }

    public abstract void getAllUniformLocations();

    /**
     * Used to get the uniform variable of this program from a name.
     *
     * @param uniformName
     * The name of the uniform variable needed.
     *
     * @return
     * The ID of the uniform's location.
     */
    protected int getUniformLocation(String uniformName){
        return GL20.glGetUniformLocation(programID, uniformName);
    }

}
