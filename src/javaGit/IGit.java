package javaGit;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGit extends Remote{
	public String test(String str) throws RemoteException;
	
	public String regRepository(String path,String repository) throws RemoteException;
	
	public String delRepository(String repository) throws RemoteException;
	
	public String showReponsities() throws RemoteException;
	
}
