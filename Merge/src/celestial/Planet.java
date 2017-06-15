package celestial;

import org.joml.Vector3f;

import entities.Entity;
import models.TexturedModel;
import utils.Timer;

/**
 * Planet class. Planets are celestial bodies although in our application they
 * are not affected by gravity - they follow predefined orbits. They only
 * produce it.
 *
 * @author Adrian Setniewski
 *
 */

public class Planet extends CelestialBody {

	private float majorAxis;
	private float minorAxis;
	private float startAngle;
	private float currentAngle;
	private float angularVelocity;
	private float rotationAround;
	private String name;

	// constructors
	public Planet(TexturedModel model, Vector3f rotation, float startAngle, float density, float radius,
			float majorAxis, float minorAxis, float angularVelocity, float rotationAround) {
		// assuming that sun will always be at y = 0 and planets will loop on y
		// = 0 surface
		super(model,
				new Vector3f((float) (majorAxis * Math.cos(Math.toRadians(startAngle))), 0f,
						(float) (minorAxis * Math.sin(Math.toRadians(startAngle)))),
				rotation.x, rotation.y, rotation.z, new Vector3f(), radius, density, false, 10000);

		this.angularVelocity = angularVelocity;
		this.startAngle = startAngle;
		this.majorAxis = majorAxis;
		this.minorAxis = minorAxis;
		this.currentAngle = startAngle;
		this.rotationAround = rotationAround;
	}

	// getters
	public float getRotationAround() {
		return rotationAround;
	}

	public Entity getEntity() {
		return this;
	}

	public String getName() {
		return this.name;
	}

	public float getStartAngle() {
		return this.startAngle;
	}

	public float getCurrentAngle() {
		return this.currentAngle;
	}

	public float getMajorAxis() {
		return majorAxis;
	}

	public float getMinorAxis() {
		return minorAxis;
	}

	// setters
	public void setRotationAround(float rot) {
		this.rotationAround = rot;
	}

	public void setStartAngle(float angle) {
		this.startAngle = angle;
	}

	public void setCurrentAngle(float angle) {
		this.currentAngle = angle;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMajorAxis(float majorAxis) {
		this.majorAxis = majorAxis;
	}

	public void setMinorAxis(float minorAxis) {
		this.minorAxis = minorAxis;
	}

	// other methods
	public void rotateAround() {
		increaseRotation(0, (float) (rotationAround * Timer.getLastLoopTime()), 0);
	}

	public void move() {
		rotateAround();
		this.currentAngle = this.startAngle + (float) (angularVelocity * Timer.getCurrentFakeTime());
		super.setPosition(new Vector3f((float) (majorAxis * Math.cos(Math.toRadians(this.currentAngle))), 0f,
				(float) (minorAxis * Math.sin(Math.toRadians(this.currentAngle)))));
	}
	
	public Vector3f predictMove(float dt) {
		float predictedAngle = this.startAngle + (float) (angularVelocity * dt);
		Vector3f predictedMove= new Vector3f((float) (majorAxis * Math.cos(Math.toRadians(predictedAngle))), 0f,
				(float) (minorAxis * Math.sin(Math.toRadians(predictedAngle))));
		return predictedMove;
		
	}

}
