package javaGit_RMI_test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements hello{
    private static final long serialVersionUID = 1L;
    public HelloImpl () throws RemoteException{  
        super();  
    }
    public String say() throws RemoteException {  
        return "Hello,I'm a parrot.";
    } 
}
