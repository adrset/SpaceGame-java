package gui;

import org.joml.Vector2f;

import entities.Camera;
import fontMeshCreator.FontType;
import game.Game;
import language.Language;

/**
 * Represents the Menu. Menu is a class like Scene - it has a loop definition.
 * Both of these classes methods are called in the Game class. In future menu
 * will be read from 'json' or 'xml' file. Same approach was undertaken with
 * Scene class. So you can get some conclusions from there.
 * 
 * 
 * @author Adrian Setniewski
 *
 */

public class Menu {
	private int currentIndex = 0;
	private UI ui;
	private Camera camera;
	public static boolean play = false;
	private static int currentLayerID;

	public Menu() {
		ui = new UI(Game.loader);
		FontType arial = new FontType(Game.loader.loadTexture("arial"), "arial");

		int id1 = ui.addLayer();
		ui.getLayer(id1).addButton(Game.loader.loadTexture("GUI1"), Game.loader.loadTexture("GUI2"),
				new Vector2f(0f, 0.3f), new Vector2f(0.2f, 0.1f), Language.getLanguageData("menu_play"), arial);
		ui.getLayer(id1).addButton(Game.loader.loadTexture("GUI1"), Game.loader.loadTexture("GUI2"),
				new Vector2f(0f, 0.05f), new Vector2f(0.2f, 0.1f), Language.getLanguageData("menu_preferences"), arial);
		ui.getLayer(id1).addButton(Game.loader.loadTexture("GUI1"), Game.loader.loadTexture("GUI2"),
				new Vector2f(0f, -0.2f), new Vector2f(0.2f, 0.1f), Language.getLanguageData("menu_exit"), arial);
		int id2 = ui.addLayer();
		ui.getLayer(id2).addButton(Game.loader.loadTexture("GUI1"), Game.loader.loadTexture("GUI2"),
				new Vector2f(0f, 0.3f), new Vector2f(0.2f, 0.1f), (Game.getVideoSync())
						? Language.getLanguageData("menu_vsync_on") : Language.getLanguageData("menu_vsync_off"),
				arial);
		ui.getLayer(id2).addButton(Game.loader.loadTexture("GUI1"), Game.loader.loadTexture("GUI2"),
				new Vector2f(0f, -0.2f), new Vector2f(0.2f, 0.1f), Language.getLanguageData("menu_back"), arial);
		ui.getLayer(id2).addButton(Game.loader.loadTexture("GUI1"), Game.loader.loadTexture("GUI2"),
				new Vector2f(0f, 0.05f), new Vector2f(0.2f, 0.1f), Language.getLanguageData("menu_antialiasing") + " x" + Game.getMultiSampling(), arial);
		ui.getLayer(id2).addButton(Game.loader.loadTexture("GUI1"), Game.loader.loadTexture("GUI2"),
				new Vector2f(0.5f, 0.05f), new Vector2f(0.2f, 0.1f), Language.getLanguageData("menu_language"), arial);
		ui.getLayer(id1).setBackground(Game.loader.loadTexture("stars"));
		ui.getLayer(id2).setBackground(Game.loader.loadTexture("stars"));
		// <3 lambdas
		ui.getLayer(id1).getButton(0).addListener(() -> {
			play = true;
		});

		ui.getLayer(id1).getButton(1).addListener(() -> {
			setCurrentLayerID(1);
			ui.setCurrentLayer(1);
		});

		ui.getLayer(id1).getButton(2).addListener(() -> {
			Game.windowShouldClose = true;
		});

		ui.getLayer(id2).getButton(0).addListener(() -> {
			if (Game.getVideoSync()) {
				ui.getLayer(id2).getButton(0).changeText(Language.getLanguageData("menu_vsync_off"));
				Game.toggleVideoSync(false);
			} else {
				ui.getLayer(id2).getButton(0).changeText(Language.getLanguageData("menu_vsync_on"));
				Game.toggleVideoSync(true);
			}

		});

		ui.getLayer(id2).getButton(1).addListener(() -> {
			setCurrentLayerID(0);
			ui.setCurrentLayer(0);
		});
		ui.getLayer(id2).getButton(2).addListener(() -> {
			int[] samples = { 0, 2, 4, 8, 16 }; // 4
			int seek = 0;
			for (int i = 0; i < samples.length; i++) {
				if (samples[i] == Game.getMultiSampling()) {
					seek = i;
					i = samples.length;
				}
			}

			Game.setMultiSampling(samples[(seek + 1) % 5]);
			ui.getLayer(id2).getButton(2).changeText(Language.getLanguageData("menu_antialiasing") +  " x" + samples[(seek + 1) % 5]);
		});
		ui.getLayer(id2).getButton(3).addListener(() -> {
			Language loader = new Language();
			if (currentIndex == 0) {
				currentIndex = 1;
				loader.loadLanguage("polish");
			} else {
				currentIndex = 0;
				loader.loadLanguage("english");
			}
			refresh(id1, id2);
			ui.getLayer(id2).getButton(3).changeText(Language.getLanguageData("menu_language"));
		});

	}

	public void refresh(int id1, int id2) {
		ui.getLayer(id1).getButton(0).changeText(Language.getLanguageData("menu_play"));
		ui.getLayer(id1).getButton(1).changeText(Language.getLanguageData("menu_preferences"));
		ui.getLayer(id1).getButton(2).changeText(Language.getLanguageData("menu_exit"));

		ui.getLayer(id2).getButton(0).changeText(Game.getVideoSync() ? Language.getLanguageData("menu_vsync_on")
				: Language.getLanguageData("menu_vsync_off"));
		ui.getLayer(id2).getButton(1).changeText(Language.getLanguageData("menu_back"));
		ui.getLayer(id2).getButton(2)
				.changeText(Language.getLanguageData("menu_antialiasing") + " x" + Game.getMultiSampling());
		ui.getLayer(id2).getButton(3).changeText("Language: " + Language.currentLanguage);
	}

	public static int getCurrentLayerID() {
		return currentLayerID;
	}

	public static void setCurrentLayerID(int index) {
		currentLayerID = index;
	}

	public void loop() {

		Game.renderer.render(camera);
		ui.render();

	}

}
