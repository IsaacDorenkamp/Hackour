package hackour.game;

import java.awt.Graphics;
import java.util.ArrayList;

public class SquareObject extends PhysicalObject{
	//NOTE: Position and size attributes are part of PhysicalObject
	private int side;
	
	private boolean jumping = false;
	private boolean first_jump = false;
	
	private boolean adjusted = false;
	
	private GameCanvas container;
	private Game ga;
	
	private SquareObject( int side, int x, int y, GameCanvas container, Game g ){
		super();
		this.side = side;
		width = side;
		height = side;
		this.x = x;
		this.y = y;
		
		velocityX = 0;
		velocityY = 0;
		
		this.container = container;
		ga = g;
	}
	public SquareObject( int x, int y, GameCanvas container, Game g ){
		this( HackourGame.UNIT_SIZE, x, y, container, g );
	}
	public SquareObject( GameCanvas container, Game g ){
		this(0,0, container, g);
	}
	
	@Override
	public void paint( Graphics gfx ){
		gfx.fillRect( x, y, side, side );
	}
	
	private boolean doGravity = true;
	private boolean onPlatform = false;
	
	public void doTick( Game g ){
		ArrayList<PhysicalObject> stats = ga.getStatics();
		
		boolean base_y_cond = (y + getSide() >= HackourGame.SOUTH_Y );
		
		if( !base_y_cond && ( CollisionDetector.CheckCollisionsFor_Direction(this, stats) == 0 || adjusted ) ){
			onPlatform = false;
			if( adjusted ) adjusted = false;
		}
		
		doGravity = !onPlatform;
		
		// Gravity right here.
		if( doGravity ){
			velocityY = PhysicsModule.calculate_gravity( this );
		}
		
		checkBounds(base_y_cond);
		
		if( !adjusted ){
			x += velocityX;
			y += velocityY;
		}else adjusted = false;
		
		CollisionDetector.CheckCollisionsFor( this, stats );
		if( !onPlatform ) adjusted = Lookahead();
		
		adjustBounds();
	}
	
	private void adjustBounds(){
		if( y + side + velocityY > HackourGame.SOUTH_Y ){
			y = HackourGame.SOUTH_Y - side;
			onGroundHit();
		}
		if( y + velocityY < HackourGame.NORTH_Y ){
			y = 0;
		}                                                      
		
		if( x + side + velocityX > HackourGame.EAST_X ){
			x = HackourGame.EAST_X - width;
		}
		if( x + velocityY < HackourGame.WEST_X ){
			x = 0;
		}
	}
	
	private void checkBounds(boolean base_y_cond){
		boolean left_x = ( x <= 0 );
		boolean right_x = ( ( x + getSide() ) >= HackourGame.EAST_X );
		if( left_x || right_x ){
			if( ( left_x && velocityX < 0 ) || ( right_x && velocityX > 0 ) ) velocityX = 0;
		}
		if( base_y_cond || y <= 0 ){
			if( y <= 0 && velocityY < 0 || base_y_cond && velocityY > 0 ){
				velocityY = 0;
			}
			if( base_y_cond && velocityY > 0 ){
				onGroundHit();
			}
		}else;
		if( x + getSide() >= HackourGame.EAST_X || x <= 0 ){
			if( x <= 0 && velocityX < 0 || x + getSide() >= HackourGame.EAST_X && velocityX > 0 ){
				velocityX = 0;
			}
			if( x <= 0 && velocityX > 0 ){
				onGroundHit();
			}
		}else;
	}
	
	private void onGroundHit(){
		if( first_jump ){
			setJumping( false );
			first_jump = false;
		}
		onPlatform = true;
		velocityY = 0;
	}
	private boolean Lookahead(){
		ArrayList<PhysicalObject> stats = ga.getStatics();
		
		SquareObject lookahead = new SquareObject( x + velocityX, y + velocityY, container, ga );
		
		PhysicalObject collided = CollisionDetector.CheckCollisionsFor_Object(lookahead, stats);
		if( collided == null ) return false;
		
		int direction = 0;
		
		int cx = collided.getX();
		int cw = collided.getWidth();
		int cy = collided.getY();
		int ch = collided.getHeight();
		
		int nx = lookahead.getX();
		int nw = lookahead.getWidth();
		int ny = lookahead.getY();
		int nh = lookahead.getHeight();
		
		int fl = Math.abs( cx - ( nx + nw ) );
		int fr = Math.abs( nx - ( cx + cw) );
		int ft = Math.abs( cy - ( ny + nh )  );
		int fb = Math.abs( ny - ( cy + ch ) );
		
		int[] vals = { fl, fr, ft, fb };
		
		int min = fl + fr + ft + fb; //Guaranteed to be larger than any single of the numbers unless they are somehow impossible all 0.
		for( int val : vals ){
			min = Math.min( min, val );
		}
		
		if( min == fl ) direction = CollisionDetector.LEFT;
		else if( min == fr ) direction = CollisionDetector.RIGHT;
		else if( min == ft ) direction = CollisionDetector.TOP;
		else if( min == fb ) direction = CollisionDetector.BOTTOM;
		else return false;
		
		if( direction == CollisionDetector.TOP ){
			y = cy - (height-1); //Preserve collision
			onGroundHit();
			return true;
		}else if( direction == CollisionDetector.BOTTOM ){
			y = cy + ch;
			return true;
		}else;
		
		if( direction == CollisionDetector.RIGHT ){
			x = cx + cw;
			return true;
		}else if( direction == CollisionDetector.LEFT ){
			x = cx - width;
			return true;
		}else;
		
		return false;
	}
	private String getDirection( int d ){
		switch(d){
		case CollisionDetector.RIGHT:
			return "RIGHT";
		case CollisionDetector.LEFT:
			return "LEFT";
		case CollisionDetector.TOP:
			return "TOP";
		case CollisionDetector.BOTTOM:
			return "BOTTOM";
		}
		return "NONE";
	}
	
	public void jump(){
		if( !jumping ){
			if( first_jump )
				setJumping( true );
			else
				first_jump = true;
			setVelocityY( -20 );
		}
		doGravity = true;
		onPlatform = false;
	}
	public void setJumping( boolean j ){
		jumping = j;
	}
	public boolean getJumping(){
		return jumping;
	}
	public void goHorizontalDirection( int direction, int velX ){
		boolean entered_loop = false;
		for( PhysicalObject obj : ga.GetObjectsOfType( PhysicalObject.TYPE_STATIC ) ){
			if( CollisionDetector.CheckCollision( x, y, width, height, obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight() ) != direction ){
				velocityX = velX;
			}
			if( !entered_loop ) entered_loop = true;
		}
		if( !entered_loop ) velocityX = velX;
	}
	
	public void setVelocityX( int vel ){
		velocityX = vel;
	}
	public void setVelocityY( int vel ){
		velocityY = vel;
	}

	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getSide(){
		return side;
	}
	
	public void setX( int x ){
		this.x = x;
	}
	public void setY( int y ){
		this.y = y;
	}
	public void setSide( int s ){
		this.side = s;
	}
	public void onCollision( PhysicalObject other, int direction ){
		if( direction == CollisionDetector.BOTTOM ){
			if( velocityY > 0 ) onGroundHit();
		}else if( direction == CollisionDetector.LEFT || direction == CollisionDetector.RIGHT ){
			setVelocityX( 0 );
		}else if( direction == CollisionDetector.TOP ){
			setVelocityY( 0 );
		}else;
	}
}