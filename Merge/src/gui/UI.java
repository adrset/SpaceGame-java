package gui;

import java.util.ArrayList;
import java.util.List;

import fontRendering.Text;
import renderEngine.Loader;

/**
 * Represents the User Interface.
 * Contains a list of layers and mechanism to switch between them.
 * 
 * @author Adrian Setniewski
 *
 */

public class UI {
	
	private List<Layer> layers;
	
	private int currentLayer;
	
	private int numLayers = 0;
	
	private GuiRenderer guiRenderer;
	
	private Text textPrint;
	
	public UI(Loader loader){
		layers = new ArrayList<Layer>();
		textPrint = new Text();
		this.guiRenderer = new GuiRenderer(loader);
		textPrint.init(loader);
	}
	
	public int addLayer(){
		
		layers.add(new Layer(textPrint, numLayers ));
		numLayers++;
		return numLayers - 1;
		
		
	}
	
	public void setCurrentLayer(int index){
		currentLayer = index;
	}

	public void render(){
		
		getLayer(currentLayer).update();
		if(getLayer(currentLayer).getBackground() != null){
			guiRenderer.render(getLayer(currentLayer).getBackground());
		}
		guiRenderer.render(getLayer(currentLayer).getButtons());
		textPrint.render();
		
	}
	
	public Layer getLayer(int index){
		return layers.get(index);
	}
	
	public void cleanUp(){
		guiRenderer.cleanUp();
		textPrint.cleanUp();
	}
}
