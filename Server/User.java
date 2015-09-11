package Server;

import java.io.Serializable;

import Client.Client;

public class User implements Serializable{
	private Integer userID;
	private int pi,pj;
	private int treasure;
	private Client client;
	
	public User(int ID){
		this.userID = ID;
	}
	
	public void setClient(Client client){
		this.client = client;
	}
	public Client getClient(){
		return this.client;
	}
	public int getPi(){
		return this.pi;
	}
	
	public void setPi(int i){
		this.pi = i;
	}
	
	public int getPj(){
		return this.pj;
	}
	
	public void setPj(int j){
		this.pj = j;
	}
	
	public int getTreasure(){
		return this.treasure;
	}
	
	public void obtainTreasure(int t){
		this.treasure += t;
	}
	
	public void moveNorth(){
		this.pj --;
	}
	
	public void moveSouth(){
		this.pj ++;
	}
	
	public void moveEast(){
		this.pi ++;
	}
	
	public void moveWest(){
		this.pi --;
	}

}
