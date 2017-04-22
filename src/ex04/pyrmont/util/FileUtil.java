package ex04.pyrmont.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class FileUtil {
	/**
	 * 将文件读入byte数组
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	static public byte[] fileReadToByteArray(String fileName) throws IOException {

		File file = new File(fileName);
		long fileLength = file.length();
		byte fileData[] = new byte[(int) fileLength];
		FileInputStream fis = new FileInputStream(file);
		int readLength = fis.read(fileData);
		if (readLength != fileLength) {
			System.err.println("***Only read " + readLength + " of " + fileLength + " for file " + file + " ***");
		}
		fis.close();
		return fileData;
	}

	/**
	 * 将byte数组写入到文件
	 * 
	 * @param fileName
	 * @param data
	 * @throws IOException
	 */
	static public void byteArrayWriteToFile(String fileName, byte[] data) throws IOException {

		// String relativelyPackageName = FileUtil.class.getPackage().getName();
		FileOutputStream fos = new FileOutputStream(fileName);
		fos.write(data);
		fos.close();
	}

	static public void writeTxtFile (String filePath, String flag) {
		try {
			if(null == flag || "".equals(flag) ) {
				flag = "true";
			}
			File file = new File(filePath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.print(flag);
//			ps.append("true");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	static public String readTxtFile (String filePath) {
		try {
			File file = new File(filePath);
			if (file.exists() && file.isFile()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(read);
				String lintTxt = null;
				while ((lintTxt = br.readLine()) != null) {
					return lintTxt;
				}
			}
			else {
				FileUtil.writeTxtFile (filePath, "false");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
