package entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import celestial.CelestialBody;
import gui.Menu;
import input.Keyboard;
import models.TexturedModel;
import scenes.Scene;
import utils.Timer;
import weaponry.Weapon;

public class Player extends CelestialBody {

	private float currentSpeed = 0; // velocity is a vector, speed is a scalar
	private float currentSpeedUp = 0;
	private float currentSpeedRotateSpeed = 0;
	float val = 0;
	float val2 = 0;
	private static final float RUN_SPEED = 20;
	private static final float MAX_SPEED = 30000f;
	private static final float ROTATE_SPEED = 30;
	private static boolean inertiaDampener = false;
	public static int score = 0;
	private Weapon weapon;
	
	public boolean isInertiaDampenerOn(){
		return inertiaDampener;
	}

	public Weapon getWeapon() {
		return weapon;
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
		super(model, position, rotationX, rotationY, rotationZ, velocity, radius, density, false);
		this.currentSpeed = speed;
	}

	public float getSpeed() {
		return currentSpeed;
	}

	public void setWeapon(Weapon w) {
		weapon = w;
	}

	public void move() {

		checkInput();
		super.increaseRotation(0, (float) (currentSpeedRotateSpeed * Timer.getLastLoopTime()), 0);
		float Vx = (float) (currentSpeed * Math.sin(Math.toRadians(super.getRotationY()))
				* Math.cos(Math.toRadians(super.getRotationX())));
		float Vz = (float) (currentSpeed * Math.cos(Math.toRadians(super.getRotationY()))
				* Math.cos(Math.toRadians(super.getRotationX())));
		float Vy = (float) (currentSpeedUp + currentSpeed * Math.sin(Math.toRadians(super.getRotationX())));
		super.setVelocity(Vx, Vy, Vz);
		super.increasePosition(Vx, Vy, Vz, Timer.getLastLoopTime());

	}

	public void checkInput() {
		if (Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_Z)) {
			inertiaDampener = inertiaDampener ? false : true;

		}
		float multi = 0, multi2 = 0;
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			if (this.currentSpeed < MAX_SPEED) {
				multi += 50f;
				this.currentSpeed += Math.exp(multi / RUN_SPEED);
			}

		} else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_U)) {
			if (this.currentSpeed < MAX_SPEED) {
				multi += 5f;
				this.currentSpeed += Math.exp(multi / RUN_SPEED);
			}

		} else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
			multi2 += 50f;
			this.currentSpeed -= Math.exp(multi2 / RUN_SPEED);
		} else {
			if (!inertiaDampener) {
				if (currentSpeed > 3) {
					multi2 += 50f;
					this.currentSpeed -= Math.exp(multi2 / RUN_SPEED);
				} else if (currentSpeed < -3) {
					multi2 += 50f;
					this.currentSpeed += Math.exp(multi2 / RUN_SPEED);
				} else {
					this.currentSpeed = 0;
				}
			}

		}

		float multi1 = 0;
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			if (this.currentSpeedUp < MAX_SPEED / 3) {
				multi1 += 50f;
				this.currentSpeedUp += Math.exp(multi1 / (RUN_SPEED * 2));
			}
		} else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_C)) {
			if (this.currentSpeedUp < MAX_SPEED / 3) {
				if (-this.currentSpeedUp < MAX_SPEED) {
					multi1 -= 50f;
					this.currentSpeedUp -= Math.exp(multi1 / (RUN_SPEED * 2));
				}

			}
		} else {
			if (!inertiaDampener) {
				if (currentSpeedUp > 3) {
					this.currentSpeedUp -= Math.exp(2 / RUN_SPEED);
				} else if (currentSpeedUp < -3) {
					this.currentSpeedUp += Math.exp(2 / RUN_SPEED);
				} else {
					this.currentSpeedUp = 0;
				}
			}
		}

		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_Q)) {
			this.currentSpeedRotateSpeed = ROTATE_SPEED;
		} else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_E)) {
			this.currentSpeedRotateSpeed = -ROTATE_SPEED;
		} else {
			this.currentSpeedRotateSpeed = 0;
		}

		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_L)) {
			super.increaseRotation(-0.7f, 0, 0);
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_O)) {
			super.increaseRotation(0.7f, 0, 0);
		}

		if (Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_ESCAPE)) {
			Menu.play = false;
		}

		if (Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_F3)) {
			Scene.isUiVisible = Scene.isUiVisible ? false : true;
		}

		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_T)) {
			val += 0.001f;
			getModel().getModelTexture().setShineDamper(val);
			System.out.println("shine : " + val);
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_G)) {
			val -= 0.001f;
			getModel().getModelTexture().setShineDamper(val);
			System.out.println("shine : " + val);
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_Y)) {
			val2 += 0.001f;
			getModel().getModelTexture().setReflect(val2);
			System.out.println("reflect : " + val2);
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_H)) {
			val2 -= 0.001f;
			getModel().getModelTexture().setReflect(val2);
			System.out.println("reflect : " + val2);
		}

		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_M)) {
			this.currentSpeed = 50;
		}

		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_F)) {
			weapon.fire(getDirection(), new Vector3f(getPosition()), getVelocity(), new Vector3f(getRotationX(), getRotationY() - 90, getRotationZ()));
		}
		if (Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_R)) {
			weapon.reload();
		}
	}

}
