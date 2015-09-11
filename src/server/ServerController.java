package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;
import java.util.Vector;

import model.Direction;
import model.Maze;
import model.MoveResult;
import model.Player;
import model.Point;
import model.exception.InvalidPlayerIdException;
import model.exception.InvalidPositionException;
import client.ClientControllerInt;

public class ServerController implements ServerControllerInt {

	private Hashtable<String, ClientControllerInt> clients = null;
	private ClientControllerInt clientController;
	private Maze maze;
	private boolean timerStarted;
	private boolean gameStarted;
	
	public ServerController(int mazeSize, int treasureNum){
		maze = new Maze(mazeSize, treasureNum);
		clients = new Hashtable<String, ClientControllerInt>();
		timerStarted = false;
		gameStarted = false;
		System.out.println("ServerController is initialized");
	}
	
	@Override
	public String joinGame(ClientControllerInt clientController) throws RemoteException{
		String playerId = null;
		Timer startGameTimer = null;
		StartGameTimerTask startGameTimerTask = null;
		long delayStartInSecond = 10;
		
		try {
			synchronized(this){
				if(gameStarted == false){
					playerId = maze.addNewPlayer();
					clients.put(playerId, clientController);
					clientController.updateId(playerId);
					if(timerStarted == false){
						//Start timerThread
						timerStarted = true;
						startGameTimerTask = new StartGameTimerTask(this);
						startGameTimer = new Timer();
						startGameTimer.schedule(startGameTimerTask, delayStartInSecond*1000);
						System.out.println("Game will be started in " + delayStartInSecond + " second(s)");
					}
				}else{
					playerId = null;
				}
			}
		} catch (Exception e) {
			playerId = null;
			e.printStackTrace();
		}
		return playerId;

	}

	@Override
	public MoveResult move(String playerId, Direction direction)
			throws RemoteException{
		Player currPlayer = null;
		Point currPlayerPos = null;
		Point newPos = null;
		int newlyCollectedTreasures = 0;
		
		synchronized(this){
			try {
				currPlayer = maze.getPlayer(playerId);
				currPlayerPos = currPlayer.getPosition();
				switch(direction){
				case NORTH:
					newPos = new Point(currPlayerPos.getX() - 1, currPlayerPos.getY());
					break;
				case SOUTH:
					newPos = new Point(currPlayerPos.getX() + 1, currPlayerPos.getY());
					break;
				case EAST:
					newPos = new Point(currPlayerPos.getX(), currPlayerPos.getY() + 1);
					break;
				case WEST:
					newPos = new Point(currPlayerPos.getX(), currPlayerPos.getY() - 1);
					break;
				case NOMOVE:
					updateAllClients();
					break;
				default:
					newPos = null;
					return MoveResult.INVALID_DIRECTION;
				}
				if(!direction.equals(Direction.NOMOVE)){
					newlyCollectedTreasures = maze.changePlayerPosition(playerId, newPos);
					if(newlyCollectedTreasures != 0){
						clients.get(playerId).showCollectedTreasures(newlyCollectedTreasures);
					}
					updateAllClients();
					if(maze.getRemainingTreasure() == 0){
						System.out.println("Game over!!");
						terminateAllClients();
						maze.drawFinalStandings();
					}
				}
			} catch (InvalidPlayerIdException e) {
				System.out.println("Invalid player id");
				return MoveResult.INVALID_PLAYER_ID;
			} catch (InvalidPositionException e) {
				System.out.println("Invalid new position");
				return MoveResult.INVALID_POSITION;
			}
		}
		return MoveResult.SUCCESS;
	}
	
	@Override
	public void startGame() throws RemoteException {
		Enumeration<String> keyEnum = null;
		Vector<String> quitPlayers = null;
		
		synchronized(this){
			System.out.println("Server starts the game!");
			keyEnum = clients.keys();
			quitPlayers = new Vector<String>();
			gameStarted = true;	
			while(keyEnum.hasMoreElements()){
				String key = null;
				key = keyEnum.nextElement();
				clientController = clients.get(key);
				try {
					clientController.updateMazeState(maze);
					clientController.startGame();
				} catch (RemoteException e) {
					System.out.println("Client with id " + key + " could not be started.");
					quitPlayers.add(key);
					e.printStackTrace();
				}
				System.out.println("player " + key + " has been successfully started");
			}

			//remove players that quit from data structure
			if(quitPlayers.size() > 0){
				for(int i = 0; i < quitPlayers.size(); i++){
					notThreadSafeQuitGame(quitPlayers.get(i));
				}
			}
		} //end of synchronized block
	}
	
