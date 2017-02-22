package javaGit;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GitImpl extends UnicastRemoteObject implements IGit{

	private static final long serialVersionUID = -3861487618555766683L;

	private GitReg git_reg;
	
	protected GitImpl() throws RemoteException {
		super();
		git_reg = GitReg.getInstance();
	}

	public String delRepository(String repository) 
			throws RemoteException {
		System.out.println("[Delete Repository]\n" + repository);
		
		return git_reg.delRepository(repository);
	}

	public String regRepository(String path, String repository)	//register new repository
			throws RemoteException {
		System.out.println("[Register Repository]\n" + repository + "\n" + path);
		
		return git_reg.regRepository(path,repository);
	}

	@Override
	public String test(String str) throws RemoteException {
		System.out.println("Test:"+str);
		return "Test:"+str;
	}

	@Override
	public String showReponsities() throws RemoteException {
		return git_reg.showReponsities();
	}
	
}
