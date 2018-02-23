package entities;

import org.lwjgl.glfw.GLFW;

import input.Keyboard;
import input.Mouse;
import input.MouseButtons;
import input.MouseScroll;

/**
 * Extended camera class for 3D environment.
 * Contains functionality allowing to change cameras position according to player.
 * 
 * @author Przemyslaw Nowak, Adrian Setniewski
 *
 */
public class Camera3D extends Camera {
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private Player player;

	public Camera3D(Player player) {
		super();
		this.player = player;

	}

	public void move() {

		calculateZoom();
		calculatePitch();
		calculateAngle();
		calculateCameraPosition(calculateHorizontalDistance());
	}

	private void calculateCameraPosition(float hDistance) {
		float theta = player.getRotationY() - angleAroundPlayer;
		float phi = player.getRotationX();
		float xOffset = (float) (hDistance * Math.sin(Math.toRadians(-theta)) * Math.cos(Math.toRadians(phi)));
		float zOffset = (float) (hDistance * Math.cos(Math.toRadians(theta + 180)) * Math.cos(Math.toRadians(phi)));
		float yOffset = (float) (hDistance * Math.sin(Math.toRadians(phi)));
		super.position.x = player.getPosition().x + xOffset;
		super.position.z = player.getPosition().z + zOffset;
		super.position.y = player.getPosition().y - yOffset;
		super.yaw = 180 - (player.getRotationY() - angleAroundPlayer);
		super.pitch = 0 - (player.getRotationX());
	}

	private void calculateZoom() {
		float zoom = MouseScroll.getOffsetY() * 0.1f;
		distanceFromPlayer -= zoom;
	}

	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private void calculatePitch() {
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT) || MouseButtons.getButtonState(2) == 1) {
			double dPitch = Mouse.mouseCursor.getDY() * 0.3;
			super.pitch -= (float) dPitch;
		} else {
			super.pitch = 0;
		}
	}

	public void calculateAngle() {
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT) || MouseButtons.getButtonState(2) == 1) {
			double dAngle = Mouse.mouseCursor.getDX() * 0.3;
			angleAroundPlayer -= (float) dAngle;
		} else {
			angleAroundPlayer = 0;
		}
	}
}
