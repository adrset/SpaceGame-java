package input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButtons extends GLFWMouseButtonCallback {
	public static int[] keysPressed = new int[24];
	public static int[] keysLastPressed = new int[24];
	public static boolean mouseUsed = false;

	@Override
	public void invoke(long window, int button, int action, int mods) {
		mouseUsed = false;
		keysPressed[button] = action;
	}

	public static int getButtonState(int button) {
		return keysPressed[button];
	}

	public static int getButtonPressedOnce(int button) {
		if (keysPressed[button] == 0) {
			keysLastPressed[button] = 0;
			return keysPressed[button];

		} else {
			if (keysLastPressed[button] == 0) {
				keysLastPressed[button] = 1;
				return keysPressed[button];
			} else {
				return 0;
			}
		}

	}

}
