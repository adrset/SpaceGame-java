package celestial;

import org.joml.Vector3f;

import entities.Player;
import models.TexturedModel;
import utils.Timer;

public class HostileShip extends CelestialBody{
	
	private float health;
	@SuppressWarnings("unused")
	private float speedMultiplier;
	@SuppressWarnings("unused")
	private int fireRate;
	@SuppressWarnings("unused")
	private float cash;

	public HostileShip(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ,
			float scale, Vector3f velocity, float health, float speedMultiplier, int fireRate, float cash) {
		super(model, position, rotationX, rotationY, rotationZ, velocity, scale, 1, false);
		this.health = health;
		this.speedMultiplier = speedMultiplier;
		this.fireRate = fireRate;
		this.cash = cash;
				
	}
	
	public boolean isAlive(){
		if(this.health < 0) {
			return false;
		}else{
			return true;
		}
	}
	
	public boolean hitShip(float damage){ // returns true when the ship is still alive
		this.health -= damage;
		
		return isAlive();
	}
	
	public void chasePlayer(Player player){
		Vector3f direction = new Vector3f(player.getPosition());
		direction.sub(this.getPosition());
		float length = direction.length();
		if(length > 1000){
			super.increasePosition(direction.normalize().x * 3000f, direction.normalize().y * 3000f, direction.normalize().z * 3000f, Timer.getLastLoopTime());
		}else {
			
		}
	}

	

}
