package models;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GameItem {

    private boolean selected;

    private Mesh[] meshes;

    protected Vector3f position;

    protected float scale;

    protected final Vector3f rotation;

    private int textPos;
    
    private boolean disableFrustumCulling;

    private boolean insideFrustum;

    public GameItem() {
        selected = false;
        position = new Vector3f();
        scale = 10;
        rotation = new Vector3f();
        textPos = 0;
        insideFrustum = true;
        disableFrustumCulling = false;
    }
    
    public void increaseRotation(Vector3f rot) {
    	rotation.add(rot);
    }
    
    public void increaseRotation(float x, float y, float z) {
    	rotation.add(x,y,z);
    }
    
    public float getRotationX() {
    	return rotation.x;
    }
    
    public float getRotationY() {
    	return rotation.y;
    }
    
    public float getRotationZ() {
    	return rotation.z;
    }

    public GameItem(Mesh mesh) {
        this();
        this.meshes = new Mesh[]{mesh};
    }

    public GameItem(Mesh[] meshes) {
        this();
        this.meshes = meshes;
    }

    public Vector3f getPosition() {
        return position;
    }

    public int getTextPos() {
        return textPos;
    }

    public boolean isSelected() {
        return selected;
    }

	public void setPosition(float dx, float dy, float dz) {
		this.position.x = dx;
		this.position.y = dy;
		this.position.z = dz;
	}
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increasePosition(Vector3f dr) {
		this.position.x += dr.x;
		this.position.y += dr.y;
		this.position.z += dr.z;
	}

	public void increasePosition(float dx, float dy, float dz, double time) {
		this.position.x += (dx * time);
		this.position.y += (dy * time);
		this.position.z += (dz * time);
	}

	public void increasePosition(Vector3f dr, double time) {
		this.position.x += (dr.x * time);
		this.position.y += (dr.y * time);
		this.position.z += (dr.z * time);
	}

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Mesh getMesh() {
        return meshes[0];
    }

    public Mesh[] getMeshes() {
        return meshes;
    }

    public void setMeshes(Mesh[] meshes) {
        this.meshes = meshes;
    }

    public void setMesh(Mesh mesh) {
        this.meshes = new Mesh[]{mesh};
    }

    public void cleanup() {
        int numMeshes = this.meshes != null ? this.meshes.length : 0;
        for (int i = 0; i < numMeshes; i++) {
            this.meshes[i].cleanUp();
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setTextPos(int textPos) {
        this.textPos = textPos;
    }

    public boolean isInsideFrustum() {
        return insideFrustum;
    }

    public void setInsideFrustum(boolean insideFrustum) {
        this.insideFrustum = insideFrustum;
    }
    
    public boolean isDisableFrustumCulling() {
        return disableFrustumCulling;
    }

    public void setDisableFrustumCulling(boolean disableFrustumCulling) {
        this.disableFrustumCulling = disableFrustumCulling;
    }    
}
