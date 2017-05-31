package weaponry;

import org.joml.Vector3f;

import models.TexturedModel;
import utils.Timer;

public class Bullet {
	@SuppressWarnings("unused")
	private float damage;
	private float existTime;
	private float shotTime;
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f velocity;
	public static TexturedModel bulletModel;
	public static float scale;
	public static float collisionSphereRadius;

	public Vector3f getRotation(){
		return rotation;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public Bullet(float damage, float existTime, Vector3f position, Vector3f velocity, Vector3f rotation){
		this.damage = damage;
		this.rotation = rotation;
		this.existTime = existTime;
		this.position = position;
		this.velocity = velocity;
		this.shotTime = (float) Timer.getCurrentTime();
	}
	
	public boolean shouldExist(){
		return ((float) Timer.getCurrentTime() - shotTime < existTime);
	}
	
	private void increasePosition(){
		this.position.x +=  this.velocity.x * Timer.getLastLoopTime();
		this.position.y +=  this.velocity.y * Timer.getLastLoopTime();
		this.position.z +=  this.velocity.z * Timer.getLastLoopTime();
	}
	
	public void move(){
		increasePosition();
	}

}
