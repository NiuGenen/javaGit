package javaGit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

public class GitTransFile {	//manage files' upload and download

	private Map<String,OutputStream> map_upload;	//uploadID - output stream to sava data from client to server
	private Map<String,Queue<GitFile>> map_download;//downloadID - data queue to send data from server to client
	
	private static GitTransFile git_trans = new GitTransFile();
	public static GitTransFile getInstance(){	//signal instance
		return git_trans;
	}
	
	private GitTransFile(){	//constructor
		map_upload = new HashMap<String, OutputStream>();
		map_download = new HashMap<String,Queue<GitFile>>();
	}
	
	public String uploadFileStart(String repository, String path)
			throws RemoteException {
		String uploadID = UUID.randomUUID().toString();
		while(map_upload.containsKey(uploadID)){
			uploadID = UUID.randomUUID().toString();	//get unique upload ID
		}

		try {
			File output_file = new File(GitServer.serverDirName + "/" + repository + path);
			if(!output_file.exists()){
				output_file.createNewFile();	//create file in server if not exist
			}
			FileOutputStream out= new FileOutputStream(output_file);	//output stream
			map_upload.put(uploadID, out);	//band output stream with upload ID
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uploadID;
	}
	
	public void uploadFile(String uploadID, byte[] data, int start, int len)
			throws RemoteException {
		try {
			OutputStream out = map_upload.get(uploadID);	//get output stream
			out.write(data, start, len);	//write data
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void uploadFileEnd(String uploadID) {
		try {
			OutputStream out = map_upload.get(uploadID);	//get upload stream
			map_upload.remove(uploadID);	//remove upload id
			out.close();	//close output stream
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean download_next(String downloadID)
			throws RemoteException {
		Queue<GitFile> queue = map_download.get(downloadID);
		return !queue.isEmpty();	//if queue is not empty, then there are still data needed to be sent from server to client
	}
	
	public String downloadRepositoryStart(String repository)
			throws RemoteException {
		String downloadID = UUID.randomUUID().toString();
		while(map_download.containsKey(downloadID)){
			downloadID = UUID.randomUUID().toString();	//get unique upload ID
		}
		
		File dir = new File(GitServer.serverDirName + "/" + repository);
		File[] files = dir.listFiles();	//get this repository's files
		
		Queue<GitFile> git_files = new LinkedList<GitFile>();
		for(int i=0;i<files.length;++i){
			download_help(repository,git_files,files[i],"/"+files[i].getName());	//create data queue
		}
		
		map_download.put(downloadID, git_files);
		
		return downloadID;
	}
	
	private void download_help(String repository,Queue<GitFile> git_files,
			File file,String path){
		try {
			if(file.isFile()){	//if is a file
				FileInputStream input = new FileInputStream(file);
				byte[] data1 = new byte[2048];
				byte[] data2 = new byte[2048];
				
				int len1 = input.read(data1);
				int len2 = input.read(data2);
				if(len2 == -1){	// when only one data object is relatived to this file, then it is THE START and TEH END at the same time
					GitFile gf = new GitFile(repository, path, data1.clone(),len1, true, true,true);	//data in queue with isFile = TRUE and isStart = TRUE and isEND = true
					git_files.add(gf);System.out.println("se "+path);
				}
				else{	//when more than one data object ard relatived to this file, then this object is THE START
					GitFile gf = new GitFile(repository, path, data1.clone(),len1, true, true,false);	//data in queue with isFile = true and isStart = TRUE and is END = false
					git_files.add(gf);System.out.println("s "+path);
					
					len1 = len2;data1 = data2;
					len2 = input.read(data2);
					while(len2 > -1){	//read other data objects which are THE MIDDLE
						gf = new GitFile(repository, path, data1.clone(),len1, true, false,false);//isFile = true and isStart = false and isEnd = false
						git_files.add(gf);System.out.println("m" + path);
						len1 = len2;data1 = data2;
						len2 = input.read(data2);
					}
					//last data object whith is THE END one
					gf = new GitFile(repository, path, data1.clone(),len1, true, false,true);//isFile = true and isStart = false and isEnd = true
					git_files.add(gf);System.out.println("e" + path);
				}
				
				input.close();
			}
			else if(file.isDirectory()){	//if is directory	//both THE START and THE END
				GitFile gf = new GitFile(repository, path, null,0, false, true,true);//isFile = false and isStart = true and isEnd = true 
				git_files.add(gf);
				
				File[] files = file.listFiles();
				for(int i=0;i<files.length;++i){	//upload files it contains
					download_help(repository,git_files,files[i],path + "/"+files[i].getName());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GitFile downloadFile(String downloadID)
			throws RemoteException {
		Queue<GitFile> queue = map_download.get(downloadID);	//send data object from server to client
		return queue.remove();
	}
}
