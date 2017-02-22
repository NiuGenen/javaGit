package javaGit;

import java.io.File;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class GitServer {
	public static String serverDirName = "E:/JavaGitServer";
	private static File srvrDir;
	
	private static IGit git;

	public static void gitServerInit(){
        try {
			srvrDir = new File(serverDirName);
			if(!srvrDir.exists()){		//check default configure directory
				srvrDir.mkdir();
			}
			
			git = new GitImpl();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
        try {
        	gitServerInit();
      
            LocateRegistry.createRegistry(2048);	//band with one port
            Naming.bind("rmi://127.0.0.1:2048/git", git);	//band remote object
            
            System.out.println("JavaGitServer∆Ù∂Ø≥…π¶");	//activated
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}