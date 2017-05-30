package weaponry;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import audio.AudioManager;
import audio.AudioSource;
import celestial.HostileShip;
import models.TexturedModel;
import utils.Timer;

public class Weapon {

	private String name;
	private int fireRate;
	private List<HostileShip> targets;
	private List<Bullet> bullets;
	private float damage;
	private int startAmmo;
	private int currentAmmunition;
	private AudioSource source;
	private static int sound =  AudioManager.loadSound("res/audio/shot.wav");
	private float lastFireTime;
	
	public int getStartAmmo() {
		return startAmmo;
	}

	public int getCurrentAmmunition() {
		return currentAmmunition;
	}
	
	public List<Bullet> getBullets(){
		return bullets;
	}
	
	public Weapon(String name, int fireRate, List<HostileShip> targets, int startAmmo, int damage, TexturedModel bulletModel, Vector3f position, float scale) {
		Bullet.scale = scale;
		Bullet.bulletModel = bulletModel;
		this.name = name;
		this.fireRate = fireRate;
		this.targets = targets;
		this.damage = damage;
		this.startAmmo = startAmmo;
		this.currentAmmunition = startAmmo;
		this.bullets = new ArrayList<Bullet>();
		source = new AudioSource(position);
		lastFireTime = (float) Timer.getCurrentTime();
	}

	// float damage, float existTime, Vector3f position, Vector3f velocity
	public void fire(Vector3f direction, Vector3f position,  Vector3f v, Vector3f rotation) {
		if (currentAmmunition > 0) {
			
			float fireTime = (float)Timer.getCurrentTime();
			if(fireTime-lastFireTime > 1/fireRate){
				bullets.add(new Bullet(damage, 25, position, direction.mul(10).add(v), rotation));
				source.play(sound);
				currentAmmunition--;
				lastFireTime = fireTime;
			}
		}
	}

	public void refreshBullets() {
		ArrayList<Integer> a = new ArrayList<Integer>();
		for (Bullet b : bullets) {
			if (!b.shouldExist()) {
				a.add(bullets.indexOf(b));

			}else{
				b.move();
			}
		}
		for(int b: a){
			bullets.remove(b);
		}
	}

	public void reload() {
		currentAmmunition = startAmmo;
	}

}