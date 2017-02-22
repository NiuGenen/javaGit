package javaGit;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GitImpl extends UnicastRemoteObject implements IGit{

	private static final long serialVersionUID = -3861487618555766683L;

	private GitReg git_reg;
	
	protected GitImpl() throws RemoteException {
		super();
		git_reg = GitReg.getInstance();
	}

	public void CreatReponsity(String name) 
			throws RemoteException {
	}

	public void DeleteReponsity(String name) 
			throws RemoteException {
	}

	public String RegReponsity(String path, String repository)	//register new repository
			throws RemoteException {
		System.out.println("[Register]\n"+repository+"\n"+path);
		
		File reponsityDir = new File(GitServer.serverDirName+"/"+repository);
		if(reponsityDir.exists()){
			return "Reponsity ["+repository+"] is already excits.";
		}
		reponsityDir.mkdir();
		
		git_reg.RegReponsity(path,repository);
		return "Register success.";
	}

	@Override
	public String test(String str) throws RemoteException {
		System.out.println("Test:"+str);
		return "Test:"+str;
	}

	@Override
	public String ShowReponsities() throws RemoteException {
		return git_reg.ShowReponsities();
	}
	
}
