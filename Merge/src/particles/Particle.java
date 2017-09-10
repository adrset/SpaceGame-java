package particles;

import org.joml.Vector3f;

import utils.Timer;

public class Particle {
	private Vector3f velocity;
	private Vector3f position;
	private float gravityEffect;
	private float lifeLength;
	private float  rotation;
	private float scale;
	private static final float GRAVITY = 0.1f;
	private float elapsedTime = 0;
	private ParticleTexture texture;
	
	ParticleTexture getTexture(){
		return this.texture;
	}
	
	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale) {
		this.texture = texture;
		this.velocity = velocity;
		this.position = position;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}
	
	protected boolean update(){
		velocity.y = (float) (0.01f *  Timer.getLastLoopTime());
		Vector3f change = new Vector3f(velocity);
		change.mul((float)Timer.getLastLoopTime());
		position.add(change);
		elapsedTime+= (float)Timer.getLastLoopTime();
		return elapsedTime < lifeLength;
			
	}

}
