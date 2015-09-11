package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import Client.Client;

public interface ServerService extends Remote{

	public Integer JoinGame(Client c) throws RemoteException;
	
	public byte Move(Integer userID, byte movement) throws RemoteException;
	
	
}
