package celestial;

import java.util.Iterator;
import org.joml.Vector3f;

import celestial.Planet;
import utils.Timer;

/**
 * 
 * 
 * @author Przemyslaw Nowak, Adrian Setniewski
 *
 */
public class Force implements Runnable {

	private Thread t1;

	final float G = (float) -6.67E-11;
	Vector3f[] k1 = new Vector3f[2]; // first increment for every
										// variable
	Vector3f[] k2 = new Vector3f[2]; // second increment for every
										// variable
	Vector3f[] k3 = new Vector3f[2]; // third increment for every
										// variable
	Vector3f[] k4 = new Vector3f[2]; // fourth increment for every
										// variable
	private boolean isRunning = false;
	Vector3f position;
	Vector3f velocity;
	/*List<Planet> planets;
	List<Asteroid> asteroids;
	List<Light> lights;*/
	private DataObject dataObject;
	int currentAsteroid;

	public Force(DataObject dataObject) {
		this.dataObject = dataObject;
		for (int ii = 0; ii < 2; ii++) {
			k1[ii] = new Vector3f();
			k2[ii] = new Vector3f();
			k3[ii] = new Vector3f();
			k4[ii] = new Vector3f();
		}
	}

	public Vector3f firstMethod(float dt) { // from first equation dx/dt=V
		return new Vector3f((velocity.x * dt), (velocity.y * dt), (velocity.z * dt));
	}

	public Vector3f secondMethod(float dt) { // from second equation
												// dv/dt=G*m*x/r^3
		Vector3f tmp = new Vector3f();
		for (Planet p : dataObject.getPlanets()) {
			tmp.add(((position).sub(p.getPosition()))
					.mul((float) (G * dt * p.getMass() / (Math.pow(((position).sub(p.getPosition())).length(), 3)))));
		}

		/*
		 * for (Light l : lights) { tmp.add(((position).sub(l.getPosition()))
		 * .mul((float) (G * dt * l.getMass() /
		 * (Math.pow(((position).sub(l.getPosition())).length(), 3))))); }
		 */

		for (int i = dataObject.getAsteroids().size() - 1; i >= 0 ; i--) {
			if(!dataObject.getAsteroids().get(i).isAlive()) continue;
			if (i != currentAsteroid) {
				tmp.add(((position).sub(dataObject.getAsteroids().get(i).getPosition()))
						.mul((float) (G * dt * dataObject.getAsteroids().get(i).getMass()
								/ (Math.pow(((position).sub(dataObject.getAsteroids().get(i).getPosition())).length(), 3)))));

			}
		}

		return tmp;
	}

	void firstUpdate() { // Runge–Kutta method after first step etc
		position.add((k1[0]).div(2));
		velocity.add((k1[1]).div(2));
	}

	void secondUpdate() {
		(position.sub(k1[0].div(2))).add(k2[0].div(2));
		(velocity.sub(k1[1].div(2))).add(k2[1].div(2));
	}

	void thirdUpdate() {
		(position.sub(k2[0].div(2))).add(k3[0]);
		(velocity.sub(k2[1].div(2))).add(k3[1]);
	}

	void fourthUpdate() {
		position.sub(k3[0]);
		velocity.sub(k3[1]);
	}

	public synchronized void calculateDeltas() { // approximation
		// coordinates
		// x(dt)
		float dt = 0.00166f;
		while (isRunning) {
			dt = (float) Timer.getLastLoopTime();
			//buggy when it starts and the object is deleted on another thread
			Iterator<Asteroid> it = dataObject.getAsteroids().iterator();
			while(it.hasNext()) {
				
			  Asteroid asteroid = it.next();	 
			  if(!asteroid.isAlive()) continue;
				currentAsteroid = dataObject.getAsteroids().indexOf(asteroid);

				velocity = new Vector3f(asteroid.getVelocity());
				position = new Vector3f(asteroid.getPosition());
				
				for (int ii = 0; ii < 2; ii++) {
					k1[ii].sub(k1[ii]);
					k2[ii].sub(k2[ii]);
					k3[ii].sub(k3[ii]);
					k4[ii].sub(k4[ii]);
				}

				k1[0].add(firstMethod(dt));
				k1[1].add(secondMethod(dt));
				firstUpdate();

				k2[0].add(firstMethod(dt));
				k2[1].add(secondMethod(dt));
				secondUpdate();

				k3[0].add(firstMethod(dt));
				k3[1].add(secondMethod(dt));
				thirdUpdate();

				k4[0].add(firstMethod(dt));
				k4[1].add(secondMethod(dt));

				fourthUpdate();

				//asteroid.setVelocity(
			//			velocity.add((k1[1].add(k2[1]).add(k2[1]).add(k3[1]).add(k3[1]).add(k4[1])).div(6)));
				Vector3f a = new Vector3f((k1[0].add(k2[0]).add(k2[0]).add(k3[0]).add(k3[0]).add(k4[0])).div(6));
				asteroid.increasePosition(a);
			}
		}
		try {
			Thread.sleep((long) (1000 - dt*1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() {
		t1 = new Thread(this, "Force Calculator");
		t1.setDaemon(true);
		isRunning = true;
		t1.start();
	}
	
	public void finish(){
		isRunning = false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		calculateDeltas();
	}

}
