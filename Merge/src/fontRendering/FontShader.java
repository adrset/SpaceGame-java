package fontRendering;

import org.joml.Vector2f;
import org.joml.Vector3f;

import shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/fontRendering/font.vert";
	private static final String FRAGMENT_FILE = "/fontRendering/font.frag";
	
	private int location_textColor;
	private int location_translation;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_textColor = super.getUniformLocation("textColor");
		location_translation = super.getUniformLocation("translation");	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");//look into loader to see the positions 
		super.bindAttribute(1, "textureCoords");
	}
	
	protected void loadColor(Vector3f color){
		super.loadVector(location_textColor, color);
	}
	
	protected void loadTranslation(Vector2f translation){
		super.loadVector2D(location_translation, translation);
	}


}
