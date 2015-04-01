package hackour.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Toolkit;

public class Game extends Thread{
	private ArrayList<GObject> worldObjects = new ArrayList<>();
	private int tickInterval;
	
	public Game( int tickInterval ){
		this.tickInterval = tickInterval;
	}
	
	protected void tick(){
		for( GObject toTick : worldObjects ){
			toTick.doTick( this );
		}
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	@Override
	public void run(){
		while( true ){
			tick();
			try{
				Thread.sleep( tickInterval );
			}catch(Exception e){}
		}
	}
	
	public Iterator<GObject> GetObjectIterator(){
		return worldObjects.iterator();
	}
	
	public ArrayList<PhysicalObject> GetObjectsOfType( int type ){
		ArrayList<PhysicalObject> out = new ArrayList<>();
		for( GObject go : worldObjects ){
			if( go instanceof PhysicalObject ){
				if( ( (PhysicalObject) go ).getType() == type ){
					out.add( (PhysicalObject) go );
				}
			}
		}
		return out;
	}
	
	public void AddObject( GObject obj ){
		worldObjects.add( obj );
	}
	public void RemoveObject( GObject obj ){
		int i = 0;
		for( GObject g : worldObjects ){
			if ( obj.equals( g ) ){
				worldObjects.remove( i );
				break;
			}else i++;
		}
	}
}