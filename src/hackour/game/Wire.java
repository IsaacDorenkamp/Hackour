package hackour.game;

public class Wire extends PowerableObject{
	public Wire( int x, int y ){
		super( x, y );
	}
	public void interact(){}
	public void onCollision(PhysicalObject other, int d){}
}