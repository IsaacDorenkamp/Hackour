package hackour.levels;

public class LevelReader{
	public static void main(String[] args){
		Level lev = LevelIO.readLevel("level.dat");
		for( Layer i : lev.GetLayers() ){
			System.out.printf( "X: %d; Y: %d\n", i.GetSpawnX(), i.GetSpawnY() );
		}
	}
}