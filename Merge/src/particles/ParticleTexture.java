package particles;

public class ParticleTexture {
	private int textureID;
	public int getTextureID() {
		return textureID;
	}
	public int getRows() {
		return rows;
	}
	private int rows;
	public ParticleTexture(int textureID, int rows) {
		super();
		this.textureID = textureID;
		this.rows = rows;
	}
	
}
