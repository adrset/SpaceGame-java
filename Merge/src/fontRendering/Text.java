package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import renderEngine.Loader;

public class Text {
	private Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private Loader loader;
	private FontRenderer fontRenderer;
	
	public void init(Loader inLoader){
		fontRenderer = new FontRenderer();
		loader = inLoader;
	}
	
	public void loadText(GUIText text){
		//get font from text
		FontType font = text.getFont();
		
		//load data to mesh
		TextMeshData data = font.loadText(text);
		
		//load data to vao
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		//set mesh info
		text.setMeshInfo(vao, data.getVertexCount());
		
		//look for font in map
		List<GUIText> txts = texts.get(font);
		
		//if does not exist, create arraylist and put it into map
		if(txts == null){
			txts = new ArrayList<GUIText>();
			texts.put(font, txts);
		}
		
		//now it must exist, so it can be added to list
		txts.add(text);
		
	}
	
	//remove text, because it is not needed
	public void removeText(GUIText text){
		List<GUIText> txts = texts.get(text.getFont());
		txts.remove(text);
		
		//if empty - remove from hash map
		if(txts.isEmpty()){
			texts.remove(text.getFont());
		}
	}
	
	//render all texts
	public void render(){
		fontRenderer.render(texts);
	}
	
	//cleanup
	public void cleanUp(){
		fontRenderer.cleanUp();
	}
}
