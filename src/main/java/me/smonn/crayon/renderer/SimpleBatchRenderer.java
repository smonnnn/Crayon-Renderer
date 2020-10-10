package me.smonn.crayon.renderer;

import me.smonn.crayon.renderer.program.BatchProgram;
import me.smonn.crayon.renderer.util.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;

public class SimpleBatchRenderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000.0f;

    private RenderData renderData;
    private VAOManager vao;

    public SimpleBatchRenderer(RenderData data, BatchProgram program){
        this.renderData = data;
        this.vao = new VAOManager();
        program.start();
        program.loadProjection(Maths.createProjectionMatrix(FAR_PLANE, NEAR_PLANE, FOV));
        program.loadTransformations(data.getTransforms());
        program.stop();
    }

    public void build(){
        vao.setup();
        vao.bindVertices(renderData.getVertices());
        vao.bindTexUVs(renderData.getTexUVs());
        vao.bindTexture("white");
        vao.bindIndices(renderData.getIndices());
        vao.bindNormals(renderData.getNormals());
        vao.bindIndirects(renderData.getIndirects());
    }

    public void render(){
        vao.bind();
        vao.enableAttributes();
        vao.enableTexture();
        GL43.glMultiDrawElementsIndirect(GL_TRIANGLES, GL_UNSIGNED_INT, vao.getIndirectID(), renderData.getDrawCallCount(), 0);
        vao.disableAttributes();
        vao.unbind();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0, 0, 1);
    }
}
