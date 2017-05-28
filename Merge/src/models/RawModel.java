package models;

public class RawModel {

	private int vaoID;
	private int vertexCount;
	private double maxDistanceBetweenVertices;
	public RawModel(int vaoID, int vertexCount, double max){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.maxDistanceBetweenVertices = max;
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
