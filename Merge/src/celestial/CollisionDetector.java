package celestial;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import entities.Player;
import scenes.Scene;

public class CollisionDetector implements Runnable {

	private Thread t1;
	private List<CelestialBody> bodies;
	private boolean isRunning =false;
	private int playerID;
	List<Planet> planets;
	List<Asteroid> asteroids;
	List<Light> lights;
	List<HostileShip> hostile;
	
	public CollisionDetector(List<Planet> planets, List<Asteroid> asteroids,List<Light> lights,List<HostileShip> hostile , Player player) {
		bodies = new ArrayList<CelestialBody>(planets.size()  + asteroids.size() + lights.size() + hostile.size() + 1);
		this.planets = planets;
		this .asteroids = asteroids;
		this.lights = lights;
		this.hostile = hostile;
		
		bodies.addAll(planets);
		bodies.addAll(asteroids);
		bodies.addAll(lights);
		bodies.addAll(hostile);
		bodies.add(player);
		
	}

	public boolean checkCollision(CelestialBody a, CelestialBody b) {
		Vector3f length = new Vector3f();
		length.add(a.getPosition()).sub(b.getPosition());
		float distance = (float) Math.sqrt(
				Math.pow(a.getPosition().x - b.getPosition().x, 2) + Math.pow(a.getPosition().y - b.getPosition().y, 2)
						+ Math.pow(a.getPosition().z - b.getPosition().z, 2));
		if (distance < a.getRadius() + b.getRadius()) {
			return true;
		}
		return false;
	}

	public void start() {
		t1 = new Thread(this, "Collision Detector");
		isRunning = true;
		t1.setDaemon(true);
		t1.start();
	}
	public boolean removeBody(CelestialBody body){
		if(asteroids.indexOf(body) != -1){
			asteroids.remove(asteroids.indexOf(body));
			bodies.remove(bodies.indexOf(body));
			return true;
		}else if(hostile.indexOf(body) != -1){
			hostile.remove(hostile.indexOf(body));
			bodies.remove(bodies.indexOf(body));
			return true;
		}else{
			return false;
		}
		
	}
	
	public synchronized void checkCollisions(){
		while (isRunning) {
			for (int i = 0; i < bodies.size(); i++) {
				for (int j = i+1; j < bodies.size(); j++) {
					if( i !=j && checkCollision(bodies.get(i), bodies.get(j))){
						if(i == playerID || j == playerID){
							isRunning = false;
							Scene.isAboutEnd = 1;
						}else{
							if(!removeBody(bodies.get(i))){
								removeBody(bodies.get(j));
							}else{
								
							}
						}
					}
				}
			}
		}
	}
	

	
	public void wakeup(){
		t1.notify();
	}
	
	
	public void finish(){
		isRunning = false;
	}

	@Override
	public void run() {

		checkCollisions();
	}
}
