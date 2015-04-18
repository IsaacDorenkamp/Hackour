package hackour.game;

import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Point;

public class CollisionDetector{
	public static final int TOP = 60;
	public static final int RIGHT = 61;
	public static final int BOTTOM = 62;
	public static final int LEFT = 63;
	
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
			for( PhysicalObject staticobj : statics ){
				int direction = CollisionDetector.CheckCollision( entity.getX(), entity.getY(), staticobj.getX(), staticobj.getY() );
				entity.onCollision( staticobj, direction );
			}
		}
	}
	
	public static int CheckCollision( int x1, int y1, int x2, int y2 ){
		int ent_far_x = x1 + HackourGame.UNIT_SIZE;
		int ent_x = x1;
		int stat_far_x = x2 + HackourGame.UNIT_SIZE;
		int stat_x = x2;
		
		int ent_far_y = y1 + HackourGame.UNIT_SIZE;
		int ent_y = y1;
		int stat_far_y = y2 + HackourGame.UNIT_SIZE;
		int stat_y = y2;
		
		int direction = 0;
		if( ent_far_x > stat_x && ent_x < stat_far_x && ent_far_y > stat_y && ent_y < stat_far_y ){
			
			Point center = new Point( stat_x + ( HackourGame.UNIT_SIZE / 2 ), stat_y + ( HackourGame.UNIT_SIZE / 2 ) );
			
			int fromleft = Math.abs( ent_far_x - stat_x );
			int fromright = Math.abs( ent_x - stat_far_x );
			int fromtop = Math.abs( ent_far_y - stat_y );
			int frombottom = Math.abs( ent_y - stat_far_y );
			
			int[] distances = { fromleft, fromright, fromtop, frombottom };
			
			int max = CollisionDetector.max( distances );
			if( max == fromleft ){
				direction = CollisionDetector.LEFT;
			}else if( max == fromright ){
				direction = CollisionDetector.RIGHT;
			}else if( max == fromtop ){
				direction = CollisionDetector.TOP;
			}else if( max == frombottom ){
				direction = CollisionDetector.BOTTOM;
			}else;
		}
		
		return direction;
	}
	
	private static int max( int[] set ){
		int max = set[0];
		for( int i : set ){
			if( i > max ){
				max = i;
			}
		}
		return max;
	}
}