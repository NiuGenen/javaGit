package javaGit_RMI_test;

import java.net.MalformedURLException;  
import java.rmi.Naming;  
import java.rmi.NotBoundException;  
import java.rmi.RemoteException; 

public class client {
    public static void main(String[] args) {  
        try {
        //��ȡԶ�̶��󣬲�����IParrot�ӿ������з�������
            hello p = (hello)Naming.lookup("rmi://127.0.0.1:2048/hello");
            System.out.println(p.say());  
        } catch (MalformedURLException e) {  
            System.out.println("url��ʽ�쳣");  
        } catch (RemoteException e) {  
            System.out.println("���������쳣");  
            e.printStackTrace();  
        } catch (NotBoundException e) {  
            System.out.println("����δ��");  
        }  
    } 
}