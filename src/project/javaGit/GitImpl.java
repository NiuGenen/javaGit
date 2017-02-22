package javaGit;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GitImpl extends UnicastRemoteObject implements IGit{

	private static final long serialVersionUID = -3861487618555766683L;

	private GitReg git_reg;
	private GitTransFile git_trans;
	
	protected GitImpl() throws RemoteException {
		super();
		git_reg = GitReg.getInstance();
		git_trans = GitTransFile.getInstance();
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

	@Override
	public String uploadFileStart(String repository, String path)
			throws RemoteException {
		return git_trans.uploadFileStart(repository, path);
	}

	@Override
	public void uploadFile(String uploadID, byte[] data, int offset, int len)
			throws RemoteException {
		git_trans.uploadFile(uploadID, data, offset, len);
	}

	@Override
	public void uploadFileEnd(String uploadID) {
		git_trans.uploadFileEnd(uploadID);
	}

	@Override
	public String mkdir(String repository, String dir)
			throws RemoteException {
		File directory = new File(GitServer.serverDirName + "/" + repository + dir);
		if(!directory.exists()){
			directory.mkdir();
		}
		return "[mkdir] " + dir;
	}
}
