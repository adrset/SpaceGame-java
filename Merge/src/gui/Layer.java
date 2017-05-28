package gui;

import java.util.ArrayList;

/**
 * Represents the Layers for User Interface.
 * Contains a list of Buttons and GUITexts
 * 
 * @author Adrian Setniewski
 *
 */

import java.util.List;

import org.joml.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.Text;

public class Layer {

	private int layerID;
	private List<Button> guiButtons;
	private List<GUIText> guiTexts;
	private Text textPrint;
	private Background bg;

	public Layer(Text textPrint, int layerID) {
		this.textPrint = textPrint;
		this.layerID = layerID;
		guiTexts = new ArrayList<GUIText>();
		guiButtons = new ArrayList<Button>();
	}
	
	public Background getBackground(){
		return bg;
	}

	public Button getButton(int id) {
		return guiButtons.get(id);
	}

	public List<Button> getButtons() {
		return guiButtons;
	}

	public GUIText getGuiText(int id) {
		return guiTexts.get(id);
	}

	public void addButton(int texture, int texture2, Vector2f position, Vector2f scale, String text, FontType font) {
		guiButtons.add(new Button(texture, texture2, position, scale, text, font, textPrint, layerID));
	}
	//(int textureID, Vector2f position, Vector2f scale, int menuID
	public void setBackground(int texture){
		bg = new Background(texture, new Vector2f(), new Vector2f(1f,1f), layerID);
	}

	public void addText(String text, float size, FontType font, Vector2f position, float screenWidth, boolean centered) {
		guiTexts.add(new GUIText(text, size, font, position, screenWidth, centered, this.textPrint, layerID));
	}

	public void update() {

		for (Button button : guiButtons) {
			button.isClicked();
		}

	}

	public int getLayerID() {
		return this.layerID;
	}

}
