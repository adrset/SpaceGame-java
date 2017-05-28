package celestial;

import org.joml.Vector3f;

import models.TexturedModel;

public class Asteroid extends CelestialBody {


	public Asteroid(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ,
			float radius, float density) {
		super(model, position, rotationX, rotationY, rotationZ,new Vector3f(), radius, density, false);
		
	}
	

}
