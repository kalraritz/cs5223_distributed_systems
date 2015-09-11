package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import Client.Client;

public interface ServerService extends Remote{

	public byte JoinGame(Client c,Long userID) throws RemoteException;
	
	public int Move(Long userID, byte movement) throws RemoteException;
	
	public int Reflash(Long userID) throws RemoteException;
	
	
}
