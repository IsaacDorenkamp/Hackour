package hackour.game;

import java.awt.Graphics;
import java.util.ArrayList;

public class SquareObject extends PhysicalObject{
	//NOTE: Position and size attributes are part of PhysicalObject
	private int side;
	
	private boolean jumping = false;
	private boolean first_jump = false;
	
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
		CollisionDetector.CheckCollisionsFor( this, ga.getStatics() );
		//if( !onPlatform ) Lookahead();
		
		doGravity = !onPlatform;
		
		// Gravity right here.
		if( doGravity ){
			velocityY = PhysicsModule.calculate_gravity( this );
		}
		
		System.out.println("onPlatform: " + String.valueOf(onPlatform));
		
		boolean base_y_cond = (y + getSide() >= container.getHeight() );
		
		boolean left_x = ( x <= 0 );
		boolean right_x = ( ( x + getSide() ) >= container.getWidth() );
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
		
		x += velocityX;
		y += velocityY;
		
		if( y + getSide() + velocityY > container.getHeight() ){
			y = ( container.getHeight() - side );
			onGroundHit();
		}
	}
	
	private void onGroundHit(){
		if( first_jump ){
			setJumping( false );
			first_jump = false;
		}
		onPlatform = true;
		velocityY = 0;
		
		System.out.println("HIT");
	}
	private void Lookahead(){
		ArrayList<PhysicalObject> stats = ga.getStatics();
		int direction = CollisionDetector.CheckCollisionsFor_Direction(this, stats);
		if( direction == 0 ) return;
		
		PhysicalObject collided = CollisionDetector.CheckCollisionsFor_Object(this, stats);
		
		int newx = 0;
		int newy = 0;
		if( velocityY > 0 ){
			newy = y + ( collided.getY() - y );
		}else if( velocityY < 0 ){
			newy = y - ( y - collided.getY()  );
		}else;
		
		x = newx;
		y = newy;
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
		System.out.println( direction );
		if( direction == CollisionDetector.TOP ){
			onGroundHit();
		}else if( direction == CollisionDetector.LEFT || direction == CollisionDetector.RIGHT ){
			setVelocityX( 0 );
		}else if( direction == CollisionDetector.BOTTOM ){
			setVelocityY( 0 );
		}else;
	}
}