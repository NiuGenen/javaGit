package javaGit;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGit extends Remote{
	public String test(String str) throws RemoteException;
	
	public String RegReponsity(String path,String repository) throws RemoteException;
	
	public void DeleteReponsity(String name) throws RemoteException;
	
	public String ShowReponsities() throws RemoteException;
	
}
