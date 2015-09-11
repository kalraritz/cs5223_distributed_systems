package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import model.Maze;

public interface ClientControllerInt extends Remote{
	boolean updateMazeState(Maze maze) throws RemoteException;
	boolean updateId(String playerId) throws RemoteException;
	String getId() throws RemoteException;
	void startGame() throws RemoteException;
	void endGame(Maze finalMazeState) throws RemoteException;
	void showCollectedTreasures(int collectedTresureNum) throws RemoteException;
} 
