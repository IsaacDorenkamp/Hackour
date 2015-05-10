package hackour.game;

import java.awt.Graphics;
import java.awt.Color;

public abstract class PowerableObject extends PhysicalObject{
	public static final Color COPPER = new Color(184,115,51);
	
	Game ga;
	public PowerableObject(int x, int y){
		super( PhysicalObject.TYPE_STATIC );
		ga = HackourGame.RUNNING_GAME;
		this.x = x;
		this.y = y;
	}
	
	protected boolean powered = false;
	public boolean isPowered(){
		return powered;
	}
	public void setPowered( boolean b ){
		powered = b;
	}
	
	public void CheckPowered(){
		if( x != 0 ){
			PhysicalObject o = ga.get_unit( x - 1, y );
			if( o instanceof PowerableObject ){
				if( ((PowerableObject) o).isPowered() ){
					powered = true;
					return;
				}
			}
		}
		if( x != HackourGame.UNITS_WIDTH ){
			PhysicalObject o = ga.get_unit( x + 1, y );
			if( o instanceof PowerableObject ){
				if( ((PowerableObject) o).isPowered() ){
					powered = true;
					return;
				}
			}
		}
		if( y != 0 ){
			PhysicalObject o = ga.get_unit( x, y - 1 );
			if( o instanceof PowerableObject ){
				if( ((PowerableObject) o).isPowered() ){
					powered = true;
					return;
				}
			}
		}
		if( y != HackourGame.UNITS_HEIGHT ){
			PhysicalObject o = ga.get_unit( x, y + 1 );
			if( o instanceof PowerableObject ){
				if( ((PowerableObject) o).isPowered() ){
					powered = true;
					return;
				}
			}
		}
		
		powered = false;
	}
	
	public void doTick(Game g){
		CheckPowered();
	}
	public void paint( Graphics gfx ){
		Color old = gfx.getColor();
		Color use = COPPER;
		if( powered ) use = Color.YELLOW;
		gfx.setColor(use);
		
		gfx.fillRect( x * HackourGame.UNIT_SIZE, y * HackourGame.UNIT_SIZE, width, height );
		
		gfx.setColor( old );
	}
}