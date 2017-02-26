package javaGit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GitVC { //version control

	private String vcFileName = "javaGitVC.txt";	//filename
	private File vcFile;							//File object
	
	private Map<String,List<Long>> vc_map;
	
	private static GitVC git_vc = new GitVC();
	public static GitVC getInstance(){		//signal instance
		return git_vc;
	}
	
	private GitVC(){
		try {
			vcFile = new File(GitServer.serverDirName + "/" + vcFileName);
			if(!vcFile.exists()){
				vcFile.createNewFile();	//if vcFile not exist
				
			}
			
			vc_map = new HashMap<String,List<Long>>(); //craete map to manage repositories' version
			
			FileInputStream vcInputStream = new FileInputStream(vcFile);
			InputStreamReader vcReader = new InputStreamReader(vcInputStream);
			BufferedReader vcBufferReader = new BufferedReader(vcReader);
			String regLine = vcBufferReader.readLine();//read register line [repository name] [version1 timestamp] [version2 timestamp][...]
			while(regLine != null){
				String[] paras = regLine.split(" ");
				
				System.out.println("[Reponsitory]\n" + paras[0] + "\n[Version]");
				
				LinkedList<Long> repository_vc = new LinkedList<Long>();
				for(int i=1;i<paras.length;++i){
					long num = Long.parseLong(paras[i]);
					System.out.println(num);
					repository_vc.add( num );
				}
				System.out.println();
				
				Collections.sort(repository_vc);
				vc_map.put(paras[0], repository_vc);
				
				regLine = vcBufferReader.readLine();//read next line
			}
			
			vcBufferReader.close();//close register file
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static long getTimeStamp(){
		return System.currentTimeMillis();
	}
	
	public boolean addVersion(String repository){
		try{
			if(!vc_map.containsKey(repository)){
				vc_map.put(repository, new LinkedList<Long>());
			}
			
			List<Long> vc = vc_map.get(repository);
			long timt_stramp = GitVC.getTimeStamp();
			
			File rep = new File(GitServer.serverDirName + "/" + repository);
			File rep_rename = new File(GitServer.serverDirName + "/" + repository + "_" + timt_stramp);
			boolean vc_rename = rep.renameTo(rep_rename);
			boolean vc_mkdir = rep.mkdir();
			if( vc_rename && vc_mkdir ){
				vc.add(timt_stramp);
				updateVcFile();
				return true;
			}
			else{
				return false;
			}
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateVcFile(){
		FileWriter writer = null;
		try {
			writer = new FileWriter(vcFile);//open vc file
			
			Set<String> keys = vc_map.keySet();
			Iterator<String> it_key = keys.iterator();
			while(it_key.hasNext()){	//for each repository
				String key = it_key.next();
				writer.write(key);
				
				List<Long> vcs = vc_map.get(key);	//get its versions
//				Iterator<Long> it_vc = vcs.iterator();
//				while(it_vc.hasNext()){	//for its versions
//					writer.write(" " + it_vc.next());
//				}
				for(int i=0;i<vcs.size();++i){
					writer.write(" " + vcs.get(i));
				}
				
				writer.write("\r\n");	//end this repository
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		try {
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getRepositoryVersions(String repository){
		List<Long> vcs = vc_map.get(repository);
		String ret = "[v1]  " + repository;
		Iterator<Long> it_vc = vcs.iterator();
		int count = 2;
		while(it_vc.hasNext()){
			ret += "\n[v" + count++ + "]  " + it_vc.next();
		}
		return ret;
	}
}
