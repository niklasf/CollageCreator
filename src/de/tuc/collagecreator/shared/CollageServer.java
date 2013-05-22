package de.tuc.collagecreator.shared;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CollageServer extends Remote {

	void addImage(SerializableImageWrapper image) throws RemoteException,
			IOException;

	SerializableImageWrapper generateCollage(SerializableImageWrapper template,
			int multi, int tileWidth, int tileHeight) throws RemoteException,
			IOException;

}
