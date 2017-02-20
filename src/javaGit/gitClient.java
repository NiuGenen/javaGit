package javaGit;

import java.net.MalformedURLException;  
import java.rmi.Naming;  
import java.rmi.NotBoundException;  
import java.rmi.RemoteException;

public class gitClient {
	
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

	public static void ShowHelp(){
		System.out.println("javaGit [command][parament...]");
		System.out.println("1.	init [reponsity name]: register current working directory");
		System.out.println("	javaGit init project1");
	}
	
	public static void main(String[] args) {
		try{
			gitClientInit();
			
			if(args.length == 0){
				ShowHelp();
				return;
			}
			
            String command = args[0];
            switch(command){
            case "show":
            	GitShowReponsities();
            	break;
            case "test":
            	GitTest(args[0]);
            	break;
            case "init":
            	GitInit(args[1]);
            	break;
            default:
            	System.out.println("command not found : "+command);
            	break;
            } 
        } catch (Exception e){
        	e.printStackTrace();
        }
	}
	
	public static void GitTest(String str){
		try{
			System.out.println(git.test(str));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void GitInit(String reponsity){
		try {
			String path = System.getProperty("user.dir");
			System.out.println("Current Working Path:\n"+path+"\nReponsity :"+reponsity);
			git.RegReponsity(path, reponsity);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void GitShowReponsities(){
		try{
			System.out.println(git.ShowReponsities());
		} catch(RemoteException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
