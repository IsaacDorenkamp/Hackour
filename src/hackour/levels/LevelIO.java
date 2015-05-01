package hackour.levels;

import java.io.*;

public class LevelIO{
	private static DataInputStream dis;
	private static DataOutputStream dos;
	
	public static Level readLevel(String file){
		try{
			dis = new DataInputStream(file);
		}catch(IOException ioe){
			ioe.printStackTrace();
			return;
		}
		
		
	}
	
	public static void writeLevel(Level l, String file){
		
	}
}