package javaGit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GitReg {	//manage server's repositories' register information
	private String regFileName = "javaGitRegister.txt";	//filename
	private File regFile;								//File object
	
	private Map<String,String> reg_map;	//relationship between client's directory with remote repository
	
	private static GitReg git_reg = new GitReg();
	public static GitReg getInstance(){	//signal instance
		return git_reg;
	}

	public boolean containRepository(String repository){	//if server has this repository whos name is @repository
		return reg_map.containsKey(repository);
	}
	
	private GitReg(){
		try{
			regFile = new File(GitServer.serverDirName+"/"+regFileName);
			if(!regFile.exists()){	//check default register file
				regFile.createNewFile();	//if not exist then create a new one
			}

			reg_map = new HashMap<String,String>(); //craete map to manage register
			
			FileInputStream regInputStream = new FileInputStream(regFile);
			InputStreamReader regReader = new InputStreamReader(regInputStream);
			BufferedReader regBufferReader = new BufferedReader(regReader);
			String regLine = regBufferReader.readLine();//read register line
			while(regLine != null){
				//System.out.println(regLine);
				String[] paras = regLine.split(" ");
				System.out.println("[Read Reponsitory]\n" + paras[0] + " -- " + paras[1]);
				reg_map.put(paras[0], paras[1]);
				regLine = regBufferReader.readLine();//read next line
			}
			
			regBufferReader.close();//close register file
			
			Set<String> keys = reg_map.keySet();
			Iterator<String> it = keys.iterator();
			while(it.hasNext()){
				String name = it.next();
				File reposity = new File(GitServer.serverDirName + "/" + name);
				if(!reposity.exists()){	//if one repository is in register file but its directoey is not exist
					System.out.println("[Rebuild Repository]\n" + name);
					reposity.mkdir();	//create this repository's directory
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String showReponsities(){	//get remote repositories' infotmation
		String ret = "";
		Iterator<String> it = reg_map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			ret += key + " ->" + reg_map.get(key) + "\n";
		}
		return ret;
	}

	public String regRepository(String path,String repository){	//register new repository
		try {	//@path: client's directory
				//@repository: server's repository
			if(reg_map.containsKey(repository)){
				return "Reponsity ["+repository+"] already exists.";
			}

			File reponsityDir = new File(GitServer.serverDirName+"/"+repository);
			boolean reg = reponsityDir.mkdir();
			if(!reg){
				return "Register repository [" + repository + "] fail.";
			}
			
			reg_map.put(repository, path);	//add new pair to reg_map
			
			FileWriter writer = new FileWriter(regFile,true);	//append new register
			writer.write(repository + " " + path + "\r\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return "Register repository [" + repository + "] success.";
	}
	
	public String delRepository(String repository){	//delete repository
		try {
			if(!reg_map.containsKey(repository)){	//if it does not exist
				return "repository [" + repository + "] does not exist.";
			}

			File reponsityDir = new File(GitServer.serverDirName+"/"+repository);
			boolean del = reponsityDir.delete();	//delete repository
			if(!del){
				return "Delete repository [" + repository + "] fail.";	//if fail to delete
			}
			
			reg_map.remove(repository);	//remove this pair from reg_map
			
			FileWriter writer = new FileWriter(regFile);
			Iterator<String> it = reg_map.keySet().iterator();	//update records in register file
			while(it.hasNext()){
				String key = it.next();
				String value = reg_map.get(key);
				writer.write( key + " " + value + "\r\n");
			}
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return "Delete repository " + repository + " success.";
	}
	
	public boolean confirmRepository(String regPath, String repository){	//check if this repository is bounded with this client's directory
		//@regPath: client's directory
		//@repository: server's repository
		return reg_map.get(repository).equals(regPath);
	}
}
