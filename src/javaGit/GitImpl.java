package javaGit;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Map;

public class GitImpl extends UnicastRemoteObject implements IGit{

	private static final long serialVersionUID = -3861487618555766683L;

	public Map<String,String> regs;
	
	protected GitImpl( Map<String,String> reg) throws RemoteException {
		super();
		regs = reg;
	}

	public void CreatReponsity(String name) throws RemoteException {
		// TODO 自动生成的方法存根
		
	}

	public void DeleteReponsity(String name) throws RemoteException {
		// TODO 自动生成的方法存根
		
	}

	public void RegReponsity(String path, String reponsity)
			throws RemoteException {
		System.out.println("[Register]\n"+reponsity+"\n"+path);
		regs.put(reponsity, path);
		gitServer.writeRegConfigure(path,reponsity);
	}

	@Override
	public String test(String str) throws RemoteException {
		System.out.println("Test:"+str);
		return "Test:"+str;
	}

	@Override
	public String ShowReponsities() throws RemoteException {
		//String[] keys = (String[])( regs.keySet().toArray() );
		Iterator<String> it = regs.keySet().iterator();
		
		String ret = "";
		while(it.hasNext()){
			String key = it.next();
			ret += key + " ->" + regs.get(key)+"\n";
		}
		return ret;
	}
	
}
