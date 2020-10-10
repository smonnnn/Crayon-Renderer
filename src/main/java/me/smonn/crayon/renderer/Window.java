package me.smonn.crayon.renderer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class Window {
	private static final int width = 1280;
	private static final int height = 720;
	private static final int fps_cap = 120;
	private static final String title = "Test Window";

	/**
	 * Creates a new display.
	 *
	 */
	public static void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat());
			Display.setTitle(title);
			Display.setResizable(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Each display update we use the fps cap, set the viewport of openGL to the resized window if it was resized and update the frame.
	 */
	public static void updateDisplay() {
		Display.sync(fps_cap);
		if(Display.wasResized()){
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		}
		Display.update();
	}

	/**
	 * Closes the display.
	 */
	public static void closeDisplay() {
		Display.destroy();
	}

}
