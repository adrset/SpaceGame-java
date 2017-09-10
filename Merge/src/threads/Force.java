package threads;

import org.joml.Vector3f;

import celestial.DataObject;
import celestial.Light;
import celestial.Planet;
import utils.Timer;

/**
 * Force class.
 * 
 * @author Przemyslaw Nowak
 *
 */
public class Force {
	final float G = (float) -6.67E-15;
	Vector3f k1r = new Vector3f(); // first increment for every
	Vector3f k1v = new Vector3f(); // variable
	Vector3f k2r = new Vector3f(); // second increment for every
	Vector3f k2v = new Vector3f(); // variable
	Vector3f k3r = new Vector3f(); // third increment for every
	Vector3f k3v = new Vector3f(); // variable
	Vector3f k4r = new Vector3f(); // fourth increment for every
	Vector3f k4v = new Vector3f(); // variable
	Vector3f position = new Vector3f();
	Vector3f velocity = new Vector3f();
	private float scale, scale2;

	private DataObject dataObject;

	public Force(DataObject dataObject) {
		this.dataObject = dataObject;
		scale = 1;
		scale2 = 1;
	}

	public Vector3f firstMethod(Vector3f tmp, float dt) {
		return new Vector3f((tmp.x * dt), (tmp.y * dt), (tmp.z * dt));
	}

	public Vector3f secondMethod(Vector3f tmp, float dt, int tier) {
		Vector3f force = new Vector3f(0f, 0f, 0f);
		Vector3f forcetmp = new Vector3f(0f, 0f, 0f);


			forcetmp = new Vector3f(0f, 0f, 0f);
			forcetmp.add(dataObject.getPlayer().getExtraForce());
			force.add(forcetmp);
			forcetmp = new Vector3f(0f, 0f, 0f);

		

		return force;
	}

	void firstUpdate() { // Runge–Kutta method after first step etc
		Vector3f tmp = new Vector3f(k1r);
		tmp.div(2);
		position.add(tmp);

		tmp = new Vector3f(k1v);
		tmp.div(2);
		velocity.add(tmp);

	}

	void secondUpdate() {
		Vector3f tmp = new Vector3f(k1r);
		tmp.div(2);
		position.sub(tmp);
		tmp = new Vector3f(k2r);
		tmp.div(2);
		position.add(tmp);

		tmp = new Vector3f(k1v);
		tmp.div(2);
		velocity.sub(tmp);
		tmp = new Vector3f(k2v);
		tmp.div(2);
		velocity.add(tmp);
	}

	void thirdUpdate() {
		Vector3f tmp = new Vector3f(k3r);
		position.sub(tmp);

		tmp = new Vector3f(k3v);
		velocity.add(tmp);

	}

	void fourthUpdate() {
		position.sub(k3r);
		velocity.sub(k3v);

	}

	void cleanSteps() {
		k1r.zero();
		k2r.zero();
		k3r.zero();
		k4r.zero();
		k1v.zero();
		k2v.zero();
		k3v.zero();
		k4v.zero();
	}

	public void calculateDeltas() { // approximation
		float dt = (float) Timer.getLastLoopTime();

		position = new Vector3f(dataObject.getPlayer().getPosition());
		velocity = new Vector3f(dataObject.getPlayer().getVelocity());
		cleanSteps();

		k1r.add(firstMethod(velocity, dt));
		k1v.add(secondMethod(position, dt, 0));
		firstUpdate();

		k2r.add(firstMethod(velocity, dt));
		k2v.add(secondMethod(position, dt, 1));
		secondUpdate();

		k3r.add(firstMethod(velocity, dt));
		k3v.add(secondMethod(position, dt, 1));
		thirdUpdate();

		k4r.add(firstMethod(velocity, dt));
		k4v.add(secondMethod(position, dt, 2));
		fourthUpdate();

		Vector3f tmp = new Vector3f(k1r);
		tmp.add(k2r);
		tmp.add(k2r);
		tmp.add(k3r);
		tmp.add(k3r);
		tmp.add(k4r);
		tmp.div(6);
		dataObject.getPlayer().increasePosition(tmp);

		tmp = new Vector3f(k1v);
		tmp.add(k2v);
		tmp.add(k2v);
		tmp.add(k3v);
		tmp.add(k3v);
		tmp.add(k4v);
		tmp.div(6);
		dataObject.getPlayer().increaseVelocity(tmp);

		cleanSteps();

	}

}