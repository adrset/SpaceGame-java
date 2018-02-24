package celestial;

import org.joml.Vector3f;

import models.Mesh;

/**
 * Light class. It is passed to shaders to affect all objects in the world by the light.
 * 
 * @author Adrian Setniewski
 *
 */

public class Light extends CelestialBody {
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1, 0, 0);// infinite distance

	public Light(Mesh[] meshes, Vector3f position, Vector3f color, Vector3f attenuation, float radius,
			float density) {

		super(meshes, position, 0, 0, 0, new Vector3f(), radius, density, true, 100000);
		this.color = color;
		this.attenuation = attenuation;
	}

	public Vector3f getAttenuation() {
		return this.attenuation;
	}

	public Light(Mesh[] meshes, Vector3f position, Vector3f color, float radius, float density) {
		super(meshes, position, 0, 0, 0, new Vector3f(), radius, density, true, 10000);

		this.color = color;
	}

	public void setPosition(Vector3f position) {
		super.position = position;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getColor() {
		return color;
	}

}
