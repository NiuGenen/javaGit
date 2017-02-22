package javaGit_RMI_test;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class server {
    public static void main(String[] args){
        try {
            hello p = new HelloImpl();
            LocateRegistry.createRegistry(2048);//绑定端口
            Naming.bind("rmi://127.0.0.1:2048/hello", p);//绑定对象
            System.out.println("HelloServer启动成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}