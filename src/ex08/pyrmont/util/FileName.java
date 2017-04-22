package ex08.pyrmont.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yinxm
 * @version 1.0 2005/06/17
 * 
 *          This class can take file's path and file's name; you must give the
 *          path where you want to take the file.
 */
public class FileName {

//	public static List<String> classFileName = new ArrayList<String>();
	
	public static void main(String[] args) {
		// This is the path where the file's name you want to take.
		String relativelyPackageName = FileUtil.class.getPackage().getName();
//		String path = System.getProperty("user.dir") + File.separator + "bin/" + relativelyPackageName + "/";
		String path2 = System.getProperty("user.dir") + File.separator + "bin/";
//		getFileName(path, fileName);
//		getClassName(path2, className);
		FileName aa = new FileName();
		aa.getFile(path2);
//			System.out.println(string);
//		}
	}
	
	public void getFile(String path) {
		// get file list where the path has
		File file = new File(path);
		// get the folder list
		File[] array = file.listFiles();

		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile() && array[i].toString().endsWith(".class")) {
				// only take file name
//				System.out.println("^^^^^" + array[i].getName());
//				 take file path and name
//				 System.out.println("#####" + array[i]);
//				 take file path and name
//				 System.out.println("*****" + array[i].getPath());
//				 classFileName.add(array[i].getPath());
			} else if (array[i].isDirectory()) {
				getFile(array[i].getPath());
			}
		}
	}
	
	public void getClassName(String path, String className) {

		File file = new File(path);
		// get the folder list
		File[] array = file.listFiles();
		for (int i = 0 ;  i < array.length ; i++) {
//			System.out.println(array[i].toString());
			if(array[i].isFile() && array[i].toString().substring(path.length()).equals(className)) {
				System.out.println(array[i].toString().substring(path.length()));
			}else if (array[i].isDirectory()) {
				getFileName(array[i].getPath(), className);
			}
		}
	}

	public void getFileName(String path, String fileName) {

		File file = new File(path);
		// get the folder list
		File[] array = file.listFiles();
		for (int i = 0 ;  i < array.length ; i++) {
			if(array[i].isFile() && array[i].toString().substring(path.length()).equals(fileName)) {
				System.out.println(array[i].toString().substring(path.length()));
			}else if (array[i].isDirectory()) {
				getFileName(array[i].getPath(), fileName);
			}
		}
	}

}
