package hackour.game;

import java.awt.Graphics;

public class Block extends PhysicalObject{
	public Block( int x, int y, int w, int h ){
		super( PhysicalObject.TYPE_STATIC );
		this.x = x;
		this.y = y;
		width = w;
		height = h;
	}
	public Block( int x, int y ){
		this( x, y, HackourGame.UNIT_SIZE, HackourGame.UNIT_SIZE );
	}
	public Block(){
		this( 0,0 );
	}
	
	public void doTick( Game g ){}
	
	@Override
	public void paint( Graphics gfx ){
		gfx.fillRect( x, y, width, height );
	}
	
	public void onCollision( PhysicalObject other, int direction ){}
}