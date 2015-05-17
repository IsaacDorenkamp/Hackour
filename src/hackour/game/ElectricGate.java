package hackour.game;

import java.awt.Graphics;
import java.awt.Color;
import javax.imageio.ImageIO;

import java.awt.Image;
import java.io.File;

public class ElectricGate extends PhysicalObject{
	protected Image image = null;
	
	protected boolean in1 = false;
	protected boolean in2 = false;
	
	protected boolean out = false;
	
	private Game ga;
	protected ElectricGate( int x, int y, String img ){
		super(PhysicalObject.TYPE_BACKGROUND);
		this.x = x;
		this.y = y;
		width = HackourGame.UNIT_SIZE;
		height = HackourGame.UNIT_SIZE;
		
		ga = HackourGame.RUNNING_GAME;
		try{
			image = ImageIO.read( new File(img) );
		}catch(Exception e){}
	}
	public ElectricGate(int x, int y){
		this( x, y, "OR.png" );
	}
	
	public void onCollision( PhysicalObject other, int d ){}
	
	protected void CheckInputs(){
		if( y - 1 != -1 ){
			PhysicalObject o = ga.get_unit( x, y - 1 );
			if( o instanceof PowerableObject ){
				in1 = ( (PowerableObject) o ).isPowered();
			}
		}
		if( y + 1 != HackourGame.UNITS_HEIGHT ){
			PhysicalObject o = ga.get_unit( x, y + 1 );
			if( o instanceof PowerableObject ){
				in2 = ( (PowerableObject) o ).isPowered();
			}
		}
	}
	public boolean isOutputting(){
		return out;
	}
	public void interact(){}
	@Override
	public void doTick( Game g ){
		CheckInputs();
		out = in1 || in2;
	}
	
	public void paint( Graphics gfx ){
		Color old = gfx.getColor();
		
		if( image != null ){
			gfx.drawImage( image, ( x * HackourGame.UNIT_SIZE ), ( y * HackourGame.UNIT_SIZE ), null );
		}else{
			gfx.setColor( Color.PINK );
			gfx.fillRect( ( x * HackourGame.UNIT_SIZE ), ( y * HackourGame.UNIT_SIZE ), HackourGame.UNIT_SIZE, HackourGame.UNIT_SIZE );
		}
		
		gfx.setColor( old );
	}
}