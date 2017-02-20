package javaGit_RMI_test;

import java.net.MalformedURLException;  
import java.rmi.Naming;  
import java.rmi.NotBoundException;  
import java.rmi.RemoteException; 

public class client {
    public static void main(String[] args) {  
        try {
        //获取远程对象，并利用IParrot接口来进行方法调用
            hello p = (hello)Naming.lookup("rmi://127.0.0.1:2048/hello");
            System.out.println(p.say());  
        } catch (MalformedURLException e) {  
            System.out.println("url格式异常");  
        } catch (RemoteException e) {  
            System.out.println("创建对象异常");  
            e.printStackTrace();  
        } catch (NotBoundException e) {  
            System.out.println("对象未绑定");  
        }  
    } 
}