package hackour.levels;

import hackour.game.*;

import java.util.ArrayList;

public class Layer{
	private ArrayList<PhysicalObject> objs = new ArrayList<>();
	private int spawn_x;
	private int spawn_y;
	
	public Layer(int sx, int sy){
		spawn_x = sx;
		spawn_y = sy;
	}
	
	public void AddObject( PhysicalObject po ){
		objs.add( po );
	}
}