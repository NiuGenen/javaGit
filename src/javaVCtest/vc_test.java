package javaVCtest;

import java.io.File;
import java.io.IOException;

public class vc_test {

	public static void main(String[] args) {
		File file_vc1 = new File("E:/vc1.txt");
		File file_vc2 = new File("E:/vc2.txt");
		System.out.println( file_vc1.lastModified() );
		System.out.println( file_vc2.lastModified() );
		
		File file_vc3 = new File("E:/vc3.txt");
		file_vc1.renameTo(file_vc3);
		try {
			file_vc1.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
