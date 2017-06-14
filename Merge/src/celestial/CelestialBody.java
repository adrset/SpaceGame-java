package celestial;

import org.joml.Vector3f;

import entities.Entity;
import models.TexturedModel;

public class CelestialBody extends Entity {
	private float mass;
	private float radius;
	private boolean isAlive = true;
	
	public boolean isAlive(){
		return this.isAlive;
	}
	public void setDead(){
		isAlive = false;
	}
	
	public float getMass(){
		return mass;
	}
	
	public float getRadius(){
		return radius;
	}
	
	public void setRadius(float r){
		this.radius = r;
	}
	
	public CelestialBody(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ,Vector3f velocity,
			float radius, float density, boolean isSun) {
		
		super(model, position, rotationX, rotationY, rotationZ, (float) (radius/model.getRawModel().getDistance()), velocity);//xD
		this.radius = radius;
		mass = (float) (density *4/3 * Math.PI * Math.pow(radius,3));
	}
	

}
