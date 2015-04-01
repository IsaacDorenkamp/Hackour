package hackour.game;

public class PhysicsModule{
	public static final int GRAVITY_FORCE = 2;
	
	public static int calculate_gravity( PhysicalObject po ){
		if( po.getVelocityY() + PhysicsModule.GRAVITY_FORCE > 15 ){
			return 15;
		}
		return po.getVelocityY() + PhysicsModule.GRAVITY_FORCE;
	}
}