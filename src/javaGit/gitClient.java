package javaGit;

import java.net.MalformedURLException;  
import java.rmi.Naming;  
import java.rmi.NotBoundException;  
import java.rmi.RemoteException;

public class GitClient {
	
	private static IGit git;
	
	private static void gitClientInit(){
		try {
        	git = (IGit)Naming.lookup("rmi://127.0.0.1:2048/git");
        } catch (MalformedURLException e) {  
            System.out.println("url格式异常");  
        } catch (RemoteException e) {  
            System.out.println("创建对象异常");  
            e.printStackTrace();  
        } catch (NotBoundException e) {  
            System.out.println("对象未绑定"); 
        }
	}

	public static void showHelp(){
		System.out.println("javaGit [command][parament...]");
		System.out.println("1.	init [reponsity name]: register current working directory");
		System.out.println("	javaGit init project1");
	}
	
	public static void main(String[] args) {
		try{
			gitClientInit();
			
			if(args.length == 0){
				showHelp();
				return;
			}
			
            String command = args[0];
            switch(command){
            case "del":
            	gitDel(args[1]);
            	break;
            case "show":
            	gitShowReponsities();
            	break;
            case "test":
            	gitTest(args[0]);
            	break;
            case "init":
            	gitInit(args[1]);
            	break;
            default:
            	System.out.println("command not found : "+command);
            	break;
            } 
        } catch (Exception e){
        	e.printStackTrace();
        }
	}
	
	public static void gitDel(String repository){
		try {
			//String path = System.getProperty("user.dir");
			//System.out.println("Current Working Path:\n"+path+"\nReponsity :"+repository);
			String ret = git.delRepository(repository);
			System.out.println(ret);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void gitTest(String str){
		try{
			System.out.println(git.test(str));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void gitInit(String repository){
		try {
			String path = System.getProperty("user.dir");
			System.out.println("Current Working Path:\n"+path+"\nReponsity name:"+repository);
			String ret = git.regRepository(path, repository);
			System.out.println(ret);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void gitShowReponsities(){
		try{
			System.out.println(git.showReponsities());
		} catch(RemoteException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
