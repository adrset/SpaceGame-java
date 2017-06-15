package threads;

//import java.util.Iterator;
import org.joml.Vector3f;

import celestial.DataObject;
import celestial.Light;
import celestial.Planet;
import utils.Timer;

/**
 * 
 * 
 * @author Przemyslaw Nowak, Adrian Setniewski
 *
 */
public class Force {

	private Thread t1;

	final float G = (float) -6.67E-15;
	Vector3f[] k1r= new Vector3f[120]; // first increment for every
	Vector3f[] k1v= new Vector3f[120];				// variable
	Vector3f[] k2r= new Vector3f[120];  // second increment for every
	Vector3f[] k2v= new Vector3f[120];				// variable
	Vector3f[] k3r= new Vector3f[120]; // third increment for every
	Vector3f[] k3v= new Vector3f[120];				// variable
	Vector3f[] k4r= new Vector3f[120]; // fourth increment for every
	Vector3f[] k4v= new Vector3f[120];			// variable
	private boolean isRunning = false;
	Vector3f[] position= new Vector3f[120];
	Vector3f[] velocity= new Vector3f[120];
	private DataObject dataObject;
	int currentAsteroid;

	public void setDimension(int d) {
		for (int ii = 0; ii < d; ii++) {
			position[0] = new Vector3f(0f,0f,0f);
			velocity[0] = new Vector3f(0f,0f,0f);
			k1r[0] = new Vector3f(0f,0f,0f);
			k2r[0] = new Vector3f(0f,0f,0f);
			k3r[0] = new Vector3f(0f,0f,0f);
			k4r[0] = new Vector3f(0f,0f,0f);
			k1v[0] = new Vector3f(0f,0f,0f);
			k2v[0] = new Vector3f(0f,0f,0f);
			k3v[0] = new Vector3f(0f,0f,0f);
			k4v[0] = new Vector3f(0f,0f,0f);
		}

	}
	
	
	public Force(DataObject dataObject) {
		this.dataObject = dataObject;
	}

	public Vector3f firstMethod(Vector3f tmp, float dt) { // from first equation dx/dt=V
		return new Vector3f((tmp.x * dt), (tmp.y * dt), (tmp.z * dt));
	}

	public Vector3f secondMethod(Vector3f tmp, float dt, int tier, int ifPlayer) { // from second equation dv/dt=G*m*x/r^3
		Vector3f force = new Vector3f(0f, 0f, 0f);
		Vector3f forcetmp = new Vector3f(0f, 0f, 0f);
		float r3Scalar=0;
		float scalar=0;
		
		for (Planet p : dataObject.getPlanets()) {
			forcetmp.add(tmp);
			forcetmp.sub(p.getPosition());
			r3Scalar=(float) Math.pow(Math.pow((tmp.x-p.getPosition().x),2)+Math.pow((tmp.y-p.getPosition().y),2)+Math.pow((tmp.z-p.getPosition().z),2),1.5);
			scalar=(float) (G * dt * p.getMass()/r3Scalar);
			forcetmp.mul(scalar);
			force.add(forcetmp);
			forcetmp=new Vector3f(0f, 0f, 0f);
			r3Scalar=0;
			scalar=0;
		}

		for (Light l : dataObject.getLights()) {
			forcetmp.add(tmp);
			forcetmp.sub(l.getPosition());
			r3Scalar=(float) Math.pow(Math.pow((tmp.x-l.getPosition().x),2)+Math.pow((tmp.y-l.getPosition().y),2)+Math.pow((tmp.z-l.getPosition().z),2),1.5);
			scalar=(float) (G * dt * l.getMass()/r3Scalar);
			forcetmp.mul(scalar);
			force.add(forcetmp);
			forcetmp=new Vector3f(0f, 0f, 0f);
			r3Scalar=0;
			scalar=0;
			
		}

		for (int ii = 0; ii < dataObject.getAsteroids().size(); ii++) {
			if (ii!=currentAsteroid){
				forcetmp.add(tmp);
				forcetmp.sub(dataObject.getAsteroids().get(ii).getPosition());
				r3Scalar=(float) Math.pow(Math.pow((tmp.x-dataObject.getAsteroids().get(ii).getPosition().x),2)+Math.pow((tmp.y-dataObject.getAsteroids().get(ii).getPosition().y),2)+Math.pow((tmp.z-dataObject.getAsteroids().get(ii).getPosition().z),2),1.5);
				scalar=(float) (G * dt * dataObject.getAsteroids().get(ii).getMass()/r3Scalar);
				forcetmp.mul(scalar);
				force.add(forcetmp);
				forcetmp=new Vector3f(0f, 0f, 0f);
				r3Scalar=0;
				scalar=0;
			}

		}
		if (ifPlayer==1){

			forcetmp=new Vector3f(0f, 0f, 0f);
			forcetmp.add(dataObject.getPlayer().getExtraForce());
			force.add(forcetmp);
			forcetmp=new Vector3f(0f, 0f, 0f);
			
			
		}
		
		return force;
	}

