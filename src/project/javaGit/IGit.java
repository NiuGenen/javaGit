package javaGit;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGit extends Remote{
	public String test(String str) throws RemoteException;
	
	public String regRepository(String path,String repository) throws RemoteException;
	
	public String delRepository(String repository) throws RemoteException;
	
	public String showReponsities() throws RemoteException;

	public String uploadFileStart(String repository,String path) throws RemoteException;
	public void uploadFile(String uploadID,byte[] data,int offset,int len) throws RemoteException;
	public void uploadFileEnd(String uploadID) throws RemoteException;
	
	public String mkdir(String repository,String dir) throws RemoteException;
}
