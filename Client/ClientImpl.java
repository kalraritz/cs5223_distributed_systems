package Client;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import Server.Grid;
import Server.ServerService;
import Server.Utils;

public class ClientImpl extends UnicastRemoteObject implements Client, Serializable{
	Integer userID;
	
	protected ClientImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String args[]){
		try{
			ClientImpl c = new ClientImpl();
			ServerService server =(ServerService)Naming.lookup("rmi://127.0.0.1:6600/RemoteService");
			Integer userID = server.JoinGame(c);
			if(userID != null)
			{
				System.out.println("JOIN SUCCESS, ASSIGNED USERID IS: " + userID);
			}
			else System.out.println("JOIN FAILED");
			
			server.Move(userID, Utils.MOVE_EAST);
			
			Scanner in=new Scanner(System.in);
			while(true){
				String readLine = in.nextLine(); 
				if(readLine.equals("w")) {
					if(server.Move(userID, Utils.MOVE_NORTH) == Utils.MOVE_SUCCESS);
					System.out.println("w");
				}
				else if(readLine.equals("s")) {
					if(server.Move(userID, Utils.MOVE_SOUTH) == Utils.MOVE_SUCCESS)
					System.out.println("s");
				}
				else if(readLine.equals("a")) server.Move(userID, Utils.MOVE_WEST);
				else if(readLine.equals("d")) server.Move(userID, Utils.MOVE_EAST);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
		
	}
	@Override
	public void Notify() throws RemoteException {
		// Game start notification, can change some flags
		System.out.println("Notification received, Game start");
	}

	@Override
	public void Refresh(Grid[][] grids) throws RemoteException {
//		TODO refresh user's view according to the grids here.
		
//		for(int i = 0; i < grids.length; i ++)
//			for(int j = 0; j < grids.length; j ++){
//				if (grids[i][j].treasure > 0)
//					System.out.println(grids[i][j].treasure);
//			}

		
	}
	
	
}
