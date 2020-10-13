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

        Matrix4f[] tempTransform = new Matrix4f[2];

        tempTransform[0] = Maths.createTransformationMatrix(new Vector3f(-10, 10, -20), 0, 0, 0, 1f);
        tempTransform[1] = Maths.createTransformationMatrix(new Vector3f(10, 10, -20), 0, 0, 0, 1f);

        Matrix4f[] tempTransform2 = new Matrix4f[2];

        tempTransform2[0] = Maths.createTransformationMatrix(new Vector3f(-10, -10, -20), 0, 0, 0, 1f);
        tempTransform2[1] = Maths.createTransformationMatrix(new Vector3f(10, -10, -20), 0, 0, 0, 1f);

        RenderData data = new RenderData();
        OBJLoader.loadObjModel("dragon", data, tempTransform, tempTransform.length);
        OBJLoader.loadObjModel("dragon", data, tempTransform2, tempTransform2.length);

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
            renderer.render(program);
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
