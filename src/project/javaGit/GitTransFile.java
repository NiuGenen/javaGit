package javaGit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GitTransFile {

	private Map<String,OutputStream> map_upload;
	//private Map<String,OutputStream> map_download; 
	
	private static GitTransFile git_trans = new GitTransFile();
	public static GitTransFile getInstance(){
		return git_trans;
	}
	
	private GitTransFile(){
		map_upload = new HashMap<String, OutputStream>();
		//map_download = new HashMap<String, OutputStream>();
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
	
	public void uploadFile(String uploadID, byte[] data, int offset, int len)
			throws RemoteException {
		try {
			OutputStream out = map_upload.get(uploadID);	//get output stream
			out.write(data, offset, len);	//write data
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
}
