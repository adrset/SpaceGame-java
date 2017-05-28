package gui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import audio.AudioManager;
import audio.AudioSource;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.Text;
import game.Game;
import input.MouseButtons;
import input.MouseCursor;

/**
 * Represents the Button and ButtonClick functional interface
 * 
 * 
 * @author Adrian Setniewski
 *
 */

interface ButtonClick { // functional interface -> only one abstract method
	public void buttonIsClicked();
}

public class Button {
	private int menuID;
	private Vector2f position;
	private Vector2f scale;
	private int textureID;
	private int textureID2;
	private GUIText guiText;
	private Text textPrint;
	private ButtonClick obj;
	private static AudioSource source =new AudioSource(new Vector3f());
	private static int sound =  AudioManager.loadSound("res/audio/click.wav");

	public Button(int textureID, int textureID2, Vector2f position, Vector2f scale, String text, FontType font,
			Text textPrint, int menuID) {
		this.position = position;
		this.scale = scale;
		this.menuID = menuID;
		this.textureID = textureID;
		this.textureID2 = textureID2; // 0.965f for 2f
		this.textPrint = textPrint; // will make it static in future <-
									// temporary
		guiText = new GUIText(text, 1.3f, font,
				new Vector2f((position.x * 0.5f + 0.5f) - 0.1f, (position.y * 0.5f + 0.5f) * (-1f) + 0.980f), scale.x,
				true, textPrint, menuID);
		obj = () -> {
			System.out.println("Clicked");
		};
	}

	public void changeText(String text) {
		FontType font = guiText.getFont();
		guiText.remove();
		guiText = new GUIText(text, 1.3f, font,
				new Vector2f((position.x * 0.5f + 0.5f) - 0.1f, (position.y * 0.5f + 0.5f) * (-1f) + 0.980f), scale.x,
				true, textPrint, menuID);
	}

	public void changeTextColor(Vector3f color) {
		guiText.setColor(color.x / color.length(), color.y / color.length(), color.z / color.length()); // in
																										// case
																										// color
																										// vector
																										// was
																										// not
																										// normalised
	}

	public void addListener(ButtonClick obj) {
		this.obj = obj;
	}

	public boolean isClicked() {

		if (isMouseOver() && Menu.getCurrentLayerID() == menuID) {
			if (MouseButtons.getButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_1) == 1) {
				source.play(sound);
				obj.buttonIsClicked();
			}
		}
		return false;
	}

	public boolean isMouseOver() {
		if (((Game.width * (0.5 + (this.position.x) / 2) - Game.width * this.scale.x / 2) < MouseCursor.getPosX())
				&& ((Game.width * (0.5 + (this.position.x) / 2) + Game.width * this.scale.x / 2) > MouseCursor
						.getPosX())
				&& ((Game.height
						- (Game.height * (0.5 + (this.position.y) / 2) + Game.height * this.scale.y / 2)) < MouseCursor
								.getPosY())
				&& ((Game.height
						- (Game.height * (0.5 + (this.position.y) / 2) - Game.height * this.scale.y / 2)) > MouseCursor
								.getPosY())) {
			return true;

		} else {
			return false;
		}
	}

	public boolean isMouseEntered() {
		return false;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public int getMenuID() {
		return menuID;
	}

	public void setMenuID(int menuID) {
		this.menuID = menuID;
	}

	public int getTexture() {
		if (isMouseOver()) {
			return textureID2;
		} else {
			return textureID;
		}

	}
}
