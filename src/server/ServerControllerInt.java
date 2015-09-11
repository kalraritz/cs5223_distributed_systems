package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ClientControllerInt;
import model.Direction;
import model.MoveResult;

public interface ServerControllerInt extends Remote{
	String joinGame(ClientControllerInt clientController)throws RemoteException;
	MoveResult move(String playerId, Direction direction)throws RemoteException;
	void startGame() throws RemoteException;
	void quitGame(String playerId) throws RemoteException;
}
