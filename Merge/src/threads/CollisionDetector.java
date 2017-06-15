package threads;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.joml.Vector3f;

import celestial.CelestialBody;
import celestial.DataObject;
import scenes.Scene;

/**
 * CollisionDetector class. Takes care of collisions.
 *
 * @author Adrian Setniewski
 *
 */


public class CollisionDetector implements Runnable {

	private Thread t1;
	private DataObject dataObject;
	private List<CelestialBody> bodies;
	private boolean isRunning =false;

	public CollisionDetector(DataObject dataObject) {
		this.dataObject = dataObject;
		
		bodies = new ArrayList<CelestialBody>(dataObject.getPlanets().size()  + dataObject.getAsteroids().size() + dataObject.getLights().size() + dataObject.getHostileShips().size() + 1);
		//make a list of all bodies
		bodies.addAll(dataObject.getPlanets());
		bodies.addAll(dataObject.getAsteroids());
		bodies.addAll(dataObject.getLights());
		bodies.addAll(dataObject.getHostileShips());
		bodies.add(dataObject.getPlayer());
		
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
	public boolean removeBody(CelestialBody body, CelestialBody win){
		if(dataObject.getAsteroids().indexOf(body) != -1){
			body.setDead();
			win.setHealth(win.getHealth() + body.getHealth());
			win.setScale(win.getScale() + win.getScale()/100);
			win.setRadius(win.getRadius() + win.getRadius()/100);
			return true;
		}else if(dataObject.getHostileShips().indexOf(body) != -1){
			body.setDead();
			win.setHealth(win.getHealth() + body.getHealth());
			win.setScale(win.getScale() + win.getScale()/100);
			win.setRadius(win.getRadius() + win.getRadius()/100);
			return true;
		}else{
			return false;
		}
		
	}
	
	public synchronized void checkCollisions(){
		while (isRunning) {
			double len = Math.sqrt(Math.pow(dataObject.getPlayer().getPosition().x - dataObject.getLights().get(0).getPosition().x, 2) + Math.pow(dataObject.getPlayer().getPosition().y - dataObject.getLights().get(0).getPosition().y, 2) + Math.pow(dataObject.getPlayer().getPosition().z - dataObject.getLights().get(0).getPosition().z, 2));
			if(len > 1000000){
				isRunning = false;
				Scene.isAboutEnd = 1;
			}
			for (int i = 0; i < bodies.size(); i++) {
				if(!bodies.get(i).isAlive()) continue;
				for (int j = i+1; j < bodies.size(); j++) {
					if(!bodies.get(j).isAlive()) continue;
					if( i !=j && checkCollision(bodies.get(i), bodies.get(j))){
						if(i == bodies.indexOf(dataObject.getPlayer()) || j == bodies.indexOf(dataObject.getPlayer())){
							isRunning = false;
							Scene.isAboutEnd = 1;
						}else{
							if(!removeBody(bodies.get(i),bodies.get(j))){
								removeBody(bodies.get(j),bodies.get(i));
							}else{
								
							}
						}
					}
				}
			}
			
			int kk=0;
			
			for(Iterator<CelestialBody> it = bodies.iterator(); it.hasNext();kk++){
				if(!it.next().isAlive()){
					System.out.println("Removing body - it crashed.");
					it.remove();
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
