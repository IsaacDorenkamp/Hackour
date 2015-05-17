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
	public static HackourGame RUNNING_HOST;
	
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
	
	public Enemy createEnemy( int x, int y ){
		return new Enemy( x, y, gc, ga );
	}
	
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
				Iterator<PhysicalObject> it = ga.GetObjects().iterator();
				while( it.hasNext() ){
					PhysicalObject drawable = it.next();
					if( drawable.equals(sqo) ) continue;
					drawable.paint( gfx );
				}
				sqo.paint( gfx );
				if( draw_gridlines ){
					for( int y = 0; y <= HackourGame.WINDOW_HEIGHT; y += HackourGame.UNIT_SIZE ){
						gfx.drawLine( 0, y, HackourGame.WINDOW_WIDTH, y );
					}
					
					for( int x = 0; x <= HackourGame.WINDOW_WIDTH; x += HackourGame.UNIT_SIZE ){
						gfx.drawLine( x, 0, x, HackourGame.WINDOW_HEIGHT ); 
					}
				}
			}
		};
		ga.setCanvas( gc );
		sqo = new SquareObject( gc, ga );
		sqo.setX( HackourGame.WINDOW_WIDTH / 2 + (HackourGame.UNIT_SIZE / 2));
		sqo.setY(HackourGame.WINDOW_HEIGHT / 2 + (HackourGame.UNIT_SIZE / 2));
		sqo.setVelocityX( 0 );
		sqo.setVelocityY( 0 );
		current_layers.add( new Layer( sqo.getX(), sqo.getY() ) );	
		
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
		
		while(true){
			if( !ga.isRemoving() ) break;
			try{
				Thread.sleep(5);
			}catch(Exception e){}
		}
		
		for( PhysicalObject po : l.GetObjects() ){
			ga.AddObject(po);
		}
		
		sqo.setX( l.GetSpawnX() );
		sqo.setY( l.GetSpawnY() );
		ga.AddObject( sqo );
	}
	public void LoadLevel(String file){
		Level lev = LevelIO.readLevel(file);
		if( lev == null ){
			JOptionPane.showMessageDialog(this,"Failed to load level.");
		}else{
			current_layers.clear();
			int cur = 0;
			for( Layer next : lev.GetLayers() ){
				current_layers.add( next );
			}
			
			current_layer = 0; //Reset current layer to 0.
		}
		LoadLayer( current_layers.get(current_layer) );
	}
	public void SaveLevel(){
		current_layers.get(current_layer).SetSpawnX(sqo.getX());
		current_layers.get(current_layer).SetSpawnY(sqo.getY());
		String filename = JOptionPane.showInputDialog(this,"Level File");
		if( filename == null ) return;
		Level lev = new Level();
		for( Layer layer : current_layers ){
			lev.AddLayer(layer);
		}
		LevelIO.writeLevel(lev, filename);
	}
	
	//DevTools stuff
	public DevTools getDevTools(){
		return dtools;
	}
	
	//External Functions
	public void teleport( int x, int y ){
		sqo.setX( x );
		sqo.setY( y );
	}
	
	//Key Events
	
	Teleporter current = null;
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
			boolean isStatic = false;
			if( (e.getModifiers() & KeyEvent.SHIFT_MASK) != 0 ){
				isStatic = true;
			}
			PlacePoweredObjectIfEditMode( isStatic );
			break;
		case KeyEvent.VK_3:
		case KeyEvent.VK_4:
		case KeyEvent.VK_5:
			PlaceGateIfEditMode(keyCode);
			break;
		case KeyEvent.VK_DOWN:
			if( sqo.IsEditMode() ) sqo.setVelocityY( HackourGame.NORMAL_VELOCITY );
			break;
		case KeyEvent.VK_0:
			PlaceDoorIfEditMode();
			break;
		case KeyEvent.VK_C:
			PlaceClockIfEditMode();
			break;
		case KeyEvent.VK_G:
			PlaceGoalIfEditMode();
			break;
		case KeyEvent.VK_N:
			if( !sqo.IsEditMode() ) return;
			if( !ga.isRemoving() ) PlusLayer();
			break;
		case KeyEvent.VK_P:
			if( !sqo.IsEditMode() ) return;
			if( !ga.isRemoving() ) MinusLayer();
			break;
		case KeyEvent.VK_6:
			PlacePlusPortalIfEditMode();
			break;
		case KeyEvent.VK_7:
			PlaceMinusPortalIfEditMode();
			break;
		case KeyEvent.VK_8:
			PlaceTeleporterIfEditMode();
			break;
		case KeyEvent.VK_E:
			SpawnEnemyIfEditMode();
			break;
		case KeyEvent.VK_T:
			PlaceTextIfEditMode();
			break;
		default:
			break;
		}
		SaveLayer();
	}
	public void respawn(){
		Layer l = current_layers.get(0);
		sqo.setX( l.GetSpawnX() );
		sqo.setY( l.GetSpawnY() );
	}
	private void SaveLayer(){
		current_layers.get(current_layer).clear();
		for( PhysicalObject po : ga.GetObjects() ){
			current_layers.get(current_layer).AddObject(po);
		}
	}
	
	public void PlusLayer(){
		if( sqo.IsEditMode() ){
			current_layers.get(current_layer).SetSpawnX(sqo.getX());
			current_layers.get(current_layer).SetSpawnY(sqo.getY());
		}
		current_layers.get(current_layer).clear();
		for( PhysicalObject po : ga.GetObjects() ){
			if( po.equals(sqo) ) continue;
			current_layers.get(current_layer).AddObject(po);
		}
		current_layer++;
		if( current_layer >= current_layers.size() ){
			if( sqo.IsEditMode() ) current_layers.add( new Layer(sqo.getX(),sqo.getY()) );
			else return;
		}
		if( !sqo.IsEditMode() ){
			current_layers.get(current_layer).SetSpawnX(sqo.getX());
			current_layers.get(current_layer).SetSpawnY(sqo.getY());
		}
		LoadLayer( current_layers.get(current_layer) );
	}
	public void MinusLayer(){
		if( sqo.IsEditMode() ){
			current_layers.get(current_layer).SetSpawnX(sqo.getX());
			current_layers.get(current_layer).SetSpawnY(sqo.getY());
		}
		current_layers.get(current_layer).clear();
		for( PhysicalObject po : ga.GetObjects() ){
			if( po.equals(sqo) ) continue;
			current_layers.get(current_layer).AddObject(po);
		}
		current_layer--;
		if( current_layer < 0 ){
			current_layer = 0;
			return;
		}
		if( !sqo.IsEditMode() ){
			current_layers.get(current_layer).SetSpawnX(sqo.getX());
			current_layers.get(current_layer).SetSpawnY(sqo.getY());
		}
		LoadLayer( current_layers.get(current_layer) );
	}
	
	private void InteractIfPlayMode(){
		if( !sqo.IsEditMode() ){
			int[] coords = sqo.CenterUnit();
			PhysicalObject po = ga.get_unit( coords[0], coords[1] );
			
			if( po == null ) return;
			po.interact();
		}
	}
	private void PlaceTextIfEditMode(){
		if( sqo.IsEditMode() ){
			String s = JOptionPane.showInputDialog( null, "Text component text" );
			if( s == null || s.equals("") ) return;
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			TextComponent tc = new TextComponent( pos_x, pos_y, s );
			
			if( !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) && !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_BACKGROUND ) ){
				ga.AddObject( tc );
			}
		}
	}
	private void SpawnEnemyIfEditMode(){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )); //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE ));
			Enemy e = new Enemy( pos_x, pos_y, gc, ga );
			
			ga.AddObject( e );
		}
	}
	private void PlaceTeleporterIfEditMode(){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			Teleporter tp = new Teleporter( pos_x, pos_y, pos_x, pos_y );
			if( current != null ){
				tp.set_dx( current.getX() * HackourGame.UNIT_SIZE );
				tp.set_dy( current.getY() * HackourGame.UNIT_SIZE );
				current.set_dx( tp.getX() * HackourGame.UNIT_SIZE );
				current.set_dy( tp.getY() * HackourGame.UNIT_SIZE );
				current = null;
			}else current = tp;
			
			if( !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) && !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_BACKGROUND ) ){
				ga.AddObject( tp );
			}
		}
	}
	private void PlaceMinusPortalIfEditMode(){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			MinusPortal mp = new MinusPortal( pos_x, pos_y );
			
			if( !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) && !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_BACKGROUND ) ){
				ga.AddObject( mp );
			}
		}
	}
	private void PlacePlusPortalIfEditMode(){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			PlusPortal pp = new PlusPortal( pos_x, pos_y );
			
			if( !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) && !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_BACKGROUND ) ){
				ga.AddObject( pp );
			}
		}
	}
	private void PlaceGoalIfEditMode(){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			Goal g = new Goal( pos_x, pos_y, this );
			
			if( !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) && !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_BACKGROUND ) ){
				ga.AddObject( g );
			}
		}
	}
	private void PlaceSwitchIfEditMode(){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			Switch s = new Switch( pos_x, pos_y );
			
			if( !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) && !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_BACKGROUND ) ){
				ga.AddObject( s );
			}
		}
	}
	private void PlaceDoorIfEditMode(){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			Door d = new Door( pos_x, pos_y );
			
			if( !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) && !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_BACKGROUND ) ){
				ga.AddObject( d );
			}
		}
	}
	private void PlaceClockIfEditMode(){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			Clock c = new Clock( pos_x, pos_y );
			
			if( !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) && !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_BACKGROUND ) ){
				ga.AddObject( c );
			}
		}
	}
	private void PlaceGateIfEditMode( int kc ){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			ElectricGate eg;
			
			switch( kc ){
			case KeyEvent.VK_3:
				eg = new ORGate( pos_x, pos_y );
				break;
			case KeyEvent.VK_4:
				eg = new ANDGate( pos_x, pos_y );
				break;
			case KeyEvent.VK_5:
				eg = new XORGate( pos_x, pos_y );
				break;
			default:
				return;
			}
			
			if( !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) && !ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_BACKGROUND ) ){
				ga.AddObject( eg );
			}
		}
	}
	private void PlacePoweredObjectIfEditMode( boolean isStatic ){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			PowerableObject po;
			if( isStatic ) po = new BlockWire( pos_x, pos_y );
			else po = new Wire( pos_x, pos_y );
			
			if( ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) || ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_BACKGROUND ) );
			else ga.AddObject( po );
		}
	}
	private void PlaceBlockIfEditMode(){
		if( sqo.IsEditMode() ){
			int pos_x = (sqo.getX() - ( sqo.getX() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE; //Round to nearest tile
			int pos_y = (sqo.getY() - ( sqo.getY() % HackourGame.UNIT_SIZE )) / HackourGame.UNIT_SIZE;
			Block b = new Block( pos_x, pos_y );
			
			if( ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_STATIC ) || ga.check_unit(pos_x, pos_y, PhysicalObject.TYPE_BACKGROUND ) ){
				ga.RemoveObject(ga.get_unit(pos_x,pos_y));
			}else{
				ga.AddObject( b );
			}
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
	
	public void reset(){
		ga.RemoveAll();
		current_layers.clear();
		current_layer = 0;
		sqo.setX( HackourGame.WINDOW_WIDTH / 2 + (HackourGame.UNIT_SIZE / 2));
		sqo.setY(HackourGame.WINDOW_HEIGHT / 2 + (HackourGame.UNIT_SIZE / 2));
		current_layers.add( new Layer( sqo.getX(), sqo.getY() ) );
		ga.AddObject( sqo );
	}
	
	public static void main(String[] args){
		HackourGame.RUNNING_HOST = new HackourGame();
		HackourGame.RUNNING_GAME = RUNNING_HOST.getGame();
		if( args.length > 0 ){
			HackourGame.RUNNING_HOST.LoadLevel(args[0]);
		}
	}
}