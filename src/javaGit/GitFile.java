package javaGit;

import java.io.Serializable;

public class GitFile implements Serializable{	//download file part
	private static final long serialVersionUID = -3570212155373044646L;
	public String filepath;
	public String repository;
	public byte[] data_part;
	public int length;
	public boolean isFile;
	public boolean isEnd;
	public boolean isStart;
	public GitFile(String repository,String filepath,
			byte[] data,int length,
			boolean isFile,boolean isStart,boolean isEnd){
		this.filepath = filepath;
		this.repository = repository;
		
		this.data_part = data;
		this.length = length;
		
		this.isFile = isFile;
		this.isEnd = isEnd;
		this.isStart = isStart;
	}
}
