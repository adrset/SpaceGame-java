package celestial;

import java.util.List;

import entities.Entity;
import entities.Player;

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
	private List<Planet> planets;
	private List<Asteroid> asteroids;
	private List<HostileShip> hostileShips;
	private List<Light> lights;
	private Player player;
	
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public List<Planet> getPlanets() {
		return planets;
	}
	public void setPlanets(List<Planet> planets) {
		this.planets = planets;
	}
	public List<Asteroid> getAsteroids() {
		return asteroids;
	}
	public void setAsteroids(List<Asteroid> asteroids) {
		this.asteroids = asteroids;
	}
	public List<HostileShip> getHostileShips() {
		return hostileShips;
	}
	public void setHostileShips(List<HostileShip> hostileShips) {
		this.hostileShips = hostileShips;
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
