package hackour.game;

public class PhysicsModule{
	public static final int GRAVITY_FORCE = 1;
	public static final int MAX_GRAVITY = 8;
	public static boolean INVERT_GRAVITY  = false;
	
	public static int calculate_gravity( PhysicalObject po ){
		if( !PhysicsModule.INVERT_GRAVITY ){
			if( po.getVelocityY() + PhysicsModule.GRAVITY_FORCE > PhysicsModule.MAX_GRAVITY ){
				return PhysicsModule.MAX_GRAVITY;
			}
			return po.getVelocityY() + PhysicsModule.GRAVITY_FORCE; 
		}else{
			if( po.getVelocityY() + PhysicsModule.GRAVITY_FORCE < -PhysicsModule.MAX_GRAVITY ){
				return -PhysicsModule.MAX_GRAVITY;
			}
			return po.getVelocityY() - PhysicsModule.GRAVITY_FORCE; 
		}
	}
}