	void firstUpdate() { // Runge–Kutta method after first step etc 
		for (int ii = 0; ii < dataObject.getAsteroids().size() + 1; ii++){
			Vector3f tmp=new Vector3f(k1r[ii]);
			tmp.div(2);
			position[ii].add(tmp);
			
			tmp=new Vector3f(k1v[ii]);
			tmp.div(2);
			velocity[ii].add(tmp);
		}
	}

	void secondUpdate() {
		for (int ii = 0; ii < dataObject.getAsteroids().size() + 1; ii++){
			Vector3f tmp=new Vector3f(k1r[ii]);
			tmp.div(2);
			position[ii].sub(tmp);
			tmp=new Vector3f(k2r[ii]);
			tmp.div(2);
			position[ii].add(tmp);
			
			tmp=new Vector3f(k1v[ii]);
			tmp.div(2);
			velocity[ii].sub(tmp);
			tmp=new Vector3f(k2v[ii]);
			tmp.div(2);
			velocity[ii].add(tmp);
		}
	}

	void thirdUpdate() {
		for (int ii = 0; ii < dataObject.getAsteroids().size() + 1; ii++){
			Vector3f tmp=new Vector3f(k3r[ii]);
			position[ii].sub(tmp);
			
			tmp=new Vector3f(k3v[ii]);
			velocity[ii].add(tmp);
		}

	}

	void fourthUpdate() {
		for (int ii = 0; ii < dataObject.getAsteroids().size() + 1; ii++){
			position[ii].sub(k3r[ii]);
			velocity[ii].sub(k3v[ii]);
		}
	}

	void cleanSteps(){
		for (int ii = 0; ii < dataObject.getAsteroids().size() + 1; ii++) {
			k1r[ii]=new Vector3f(0f,0f,0f);
			k2r[ii]=new Vector3f(0f,0f,0f);
			k3r[ii]=new Vector3f(0f,0f,0f);
			k4r[ii]=new Vector3f(0f,0f,0f);
			k1v[ii]=new Vector3f(0f,0f,0f);
			k2v[ii]=new Vector3f(0f,0f,0f);
			k3v[ii]=new Vector3f(0f,0f,0f);
			k4v[ii]=new Vector3f(0f,0f,0f);
		}
	}
	
