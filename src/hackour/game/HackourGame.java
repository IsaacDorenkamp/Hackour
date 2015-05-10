package hackour.game;

import hackour.levels.*;
import hackour.devtools.*;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Insets;

import java.util.Iterator;
import java.util.ArrayList;

public class HackourGame extends JFrame implements KeyListener{
	public static Game RUNNING_GAME;
	
	private GameCanvas gc;
	private Game ga;
	private SquareObject sqo;
	
	private DevTools dtools = new DevTools(this);
	
	private ArrayList<Layer> current_layers = new ArrayList<>();
	private int current_layer = 0;
	
	public static final int UNIT_SIZE = 15;
	public static final int NORMAL_VELOCITY = 2;
	public static final int JUMP_VELOCITY = 10;
	
	public static final int NORTH_Y = 0;
	public static final int SOUTH_Y = 600;
	public static final int WEST_X  = 0;
	public static final int EAST_X  = 1050;
	
	public static final int WINDOW_HEIGHT = 600;
	public static final int WINDOW_WIDTH =  1050;
	
	public static final int UNITS_HEIGHT = WINDOW_HEIGHT / UNIT_SIZE;
	public static final int UNITS_WIDTH =  WINDOW_WIDTH / UNIT_SIZE;
	
	public static int[] NearestUnit(int pix_x, int pix_y){
		int[] coords = new int[2];
		coords[0] = (pix_x - ( pix_x % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
		coords[1] = (pix_y - ( pix_y % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
		return coords;
	}
	
	private void CreateGUI(){
		add( gc );
		setTitle( "Hackour" );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setVisible(true);
		
		Insets insets = getInsets();
		setSize( HackourGame.WINDOW_WIDTH + insets.left + insets.right, HackourGame.WINDOW_HEIGHT + insets.bottom + insets.top );
		
		addKeyListener( this );
		ga.AddObject( sqo );
		
		ga.start();
		
		sqo.setX( HackourGame.WINDOW_WIDTH / 2 + (HackourGame.UNIT_SIZE / 2));
		sqo.setY(HackourGame.WINDOW_HEIGHT / 2 + (HackourGame.UNIT_SIZE / 2));
		sqo.setVelocityX( 0 );
		sqo.setVelocityY( 0 );
	}
	
	private boolean draw_gridlines = false;
	public void setGridlines( boolean b ){
		draw_gridlines = b;
	}
	public boolean getGridlines(){
		return draw_gridlines;
	}
	
	public HackourGame(){
		ga = new Game( 12, dtools );
		gc = new GameCanvas(){
			@Override
			public void CanvasPaint( Graphics gfx ){
				Iterator<PhysicalObject> it = ga.GetObjectIterator();
				while( it.hasNext() ){
					PhysicalObject drawable = it.next();
					drawable.paint( gfx );
				}
				if( draw_gridlines ){
					for( int y = 0; y <= HackourGame.WINDOW_HEIGHT; y += HackourGame.UNIT_SIZE ){
						gfx.drawLine( 0, y, HackourGame.WINDOW_WIDTH, y );
					}
					
					for( int x = 0; x <= HackourGame.WINDOW_WIDTH; x += HackourGame.UNIT_SIZE ){
						gfx.drawLine( x, 0, x, HackourGame.WINDOW_HEIGHT ); 
					}
				}
				
				dtools.log_var("onPlatform", String.valueOf(sqo.isOnPlatform()));
				dtools.log_var("sqo.CenterUnit() X", String.valueOf(sqo.CenterUnit()[0]));
				dtools.log_var("sqo.CenterUnit() Y", String.valueOf(sqo.CenterUnit()[1]));
			}
		};
		ga.setCanvas( gc );
		sqo = new SquareObject( gc, ga );
		
		CreateGUI();
	}
	
	public Game getGame(){
		return ga;
	}
	
	//Character Stuff
	public SquareObject GetCharacter(){
		return sqo;
	}
	
	//LevelIO
	public void LoadLayer(Layer l){
		dtools.log("Loading layer " + String.valueOf(current_layer) );
		ga.RemoveAll();
		for( PhysicalObject po : l.GetObjects() ){
			ga.AddObject(po);
		}
		
		sqo.setX( l.GetSpawnX() );
		sqo.setY( l.GetSpawnY() );
		ga.AddObject( sqo );
	}
	public void LoadLevel(String file){
		dtools.log("Loading level.");
		Level lev = LevelIO.readLevel(file);
		if( lev == null ){
			JOptionPane.showMessageDialog(this,"Failed to load level.");
		}else{
			current_layers.clear();
			for( Layer next : lev.GetLayers() ){
				current_layers.add( next );
			}
			
			current_layer = 0; //Reset current layer to 0.
		}
		LoadLayer( current_layers.get(current_layer) );
	}
	public void SaveLevel(){
		String filename = JOptionPane.showInputDialog(this,"Level File");
		if( filename == null ) return;
		Level lev = new Level();
		Layer layer = new Layer(sqo.getX(), sqo.getY());
		for( PhysicalObject po : ga.GetObjects() ){
			layer.AddObject( po );
		}
		lev.AddLayer(layer);
		LevelIO.writeLevel(lev, filename);
	}
	
	//DevTools stuff
	public DevTools getDevTools(){
		return dtools;
	}
	
	//Key Events
	@Override
	public void keyPressed( KeyEvent e ){
		int keyCode = e.getKeyCode();
		
		switch( keyCode ){
		case KeyEvent.VK_UP:
			if( !sqo.IsEditMode() ) sqo.jump();
			else sqo.setVelocityY( -HackourGame.NORMAL_VELOCITY );
			break;
		case KeyEvent.VK_LEFT:
			if( !sqo.IsEditMode() ) sqo.goHorizontalDirection( CollisionDetector.LEFT, -HackourGame.NORMAL_VELOCITY );
			else sqo.setVelocityX( -HackourGame.NORMAL_VELOCITY );
			break;
		case KeyEvent.VK_RIGHT:
			if( !sqo.IsEditMode() ) sqo.goHorizontalDirection( CollisionDetector.RIGHT, HackourGame.NORMAL_VELOCITY );
			else sqo.setVelocityX( HackourGame.NORMAL_VELOCITY );
			break;
		case KeyEvent.VK_D:
			if( (e.getModifiers() & KeyEvent.CTRL_MASK) != 0 && (e.getModifiers() & KeyEvent.SHIFT_MASK) != 0 ){
				dtools.ShowWindow();
			}
			break;
		case KeyEvent.VK_SPACE:
			PlaceBlockIfEditMode();
			InteractIfPlayMode();
			break;
		case KeyEvent.VK_1:
			PlaceSwitchIfEditMode();
			break;
		case KeyEvent.VK_2:
			PlacePoweredObjectIfEditMode();
			break;
		case KeyEvent.VK_DOWN:
			if( sqo.IsEditMode() ) sqo.setVelocityY( HackourGame.NORMAL_VELOCITY );
		default:
			break;
		}
	}
	
	private void InteractIfPlayMode(){
		if( !sqo.IsEditMode() ){
			int[] coords = HackourGame.NearestUnit(sqo.getX(), sqo.getY());
			PhysicalObject po = ga.get_unit( coords[0], coords[1] );
			po.interact();
		}
	}
	private void PlaceSwitchIfEditMode(){
		if( sqo.IsEditMode() ){
			dtools.log("Yeppers");
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			Switch s = new Switch( pos_x, pos_y );
			
			if( !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) ) ga.AddObject( s );
		}
	}
	private void PlacePoweredObjectIfEditMode(){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			PowerableObject po = new PowerableObject( pos_x, pos_y ){
				public void interact(){}
				public void onCollision(PhysicalObject other, int d){}
			};
			
			if( ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) );
			else ga.AddObject( po );
		}
	}
	private void PlaceBlockIfEditMode(){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			Block b = new Block( pos_x, pos_y );
			
			if( ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) ){
				ga.RemoveObject(ga.get_unit(pos_x,pos_y));
			}else ga.AddObject( b );
		}
	}
	
	public void keyReleased( KeyEvent e ){
		int keyCode = e.getKeyCode();
		
		switch( keyCode ){
		case KeyEvent.VK_LEFT:
			if( sqo.getVelocityX() < 0 ) sqo.setVelocityX( 0 );
			break;
		case KeyEvent.VK_RIGHT:
			if( sqo.getVelocityX() > 0 ) sqo.setVelocityX( 0 );
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
			if( sqo.IsEditMode() ) sqo.setVelocityY( 0 );
			break;
		default:
			break;
		}
	}
	public void keyTyped( KeyEvent e ){}
	
	public static void main(String[] args){
		HackourGame.RUNNING_GAME = new HackourGame().getGame();
	}
}