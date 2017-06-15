package fontRendering;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import gui.Menu;

public class FontRenderer {

	/**
	 * Font renderer class
	 *
	 * @author Karl, Adrian Setniewski
	 *
	 */
	
	private FontShader shader;

	public FontRenderer() {
		shader = new FontShader();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	public void render(Map<FontType, List<GUIText>> texts) {
		prepare();

		// loop through all fonts
		for (FontType font : texts.keySet()) {
			// currently using only texture0s
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());

			// render text for specific font
			for (GUIText text : texts.get(font)) {
				if(text.getVisible() && text.getMenuID() == Menu.getCurrentLayerID()){
					renderText(text);
				}
				
			}

		}
		endRendering();
	}

	private void prepare() {
		// enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// disable depth test
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		// start shader
		shader.start();
	}

	private void renderText(GUIText text) {

		// enable position and translation arrays
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		// load variables to shader
		shader.loadColor(text.getColor());
		shader.loadTranslation(text.getPosition());

		// draw
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());

		// disable attribute arrays
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);

		// unbind vertex array
		GL30.glBindVertexArray(0);

	}

	private void endRendering() {
		
		//stop the shader
		shader.stop();
		
		//disable alpha blending (black vertices will be transparent)
		GL11.glDisable(GL11.GL_BLEND);
		
		//enable depth test
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
