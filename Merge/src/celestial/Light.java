package celestial;

import org.joml.Vector3f;

import models.TexturedModel;

public class Light extends CelestialBody {
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1, 0, 0);// infinite distance

	public Light(TexturedModel model, Vector3f position, Vector3f color, Vector3f attenuation, float radius,
			float density) {

		super(model, position, 0, 0, 0, new Vector3f(), radius, density, true);
		this.color = color;
		this.attenuation = attenuation;
		super.getModel().getModelTexture().setFakeLight(true);
	}

	public Vector3f getAttenuation() {
		return this.attenuation;
	}

	public Light(TexturedModel model, Vector3f position, Vector3f color, float radius, float density) {
		super(model, position, 0, 0, 0, new Vector3f(), radius, density, true);

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
