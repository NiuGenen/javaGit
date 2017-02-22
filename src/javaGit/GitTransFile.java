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

public class GitTransFile {

	private Map<String,OutputStream> map_upload;
	private Map<String,Queue<GitFile>> map_download; 
	
	private static GitTransFile git_trans = new GitTransFile();
	public static GitTransFile getInstance(){
		return git_trans;
	}
	
	private GitTransFile(){
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
				output_file.createNewFile();	//create file if not exist
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
			OutputStream out = map_upload.get(uploadID);
			map_upload.remove(uploadID);	//remove upload id
			out.close();	//close output stream
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean download_next(String downloadID)
			throws RemoteException {
		Queue<GitFile> queue = map_download.get(downloadID);
		return !queue.isEmpty();
	}
	
	public String downloadRepositoryStart(String repository)
			throws RemoteException {
		String downloadID = UUID.randomUUID().toString();
		while(map_download.containsKey(downloadID)){
			downloadID = UUID.randomUUID().toString();	//get unique upload ID
		}
		
		File dir = new File(GitServer.serverDirName + "/" + repository);
		File[] files = dir.listFiles();
		
		Queue<GitFile> git_files = new LinkedList<GitFile>();
		for(int i=0;i<files.length;++i){
			download_help(repository,git_files,files[i],"/"+files[i].getName());
		}
		
		map_download.put(downloadID, git_files);
		
		return downloadID;
	}
	
	private void download_help(String repository,Queue<GitFile> git_files,
			File file,String path){
		try {
			if(file.isFile()){
				FileInputStream input = new FileInputStream(file);
				byte[] data1 = new byte[2048];
				byte[] data2 = new byte[2048];
				
				int len1 = input.read(data1);
				int len2 = input.read(data2);
				if(len2 == -1){
					GitFile gf = new GitFile(repository, path, data1.clone(),len1, true, true,true);
					git_files.add(gf);System.out.println("se "+path);
				}
				else{
					GitFile gf = new GitFile(repository, path, data1.clone(),len1, true, true,false);
					git_files.add(gf);System.out.println("s "+path);
					
					len1 = len2;data1 = data2;
					len2 = input.read(data2);
					while(len2 > -1){
						gf = new GitFile(repository, path, data1.clone(),len1, true, false,false);
						git_files.add(gf);System.out.println("m" + path);
						len1 = len2;data1 = data2;
						len2 = input.read(data2);
					}
					
					gf = new GitFile(repository, path, data1.clone(),len1, true, false,true);
					git_files.add(gf);System.out.println("e" + path);
				}
				
				input.close();
			}
			else if(file.isDirectory()){
				GitFile gf = new GitFile(repository, path, null,0, false, true,true);
				git_files.add(gf);
				
				File[] files = file.listFiles();
				for(int i=0;i<files.length;++i){
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
		Queue<GitFile> queue = map_download.get(downloadID);
		return queue.remove();
	}
}
