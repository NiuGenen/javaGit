package javaGit_RMI_test;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface hello extends Remote{
    //Զ�̷��ʱ�ǽӿڣ�ֻ����Remote�ӿ��б�ǵķ����¿���Զ�̵���
    public String say() throws RemoteException;
}