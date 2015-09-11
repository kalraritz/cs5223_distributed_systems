package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Client.Client;

public class ServerServiceImpl extends UnicastRemoteObject implements ServerService{
	
	HashMap<Integer, User> users;
	Map map;
	int index = 0;
	int N_Grid, M_Treasure;
	int userNumber;
	long currentTime;
	boolean isTimeOut = false;
	

	
	protected ServerServiceImpl(int n, int m) throws RemoteException {
		super();
		map = new Map(n,m);
		this.N_Grid = n;
		this.M_Treasure = m;
		users = new HashMap<Integer, User>();

	}
	
	/*
	 * Start a Timer to count for 20 s
	 * After 20s, change the 20s flag
	 * 
	 */
	public void StartTimer() {
		Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                System.out.println("Server: 20s time out");
                //HERE MAY HAVE PROBLEM
                isTimeOut = true;
                StartGame(users);
            }
        }, 10000);
	}
	
	public Integer JoinGame(Client c) throws RemoteException {
		if (users.isEmpty()){
			//when the first user initial JoinGame, Start a alarm and count for 20s;
			StartTimer();
		}
		// if 20s, start game and notify every one
		if(isTimeOut){
//			System.out.println("Client " + userID + " joined failed due to time out");
			return null;
		}
		else{
			Integer userID = userNumber ++;
			User user = new User(userID);
			user.setClient(c);
			users.put(userID, user);
			System.out.println("Client " + userID +" joined");
			return userID;
			}
	}
	
	public void StartGame(HashMap<Integer, User> users){
		Collection cl = users.values();
		Iterator it = cl.iterator();
		while(it.hasNext()){
			User user = (User) it.next();
			Client c = user.getClient();
			try{
				c.Notify();
				map.setTreasures(1, 1);
				c.Refresh(map.grids);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		}
	
	@Override
	public synchronized byte Move(Integer userID, byte movement) throws RemoteException {
		User u = users.get(userID);
		Client c = u.getClient();
		int i = u.getPi();
		int j = u.getPj();
		boolean isPositionValid = true;
		switch (movement){
		case Utils.MOVE_WEST:{
			// Check whether the upcoming move is valid or not
			isPositionValid = CheckPostion(i, j-1);
			if(isPositionValid){
				// update the map
				map.moveUser(userID, i, j, i, j - 1);
				// user moves
				u.moveWest();
				u.obtainTreasure(map.grids[i][j - 1].treasure);
				map.grids[i][j - 1].treasure = 0;
				c.Refresh(map.grids);
				System.out.println("Client " + userID + "move WEST");
				return Utils.MOVE_SUCCESS;
			}
			else return Utils.MOVE_FAIL_INVALIDE_POSITION;
		}
		case Utils.MOVE_EAST:{
			isPositionValid = CheckPostion(i + 1,j);
			if(isPositionValid){
				map.moveUser(userID, i, j, i, j + 1);
				u.moveEast();
				u.obtainTreasure(map.grids[i][j].treasure);
				map.grids[i][j].treasure = 0;
				c.Refresh(map.grids);
				System.out.println("Client " + userID + "move EAST");
				return Utils.MOVE_SUCCESS;
			}
			else return Utils.MOVE_FAIL_INVALIDE_POSITION;
		}
		case Utils.MOVE_SOUTH:{
			isPositionValid = CheckPostion(i + 1,j);
			if(isPositionValid){
				map.moveUser(userID, i, j, i + 1, j);
				u.moveSouth();
				c.Refresh(map.grids);
				u.obtainTreasure(map.grids[i][j].treasure);
				map.grids[i][j].treasure = 0;
				System.out.println("Client " + userID + "move SOUTH");
				return Utils.MOVE_SUCCESS;
			}
			else return Utils.MOVE_FAIL_INVALIDE_POSITION;
		}
			
		case Utils.MOVE_NORTH:{
			isPositionValid = CheckPostion(i - 1,j);
			if(isPositionValid){
				map.moveUser(userID, i, j, i - 1, j);
				u.moveNorth();
				c.Refresh(map.grids);
				u.obtainTreasure(map.grids[i][j].treasure);
				map.grids[i][j].treasure = 0;
				System.out.println("Client " + userID + "move NORTH");
				return Utils.MOVE_SUCCESS;
			}
			else return Utils.MOVE_FAIL_INVALIDE_POSITION;
		}
		case Utils.MOVE_STILL:{
			System.out.println("Client " + userID + "NOT move");
			return Utils.MOVE_SUCCESS;
		}
		default:{
			return Utils.MOVE_FAIL_INVALIDE_INPUT;
		}
		}
	}

	private boolean CheckPostion(int pi, int pj) {
		//check whether run out of borders
		if((pi >= N_Grid)||(pj >= N_Grid)||(pi < 0)||(pj < 0)) {
			return false;
		}
		//check whether "crash into" another user
		if(map.grids[pi][pj].user != null) {
			return false;
		}
		return true;		
	}
			
}
