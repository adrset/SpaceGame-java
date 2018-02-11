package models;

import org.joml.Vector3f;

/**
 * RawModel class. Contains vaoID that indicates to data that was uploaded to the GPU.
 *
 * @author Adrian Setniewski
 *
 */

public class RawModel {

	private int vaoID;
	private int vertexCount;
	private double maxDistanceBetweenVertices = 100f;
	Vector3f min;
	public Vector3f getMin() {
		return min;
	}

	public void setMin(Vector3f min) {
		this.min = min;
	}

	public Vector3f getMax() {
		return max;
	}

	public void setMax(Vector3f max) {
		this.max = max;
	}
	Vector3f max;
	public RawModel(int vaoID, int vertexCount,  Vector3f min, Vector3f max){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.min = min;
		this.max = max;
	}
	
	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	public double getDistance(){
		return this.maxDistanceBetweenVertices;
	}
}
