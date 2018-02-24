package entities;

import org.joml.Vector3f;

import models.GameItem;
import models.Mesh;

/**
 * Entity class that contains basic info about an entity, like it's position,
 * scale and model. Currently it is used directly for objects without mass.
 * 
 * @author Adrian Setniewski
 *
 */

public class Entity extends GameItem {

	private static Vector3f maxS1 = new Vector3f();
	private static Vector3f minS1 = new Vector3f();
	private static Vector3f maxS2 = new Vector3f();
	private static Vector3f minS2 = new Vector3f();
	
	private Vector3f velocity;

	// Copy constructor
	public Entity(Mesh[] meshes, Vector3f position, float rotationX, float rotationY, float rotationZ,
			float scale, Vector3f velocity) {
		super(meshes);
		super.position = position;
		super.scale = scale;
		this.velocity = velocity;
	}
	
	public boolean checkCollision(Entity e) {
		/*maxS1.set(model.getRawModel().getMax());
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
				    minS1.z < maxS2.z);*/
		return false;
		
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void move() {
		increasePosition(velocity);
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


}
