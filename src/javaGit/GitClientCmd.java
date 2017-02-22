package javaGit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
			if(!git.containRepository(repository)){
				System.out.println("Repository [" + repository + "] does not exist.");
				return;
			}

			String work_path = System.getProperty("user.dir");
			
			if(!git.confirmRepository(work_path, repository)){
				System.out.println("Repository [" + repository + "] is not for \"" + work_path + "\".");
				return;
			}
			
			git.clearRepository(repository);	//clear this repository
			
			File work_dir = new File(work_path);	//get current working directory to upload
			
			gitCommit_help(work_dir,repository,"/");	//upload files and directory
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
					int start = 0, len = input.read(data);
					while(len > -1){
						git.uploadFile(id, data, start, len);
						len = input.read(data);
					}
					input.close();
					
					git.uploadFileEnd(id);
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
	
	public void gitClone(String repository){
		try{
			if(!git.containRepository(repository)){
				System.out.println("Repository [" + repository + "] does not exist.");
				return;
			}
			
			String work_path = System.getProperty("user.dir");

			File output_dir = new File(work_path + "/" + repository);
			if(!output_dir.exists()){
				output_dir.mkdir();
			}
			else{
				System.out.println("Repository [" + repository + "] already exsits in working directory.");
				return;
			}
			
			String downloadID = git.downloadRepositoryStart(repository);
			FileOutputStream output = null;
			while(git.download_next(downloadID)){
				GitFile git_file = git.downloadFile(downloadID);
				String output_file_path = work_path + "/" + repository + git_file.filepath;
				File output_file = new File(output_file_path);
				if(!git_file.isFile){
					output_file.mkdir();
				}
				else{
					if(git_file.isStart){
						output = new FileOutputStream(output_file);
					}
					output.write(git_file.data_part, 0, git_file.length);
					if(git_file.isEnd){
						output.close();
					}
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
