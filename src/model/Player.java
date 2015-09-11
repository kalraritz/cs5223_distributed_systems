package model;

import java.io.Serializable;

public class Player implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8806390778859107832L;
	private String id;
	private Point position;
	private int numOfCollectedTreasures;
	private boolean isOnline;
		
	public Player(String id){
		this.id = id;
		position = new Point(0,0);
		numOfCollectedTreasures = 0;
	}
	
	public Player(String id, Point initialPosition){
		this.id = id;
		position = initialPosition;
		numOfCollectedTreasures = 0;
	}
	
	public String getId() {
		return id;
	}

	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public int getNumOfCollectedTreasures() {
		return numOfCollectedTreasures;
	}
	
	public void setNumOfCollectedTreasures(int numOfCollectedTreasures) {
		this.numOfCollectedTreasures = numOfCollectedTreasures;
	}
	
	public void addNumOfCollectedTreasures(int treasureVal){
		numOfCollectedTreasures += treasureVal;
	}
	
	public boolean decreaseNumOfCollectedTreasures(int treasureVal){
		if(treasureVal > numOfCollectedTreasures){
			return false;
		}
		else{
			numOfCollectedTreasures -= treasureVal;
			return true;
		}
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
}
