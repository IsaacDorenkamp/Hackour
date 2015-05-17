package hackour.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Toolkit;

import hackour.devtools.*;

public class Game{
	private ArrayList<PhysicalObject> worldObjects = new ArrayList<>();
	private ArrayList<PhysicalObject> entities = new ArrayList<>();
	private ArrayList<PhysicalObject> stats = new ArrayList<>();
	private PhysicalObject[][] stat_map = new PhysicalObject[HackourGame.UNITS_HEIGHT][HackourGame.UNITS_WIDTH];
	
	private ArrayList<PhysicalObject> toremove = new ArrayList<>();
	private ArrayList<PhysicalObject> toadd    = new ArrayList<>();
	
	private GameCanvas canv = null;
	
	private boolean removing = false;
	public boolean isRemoving(){
		return removing;
	}
	public void repaint(){
		canv.repaint();
	}
	private Game parent = this;
	private Thread ticker = new Thread(){
		@Override
		public void run(){
			while( true ){
				check_cues();
				tick();
				try{
					Thread.sleep( tickInterval );
				}catch(Exception e){
					dtools.log( e.getMessage() );
				}
			}
		}
		public void check_cues(){
			synchronized( worldObjects ){
				synchronized(toremove){
					if( toremove.size() > 0 ){
						for( PhysicalObject rmv : toremove ){
							if( rmv.getType() == PhysicalObject.TYPE_ENTITY || rmv.getType() == PhysicalObject.TYPE_ENEMY ) entities.remove( rmv );
							else if( rmv.getType() == PhysicalObject.TYPE_STATIC || rmv.getType() == PhysicalObject.TYPE_BACKGROUND ){
								stats.remove( rmv );
								stat_map[rmv.getY()][rmv.getX()] = null;
							}else;
							worldObjects.remove(rmv);
						}
						toremove.clear();
					}
					removing = false;
				}
				synchronized(toadd){
					if( toadd.size() > 0 ){
						for( PhysicalObject obj : toadd ){
							worldObjects.add( obj );
							int type = ( ( PhysicalObject ) obj ).getType();
							if( type == PhysicalObject.TYPE_ENTITY || type == PhysicalObject.TYPE_ENEMY ) entities.add( (PhysicalObject) obj );
							if( type == PhysicalObject.TYPE_STATIC || type == PhysicalObject.TYPE_BACKGROUND ){
								if( stat_map[obj.getY()][obj.getX()] != null ) continue;
								else{
									stats.add( obj );
									stat_map[obj.getY()][obj.getX()] = obj;
								}
							}
							
						}
						toadd.clear();
					}
				}
			}
		}
		public void tick(){
			synchronized(worldObjects){
				for( PhysicalObject toTick : worldObjects ){
					toTick.doTick( parent );
				}
			}
			
			canv.repaint();
			Toolkit.getDefaultToolkit().sync();
		}
	};
	
	public void devtools_log( String msg ){
		dtools.log( msg );
	}
	public void devtools_log_var( String var, String val ){
		dtools.log_var( var, val );
	}
	
	public void start(){
		ticker.start();
	}

	
	public boolean check_unit( int x, int y, int type ){
		if( x < 0 || x >= HackourGame.UNITS_WIDTH || y < 0 || y >= HackourGame.UNITS_HEIGHT ) return false;
		if( stat_map[y][x] != null && stat_map[y][x].getType() == type ){
			return true;
		}
		return false;
	}
	public PhysicalObject get_unit( int x, int y ){
		if( x < 0 || x >= HackourGame.UNITS_WIDTH || y < 0 || y >= HackourGame.UNITS_HEIGHT ) return null;
		return stat_map[y][x];
	}
	public void set_unit( int x, int y, PhysicalObject obj ){
		if( x < 0 || x >= HackourGame.UNITS_WIDTH || y < 1 || y >= HackourGame.UNITS_HEIGHT ) return;
		stat_map[y][x] = obj;
	}
	public ArrayList<PhysicalObject> getStatics() {
		return stats;
	}

	public ArrayList<PhysicalObject> getEntities() {
		return entities;
	}
	
	private int tickInterval;
	private DevTools dtools;
	
	public Game( int tickInterval, DevTools dtools ){
		this.tickInterval = tickInterval;
		this.dtools = dtools;
	}
	
	public void setCanvas(GameCanvas gc){
		canv = gc;
	}
	
	public Iterator<PhysicalObject> GetObjectIterator(){
		return worldObjects.iterator();
	}
	
	public ArrayList<PhysicalObject> GetObjectsOfType( int type ){
		ArrayList<PhysicalObject> out = new ArrayList<>();
		for( PhysicalObject go : worldObjects ){
			if( go.getType() == type ){
				out.add( go );
			}
		}
		return out;
	}
	
	public ArrayList<PhysicalObject> GetObjects(){
		return new ArrayList<PhysicalObject>(worldObjects);
	}
	
	public void AddObject( PhysicalObject obj ){
		toadd.add( obj );
	}
	public void RemoveObject( PhysicalObject obj ){
		for( PhysicalObject g : worldObjects ){
			if ( obj.equals( g ) ){
				toremove.add( g );
				break;
			}else;
		}
	}
	public void RemoveAll(){
		for( PhysicalObject g : worldObjects ){
			toremove.add(g);
		}
		removing = true;
	}
}