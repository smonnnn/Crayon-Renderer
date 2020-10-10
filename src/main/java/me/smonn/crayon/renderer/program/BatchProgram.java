package me.smonn.crayon.renderer.program;

import me.smonn.crayon.renderer.entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import me.smonn.crayon.renderer.util.Maths;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

public class BatchProgram extends BaseProgram{
    private static final String VERTEX_SHADER = "src/main/java/me/smonn/crayon/renderer/program/shaders/vertexShader.txt";
    private static final String FRAGMENT_SHADER = "src/main/java/me/smonn/crayon/renderer/program/shaders/fragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightColor;
    private int location_lightPos;

    public BatchProgram() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "pos");
        super.bindAttribute(1, "texUV");
        super.bindAttribute(2, "normal");
    }

    @Override
    public void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("mat_transformations");
        location_projectionMatrix = super.getUniformLocation("mat_projection");
        location_viewMatrix = super.getUniformLocation("mat_view");
        location_lightColor = super.getUniformLocation("lightColor");
        location_lightPos = super.getUniformLocation("lightPos");
    }

    public void loadLightTemp(){
        super.loadVec3(location_lightColor, new Vector3f(1.0f, 1.0f, 1.0f));
        super.loadVec3(location_lightPos, new Vector3f(-1f, -1f, -20f));
    }

    public void loadTransformations(FloatBuffer matrices){
        super.loadMatrices(location_transformationMatrix, matrices);
    }

    public void loadView(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, matrix);
    }

    public void loadProjection(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
}
