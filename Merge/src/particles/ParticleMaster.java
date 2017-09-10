package particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joml.Matrix4f;

import entities.Camera3D;
import renderEngine.Loader;

public class ParticleMaster {
	private static Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
	private static ParticleRenderer renderer;
	
	public static void init(Loader loader, Matrix4f projectionMatrix){
		renderer = new ParticleRenderer(loader, projectionMatrix);
	}
	
	public static void render(Camera3D camera){
		renderer.render(particles, camera);
	}
	
	public static void cleanUp(){
		renderer.cleanUp();
	}
	
	public static void addParticle(Particle p){
		List <Particle> ps = particles.get(p.getTexture());
		if(ps == null){
			ps = new ArrayList<Particle>();
			particles.put(p.getTexture(), ps);
		}
		ps.add(p);
	}
	
	public static int getParticleAmount(){
		return particles.size();
	}
	
	public static void update(){
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		
		while(mapIterator.hasNext()){
			List<Particle> list = mapIterator.next().getValue();
			int i =0;
			Iterator<Particle> iterator = list.iterator();
			while(iterator.hasNext()){
				i++;
				Particle p = iterator.next();
				boolean state = p.update();
				if(!state){
					
					iterator.remove();
					if(list.isEmpty()){
						
						mapIterator.remove();
					}
				}
				
			}
			System.out.println(i);
			
		}
		
	}
}
