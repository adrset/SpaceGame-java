package entities;
import org.joml.Vector3f;

public class Camera {
	
	/**
	 * Camera class containing its position, and euler's angles
	 * 
	 * @author Adrian Setniewski
	 *
	 */
	protected Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	protected float pitch;
	protected float yaw;
	protected float roll;
	
	public Camera(){
		
	}
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public void setPosition(Vector3f newPosition){
		position = newPosition;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
}
