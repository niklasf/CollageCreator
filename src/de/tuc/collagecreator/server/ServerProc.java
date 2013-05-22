package de.tuc.collagecreator.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class ServerProc {

	public static void main(String[] args) throws RemoteException,
			MalformedURLException {
		CollageServerImpl collageServer = new CollageServerImpl();
		Naming.rebind("rmi://localhost:1234/CollageServer", collageServer);
	}
}
