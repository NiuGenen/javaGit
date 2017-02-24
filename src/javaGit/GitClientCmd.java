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
		System.out.println("1.	init [repository name]: register current working directory to a repository");
		System.out.println("	eg. javaGit init project1");
		System.out.println("2.	del [repository name]: delete one repository");
		System.out.println("	eg. javaGit del project1");
		System.out.println("3.	commit [repository name]: upload files from current working directory to registered repository");
		System.out.println("	eg. javaGit commit project1");
		System.out.println("4.	clone [repository name]: download files from repository to current working directory");
		System.out.println("	eg. javaGit clone project1");
	}

	public void gitCommit(String repository){	//upload files from current working directory to registered repository
		try{	//@repository: remote repository name
			if(!git.containRepository(repository)){	//if repository exists
				System.out.println("Repository [" + repository + "] does not exist.");
				return;
			}

			String work_path = System.getProperty("user.dir");	//get working directory on client
			
			if(!git.confirmRepository(work_path, repository)){	//if repository is bound with this working directory
				System.out.println("Repository [" + repository + "] is not for \"" + work_path + "\".");
				return;
			}
			
			git.clearRepository(repository);	//clear the repository
			
			File work_dir = new File(work_path);	//get current working directory to upload
			
			gitCommit_help(work_dir,repository,"/");	//upload files to remote repository
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void gitCommit_help(File file,String repository,String path)
			throws RemoteException, FileNotFoundException{	//upload files to remote repository
		//@file: current client's working directory
		//@repository: remote repository name
		//@path: current upload directory according to client's working directory
		File[] files = file.listFiles();
		for(int i=0;i<files.length;++i){	//for each file in @file
			File f = files[i];
			if(f.isFile()){	//if is a file
				try {
					System.out.println("[upload file] " + path + f.getName());
					
					String id = git.uploadFileStart(repository, path + f.getName());	//start upload and get unique uploadID for this file
					
					FileInputStream input = new FileInputStream(f);	//file input stream
					byte[] data = new byte[2048];
					int start = 0, len = input.read(data);	//read data part
					while(len > -1){
						git.uploadFile(id, data, start, len);	//upload according to uploadID
						len = input.read(data);
					}
					input.close();
					
					git.uploadFileEnd(id);	//finish upload
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if( f.isDirectory() ){	//if is a directory
				git.mkdir(repository, path + f.getName());	//make directory in repository
				gitCommit_help(f,repository,path + f.getName() + "/");	//upload files contained by this directory
			}
		}
	}
	
	public void gitTest(String str){	//test
		try{
			System.out.println( git.test(str) );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void gitInit(String repository){	//register repository
		try {	//@repository: remote repository name
			String path = System.getProperty("user.dir");	//get client's current working directory
			System.out.println("Current Working Path:\n"+path+"\nReponsity name:"+repository);
			String ret = git.regRepository(path, repository);	//register repository with path
			System.out.println(ret);	//show returned result
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void gitShowReponsities(){	//print remote repositories' name and the client's directory they are bound with
		try{
			System.out.println(git.showReponsities());	//print returned result
		} catch(RemoteException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void gitDel(String repository){	//delete remote repository
		try {	//@repository: remote repository name
			String ret = git.delRepository(repository);
			System.out.println(ret);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void gitClone(String repository){	//download files stored in remote repository
		try{	//@repository: remote repository name
			if(!git.containRepository(repository)){
				System.out.println("Repository [" + repository + "] does not exist.");
				return;
			}
			
			String work_path = System.getProperty("user.dir");	//get client's current working directory

			File output_dir = new File(work_path + "/" + repository);
			if(!output_dir.exists()){
				output_dir.mkdir();
			}
			else{
				System.out.println("Repository [" + repository + "] already exsits in working directory.");
				return;
			}
			
			String downloadID = git.downloadRepositoryStart(repository);	//start download and get unique download ID
			FileOutputStream output = null;	//get ready output stream
			while(git.download_next(downloadID)){	//while data left
				GitFile git_file = git.downloadFile(downloadID);	//get data with its information
				String output_file_path = work_path + "/" + repository + git_file.filepath;
				File output_file = new File(output_file_path);	//get output file's path
				if(!git_file.isFile){	//if is a directory
					output_file.mkdir();	//make directory directly
				}
				else{	//if is a file
					if(git_file.isStart){	//if is a start part
						output = new FileOutputStream(output_file); //create output stream
					}
					output.write(git_file.data_part, 0, git_file.length);	//output data
					if(git_file.isEnd){		//if is a end part
						output.close();	//close output stream
					}
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void gitInfo(String repository){
		String info;
		try {
			info = git.infoRepository(repository);
			System.out.println(info);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void gitInfoDetail(String repository){
		String info;
		try {
			info = git.infoRepositoryDetail(repository);
			System.out.println(info);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
