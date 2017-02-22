package javaGit_RMI_test;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface hello extends Remote{
    //远程访问标记接口，只有在Remote接口中标记的方法猜可以远程调用
    public String say() throws RemoteException;
}