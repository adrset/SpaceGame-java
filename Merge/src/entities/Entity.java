package entities;

import java.util.Random;

import org.joml.Vector3f;

import models.TexturedModel;

/**
 * Entity class that contains basic info about an entity, like it's position,
 * scale and model. Currently it is used directly for objects without mass.
 * 
 * @author Adrian Setniewski
 *
 */

public class Entity {

	private static Vector3f maxS1 = new Vector3f();
	private static Vector3f minS1 = new Vector3f();
	private static Vector3f maxS2 = new Vector3f();
	private static Vector3f minS2 = new Vector3f();
	protected Vector3f position;
	private Vector3f velocity;
	private float rotationX, rotationY, rotationZ;
	private float scale;
	private TexturedModel model;
	private static Random generator = new Random();
	private float timeElapsed = 0f;
	private float timeChanged = 0f;
	private int period;

	public Entity(Entity a) {
		this.position = new Vector3f(a.getPosition());
		this.rotationX = a.getRotationX();
		this.rotationY = a.getRotationY();
		this.rotationZ = a.getRotationZ();
		this.model = a.getModel();
		this.scale = a.getScale();
		velocity = new Vector3f();
	}

	// Copy constructor
	public Entity(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ,
			float scale, Vector3f velocity) {
		this.model = model;
		this.position = position;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
		this.scale = scale;
		this.velocity = velocity;
		period = 5 + generator.nextInt(10);
	}
	
	public boolean checkCollision(Entity e) {
		maxS1.set(model.getRawModel().getMax());
		maxS2.set(e.getModel().getRawModel().getMax());
		
		minS1.set(model.getRawModel().getMin());
		minS2.set(e.getModel().getRawModel().getMin());
	
		maxS1.mul(scale).add(position);
		maxS2.mul(e.getScale()).add(e.getPosition());
		
		minS1.mul(scale).add(position);
		minS2.mul(e.getScale()).add(e.getPosition());
	        
		 return(maxS1.x > minS2.x &&
				 minS1.x < maxS2.x &&
				    maxS1.y > minS2.y &&
				    minS1.y < maxS2.y &&
				    maxS1.z > minS2.z &&
				    minS1.z < maxS2.z);
		
	}

	// setters
	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public void setRotationZ(float rotationZ) {
		this.rotationZ = rotationZ;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	// getters
	public TexturedModel getModel() {
		return model;
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public float getScale() {
		return scale;
	}

	public float getRotationY() {
		return rotationY;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotationX() {
		return rotationX;
	}

	public void move() {
		increasePosition(velocity);
		/*if (timeElapsed > period * Math.pow(10, 9)) {
			setVelocity(-0.5f + generator.nextFloat(), -0.5f + generator.nextFloat(), -0.5f + generator.nextFloat());
			timeChanged = (float) (System.nanoTime());
			timeElapsed = 0f;
		} else {
			timeElapsed = (float) (System.nanoTime() - timeChanged);
		}*/
	}

	public void setPosition(float dx, float dy, float dz) {
		this.position.x = dx;
		this.position.y = dy;
		this.position.z = dz;
	}

	// other methods
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increasePosition(Vector3f dr) {
		this.position.x += dr.x;
		this.position.y += dr.y;
		this.position.z += dr.z;
	}

	public void increasePosition(float dx, float dy, float dz, double time) {
		this.position.x += (dx * time);
		this.position.y += (dy * time);
		this.position.z += (dz * time);
	}

	public void increasePosition(Vector3f dr, double time) {
		this.position.x += (dr.x * time);
		this.position.y += (dr.y * time);
		this.position.z += (dr.z * time);
	}

	public void setVelocity(float dx, float dy, float dz) {
		velocity.x = dx;
		velocity.y = dy;
		velocity.z = dz;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}

	public void increaseVelocity(float dx, float dy, float dz) {
		velocity.x += dx;
		velocity.y += dy;
		velocity.z += dz;
	}

	public void increaseVelocity(Vector3f velocity) {
		this.velocity.add(velocity);
	}

	public void increaseRotation(float rx, float ry, float rz) {
		this.rotationX += rx;
		this.rotationY += ry;
		this.rotationZ += rz;
	}

}
