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

public class GitReg {
	private String regFileName = "javaGitRegister.txt";
	private File regFile;
	
	private Map<String,String> reg_map;
	
	private static GitReg git_reg = new GitReg();
	public static GitReg getInstance(){	//signal instance
		return git_reg;
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
				System.out.println(regLine);
				String[] paras = regLine.split(" ");
				System.out.println(paras[0]+"--"+paras[1]);
				reg_map.put(paras[0], paras[1]);
				regLine = regBufferReader.readLine();//read next line
			}
			
			regBufferReader.close();//close register file
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String ShowReponsities(){
		String ret = "";
		Iterator<String> it = reg_map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			ret += key + " ->" + reg_map.get(key) + "\n";
		}
		return ret;
	}

	public void RegReponsity(String path,String repository){
		try {
			reg_map.put(repository, path);
			
			FileWriter writer = new FileWriter(regFile,true);
			writer.write(repository + " " + path + "\r\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
