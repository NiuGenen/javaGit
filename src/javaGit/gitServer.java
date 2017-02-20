package javaGit;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

public class gitServer {
	
	public static String serverDirName = "E:/JavaGitServer";
	public static String configureFileName = "javaGitConfigure.txt";
	
	private static File srvrDir;
	private static File cnfgrFile;
	
	private static FileInputStream configureInputStream;
	private static InputStreamReader configureReader;
	private static FileOutputStream configureOutputStream;
	private static OutputStreamWriter configureWriter;
	
	private static Map<String,String> regs;
	
	private static IGit git;

	public static void gitServerInit(){
		try {
			srvrDir = new File(serverDirName);
			if(!srvrDir.exists()){
				srvrDir.mkdir();
			}
			
			cnfgrFile = new File(serverDirName+"/"+configureFileName);
			if(!cnfgrFile.exists()){
				cnfgrFile.createNewFile();
			}
			
			regs = new HashMap<String,String>();
			
			configureInputStream = new FileInputStream(cnfgrFile);
			configureReader = new InputStreamReader(configureInputStream);
			
			BufferedReader configureBufferReader = new BufferedReader(configureReader);
			
			String configureLine = configureBufferReader.readLine();
			while(configureLine != null){
				System.out.println(configureLine);
				String[] paras = configureLine.split(" ");
				System.out.println(paras[0]+"--"+paras[1]);
				regs.put(paras[0], paras[1]);
				configureLine = configureBufferReader.readLine();
			}
			
			configureBufferReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
        try {
        	gitServerInit();
        	
            git = new GitImpl(regs);
            
            LocateRegistry.createRegistry(2048);//绑定端口
            Naming.bind("rmi://127.0.0.1:2048/git", git);//绑定对象
            
            System.out.println("JavaGitServer启动成功");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void writeRegConfigure(String path,String rep){
		try {
			//configureOutputStream = new FileOutputStream(cnfgrFile);
			//configureWriter = new OutputStreamWriter(configureOutputStream);
			//BufferedWriter writer = new BufferedWriter(configureWriter);
			//writer.append(rep+" "+path);
			//writer.close();
			
			FileWriter writer = new FileWriter(cnfgrFile,true);
			writer.write(rep+" "+path+"\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
