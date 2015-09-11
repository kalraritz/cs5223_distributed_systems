package Server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIService {
	static int N_Grid, M_Treasure;
	
	public static void main(String args[]){
		N_Grid = Integer.parseInt(args[0]);
		M_Treasure = Integer.parseInt(args[1]);
		
	try {
		ServerService remoteService=new ServerServiceImpl(N_Grid, M_Treasure);
		//注册通讯端口
		LocateRegistry.createRegistry(6600);
		//注册通讯路径,把remoteService当成一个对象放进stub,之后由rmi来维护
		Naming.rebind("rmi://127.0.0.1:6600/RemoteService",remoteService);
		System.out.println("Server: Service Start!");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
}

