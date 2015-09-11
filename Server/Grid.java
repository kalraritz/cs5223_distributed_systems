package Server;

import java.io.Serializable;

/**
 * includes two variable:
 * @param treasure
 * @param user
 * 
 */

public class Grid implements Serializable {
	public int treasure;
	public Integer user;
	
	public Grid(){
		this.treasure = 0;
		this.user = null;
	}
}