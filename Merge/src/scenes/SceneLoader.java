package scenes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Vector3f;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import celestial.Light;
import celestial.Planet;
import entities.Player;
import game.Game;
import models.RawModel;
import models.TexturedModel;
import renderEngine.ResourceCache;
import textures.ModelTexture;

public class SceneLoader {
	private List<Planet> planets;
	private List<Light> lights;
	private Player player;
	private static String[] TEXTURES;

	public SceneLoader() {
		planets = new ArrayList<Planet>();
		lights = new ArrayList<Light>();
		TEXTURES = new String[6];
	}

	public void load(String fileName) {
		JSONParser parser = new JSONParser();

		try {
			InputStreamReader in = new InputStreamReader(Class.class.getResourceAsStream("/res/levels/" + fileName + ".json"));
			BufferedReader reader = new BufferedReader(in);

			Object obj = parser.parse(reader);

			JSONObject jsonObject = (JSONObject) obj;

			String version = (String) jsonObject.get("supportedVersion");
			if (Float.parseFloat(version) < Game.getCurrentVersion()) {
				throw new Exception("Level format unsupported!");
			}

			// load planets
			JSONObject planets = (JSONObject) ((JSONObject) jsonObject.get("entities")).get("planet");

			Iterator<?> iterator = planets.values().iterator();

			while (iterator.hasNext()) {

				JSONObject jsonChildObject = (JSONObject) iterator.next();

				// using ResourceCache (blazing speed)
				RawModel model = ResourceCache.loadOBJ((String) (jsonChildObject.get("model")), Game.loader);
				TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(
						ResourceCache.loadTexture((String) (jsonChildObject.get("texture")), Game.loader)));

				// dirty conversions -> to be fixed
				this.planets.add(new Planet(texturedModel, new Vector3f(0,0,0),
						((Long) ((JSONObject) jsonChildObject.get("position")).get("angle")).floatValue(),
						((Number) jsonChildObject.get("mass")).floatValue(),
						((Number) jsonChildObject.get("radius")).floatValue(),
						((Long) ((JSONObject) jsonChildObject.get("orbit")).get("a")).floatValue(),
						((Long) ((JSONObject) jsonChildObject.get("orbit")).get("b")).floatValue(),
						((Number) ((JSONObject) jsonChildObject.get("position")).get("angularVelocity")).floatValue(),
						((Number) jsonChildObject.get("rotation")).floatValue()));

			}

			// load lights
			JSONObject lights = (JSONObject) ((JSONObject) jsonObject.get("entities")).get("lights");

			iterator = lights.values().iterator();

			while (iterator.hasNext()) {

				JSONObject jsonChildObject = (JSONObject) iterator.next();

				TexturedModel model2 = new TexturedModel(ResourceCache.loadOBJ((String) (jsonChildObject.get("model")), Game.loader), new ModelTexture(
						ResourceCache.loadTexture((String) (jsonChildObject.get("texture")), Game.loader)));
				this.lights.add(new Light(model2,
						new Vector3f(
								((Number) ((JSONObject) jsonChildObject.get("position")).get("x")).floatValue(),
								((Number) ((JSONObject) jsonChildObject.get("position")).get("y")).floatValue(),
								((Number) ((JSONObject) jsonChildObject.get("position")).get("z")).floatValue()),
						new Vector3f( //remember to normalise!!!
								((Number) ((JSONObject) jsonChildObject.get("color")).get("r")).floatValue()/255,
								((Number) ((JSONObject) jsonChildObject.get("color")).get("g")).floatValue()/255,
								((Number) ((JSONObject) jsonChildObject.get("color")).get("b")).floatValue()/255),
						new Vector3f(
								((Number) ((JSONObject) jsonChildObject.get("attentuation")).get("d1")).floatValue(),
								((Number) ((JSONObject) jsonChildObject.get("attentuation")).get("d2")).floatValue(),
								((Number) ((JSONObject) jsonChildObject.get("attentuation")).get("d3")).floatValue()), 
						((Number) jsonChildObject.get("radius")).floatValue(), ((Number) jsonChildObject.get("density")).floatValue()));

			}
			this.lights.get(this.lights.size() - 1).getModel().getModelTexture().setFakeLight(true);
			// player
			JSONObject player = (JSONObject) ((JSONObject) jsonObject.get("entities")).get("player");

			TexturedModel texturedModel = new TexturedModel(
					ResourceCache.loadOBJ((String) (player.get("model")), Game.loader), new ModelTexture(
							ResourceCache.loadTexture((String) (player.get("texture")), Game.loader)));


			this.player = new Player(texturedModel,
			new Vector3f(
					((Number) ((JSONObject) player.get("position")).get("x")).floatValue(),
					((Number) ((JSONObject) player.get("position")).get("y")).floatValue(),
					((Number) ((JSONObject) player.get("position")).get("z")).floatValue()),
					0,0,0, new Vector3f(), ((Number) (player.get("speed"))).floatValue() ,((Number) (player.get("size"))).floatValue(), 0);

			
			
			//skybox  {"skybox_right", "skybox_left", "skybox_top", "skybox_bottom", "skybox_back", "skybox_front"};
			JSONObject skybox = ((JSONObject) jsonObject.get("skybox"));
			
			TEXTURES[0] = (String) skybox.get("right");
			TEXTURES[1] = (String) skybox.get("left");
			TEXTURES[2] = (String) skybox.get("top");
			TEXTURES[3] = (String) skybox.get("bottom");
			TEXTURES[4] = (String) skybox.get("back");
			TEXTURES[5] = (String) skybox.get("front");
			

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	// new object maybe? NAH
	public List<Planet> getPlanets() {
		return planets;
	}

	public String[] getSkyboxTextureNames(){
		return TEXTURES;
	}
	
	public List<Light> getLights() {
		return lights;
	}
	
	public Player getPlayer(){
		return player;
	}

}
