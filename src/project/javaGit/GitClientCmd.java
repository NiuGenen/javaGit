package javaGit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

public class GitClientCmd {

	private IGit git;
	
	public GitClientCmd(IGit git){
		this.git = git;
	}
	
	public void showHelp(){
		System.out.println("javaGit [command] [parament]");
		System.out.println("1.	init [repository name]: register current working directory");
		System.out.println("	eg. javaGit init project1");
		System.out.println("2.	del [repository name]: delete one repository");
		System.out.println("	eg. javaGit del project1");
	}

	public void gitCommit(String repository){
		try{
			String work_path = System.getProperty("user.dir");
			File work_dir = new File(work_path);
			gitCommit_help(work_dir,repository,"/");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void gitCommit_help(File file,String repository,String path)
			throws RemoteException, FileNotFoundException{
		File[] files = file.listFiles();
		for(int i=0;i<files.length;++i){
			File f = files[i];
			if(f.isFile()){
				try {
					System.out.println("[upload file] " + path + f.getName());
					String id = git.uploadFileStart(repository, path + f.getName());
					FileInputStream input = new FileInputStream(f);
					byte[] data = new byte[2048];
					int offset = 0;
					int len = input.read(data);
					while(len > -1){
						System.out.println(len + " bytes");
						git.uploadFile(id, data, offset, len);
						//offset += len;
						len = input.read(data);
					}
					git.uploadFileEnd(id);
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if( f.isDirectory() ){
				git.mkdir(repository, path + f.getName());
				gitCommit_help(f,repository,path + f.getName() + "/");
			}
		}
	}
	
	public void gitTest(String str){
		try{
			System.out.println( git.test(str) );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void gitInit(String repository){
		try {
			String path = System.getProperty("user.dir");
			System.out.println("Current Working Path:\n"+path+"\nReponsity name:"+repository);
			String ret = git.regRepository(path, repository);
			System.out.println(ret);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void gitShowReponsities(){
		try{
			System.out.println(git.showReponsities());
		} catch(RemoteException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void gitDel(String repository){
		try {
			//String path = System.getProperty("user.dir");
			//System.out.println("Current Working Path:\n"+path+"\nReponsity :"+repository);
			String ret = git.delRepository(repository);
			System.out.println(ret);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
