package server;

import java.rmi.RemoteException;
import java.util.TimerTask;

public class StartGameTimerTask extends TimerTask {

	private ServerControllerInt serverController;
	
	public StartGameTimerTask(ServerControllerInt sci){
		super();
		serverController = sci;
	}
	@Override
	public void run() {
		try {
			serverController.startGame();
		} catch (RemoteException e) {
			System.out.println("Game can't be started. Exception stack trace : ");
			e.printStackTrace();
		}
	}

}
