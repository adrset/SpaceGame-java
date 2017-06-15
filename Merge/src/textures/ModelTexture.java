package textures;

/**
 * ModelTexture class. Contains shine, reflect variables that will be loaded to
 * GLSL shader and will affect the way light will affect textured object.
 *
 * @author Adrian Setniewski
 *
 */

public class ModelTexture {

	private int textureID;
	private float shineDamper = 2.4f;
	private float reflect = 0.35f;
	private boolean hasTransparency = false;
	private boolean fakeLight = false;

	public boolean isFakeLight() {
		return fakeLight;
	}

	public void setFakeLight(boolean fakeLight) {
		this.fakeLight = fakeLight;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean trans) {
		hasTransparency = trans;
	}

	public int getTextureID() {
		return textureID;
	}

	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflect() {
		return reflect;
	}

	public void setReflect(float reflect) {
		this.reflect = reflect;
	}

	public int getID() {
		return textureID;
	}

	public ModelTexture(int id) {
		textureID = id;
	}

}
