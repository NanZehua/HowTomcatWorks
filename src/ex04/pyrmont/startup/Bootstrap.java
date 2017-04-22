/* explains Tomcat's default container */
package ex04.pyrmont.startup;

import ex04.pyrmont.core.SimpleContainer;
import ex04.pyrmont.encrypt.EncryptClass;
import ex04.pyrmont.util.FileUtil;
import ex04.pyrmont.util.GenerateKey;

import java.io.File;
import java.util.Date;

import org.apache.catalina.connector.http.HttpConnector;

public final class Bootstrap {

	public static final String CLASS_PATH = System.getProperty("user.dir") + File.separator + "classes"
			+ File.separator;
//	public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
	public static final String ENCRTPT_FLAG = "flag.txt";
	public static final String KEY_FILE_NAME = "key.strore";

	public static void main(String[] args) {

		EncryptClass encrypt;
		GenerateKey genKey;
		File file = new File(CLASS_PATH + KEY_FILE_NAME);
		try {
			if (!file.exists()) {
				genKey = new GenerateKey(KEY_FILE_NAME);
				genKey.makeKey();
			} else {
				System.out.println("***The key file is exists***");
			}
			if("false".equals(FileUtil.readTxtFile(CLASS_PATH+ENCRTPT_FLAG)) ) {
				Date a = new Date();
				encrypt = new EncryptClass();
				encrypt.encrypt();
				FileUtil.writeTxtFile(CLASS_PATH+ENCRTPT_FLAG,null);
				Date b = new Date();
				System.out.println("加密消耗的时间为：" + (b.getTime() - a.getTime()) / 1000);
				System.out.println("***This servlets has encrypt***");
			} else if ("true".equals(FileUtil.readTxtFile(CLASS_PATH+ENCRTPT_FLAG))){
				System.out.println("***The class file has encrypted***");
			}
		} catch (Exception e1) {
			System.out.println(e1.toString());
		}
		HttpConnector connector = new HttpConnector();
		SimpleContainer container = new SimpleContainer();
		connector.setContainer(container);
		try {
			connector.initialize();
			connector.start();
			// make the application wait until we press any key.
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}