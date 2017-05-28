package celestial;

import java.util.List;

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
	List<Planet> planets;
	List<Asteroid> asteroids;
	List<Light> lights;
	int currentAsteroid;

	public Force(List<Planet> planets, List<Asteroid> asteroids, List<Light> lights) {
		this.lights = lights;
		this.planets = planets;
		this.asteroids = asteroids;
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

		for (Planet p : planets) {
			tmp.add(((position).sub(p.getPosition()))
					.mul((float) (G * dt * p.getMass() / (Math.pow(((position).sub(p.getPosition())).length(), 3)))));
		}

		/*
		 * for (Light l : lights) { tmp.add(((position).sub(l.getPosition()))
		 * .mul((float) (G * dt * l.getMass() /
		 * (Math.pow(((position).sub(l.getPosition())).length(), 3))))); }
		 */

		for (int i = 0; i < asteroids.size(); i++) {
			if (i != currentAsteroid) {
				tmp.add(((position).sub(asteroids.get(i).getPosition()))
						.mul((float) (G * dt * asteroids.get(i).getMass()
								/ (Math.pow(((position).sub(asteroids.get(i).getPosition())).length(), 3)))));

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
			for (Asteroid asteroid : asteroids) {
				currentAsteroid = asteroids.indexOf(asteroid);

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

				asteroid.setVelocity(
						velocity.add((k1[1].add(k2[1]).add(k2[1]).add(k3[1]).add(k3[1]).add(k4[1])).div(6)));
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
