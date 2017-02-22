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

public class GitReg {
	private String regFileName = "javaGitRegister.txt";
	private File regFile;
	
	private Map<String,String> reg_map;
	
	private static GitReg git_reg = new GitReg();
	public static GitReg getInstance(){	//signal instance
		return git_reg;
	}

	public boolean containRepository(String repository){
		return reg_map.containsKey(repository);
	}
	
	private GitReg(){
		try{
			regFile = new File(GitServer.serverDirName+"/"+regFileName);
			if(!regFile.exists()){	//check default register file
				regFile.createNewFile();
			}

			reg_map = new HashMap<String,String>(); //map to manage register
			
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
				if(!reposity.exists()){
					System.out.println("[Rebuild Repository]\n" + name);
					reposity.mkdir();
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String showReponsities(){
		String ret = "";
		Iterator<String> it = reg_map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			ret += key + " ->" + reg_map.get(key) + "\n";
		}
		return ret;
	}

	public String regRepository(String path,String repository){
		try {
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
	
	public String delRepository(String repository){
		try {
			if(!reg_map.containsKey(repository)){
				return "repository [" + repository + "] does not exist.";
			}

			File reponsityDir = new File(GitServer.serverDirName+"/"+repository);
			boolean del = reponsityDir.delete();
			if(!del){
				return "Delete repository [" + repository + "] fail.";
			}
			
			reg_map.remove(repository);	//remove pair from reg_map
			
			FileWriter writer = new FileWriter(regFile);
			Iterator<String> it = reg_map.keySet().iterator();	//update registers
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
}
