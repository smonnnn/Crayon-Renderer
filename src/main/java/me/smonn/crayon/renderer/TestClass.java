package me.smonn.crayon.renderer;

import me.smonn.crayon.renderer.entities.Camera;
import me.smonn.crayon.renderer.program.BatchProgram;
import me.smonn.crayon.renderer.util.Maths;
import me.smonn.crayon.renderer.util.OBJLoader;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.File;

public class TestClass {

    public static void main(String[] args){
        init();
        Window.createDisplay();

        Matrix4f[] tempTransform = new Matrix4f[]{Maths.createTransformationMatrix(new Vector3f(0f, 0f, -25f), 0, 0, 0, 1)};

        RenderData data = new RenderData();
        OBJLoader.loadObjModel("dragon", data, tempTransform, tempTransform.length);

        BatchProgram program = new BatchProgram();
        SimpleBatchRenderer renderer = new SimpleBatchRenderer(data, program);
        renderer.build();

        Camera camera = new Camera();

        while(!Display.isCloseRequested()){
            camera.move();
            renderer.prepare();
            program.start();
            program.loadLightTemp();
            program.loadView(camera);
            renderer.render();
            program.stop();
            Window.updateDisplay();
        }
        
        Window.closeDisplay();
        program.exit();
    }

    private static void init(){
        File file;
        switch(LWJGLUtil.getPlatform())
        {
            case LWJGLUtil.PLATFORM_WINDOWS:
            {
                file = new File("./lwjgl/native/windows/");
            }
            break;

            case LWJGLUtil.PLATFORM_LINUX:
            {
                file = new File("./lwjgl/native/linux/");
            }
            break;

            case LWJGLUtil.PLATFORM_MACOSX:
            {
                file = new File("./lwjgl/native/macosx/");
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + LWJGLUtil.getPlatform());
        }
        System.setProperty("org.lwjgl.librarypath", file.getAbsolutePath());
    }
}
