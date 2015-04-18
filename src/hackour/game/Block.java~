package hackour.game;

import java.awt.Graphics;

public class Block extends PhysicalObject{
	private int width;
	private int height;
	
	public int getVelocityY(){
		return 0;
	}
	public int getVelocityX(){
		return 0;
	}
	
	public Block( int x, int y ){
		super( PhysicalObject.TYPE_STATIC );
		this.x = x;
		this.y = y;
	}
	
	public void doTick( Game g ){}
	
	public void draw( Graphics g ){
		g.fillRect( x, y, HackourGame.UNIT_SIZE, HackourGame.UNIT_SIZE );
	}
	
	public void onCollision( PhysicalObject other, int direction ){}
}