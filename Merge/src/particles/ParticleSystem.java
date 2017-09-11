package particles;

import org.joml.Vector3f;

import utils.Timer;

public class ParticleSystem{
     
    private float pps;
    private float speed;
    private float gravityComplient;
    private float lifeLength;
    private ParticleTexture texture;
    private float scale;
    Vector3f reusableVelocity = new Vector3f();
     
    public ParticleSystem(ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength, float scale) {
    	this.texture = texture;
        this.pps = pps;
        this.scale = scale;
        this.speed = speed;
        this.gravityComplient = gravityComplient;
        this.lifeLength = lifeLength;
    }
     
    public void generateParticles(Vector3f systemCenter){
        float delta = (float) Timer.getLastLoopTime();
        float particlesToCreate = pps * delta;
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for(int i=0;i<count;i++){
            emitParticle(systemCenter);
        }
        if(Math.random() < partialParticle){
            emitParticle(systemCenter);
        }
    }
     
    private void emitParticle(Vector3f center){
        float dirX = (float) Math.random() * 2f - 1f;
        float dirZ = (float) Math.random() * 2f - 1f;
        float dirY = (float) Math.random() * 2f - 1f;
        Vector3f velocity = new Vector3f(dirX, (dirY), dirZ);
        velocity.normalize();
        velocity.mul(speed);
        new Particle(texture, new Vector3f(center), velocity, gravityComplient, lifeLength, 0, scale);
    }
     
 
}