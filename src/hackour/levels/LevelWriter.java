package hackour.levels;

import hackour.game.*;
import java.util.ArrayList;

public class LevelWriter{
	private Level lev = new Level();
	public LevelWriter(){}
	
	public void writeLevel(String file){
		LevelIO.writeLevel(lev, file);
		lev = new Level();
	}
	public void createLayer( int sx, int sy, ArrayList<PhysicalObject> objs ){
		Layer cur = new Layer(sx,sy);
		for( PhysicalObject po : objs ){
			cur.AddObject(po);
		}
		lev.AddLayer(cur);
	}
	
	public static void main(String[] args){
		LevelWriter lw = new LevelWriter();
		
		Block b = new Block( 300, 400, 100, HackourGame.UNIT_SIZE * 4 );
		ArrayList<PhysicalObject> objs = new ArrayList<>();
		objs.add(b);
		
		lw.createLayer( 343, 370, objs );
		lw.writeLevel("level.dat");
	}
}