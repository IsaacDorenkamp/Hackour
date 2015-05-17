package hackour.game;

import java.awt.Graphics;
import java.awt.Color;

public abstract class PowerableObject extends PhysicalObject{
	public static final int TOP = 60;
	public static final int RIGHT = 61;
	public static final int BOTTOM = 62;
	public static final int LEFT = 63;
	
	public static final Color COPPER = new Color(184,115,51);
	
	private int last_dir = 0;
	Game ga;
	
	protected PowerableObject(int x, int y, int type){
		super( type );
		ga = HackourGame.RUNNING_GAME;
		this.x = x;
		this.y = y;
		width = HackourGame.UNIT_SIZE;
		height = HackourGame.UNIT_SIZE;
	}
	public PowerableObject(int x, int y){
		this( x, y, PhysicalObject.TYPE_BACKGROUND );
	}
	
	protected boolean powered = false;
	public boolean isPowered(){
		return powered;
	}
	public void setPowered( boolean b ){
		powered = b;
	}
	
	public void CheckPowered(){
		if( x - 1 != -1 ){
			PhysicalObject o = ga.get_unit( x - 1, y );
			if( o instanceof PowerableObject ){
				if( ((PowerableObject) o).isPowered() && ( last_dir == PowerableObject.LEFT || last_dir == 0 ) ){
					powered = true;
					last_dir = PowerableObject.LEFT;
					return;
				}
			}else if( o instanceof ElectricGate ){
				if( ( (ElectricGate) o ).isOutputting() ){
					powered = true;
					last_dir = PowerableObject.LEFT;
					return;
				}
			}else;
		}
		if( x + 1 != HackourGame.UNITS_WIDTH ){
			PhysicalObject o = ga.get_unit( x + 1, y );
			if( o instanceof PowerableObject ){
				if( ((PowerableObject) o).isPowered() && ( last_dir == PowerableObject.RIGHT || last_dir == 0 ) ){
					powered = true;
					last_dir = PowerableObject.RIGHT;
					return;
				}
			}
		}
		if( y - 1 != -1 ){
			PhysicalObject o = ga.get_unit( x, y - 1 );
			if( o instanceof PowerableObject ){
				if( ((PowerableObject) o).isPowered() && ( last_dir == PowerableObject.TOP || last_dir == 0 ) ){
					powered = true;
					last_dir = PowerableObject.TOP;	
					return;
				}
			}
		}
		if( y + 1 != HackourGame.UNITS_HEIGHT ){
			PhysicalObject o = ga.get_unit( x, y + 1 );
			if( o instanceof PowerableObject ){
				if( ((PowerableObject) o).isPowered() && ( last_dir == PowerableObject.BOTTOM || last_dir == 0 ) ){
					powered = true;
					last_dir = PowerableObject.BOTTOM;
					return;
				}
			}
		}
		
		last_dir = 0;
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