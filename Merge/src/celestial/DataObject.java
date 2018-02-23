package celestial;

import java.util.List;

import entities.Entity;
import entities.Player;
import models.GameItem;

/**
 * Data object class. It holds all the data needed for various threads.
 *
 * @author Adrian Setniewski
 *
 */
public class DataObject {
	
	
	public DataObject(){
		
	}
	
	private List<Entity> entities;
	private List<GameItem> gameItems;
	private List<Light> lights;
	private Player player;
	
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public List<GameItem> getGameItems() {
		return gameItems;
	}
	public void setGameItems(List<GameItem> gameItems) {
		this.gameItems = gameItems;
	}
	
	public List<Light> getLights() {
		return lights;
	}
	public void setLights(List<Light> lights) {
		this.lights = lights;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
}
