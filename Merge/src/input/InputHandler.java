package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

/**
 * Input Handler. Uses GLFW methods to track whuch byuttons/keys were pressed and saves them into an array.
 *
 * @author Adrian Setniewski, GLFW examples
 *
 */

public final class InputHandler
{
    @SuppressWarnings("unused")
	private static long window;
    private static final int KEYBOARD_SIZE = 512;
    private static final int MOUSE_SIZE = 16;

    private static int[] keyStates = new int[KEYBOARD_SIZE];
    private static boolean[] activeKeys = new boolean[KEYBOARD_SIZE];

    private static int[] mouseButtonStates = new int[MOUSE_SIZE];
    private static boolean[] activeMouseButtons = new boolean[MOUSE_SIZE];

    private static int NO_STATE = -1;


    protected static GLFWKeyCallback keyboard = new GLFWKeyCallback()
    {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods)
        {
            activeKeys[key] = action != GLFW.GLFW_RELEASE;
            keyStates[key] = action;
        }
    };

    protected static GLFWMouseButtonCallback mouse = new GLFWMouseButtonCallback()
    {
        @Override
        public void invoke(long window, int button, int action, int mods)
        {
            activeMouseButtons[button] = action != GLFW.GLFW_RELEASE;
            mouseButtonStates[button] = action;
        }
    };

    public static void init(long window)
    {
        InputHandler.window = window;

        resetKeyboard();
        resetMouse();
    }

    protected static void update()
    {
        resetKeyboard();
        resetMouse();

        GLFW.glfwPollEvents();
    }

    private static void resetKeyboard()
    {
        for (int i = 0; i < keyStates.length; i++)
        {
            keyStates[i] = NO_STATE;
        }
    }

    private static void resetMouse()
    {
        for (int i = 0; i < mouseButtonStates.length; i++)
        {
            mouseButtonStates[i] = NO_STATE;
        }

    }

    public static boolean keyDown(int key)
    {
        return activeKeys[key];
    }

    public static boolean keyPressed(int key)
    {
        return keyStates[key] == GLFW.GLFW_PRESS;
    }

    public static boolean keyReleased(int key)
    {
        return keyStates[key] == GLFW.GLFW_RELEASE;
    }

    public static boolean mouseButtonDown(int button)
    {
        return activeMouseButtons[button];
    }

    public static boolean mouseButtonPressed(int button)
    {
        return mouseButtonStates[button] == GLFW.GLFW_RELEASE;
    }

    public static boolean mouseButtonReleased(int button)
    {
        boolean flag = mouseButtonStates[button] == GLFW.GLFW_RELEASE;

        return flag;
    }

}