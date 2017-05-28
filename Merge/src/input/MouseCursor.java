package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseCursor extends GLFWCursorPosCallback{
	private static double posX=0;
	private static double posY=0;
	private static double dx=0;
	private static double dy=0;
	
	@Override
	public void invoke(long window, double xpos, double ypos) {
		
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT) || MouseButtons.getButtonState(2) == 1){
			dy -= (int)ypos - posY;
		}else {
			dy = 0;
		}
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT) || MouseButtons.getButtonState(2) == 1){
			dx += (int)xpos - posX;
		}else{
			dx = 0;
		}
        
		
		posX = xpos;
		posY = ypos;
	}
	
	public static double getPosX() {
		return posX;
	}

	public static double getPosY() {
		return posY;
	}
	
	public double getDX(){
		double temp = dx;
		dx=0;
		return temp ;
	}
	 
	public double getDY(){
		double temp = dy;
		dy=0;
	    return temp ;
	}
	

}
