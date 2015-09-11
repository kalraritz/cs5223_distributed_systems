package client;

public class GameStartMonitor {
	private boolean gameStarted;
	
	public GameStartMonitor(){
		gameStarted = false;
	}
	
	public synchronized void waitForStart(){
		if(gameStarted == false){
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Monitor exception");
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void notifyForStart(){
		gameStarted = true;
		notifyAll();
	}
	
	

}
