package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Server.Grid;

public interface Client extends Remote{
	// put what as the parameter? which relates to whether to 
	public void Notify() throws RemoteException;
	// refresh the map situation accroding to grids from Server
	public void Refresh(Grid[][] grids) throws RemoteException;
}
