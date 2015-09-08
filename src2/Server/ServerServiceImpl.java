package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Client.Client;

public class ServerServiceImpl extends UnicastRemoteObject implements ServerService{
	
	HashMap<Long, User> users;
	int index = 0;
	long currentTime;
	boolean flag20 = false;

	
	protected ServerServiceImpl() throws RemoteException {
		super();
		users = new HashMap<Long, User>();
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * Start a Timer to count for 20 s
	 * After 20s, shift the 20s flag
	 * @Ouyang
	 */
	public void StartAlarm() {
	}
	
	public byte JoinGame(Client c, Long userID) throws RemoteException {
		if (index == 0){
			//when the first user initial JoinGame, Start a alarm and count for 20s;
			StartAlarm();
		}
		if(flag20){
			//run a new thread for the game
//			Thread.run();
		}
		if(!flag20) {
			if(!users.containsKey(userID)){	
				User user = new User(userID);
				user.setClient(c);
				users.put(userID, user);
				return Utils.JOIN_SUCCESS;
			}
			else return Utils.JOIN_JOINED;
		}
		else return Utils.JOIN_FAIL;
	}
	
	@Override
	public int Move(Long userID, byte movement) throws RemoteException {
		switch (movement){
		case Utils.MOVE_NORTH:
		case Utils.MOVE_SOUTH:
		case Utils.MOVE_EAST :
		case Utils.MOVE_WEST :
		}
		return 0;
	}

	@Override
	public int Reflash(Long userID) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void StartGame(List<User> users){
		
	}
	
}
