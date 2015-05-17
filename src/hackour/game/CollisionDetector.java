package hackour.game;

import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Point;

public class CollisionDetector{
	public static final int TOP = 60;
	public static final int RIGHT = 61;
	public static final int BOTTOM = 62;
	public static final int LEFT = 63;
	
	
	public static boolean CheckHiddenEdge( int x, int y, int edge ){
		int tox = 0;
		int toy = 0;
		
		if( edge == CollisionDetector.LEFT ) tox = -1;
		else if( edge == CollisionDetector.RIGHT ) tox = 1;
		else if( edge == CollisionDetector.TOP ) toy = -1;
		else if( edge == CollisionDetector.BOTTOM ) toy = 1;
		else;
		
		if( x + tox < 0 || x + tox >= HackourGame.UNITS_WIDTH ){
			return false;
		}
		if( y + toy < 0 || y + toy >= HackourGame.UNITS_HEIGHT ){
			return false;
		}
		
		return HackourGame.RUNNING_GAME.check_unit( x + tox, y + toy, PhysicalObject.TYPE_STATIC );
	}
	public static boolean OnBlock( int x, int y, Game g ){
		boolean has_block = g.check_unit(x,y + 1, PhysicalObject.TYPE_STATIC);
		return has_block;
	}
	
	public static void CheckCollisionsFor( PhysicalObject entity, ArrayList<PhysicalObject> stats ){
		for( PhysicalObject stat : stats ){
			int sx = stat.getX();
			int sy = stat.getY();
			if( stat.getType() == PhysicalObject.TYPE_STATIC || stat.getType() == PhysicalObject.TYPE_BACKGROUND ){
				sx *= HackourGame.UNIT_SIZE;
				sy *= HackourGame.UNIT_SIZE;
			}
			int direction = CollisionDetector.CheckCollision( entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight(), sx, sy, stat.getWidth(), stat.getHeight() );
			if( direction != 0 ) entity.onCollision( stat, direction );
		}
	}
	public static int CheckCollisionsFor_Direction( PhysicalObject entity, ArrayList<PhysicalObject> stats ){
		for( PhysicalObject stat : stats ){
			int direction = CollisionDetector.CheckCollision( entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight(), stat.getX(), stat.getY(), stat.getWidth(), stat.getHeight() );
			if( direction != 0 ) return direction;
		}
		return 0;
	}
	public static PhysicalObject CheckCollisionsFor_Object( PhysicalObject entity, ArrayList<PhysicalObject> stats ){
		for( PhysicalObject stat : stats ){
			int direction = CollisionDetector.CheckCollision( entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight(), stat.getX(), stat.getY(), stat.getWidth(), stat.getHeight() );
			if( direction != 0 ) return stat;
		}
		return null;
	}
	
	public static void CheckCollisions( Iterator<PhysicalObject> poi ){
		ArrayList<PhysicalObject> statics = new ArrayList<>();
		ArrayList<PhysicalObject> entities = new ArrayList<>();
		
		while( poi.hasNext() ){
			PhysicalObject obj = poi.next();
			if( obj.getType() == PhysicalObject.TYPE_ENTITY ){
				entities.add( obj );
			}else if( obj.getType() == PhysicalObject.TYPE_STATIC ){
				statics.add( obj );
			}else;
		}
		for( PhysicalObject entity : entities ){
			CollisionDetector.CheckCollisionsFor( entity, statics );
		}
	}
	
	public static int CheckCollision( int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2 ){
		int ent_far_x = x1 + w1;
		int ent_x = x1;
		int stat_far_x = x2 + w2;
		int stat_x = x2;
		
		int ent_far_y = y1 + h1;
		int ent_y = y1;
		int stat_far_y = y2 + h2;
		int stat_y = y2;
		
		int direction = 0;
		if( ent_far_x > stat_x && ent_x < stat_far_x && ent_far_y > stat_y && ent_y < stat_far_y ){
			int fromleft = Math.abs( stat_x - ent_far_x );
			int fromright = Math.abs( ent_x - stat_far_x );
			int fromtop = Math.abs( stat_y - ent_far_y );
			int frombottom = Math.abs( ent_y - stat_far_y );
			
			int MAX = fromleft + fromright + fromtop + frombottom;
			int[] distances = { fromleft, fromright, fromtop, frombottom };
			
			if( CollisionDetector.CheckHiddenEdge( stat_x, stat_y, CollisionDetector.LEFT ) ){
				distances[0] = MAX;
			}
			if( CollisionDetector.CheckHiddenEdge( stat_x, stat_y, CollisionDetector.RIGHT ) ){
				distances[1] = MAX;
			}
			if( CollisionDetector.CheckHiddenEdge( stat_x, stat_y, CollisionDetector.TOP ) ){
				distances[2] = MAX;
			}
			if( CollisionDetector.CheckHiddenEdge( stat_x, stat_y, CollisionDetector.BOTTOM ) ){
				distances[3] = MAX;
			}
			
			int min = CollisionDetector.min( distances );
			if( min == fromleft ){
				direction = CollisionDetector.LEFT;
			}else if( min == fromright ){
				direction = CollisionDetector.RIGHT;
			}else if( min == fromtop ){
				direction = CollisionDetector.TOP;
			}else if( min == frombottom ){
				direction = CollisionDetector.BOTTOM;
			}else;
		}
		
		return direction;
	}
	
	private static int min( int[] set ){
		int min = set[0];
		for( int i : set ){
			min = Math.min( min, i );
		}
		return min;
	}
}