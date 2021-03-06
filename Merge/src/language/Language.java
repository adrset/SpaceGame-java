package language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game.Game;
import utils.Logs;

public class Language {
	// private List<String> fileNames;
	private static JSONObject languageData;

	public Language() {

		// needs rework for InputStream
		/*
		 * File folder = new File(path); fileNames = new ArrayList<String>();
		 * File[] listOfFiles = folder.listFiles();
		 * 
		 * for (File file : listOfFiles) { if (file.isFile()) {
		 * fileNames.add(file.getName().replaceAll(".json", "")); } }
		 */
	}

	public boolean loadLanguage(String language) {
		JSONParser parser = new JSONParser();

		try {
			InputStreamReader in = new InputStreamReader(Class.class.getResourceAsStream("/res/language/" + language + ".json"));
			BufferedReader reader = new BufferedReader(in);
			Object obj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;
			languageData = (JSONObject) jsonObject.get("sentences");

			String version = (String) jsonObject.get("supportedVersion");

			if (Float.parseFloat(version) < Game.getCurrentVersion()) {
				throw new Exception("Language file is outdated!");
			}

		} catch (IOException e) {
			e.printStackTrace();
			Logs.printLog(e.getMessage());
			return false;
		} catch (ParseException e) {
			e.printStackTrace();
			Logs.printLog(e.getMessage());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			Logs.printLog(e.getMessage());
			return false;
		}

		return true;

	}

	public static String getLanguageData(String key) {
		// no error checking for now
		return new String((String) languageData.get(key));
	}

}
