package weaponry;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import audio.AudioManager;
import audio.AudioSource;
import celestial.DataObject;
import celestial.HostileShip;
import models.TexturedModel;
import utils.Timer;

public class Weapon {

	@SuppressWarnings("unused")
	private String name;
	private int fireRate;
	private List<Bullet> bullets;
	private float damage;
	private int startAmmo;
	private int currentAmmunition;
	private DataObject dataObject;
	private int currentSource = 0;
	private List<AudioSource> sources = new ArrayList<AudioSource>();
	private static int sound = AudioManager.loadSound("res/audio/rifle.wav");
	private float lastFireTime;

	public int getStartAmmo() {
		return startAmmo;
	}

	public int getCurrentAmmunition() {
		return currentAmmunition;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public Weapon(String name, int fireRate, DataObject dataObject, int startAmmo, int damage,
			TexturedModel bulletModel, Vector3f position, float scale) {
		this.dataObject = dataObject;
		Bullet.scale = scale;
		Bullet.bulletModel = bulletModel;
		Bullet.collisionSphereRadius = 43f * scale;
		this.name = name;
		this.fireRate = fireRate;
		this.damage = damage;
		this.startAmmo = startAmmo;
		this.currentAmmunition = startAmmo;
		this.bullets = new ArrayList<Bullet>();
		for (int i = 0; i < 20; i++) {
			sources.add(new AudioSource(position));
		}
		lastFireTime = (float) Timer.getCurrentTime();
	}

	// float damage, float existTime, Vector3f position, Vector3f velocity
	public void fire(Vector3f direction, Vector3f position, Vector3f v, Vector3f rotation) {
		if (currentAmmunition > 0) {

			float fireTime = (float) Timer.getCurrentTime();
			if (fireTime - lastFireTime > 1f / (float) fireRate) {
				if (currentSource == 20)
					currentSource = 0;
				bullets.add(new Bullet(damage, 25, position, direction.mul(40).add(v), rotation));
				sources.get(currentSource).play(sound);
				currentSource++;
				currentAmmunition--;
				lastFireTime = fireTime;
			}
		}
	}

	private boolean checkCollision(Bullet b) { // will be moved to collision
											// detector
		for (HostileShip ship : dataObject.getHostileShips()) {
			if(!ship.isAlive())	continue;
			float distance = (float) Math.sqrt(
					Math.pow(ship.getPosition().x - b.getPosition().x, 2) + Math.pow(ship.getPosition().y - b.getPosition().y, 2)
							+ Math.pow(ship.getPosition().z - b.getPosition().z, 2));
			
			if(distance < ship.getRadius() + Bullet.collisionSphereRadius){
				if(!ship.hitShip(damage)){	
					dataObject.getPlayer().addScore(1000);
					return true;
				}
				dataObject.getPlayer().addScore((int) damage);
				return true;
			}else{
				continue;
			}
		}
		return false;
		
	}

	public void refreshBullets() {
		ArrayList<Integer> a = new ArrayList<Integer>();
		for (Bullet b : bullets) {

			if (!b.shouldExist()) {
				a.add(bullets.indexOf(b));

			} else {
				b.move();
			}
			
			if(checkCollision(b)){
				a.add(bullets.indexOf(b));
			}
		}
		for (int b : a) {
			bullets.remove(b);
		}
	}

	public void reload() {
		currentAmmunition = startAmmo;
	}

}
