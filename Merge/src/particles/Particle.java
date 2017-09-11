package particles;

import org.joml.Vector2f;
import org.joml.Vector3f;

import entities.Camera3D;
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
	private Vector2f texOffset1 = new Vector2f();
	
	Vector3f change = new Vector3f();
	
	private Vector3f dist = new Vector3f();
	private float distance;
	
	private boolean isAlive = false;
	
	public float getDistance() {
		return this.distance;
	}
	public Vector2f getTexOffset1() {
		return texOffset1;
	}

	public Vector2f getTexOffset2() {
		return texOffset2;
	}

	public float getBlend() {
		return blend;
	}

	private Vector2f texOffset2 = new Vector2f();
	private float blend;
	
	ParticleTexture getTexture(){
		return this.texture;
	}
	
	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale) {
		this.isAlive = true;
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
	
	private void updateTextureCoords() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getRows() * texture.getRows();
		
		float atlasProgression = lifeFactor * stageCount;
		
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1; 
		this.blend = atlasProgression % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	}
	
	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getRows();
		int row = index / texture.getRows();
		offset.x = (float)column / texture.getRows();
		offset.y = (float)row / texture.getRows();
	}
	
	protected boolean update(Camera3D camera){
		change.set(velocity);
		change.mul((float)Timer.getLastLoopTime());
		distance = dist.set(camera.getPosition()).sub(position).lengthSquared();
		
		position.add(change);
		updateTextureCoords();
		elapsedTime+= (float)Timer.getLastLoopTime();
		if (elapsedTime < lifeLength) {
			return true;
		}else {
			isAlive = false;
			return false;
		}
			
	}

}
