package game;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import renderEngine.Loader;
import renderEngine.MasterRenderer;
import scenes.Scene;
import scenes.SceneLoader;
import utils.Logs;
import utils.Timer;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import audio.AudioManager;
import celestial.DataObject;
import gui.Menu;
import input.Keyboard;
import input.Mouse;
import language.Language;

/**
 * Game class. Starts a new thread and handles scenes and menus.
 *
 * @author Adrian Setniewski
 *
 */

public class Game implements Runnable {

	private long window;
	private Thread thread;
	private String title;
	
	public static int width;
	public static int height;
	public static boolean windowShouldClose = false;
	public static Loader loader;
	public static MasterRenderer renderer;
	
	private static boolean vSync;
	private static int multiSampling;
	
	private boolean mode;
	private Scene scene;
	private SceneLoader sceneLoader;
	
	private DataObject dataObject = new DataObject();

	public Game(String name, int desiredWidth, int desiredHeight, boolean mode) {
		this.mode = mode;
		this.title = name;
		Game.width = desiredWidth;
		Game.height = desiredHeight;
		thread = new Thread(this, "Game");
		thread.start();
		vSync = true;
	}

	public void init() {

		Language languageLoader = new Language();
		languageLoader.loadLanguage("english");

		if (!glfwInit()) {
			throw new RuntimeException(Language.getLanguageData("glfw_init_failed"));
		}

		// Set error printing to System.err
		GLFWErrorCallback.createPrint(System.err).set();

		System.out.println(Language.getLanguageData("glfw_version") + Version.getVersion() + "!");

		// Set window to not resizable
		glfwWindowHint(GLFW_RESIZABLE, GL11.GL_FALSE);

		// Create window
		if (mode) {
			window = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), NULL);
		} else {
			window = glfwCreateWindow(width, height, title, NULL, NULL);
		}

		glfwShowWindow(window);

		// center the window
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (int) ((vidmode.width() - width) * 0.5), (int) ((vidmode.height() - height) * 0.5));

		glfwMakeContextCurrent(window);
		// very important
		GL.createCapabilities();

		// disable v-sync
		toggleVideoSync(vSync);

		// antialiasing + multisampling
		setMultiSampling(8);

		// Set input callbacks
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		glfwSetKeyCallback(window, new Keyboard());
		glfwSetCursorPosCallback(window, Mouse.mouseCursor);
		glfwSetScrollCallback(window, Mouse.mouseScroll);
		glfwSetMouseButtonCallback(window, Mouse.mouseButtons);
		// SceneLoader sceneLoader = new SceneLoader();
		
		AudioManager.init();
	}

	public static void setMultiSampling(int amount) {
		multiSampling = amount;
		glfwWindowHint(GLFW_SAMPLES, multiSampling);
	}

	public static int getMultiSampling() {
		return multiSampling;
	}

	public static void toggleVideoSync(boolean state) {
		glfwSwapInterval((state) ? 1 : 0);// could be fitted in the if block xd
		if (state) {
			Logs.printLog("V-sync enabled");
			// System.out.println("V-sync enabled");
			vSync = true;
		} else {
			Logs.printLog("V-sync disabled");
			// System.out.println("V-sync disabled");
			vSync = false;
		}
	}

	public static boolean getVideoSync() {
		return vSync;
	}

	public static float getCurrentVersion() {
		return 1.0f;
	}

	public void run() {
		init();
		// set desired game fps
		Timer.init(60.0f);
		// gameloop
		loop();
		// Free memory etc.
		close();
	}

	private void loop() {

		// loads stuff
		loader = new Loader();
		
		renderer = new MasterRenderer(loader);
		// loads levels
		sceneLoader = new SceneLoader();
		scene = new Scene(sceneLoader, "level1", dataObject);
		renderer.setSkybox(sceneLoader);
		// A master that grabs all its renderers and rules them
	
		// Main menu
		Menu menu = new Menu();
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		// game loop
		while (!glfwWindowShouldClose(window) && !windowShouldClose) {

			if (!Menu.play) {
			
				menu.loop();
			} else {
				// 3d scene loop
				if (!Scene.isFinished) {
					scene.loop();
				}else {
					windowShouldClose = true;
				}
			}

			glfwSwapBuffers(window);

		}
		
		cleanUp();
	}

	private void cleanUp() {
		scene.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		AudioManager.cleanUp();
	}

	private void close() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		// Terminate GLFW
		glfwTerminate();
	}

}
