package javaGit;

import java.net.MalformedURLException;  
import java.rmi.Naming;  
import java.rmi.NotBoundException;  
import java.rmi.RemoteException;

public class GitClient {

	private static GitClientCmd cmd;
	
	private static void gitClientInit(){
		try {
        	IGit git = (IGit)Naming.lookup("rmi://127.0.0.1:2048/git");	//get remote object 
        	cmd = new GitClientCmd(git);	//create client command object
        } catch (MalformedURLException e) {  
            System.out.println("URL format Exception.");  
        } catch (RemoteException e) {  
            System.out.println("Remote Exception.");
            e.printStackTrace();  
        } catch (NotBoundException e) {  
            System.out.println("Remote Object not bound.");
        }
	}

	public static void main(String[] args) {
		try{
			gitClientInit();	//init git client
			
			if(args.length == 0 || 
					args[0].toLowerCase().equals("help")){	//check if ask help
				cmd.showHelp();
				return;
			}
			
			dispatchCmd(args);	//run command
			
        } catch (Exception e){
        	e.printStackTrace();
        } 
	}
	
	public static void dispatchCmd(String[] args){
		try{
            String command = args[0];
            switch(command){
            case "info":
            	if(args.length==2) cmd.gitInfo(args[1]);
            	else if(args.length==3 && args[2].toString().toLowerCase().equals("detail")){
            		cmd.gitInfoDetail(args[1]);
            	}
            	break;
            case "clone":			//download files
            	cmd.gitClone(args[1]);
            	break;
            case "commit":			//upload files
            	cmd.gitCommit(args[1]);
            	break;
            case "del":				//delete repository
            	cmd.gitDel(args[1]);
            	break;
            case "show":			//show repositories
            	cmd.gitShowReponsities();
            	break;
            case "test":			//test
            	cmd.gitTest(args[0]);
            	break;
            case "init":			//register repository
            	cmd.gitInit(args[1]);
            	break;
            default:				//command not found
            	System.out.println("command not found : "+command);
            	break;
            }
		} catch (Exception e){
        	e.printStackTrace();
        } 
	}
}
