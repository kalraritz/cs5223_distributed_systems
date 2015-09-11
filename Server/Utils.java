package Server;

public class Utils {
	public static final int USER_MAX = 100;
	public static final int N_MAX = 32767;
	public static final int TIMEOUT = 20000;
	
	public static final byte JOIN_SUCCESS = 0x00;
	public static final byte JOIN_FAIL 	 = 0x01;	// 
	public static final byte JOIN_JOINED = 0x02;	// already joined
	
	public static final byte MOVE_NORTH = 0x10;		// up
	public static final byte MOVE_SOUTH = 0x11; 	// down
	public static final byte MOVE_EAST  = 0x12;		// right
	public static final byte MOVE_WEST  = 0x13;		// left
	public static final byte MOVE_STILL = 0x14;    	// defined by client

	public static final byte MOVE_FAIL_INVALIDE_POSITION = 0x20;
	public static final byte MOVE_SUCCESS = 0x21;
	public static final byte MOVE_FAIL_INVALIDE_INPUT = 0x22;
}