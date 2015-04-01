package hackour.game.test;

import hackour.game.*;

public class SquareObject implements GObject{
	private int side;
	
	private int x;
	private int y;
	
	private int velocityX = 0;
	private int velocityY = 0;
	
	private GameCanvas container;
	
	public SquareObject( int side, int x, int y, GameCanvas container ){
		this.side = side;
		this.x = x;
		this.y = y;
		
		this.container = container;
	}
	public SquareObject( int x, int y, GameCanvas container ){
		this( 15, x, y, container );
	}
	public SquareObject( GameCanvas container ){
		this(0,0, container);
	}
	
	public void doTick( Game g ){
		if( ( x  + getSide() >= container.getWidth() && velocityX > 0 ) || ( x <= 0 && velocityX < 0 ) ){
			velocityX = 0;
		}else if( (y + getSide() >= container.getHeight() && velocityY > 0 ) || (y <= 0  && velocityY < 0 ) ){
			velocityY = 0;
		}else{
			x += velocityX;
			y += velocityY;
		}
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
}