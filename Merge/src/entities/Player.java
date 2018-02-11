package entities;

import java.awt.Menu;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import celestial.CelestialBody;
import input.Keyboard;
import models.TexturedModel;
import scenes.Scene;
import utils.Timer;

/**
 * Player class. Currently there's only one player. This class handles player
 * movement.
 *
 * @author Adrian Seniewski
 *
 */

public class Player extends CelestialBody {

	private float currentForce = 0;
	private float currentForceUp = 0;
	private float currentSpeedRotateSpeed = 0;
	float val = 0;
	float val2 = 0;
	private static final float RUN_SPEED = 20f;
	private static final float MAX_SPEED = 100f;
	private static final float ROTATE_SPEED = 60;
	private static boolean inertiaDampener = false;
	public boolean force = false;
	private int score = 0;

	public void addScore(int number) {
		this.score += number;
	}

	public int getScore() {
		return this.score;
	}

	public boolean isInertiaDampenerOn() {
		return inertiaDampener;
	}

	public Vector3f getDirection() {
		float x = (float) (Math.sin(Math.toRadians(super.getRotationY()))
				* Math.cos(Math.toRadians(super.getRotationX())));
		float z = (float) (Math.cos(Math.toRadians(super.getRotationY()))
				* Math.cos(Math.toRadians(super.getRotationX())));
		float y = (float) (Math.sin(Math.toRadians(super.getRotationX())));
		return new Vector3f(x, y, z);
	}

	public Player(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ,
			Vector3f velocity, float speed, float radius, float density) {
		super(model, position, rotationX, rotationY, rotationZ, velocity, radius, density, false, 10000);

	}

	public float getSpeed() {
		return currentForce;
	}
	
	public void move() {

		checkInput();
		super.increaseRotation(0, (float) (currentSpeedRotateSpeed * Timer.getLastLoopTime()), 0);
		float Fx = (float) (currentForce * Math.sin(Math.toRadians(super.getRotationY()))
				* Math.cos(Math.toRadians(super.getRotationX())));
		float Fz = (float) (currentForce * Math.cos(Math.toRadians(super.getRotationY()))
				* Math.cos(Math.toRadians(super.getRotationX())));
		float Fy = (float) (currentForceUp + currentForce * Math.sin(Math.toRadians(super.getRotationX())));
		increasePosition(Fx, Fy, Fz);

	}

	public void checkInput() {

		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			if (Math.abs(this.currentForce) < MAX_SPEED) {
				this.currentForce += 0.1f;
			}
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
			if (Math.abs(this.currentForce) < MAX_SPEED) {
				this.currentForce -= 0.1f;
			}
		}
		if(!Keyboard.isKeyDown(GLFW.GLFW_KEY_S) && !Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			if(Math.abs(this.currentForce) < 0.07f) {
				this.currentForce =0f;
			}
			if(this.currentForce >0) {
				this.currentForce-=0.05f;
			}else if(this.currentForce <0){
				this.currentForce+=0.05f;
			}
		}
		
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			if (Math.abs(this.currentForceUp) < MAX_SPEED) {
				this.currentForceUp += 0.1f;
			}
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_C)) {
			if (Math.abs(this.currentForceUp) < MAX_SPEED) {
				this.currentForceUp -= 0.1f;
			}
		}
		if(!Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE) && !Keyboard.isKeyDown(GLFW.GLFW_KEY_C)) {
			if(Math.abs(this.currentForceUp) < 0.07f) {
				this.currentForceUp =0f;
			}
			if(this.currentForceUp >0) {
				this.currentForceUp-=0.05f;
			}else if(this.currentForceUp <0){
				this.currentForceUp+=0.05f;
			}
		}
		
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_Q)) {
			this.currentSpeedRotateSpeed = ROTATE_SPEED;
		} else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_E)) {
			this.currentSpeedRotateSpeed = -ROTATE_SPEED;
		} else {
			this.currentSpeedRotateSpeed = 0;
		}
		
		
		

	}

}