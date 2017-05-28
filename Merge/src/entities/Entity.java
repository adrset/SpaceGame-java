package entities;

import org.joml.*;

import models.TexturedModel;

/**
 * Entity class that contains basic info about an entity, like it's position,
 * scale and model
 * 
 * @author Adrian Setniewski
 *
 */

public class Entity {
	protected Vector3f position;
	private Vector3f velocity;
	private float rotationX, rotationY, rotationZ;
	private float scale;
	private TexturedModel model;


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
		this.velocity=velocity;
//xD		velocity = new Vector3f();
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

	public void setPosition(float dx, float dy, float dz) {
		this.position.x = dx;
		this.position.y = dy;
		this.position.z = dz;
//		setVelocity(dx, dy, dz);

	}
	
	
	
	// methods
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
//		setVelocity(dx, dy, dz);

	}
	public void increasePosition(Vector3f dr) {
		this.position.x += dr.x;
		this.position.y += dr.y;
		this.position.z += dr.z;
//		setVelocity(dr.x, dr.y, dr.z);

	}

	
	public void increasePosition(float dx, float dy, float dz, double time) {
		this.position.x += (dx*time);
		this.position.y += (dy*time);
		this.position.z += (dz*time);
//		setVelocity(dx, dy, dz);

	}
	public void increasePosition(Vector3f dr, double time) {
		this.position.x += (dr.x*time);
		this.position.y += (dr.y*time);
		this.position.z += (dr.z*time);
//		setVelocity(dr.x, dr.y, dr.z);

	}
	
	
	
	public void setVelocity(float dx, float dy, float dz) {
		velocity.x = dx;
		velocity.y = dy;
		velocity.z = dz;
	}

	public Vector3f getVelocity(){
		return velocity;
	}
	
	public void setVelocity(Vector3f velocity){
		this.velocity = velocity;
	}
	
	public void increaseVelocity(float dx, float dy, float dz) {
		velocity.x += dx;
		velocity.y += dy;
		velocity.z += dz;
	}

	
	public void increaseVelocity(Vector3f velocity){
		this.velocity.add(velocity) ;
	}
	
	
	
	
	
	public void increaseRotation(float rx, float ry, float rz) {
		this.rotationX += rx;
		this.rotationY += ry;
		this.rotationZ += rz;
	}

}
