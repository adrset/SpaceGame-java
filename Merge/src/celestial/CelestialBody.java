package celestial;

import org.joml.Vector3f;

import entities.Entity;
import models.TexturedModel;

public class CelestialBody extends Entity {
	private float mass;
	private float radius;
	
	public float getMass(){
		return mass;
	}
	
	public float getRadius(){
		return radius;
	}
	
	public CelestialBody(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ,Vector3f velocity,
			float radius, float density, boolean isSun) {
		
		super(model, position, rotationX, rotationY, rotationZ, (float) (radius/model.getRawModel().getDistance()), velocity);//xD
		this.radius = radius;
		mass = (float) (density *4/3 * Math.PI * Math.pow(radius,3));
	}
	

}
