package Server;

import java.io.Serializable;

import Client.Client;

public class User implements Serializable{
	private long userID;
	private int pi,pj;
	private int treasure;
	private Client client;
	
	public User(long userID){
		this.userID = userID;
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
	
	public void setTreasure(int t){
		this.treasure = t;
	}
}
