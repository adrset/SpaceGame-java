package models;

import textures.ModelTexture;

/**
 * TexturedModel class. Contains RawModel and texture.
 *
 * @author Adrian Setniewski
 *
 */

public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture modelTexture;
	
	public TexturedModel(RawModel model, ModelTexture texture){
		this.rawModel = model;
		this.modelTexture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getModelTexture() {
		return modelTexture;
	}
}