	@Override
	public synchronized void quitGame(String playerId) throws RemoteException {
			notThreadSafeQuitGame(playerId);
	}
	
	private void notThreadSafeQuitGame(String playerId){
		try {
			maze.removePlayer(playerId);
			clients.remove(playerId);
			updateAllClients();
		} catch (InvalidPlayerIdException e) {
			System.out.println("Invalid player Id is queried");
		}
	}
	
	
	private void updateAllClients(){
		Enumeration<String> keyEnum = null;
		Vector<String> quitPlayers = null;
		
		//System.out.println("Server updates all clients!");
		keyEnum = clients.keys();
		quitPlayers = new Vector<String>();
		gameStarted = true;	
		while(keyEnum.hasMoreElements()){
			String key = null;
			key = keyEnum.nextElement();
			clientController = clients.get(key);
			try {
				clientController.updateMazeState(this.maze);
			} catch (RemoteException e) {
				System.out.println("Client with id " + key + " could not be updated.");
				quitPlayers.add(key);
				e.printStackTrace();
			}
			//System.out.println("player " + key + " has been successfully updated");
		}

		//remove players that quit from data structure
		if(quitPlayers.size() > 0){
			for(int i = 0; i < quitPlayers.size(); i++){
				notThreadSafeQuitGame(quitPlayers.get(i));
			}
		}
	}
	
	private void terminateAllClients(){
		Enumeration<String> keyEnum = null;
		Vector<String> playerIds = null;
		
		playerIds = new Vector<String>();
		keyEnum = clients.keys();
		
		while(keyEnum.hasMoreElements()){
			String key = null;
			key = keyEnum.nextElement();
			clientController = clients.get(key);
			playerIds.add(key);
			try {
				clientController.endGame(this.maze);
			} catch (RemoteException e) {
				System.out.println("Player " + key + " is offline");
			}
		}
		
		for(int i = 0; i < playerIds.size(); i++){
			try {
				maze.removePlayer(playerIds.get(i));
				clients.remove(playerIds.get(i));
			} catch (InvalidPlayerIdException e) {
				//Do nothing
			}
		}

	}
	public static void main(String args[]){
		ServerControllerInt serverStub = null;
		ServerControllerInt serverController = null;
		Registry registry = null;
		int size = 0;
		int maxTreasure = 0;
		int maxArgs = 2;
		
		try {
			for(int x = 0; x < maxArgs; x++){
				if((args[x] == null) || args[x].equals("")){
					System.out.println("Invalid parameter\nThis client application will quit!");
					return;
				}
			}
			size = Integer.parseInt(args[0]);
			maxTreasure = Integer.parseInt(args[1]);
		} catch (NumberFormatException e1) {
			System.out.println("Invalid input\nAssign the size and maximum number of treasure to the default (5)");
			size = 5;
			maxTreasure = 5;
			e1.printStackTrace();
		} catch (Exception e){
			System.out.println("Invalid input\nAssign the size and maximum number of treasure to the default (5)");
			size = 5;
			maxTreasure = 5;
			e.printStackTrace();
		}
		
		try {
			serverController = new ServerController(size, maxTreasure);
			serverStub = (ServerControllerInt) UnicastRemoteObject.exportObject(serverController, 0);
			//registry = LocateRegistry.getRegistry(null, Integer.parseInt(args[2]));
			registry = LocateRegistry.getRegistry(null, 30030);
			registry.bind("Server", serverStub);
			//registry.bind(args[3], serverStub);
			System.out.println("ServerReady!!");
		} catch (Exception e) {
			System.out.println("Server is not ready!!. Quit application");
			e.printStackTrace();
		}
		
	}
}
