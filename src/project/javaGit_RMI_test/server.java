package javaGit_RMI_test;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class server {
    public static void main(String[] args){
        try {
            hello p = new HelloImpl();
            LocateRegistry.createRegistry(2048);//�󶨶˿�
            Naming.bind("rmi://127.0.0.1:2048/hello", p);//�󶨶���
            System.out.println("HelloServer�����ɹ�");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}