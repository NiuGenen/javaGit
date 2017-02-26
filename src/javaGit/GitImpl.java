package javaGit;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GitImpl extends UnicastRemoteObject implements IGit{

	private static final long serialVersionUID = -3861487618555766683L;

	private GitReg git_reg;
	private GitVC git_vc;
	private GitTransFile git_trans;
	
	protected GitImpl() throws RemoteException {
		super();
		git_reg = GitReg.getInstance();
		git_trans = GitTransFile.getInstance();
		git_vc = GitVC.getInstance();
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
	public boolean uploadRepositoryStart(String repository){
		return git_vc.addVersion(repository);
	}
	
	@Override
	public String uploadFileStart(String repository, String path)
			throws RemoteException {
		return git_trans.uploadFileStart(repository, path);
	}

	@Override
	public void uploadFile(String uploadID, byte[] data, int start, int len)
			throws RemoteException {
		git_trans.uploadFile(uploadID, data, start, len);
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
	
	@Override
	public void clearRepository(String repository){
		File directory = new File(GitServer.serverDirName + "/" + repository);
		if(directory.exists()){
			File[] files = directory.listFiles();
			for(int i=0;i<files.length;++i){
				files[i].delete();
			}
		}
	}

	@Override
	public boolean containRepository(String repository)
			throws RemoteException {
		return git_reg.containRepository(repository);
	}

	@Override
	public boolean confirmRepository(String regPath, String repository)
			throws RemoteException {
		return git_reg.confirmRepository(regPath,repository);
	}

	@Override
	public boolean download_next(String downloadID)
			throws RemoteException {
		return git_trans.download_next(downloadID);
	}

	@Override
	public String downloadRepositoryStart(String repository)
			throws RemoteException {
		return git_trans.downloadRepositoryStart(repository);
	}

	@Override
	public GitFile downloadFile(String downloadID)
			throws RemoteException {
		return git_trans.downloadFile(downloadID);
	}

	@Override
	public String infoRepository(String repository)
			throws RemoteException {
		if(!git_reg.containRepository(repository)){
			return "repository [" + repository + "] does not exist.";
		}
		String ret = "";
		File rep = new File(GitServer.serverDirName + "/" + repository);
		File[] files = rep.listFiles();
		for(int i=0;i<files.length;++i){
			File f = files[i];
			if(f.isFile()){
				ret += "FILE ";
			}
			else{
				ret += "DIRE ";
			}
			ret += String.format("%24s %10d\n", f.getName(),f.length());
		}
		return ret;
	}
	
	public String infoRepositoryDetail(String repository){
		if(!git_reg.containRepository(repository)){
			return "repository [" + repository + "] does not exist.";
		}
		return "[Repository]" + repository + "\n" + infoRepHelp(GitServer.serverDirName + "/" + repository,1);
	}
	
	private String infoRepHelp(String rootPath,int level){
		String ret = "";
		File root = new File(rootPath);
		File[] files = root.listFiles();
		for(int i=0;i<files.length;++i){
			File f = files[i];
			String line = "";
			for(int j=level;j>0;--j){
				line += "      ";
			}
			if(f.isFile()){
				line += String.format("[F]%s", f.getName()) + "\n";
			}
			else if(f.isDirectory()){
				line += String.format("[D]%s", f.getName()) + "\n";
				line += infoRepHelp(rootPath + "/" + f.getName(),level + 1);
			}
			ret += line;
		}
		return ret;
	}

	@Override
	public String vcRepository(String repository) throws RemoteException {
		return git_vc.getRepositoryVersions(repository);
	}
}
