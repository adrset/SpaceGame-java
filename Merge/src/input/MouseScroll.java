package input;

import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * MouseScroll class
 *
 * @author Adrian Setniewski
 *
 */

public class MouseScroll extends GLFWScrollCallback{
	private static int xOffset=0;
	private static int yOffset=0;
	
	public static int getOffsetX() {
		return xOffset;
	}
	
	public static int getOffsetY() {
		return yOffset;
	}
	
	@Override
	public void invoke(long window, double xoffset, double yoffset) {
		yOffset+=yoffset;
		xOffset+=xoffset;
	}

}
