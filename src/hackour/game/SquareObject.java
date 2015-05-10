package hackour.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class SquareObject extends PhysicalObject{
	//NOTE: Position and size attributes are part of PhysicalObject
	private int side;
	
	private boolean jumping = false;
	private boolean first_jump = false;
	
	private boolean adjusted = false;
	
	private boolean edit_mode = false;
	private boolean noClip = false;
	
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
		Color c = gfx.getColor();
		gfx.setColor( Color.BLACK );
		gfx.fillRect( x, y, side, side );
		gfx.setColor( c );
	}
	
	private boolean doGravity = true;
	private boolean onPlatform = false;
	public boolean isOnPlatform(){
		return onPlatform;
	}
	
	public int[] CenterUnit(){
		int[] coords = new int[2];
		coords[0] = (x - (x % HackourGame.UNIT_SIZE)) / HackourGame.UNIT_SIZE;
		coords[1] = (y - (y % HackourGame.UNIT_SIZE)) / HackourGame.UNIT_SIZE;
		return coords;
	}
	
	public void doTick( Game g ){
		ArrayList<PhysicalObject> stats = ga.getStatics();
		boolean base_y_cond = (y + getSide() >= HackourGame.WINDOW_HEIGHT );
		if( !edit_mode ){
			int[] coords = CenterUnit();
			if( !base_y_cond && ( !CollisionDetector.OnBlock( coords[0], coords[1], HackourGame.RUNNING_GAME) ) ){
				onPlatform = false;
			}
			
			doGravity = !onPlatform;
			
			// Gravity right here.
			if( doGravity ){
				velocityY = PhysicsModule.calculate_gravity( this );
			}
		}
		checkBounds(base_y_cond);
		
		if( !adjusted ){
			x += velocityX;
			y += velocityY;
		}else adjusted = false;
		
		if( !noClip ){
			CollisionDetector.CheckCollisionsFor( this, stats );
			if( !onPlatform ) Lookahead();
		}
		
		adjustBounds();
	}
	
	private void adjustBounds(){
		if( y + side + velocityY > HackourGame.SOUTH_Y ){
			y = HackourGame.SOUTH_Y - side;
			if( !PhysicsModule.INVERT_GRAVITY ) onGroundHit();
		}
		if( y + velocityY < HackourGame.NORTH_Y ){
			y = 0;
			if( PhysicsModule.INVERT_GRAVITY ) onGroundHit();
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
	
	public void EditMode(){
		edit_mode = true;
		doGravity = false;
		noClip = true;
	}
	public void PlayMode(){
		edit_mode = false;
		noClip = false;
	}
	public void ToggleMode(){
		if( !edit_mode ){
			EditMode();
		}else{
			PlayMode();
		}
	}
	
	public boolean IsEditMode(){
		return edit_mode;	
	}
	
	private void onGroundHit(){
		if( first_jump ){
			setJumping( false );
			first_jump = false;
		}
		onPlatform = true;
		velocityY = 0;
	}
	
	private boolean CheckHiddenEdge( int x, int y, int edge ){
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
		
		return ga.check_unit( x + tox, y + toy, PhysicalObject.TYPE_STATIC );
	}
	private void Lookahead(){
		SquareObject character = this;
		SquareObject lookahead = new SquareObject( x + velocityX, y + velocityY, container, ga ){
			public void onCollision( PhysicalObject other, int direction){
				switch(direction){
				case CollisionDetector.TOP:
					if( CheckHiddenEdge( other.getX(), other.getY(), CollisionDetector.TOP ) ) return;
					ga.devtools_log( String.format( "Old Y: %d", y ) );
					ga.devtools_log( String.format( "other.getY(): %d", other.getY() ) );
					ga.devtools_log( String.format( "character.getHeight(): %d", character.getHeight() ) );
					y = ( ( other.getY() * HackourGame.UNIT_SIZE ) - character.getHeight() );
					ga.devtools_log( String.format( "New Y: %d", y ) );
					break;
				case CollisionDetector.BOTTOM:
					if( CheckHiddenEdge( other.getX(), other.getY(), CollisionDetector.BOTTOM ) ) return;
					y = ( ( other.getY() * HackourGame.UNIT_SIZE ) + other.getHeight() );
					ga.devtools_log(String.valueOf(y));
					break;
				case CollisionDetector.LEFT:
					if( CheckHiddenEdge( other.getX(), other.getY(), CollisionDetector.LEFT ) ) return;
					x = ( ( other.getX() * HackourGame.UNIT_SIZE ) - character.getWidth() );
					ga.devtools_log(String.valueOf(x));
					break;
				case CollisionDetector.RIGHT:
					if( CheckHiddenEdge( other.getX(), other.getY(), CollisionDetector.RIGHT ) ) return;
					x = ( (other.getX() * HackourGame.UNIT_SIZE) + other.getWidth() );
					ga.devtools_log(String.valueOf(x));
					break;
				default:
					return;
				}
				character.setX( x );
				character.setY( y );
				character.onCollision( other, direction );
			}
		};
		
		CollisionDetector.CheckCollisionsFor( lookahead, ga.getStatics() );
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
			if( !PhysicsModule.INVERT_GRAVITY ) setVelocityY( -HackourGame.JUMP_VELOCITY );
			else{
				setVelocityY( HackourGame.JUMP_VELOCITY );
			}
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
		if( other.getType() == PhysicalObject.TYPE_STATIC ){
			if( direction == CollisionDetector.TOP ){
				if( !onPlatform && !PhysicsModule.INVERT_GRAVITY ) onGroundHit();
				else setVelocityY(0);
			}else if( direction == CollisionDetector.LEFT || direction == CollisionDetector.RIGHT ){
				int nd = direction;
				if( nd == CollisionDetector.RIGHT ) nd = CollisionDetector.LEFT;
				else nd = CollisionDetector.RIGHT;
				if(!CheckHiddenEdge(other.getX(),other.getY(),nd)) setVelocityX( 0 );
			}else if( direction == CollisionDetector.BOTTOM ){
				if( PhysicsModule.INVERT_GRAVITY ) onGroundHit();
				else setVelocityY(0);
			}else;
		}
	}
	
	public void interact(){}
}