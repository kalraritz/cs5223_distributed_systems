package model;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import model.exception.InvalidPlayerIdException;
import model.exception.InvalidPositionException;

public class Maze implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2474167948087808225L;
	
	private int horizontalSize;
	private int verticalSize;
	private int treasureNum;
	private int treasureCollected;
	
	private int[][] treasureMap;
	private String[][] playerMap;
	
	private Hashtable<String, Player> players = null;
	
	public Maze(int size, int numTreasure){
		//initialize private properties
		horizontalSize = size;
		verticalSize = size;
		
		treasureNum = numTreasure;
		treasureCollected = 0;
		players = new Hashtable<String, Player>();
		
		//initialize treasure map and player map array
		treasureMap = new int[horizontalSize][verticalSize];
		playerMap = new String[horizontalSize][verticalSize];
		
		for(int i = 0; i < horizontalSize; i++){
			for(int j = 0; j < verticalSize; j++){
				treasureMap[i][j] = 0;
				playerMap[i][j] = null;
			}
		}
		
		//fill in the map with random number of treasures
		int treasureAllocated = 0;
		int currTreasureGenerated = 0;
		int posXGenerated = 0;
		int posYGenerated = 0;
		Random randomGeneratorPosX = new Random();
		Random randomGeneratorPosY = new Random();
		Random randomGeneratorTreasureVal = new Random();
		
		while(treasureAllocated < treasureNum){
			if(treasureNum - treasureAllocated < 5){
				currTreasureGenerated = randomGeneratorTreasureVal.nextInt(treasureNum - treasureAllocated + 1);
			} else{
				currTreasureGenerated = randomGeneratorTreasureVal.nextInt(5);
			}
			if(currTreasureGenerated > 0){
				treasureAllocated += currTreasureGenerated;
				posXGenerated = randomGeneratorPosX.nextInt(horizontalSize);
				posYGenerated = randomGeneratorPosY.nextInt(verticalSize);
				treasureMap[posXGenerated][posYGenerated] += currTreasureGenerated;
			}
		}
		drawTreasureState();
		
		
	}
	
	public void drawTreasureState(){
		for(int i = 0; i < horizontalSize; i++){
			for(int j = 0; j < verticalSize; j++){
				System.out.print("| " + treasureMap[i][j] + "\t");
			}
			System.out.println("|");
		}	
	}
	
	public void printTreasureState(){
		System.out.println("--------Treasure State-----------");
		System.out.println("Available treasure: " + treasureNum);
		System.out.println("Collected treasure: " + treasureCollected);
	}
	
	public void drawPlayerState(){
		Enumeration<String> playerEnumeration = null;
		String key = null;
		Player currPlayer = null;
		
		for(int i = 0; i < horizontalSize; i++){
			for(int j = 0; j < verticalSize; j++){
				if(playerMap[i][j] != null){
					System.out.print(playerMap[i][j] + " ");
				} else {
					System.out.print("_ ");
				}
			}
			System.out.println("");
		}	
		
		System.out.println("-------Standings-----------");
		playerEnumeration = players.keys();
		while(playerEnumeration.hasMoreElements()){
			key = playerEnumeration.nextElement();
			currPlayer = players.get(key);
			System.out.println("Player " + currPlayer.getId() + ", treasure collected = " +  currPlayer.getNumOfCollectedTreasures() + ", online : " + currPlayer.isOnline());
		}
		System.out.println("---------------------------");
	}
	
	public void drawFinalStandings(){
		if(treasureNum != 0){
			return;
		}
		Enumeration<String> playerEnumeration = null;
		String key = null;
		Player currPlayer = null;
		String winnerPlayer = null;
		int maxCollectedTreasure = 0;
		
		System.out.println("------Final Standings------");
		playerEnumeration = players.keys();
		while(playerEnumeration.hasMoreElements()){
			key = playerEnumeration.nextElement();
			currPlayer = players.get(key);
			System.out.println("Player " + currPlayer.getId() + ", treasure collected = " +  currPlayer.getNumOfCollectedTreasures() + ", online : " + currPlayer.isOnline());
			if(currPlayer.getNumOfCollectedTreasures() > maxCollectedTreasure){
				maxCollectedTreasure = currPlayer.getNumOfCollectedTreasures();
				winnerPlayer = currPlayer.getId();
			}
		}
		System.out.println("---------------------------");
		System.out.println("The winner is " + winnerPlayer);
	}
	
	public String addNewPlayer(){
		String id = null;
		Player newPlayer = null;
		
		//generate unique Id
		id = Integer.toString(players.size());
		
		//generate random position
		Random randomGeneratorPosX = null;
		Random randomGeneratorPosY = null;
		int x = 0;
		int y = 0;
		boolean validPosition = false;
		
		randomGeneratorPosX = new Random();
		randomGeneratorPosY = new Random();
		
		while(!validPosition){
			x = randomGeneratorPosX.nextInt(horizontalSize);
			y = randomGeneratorPosY.nextInt(verticalSize);
			
			if(playerMap[x][y] == null){
				playerMap[x][y] = id;
				validPosition = true;
			}
		}
		
		newPlayer = new Player(id,new Point(x,y));
		newPlayer.setOnline(true);
		players.put(newPlayer.getId(), newPlayer);
		return newPlayer.getId();
	}
	
	public void removePlayer(String id)throws InvalidPlayerIdException{
		if(id == null){
			throw new InvalidPlayerIdException("null");
		}
		
		if(players.containsKey(id)){
			Player currPlayer = null;

			try {
				currPlayer = ((Player)players.get(id));
				currPlayer.setOnline(false);

				playerMap[currPlayer.getPosition().getX()][currPlayer.getPosition().getY()] = null;
			} catch (NullPointerException e) {
				if(currPlayer == null){
					System.out.println("Null pointer exception is detected");
					throw new InvalidPlayerIdException("null");
				}
				e.printStackTrace();
			}
		}else{
			throw new InvalidPlayerIdException(id);
		}
	}

	public Player getPlayer(String id) throws InvalidPlayerIdException{
		if(id == null){
			throw new InvalidPlayerIdException("null");
		}
		
		if(players.containsKey(id)){
			Player currPlayer = null;
			currPlayer = (Player) players.get(id);
			return currPlayer;
		}else{
			throw new InvalidPlayerIdException(id);
		}
	}
	
	public int changePlayerPosition(String playerId, Point newPos)throws InvalidPositionException, InvalidPlayerIdException {
		if(!isNewPositionValid(newPos)){
			throw new InvalidPositionException(newPos);
		}
		
		if(playerId == null){
			throw new InvalidPlayerIdException("null");
		}
		
		if(players.containsKey(playerId)){
			Player currPlayer = null;
			Point oldPos = null;
			int currTreasureVal = 0;
			
			try {
				currPlayer = players.get(playerId);
				oldPos = currPlayer.getPosition();
				
				playerMap[oldPos.getX()][oldPos.getY()] = null;
				playerMap[newPos.getX()][newPos.getY()] = currPlayer.getId();
				currPlayer.setPosition(newPos);
				currTreasureVal = treasureMap[newPos.getX()][newPos.getY()];
				if(currTreasureVal != 0){
					currPlayer.addNumOfCollectedTreasures(currTreasureVal);
					treasureMap[newPos.getX()][newPos.getY()] = 0;
					treasureNum -= currTreasureVal;
					treasureCollected += currTreasureVal;
					System.out.println("Player " + playerId + " collects " + currTreasureVal + " treasures at (" + newPos.getX() + ", " + newPos.getY() + ")");
					System.out.println("Updated treasure state: ");
					this.drawTreasureState();
					System.out.println("---------------------------------------------");
					return currTreasureVal;
				}				
			} catch (NullPointerException npe){
				if(currPlayer == null){
					throw new InvalidPlayerIdException(playerId);
				}
				if(oldPos == null){
					throw new InvalidPositionException(oldPos);
				}
				npe.printStackTrace();	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			throw new InvalidPlayerIdException(playerId);
		}
		return 0;
	}
	
	public int getRemainingTreasure(){
		return treasureNum;
	}
	private boolean isPositionValid(Point position){
		if(position.getX() >= horizontalSize || position.getX() < 0 || position.getY() >= verticalSize || position.getY() < 0){
			return false;
		}
		return true;
	}
	
	private boolean isNewPositionValid(Point newPos){
		if(newPos != null && isPositionValid(newPos)){
			if(playerMap[newPos.getX()][newPos.getY()] == null){
				return true;
			} else {
				return false;
			}
		}else{
			return false;
		}
	}
}
