package javaGit;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGit extends Remote{
	public String test(String str) throws RemoteException;
	
	public String regRepository(String path,String repository) throws RemoteException;
	
	public String delRepository(String repository) throws RemoteException;
	
	public String showReponsities() throws RemoteException;
	public boolean confirmRepository(String regPath,String repository) throws RemoteException;
	public boolean containRepository(String repository) throws RemoteException;

	public boolean uploadRepositoryStart(String repository) throws RemoteException;
	public String uploadFileStart(String repository,String filepath) throws RemoteException;
	public void uploadFile(String uploadID,byte[] data,int start,int len) throws RemoteException;
	public void uploadFileEnd(String uploadID) throws RemoteException;
	
	public String mkdir(String repository,String dir) throws RemoteException;

	void clearRepository(String repository) throws RemoteException;
	
	public boolean download_next(String downloadID) throws RemoteException;
	public String downloadRepositoryStart(String repository) throws RemoteException;
	public GitFile downloadFile(String downloadID) throws RemoteException;
	
	public String infoRepository(String repository) throws RemoteException;
	public String infoRepositoryDetail(String repository) throws RemoteException;
	
	public String vcRepository(String repository) throws RemoteException;
}
