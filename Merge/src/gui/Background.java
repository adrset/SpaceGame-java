package gui;

import org.joml.Vector2f;

public class Background {
	private int menuID;
	private Vector2f position;
	private Vector2f scale;
	private int textureID;

	public Background(int textureID, Vector2f position, Vector2f scale, int menuID) {
		this.position = position;
		this.scale = scale;
		this.menuID = menuID;
		this.textureID = textureID;
	}

	public int getMenuID() {
		return menuID;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public int getTexture() {
		return textureID;
	}
}
