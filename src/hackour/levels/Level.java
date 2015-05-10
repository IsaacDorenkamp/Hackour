package hackour.levels;

import java.util.ArrayList;

public class Level{
	private ArrayList<Layer> layers = new ArrayList<>();
	
	public Level(){}
	
	public void AddLayer(Layer l){
		layers.add(l);
	}
	
	public ArrayList<Layer> GetLayers(){
		return new ArrayList<Layer>(layers);
	}
}