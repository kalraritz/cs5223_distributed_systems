package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

import server.ServerControllerInt;

import model.Direction;
import model.Maze;
import model.MoveResult;

public class ClientController implements ClientControllerInt {

	private String clientId = null;
	private GameStartMonitor gameStartMonitor = null;
	private boolean gameEnded;
	
	public ClientController(){
		gameStartMonitor = new GameStartMonitor();
		gameEnded = false;
	}
	
	@Override
	public boolean updateMazeState(Maze maze) throws RemoteException{
		System.out.println("Player " + clientId + " : Updated maze state >>");
		if(maze != null){
			maze.drawPlayerState();
		}
		System.out.println("");
		return true;
	}
	
	@Override
	public boolean updateId(String playerId) throws RemoteException {
		if(playerId != null){
			clientId = playerId;
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void startGame() throws RemoteException {
		gameStartMonitor.notifyForStart();
	}
	
	@Override
	public String getId() throws RemoteException {
		return this.clientId;
	}
	
	@Override
	public void endGame(Maze finalMazeState) throws RemoteException {
		gameEnded = true;
		System.out.println("Player " + clientId + " : Game is over!!");
		finalMazeState.drawFinalStandings();
		
	}
	
	@Override
	public void showCollectedTreasures(int collectedTreasureNum)
			throws RemoteException {
		System.out.println("Player " + clientId+ " : collects " + collectedTreasureNum + " treasure(s)");
	}
	
	public void waitForStart(){
		gameStartMonitor.waitForStart();
	}
	
	public boolean getEndGame(){
		return gameEnded;
	}
	
	public synchronized static void main(String[] args){
		ClientControllerInt clientStub = null;
		ClientController clientController = null;
		
		ServerControllerInt serverController = null;
		Registry registry = null;
		String assignedPlayerId = null;
		
		String serverObjectName = null;
		String serverHostName = null;
		int serverPortNum;
		int maxArgs = 1;
			
		try {
			for(int x = 0; x < maxArgs; x++){
				if((args[x] == null) || args[x].equals("")){
					System.out.println("Invalid parameter\nThis client application will quit!");
					return;
				}
			}
			
			serverHostName = "localhost"; // args[0];
			serverPortNum = 30030;// Integer.parseInt(args[1]);
			serverObjectName = "Server"; // args[2];
			System.out.println("Server host name : " + serverHostName + ", port : " + serverPortNum);
			System.out.println("Remote object name : " + serverObjectName);
			if(serverHostName.equals("localhost") || serverHostName.equals("127.0.0.1")){
				registry = LocateRegistry.getRegistry(null, serverPortNum);
			}else{
				registry = LocateRegistry.getRegistry(serverHostName, serverPortNum);
			}
			serverController = (ServerControllerInt) registry.lookup(serverObjectName);
			
			//create instance of client controller
			clientController = new ClientController();
			clientStub = (ClientControllerInt) UnicastRemoteObject.exportObject(clientController,0);

			//pass the remote object reference, get player id from the server
			assignedPlayerId = serverController.joinGame(clientStub);
			
			if(assignedPlayerId == null){
				System.out.println("Game has been started. You can't join the existing game.\nClient application will quit");
				return;
			}
			
			System.out.println("assigned player id = " + assignedPlayerId);	
			System.out.println("Client RMI is ready! Waiting for server notification ... ");
			
			//wait for server notification, will be notified using clientController.notifyToStart method
			clientController.waitForStart();
			
			//read the input from command prompt
			Direction dir = null;
			MoveResult moveResult = null;
			String input = "empty";
			BufferedReader in = null;
			
			//random move part
			boolean randomMove = false;
			Random randomGenerator = null;
			int generatedNum = 0;
			
			if(args[0] != null){
				if(args[0].equals("r")){
					randomMove = true;
				}
			}
			
			in = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Game started!!");
			while(input != null && !input.equals("Quit")){
				if(clientController.getEndGame() == true){
					System.out.println("Player " + clientController.getId()  + " : Enter any key to terminate the client");
					input = in.readLine();
					return;
				}
				
				try {
					if(!randomMove){
						System.out.println("Player " + clientController.getId()  + " : Please enter the direction");
						System.out.print("Player " + clientController.getId() + " >> ");
						input = in.readLine();
					} else {
						randomGenerator = new Random();
						generatedNum = randomGenerator.nextInt(5);
						switch(generatedNum){
							case 0:
								input = "n";
								break;
							case 1:
								input = "s";
								break;
							case 2:
								input = "e";
								break;
							case 3: 
								input = "w";
								break;
							case 4:
								input = "x";
								break;
							default:
								input = "x";
								break;
						}
						System.out.println("Player " + clientController.getId() + " >> " + input);
					}
					input = input.trim();
					
					if(input.equals("N") || input.equals("n") ){
						dir = Direction.NORTH;
					} else if(input.equals("S") || input.equals("s")){
						dir = Direction.SOUTH;
					} else if(input.equals("E") || input.equals("e")){
						dir = Direction.EAST;
					} else if(input.equals("W") || input.equals("w")){
						dir = Direction.WEST;
					} else if(input.equals("X") || input.equals("x")){
						dir = Direction.NOMOVE;
					} else if(input.equals("Quit")){
						System.out.println("Good bye!!!");
						return;
					} else {
						dir = null;
					}
					
					if(dir == null){
						System.out.println("Invalid input.\nThe valid input are : N,n,S,s,E,e,W,e,X,x and Quit");
					}else{
						moveResult = serverController.move(assignedPlayerId, dir);
						switch(moveResult){
						case INVALID_DIRECTION:
							System.out.println("Invalid direction. Please try again!");
							break;
						case INVALID_PLAYER_ID:
							System.out.println("Invalid player id. Client will quit");
							return;
						case INVALID_POSITION:
							System.out.println("Invalid position. Please try again!");
							break;
						}
					}
				} catch (IOException e) {
					System.out.println("IO Exception occurs. ClientProgam will quit. Stacktrace: ");
					e.printStackTrace();
					return;
				} catch (Exception e){
					System.out.println("General exception occurs. ClientProgram will quit. Stacktrace: ");
					e.printStackTrace();
				}
			}
		} catch(NotBoundException nbe){
			System.out.println("Can't bound \"" + serverObjectName + "\" in host " + args[0] + ". Client application will quit\nException stack trace: \n");
			nbe.printStackTrace();
		} catch(RemoteException re) {
			System.out.println("Client could not connect to the server\nRemote exception is thrown. Client application will quit");
			re.printStackTrace();
		} catch(NumberFormatException nfe){
			System.out.println("Invalid port number\"" + args[1] + "\". Client application will quite\n. Exception stack trace: \n");
			nfe.printStackTrace();
		} catch (Exception e) {
			System.out.println("General exception has occured");
			e.printStackTrace();
		}
	}
}