	public void calculateDeltas() { // approximation
			float dt = (float) Timer.getLastLoopTime();
			// buggy when it starts and the object is deleted on another thread
			
			for (int jj=0; jj<dataObject.getAsteroids().size();jj++){
				currentAsteroid=jj;
				position[jj]= new Vector3f();

				position[jj] = new Vector3f(dataObject.getAsteroids().get(jj).getPosition());
				velocity[jj] = new Vector3f(dataObject.getAsteroids().get(jj).getVelocity());
			}
			position[dataObject.getAsteroids().size()] = new Vector3f(dataObject.getPlayer().getPosition());
			velocity[dataObject.getAsteroids().size()] = new Vector3f(dataObject.getPlayer().getVelocity());
			cleanSteps();
			//System.out.print(dataObject.getAsteroids().size());
			for (int ii = 0; ii < dataObject.getAsteroids().size(); ii++){
				currentAsteroid=ii;
				k1r[ii].add(firstMethod(velocity[ii],dt));
				k1v[ii].add(secondMethod(position[ii],dt,0,0));
			}
			currentAsteroid+=3;
			k1r[dataObject.getAsteroids().size()].add(firstMethod(velocity[dataObject.getAsteroids().size()],dt));
			k1v[dataObject.getAsteroids().size()].add(secondMethod(position[dataObject.getAsteroids().size()],dt,0,1));
			firstUpdate();
			
			for (int ii = 0; ii < dataObject.getAsteroids().size(); ii++){
				currentAsteroid=ii;
				k2r[ii].add(firstMethod(velocity[ii],dt));
				k2v[ii].add(secondMethod(position[ii],dt,1,0));
			}
			currentAsteroid+=3;
			k2r[dataObject.getAsteroids().size()].add(firstMethod(velocity[dataObject.getAsteroids().size()],dt));
			k2v[dataObject.getAsteroids().size()].add(secondMethod(position[dataObject.getAsteroids().size()],dt,1,1));
			secondUpdate();
			
			for (int ii = 0; ii < dataObject.getAsteroids().size(); ii++){
				currentAsteroid=ii;
				k3r[ii].add(firstMethod(velocity[ii],dt));
				k3v[ii].add(secondMethod(position[ii],dt,1,0));
			}
			currentAsteroid+=3;
			k3r[dataObject.getAsteroids().size()].add(firstMethod(velocity[dataObject.getAsteroids().size()],dt));
			k3v[dataObject.getAsteroids().size()].add(secondMethod(position[dataObject.getAsteroids().size()],dt,1,0));
			thirdUpdate();
			
			for (int ii = 0; ii < dataObject.getAsteroids().size(); ii++){
				currentAsteroid=ii;
				k4r[ii].add(firstMethod(velocity[ii],dt));
				k4v[ii].add(secondMethod(position[ii],dt,2,0));
			}
			currentAsteroid+=3;
			k4r[dataObject.getAsteroids().size()].add(firstMethod(velocity[dataObject.getAsteroids().size()],dt));
			k4v[dataObject.getAsteroids().size()].add(secondMethod(position[dataObject.getAsteroids().size()],dt,2,1));
			fourthUpdate();
			
			for (int ii = 0; ii < dataObject.getAsteroids().size()+1; ii++){
				if(ii<dataObject.getAsteroids().size()){
					currentAsteroid=ii;
					Vector3f tmp=new Vector3f(k1r[ii]);
					tmp.add(k2r[ii]);
					tmp.add(k2r[ii]);
					tmp.add(k3r[ii]);
					tmp.add(k3r[ii]);
					tmp.add(k4r[ii]);
					tmp.div(6);
					dataObject.getAsteroids().get(ii).increasePosition(tmp);
					
					tmp=new Vector3f(k1v[ii]);
					tmp.add(k2v[ii]);
					tmp.add(k2v[ii]);
					tmp.add(k3v[ii]);
					tmp.add(k3v[ii]);
					tmp.add(k4v[ii]);
					tmp.div(6);
					dataObject.getAsteroids().get(ii).increaseVelocity(tmp);
				}
				else if(ii==dataObject.getAsteroids().size()){
					Vector3f tmp=new Vector3f(k1r[ii]);
					tmp.add(k2r[ii]);
					tmp.add(k2r[ii]);
					tmp.add(k3r[ii]);
					tmp.add(k3r[ii]);
					tmp.add(k4r[ii]);
					tmp.div(6);
					dataObject.getPlayer().increasePosition(tmp);
					
					tmp=new Vector3f(k1v[ii]);
					tmp.add(k2v[ii]);
					tmp.add(k2v[ii]);
					tmp.add(k3v[ii]);
					tmp.add(k3v[ii]);
					tmp.add(k4v[ii]);
					tmp.div(6);
					dataObject.getPlayer().increaseVelocity(tmp);
				}
			}
			cleanSteps();
			


	}

}