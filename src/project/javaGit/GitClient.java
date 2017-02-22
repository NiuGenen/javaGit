package javaGit;

import java.net.MalformedURLException;  
import java.rmi.Naming;  
import java.rmi.NotBoundException;  
import java.rmi.RemoteException;

public class GitClient {

	private static GitClientCmd cmd;
	
	private static void gitClientInit(){
		try {
        	IGit git = (IGit)Naming.lookup("rmi://127.0.0.1:2048/git");
        	cmd = new GitClientCmd(git);
        } catch (MalformedURLException e) {  
            System.out.println("url格式异常");  
        } catch (RemoteException e) {  
            System.out.println("创建对象异常");  
            e.printStackTrace();  
        } catch (NotBoundException e) {  
            System.out.println("对象未绑定"); 
        }
	}

	public static void main(String[] args) {
		try{
			gitClientInit();
			
			if(args.length == 0 || args[0].toLowerCase().equals("help")){
				cmd.showHelp();
				return;
			}
			
			dispatchCmd(args);
			
        } catch (Exception e){
        	e.printStackTrace();
        } 
	}
	
	public static void dispatchCmd(String[] args){
		try{
            String command = args[0];
            switch(command){
            case "commit":
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
