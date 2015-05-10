package hackour.levels;

import hackour.game.*;

import java.io.*;

public class LevelIO{
	public static final int CODE_BLOCK = 0;
	
	public static final int LAYER_END = 1600;
	public static final int FILE_END  = 1601;
	
	private static DataInputStream dis;
	private static DataOutputStream dos;
	
	public static Level readLevel(String file){
		try{
			dis = new DataInputStream( new FileInputStream(file) );
		}catch(IOException ioe){
			ioe.printStackTrace();
			return null;
		}
		Level out = new Level();
		Layer cur;
		
		while( true ){
			try{
				int in_int = dis.readInt();
				
				if( in_int == FILE_END ){
					break;
				}
				
				int x = in_int;
				int y = dis.readInt();
				
				cur = new Layer(x,y);
				
				int cur_val = 0;
				while( (cur_val = dis.readInt()) != LAYER_END && cur_val != FILE_END ){
					switch(cur_val){
					case CODE_BLOCK:
						int bx = dis.readInt();
						int by = dis.readInt();
						int bw = dis.readInt();
						int bh = dis.readInt();
						Block b = new Block( bx, by, bw, bh );
						cur.AddObject(b);
						break;
					default:
						break;
					}
				}
				out.AddLayer(cur);
			}catch(IOException ioe){
				break;
			}
		}
		return out;
	}
	
	public static void writeLevel(Level l, String file){
		try{
			dos = new DataOutputStream( new FileOutputStream(file) );
			for( Layer la : l.GetLayers() ){
				write_spawn_data(la);
				for( PhysicalObject po : la.GetObjects() ){
					
					if( po instanceof Block ){
						write_object( CODE_BLOCK, (Block)po );
					}
					
				}
			}
			
			end_file();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	private static void write_object( int code, PhysicalObject po ) throws IOException{
		dos.writeInt(code);
		dos.writeInt(po.getX());
		dos.writeInt(po.getY());
		dos.writeInt(po.getWidth());
		dos.writeInt(po.getHeight());
	}
	private static void end_layer() throws IOException{
		dos.writeInt( LAYER_END );
	}
	private static void end_file() throws IOException{
		dos.writeInt( FILE_END );
		dos.close();
	}
	private static void write_spawn_data(Layer data) throws IOException{
		dos.writeInt( data.GetSpawnX() );
		dos.writeInt( data.GetSpawnY() );
	}
}