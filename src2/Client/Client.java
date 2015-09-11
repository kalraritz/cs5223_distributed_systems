package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote{
	// put what as the parameter? which relates to whether to 
	public void Notify() throws RemoteException;
}
