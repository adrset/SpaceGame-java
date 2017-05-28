package renderEngine;

import java.util.HashMap;

import models.RawModel;
import utils.Logs;

public class ResourceCache {
	static HashMap<String, RawModel> objects = new HashMap<>();
	static HashMap<String, Integer> textures = new HashMap<>();

	public static RawModel loadOBJ(String name, Loader loader) {

		if (objects.get(name) != null) {
			Logs.printLog("Cached model: (" + name + ".obj) loaded!");
			return objects.get(name);
		} else {
			Logs.printLog("New model: (" + name + ".obj) loaded!");
			RawModel model = OBJLoader.loadObjModel(name, loader);
			objects.put(name, model);
			return model;
		}

	}

	public static int loadTexture(String name, Loader loader) {

		if (textures.get(name) != null) {
			Logs.printLog("Cached texture: (" + name + ".png) loaded!");
			return textures.get(name);
		} else {
			Logs.printLog("New texture: (" + name + ".png) loaded!");
			int textureID = loader.loadTexture(name);
			textures.put(name, textureID);
			return textureID;
		}

	}

}
