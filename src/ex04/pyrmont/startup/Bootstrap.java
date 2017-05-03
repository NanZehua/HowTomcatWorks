/* explains Tomcat's default container */
package ex04.pyrmont.startup;

import ex04.pyrmont.core.SimpleContainer;
import ex04.pyrmont.encrypt.EncryptClass;
import ex04.pyrmont.util.FileUtil;
import ex04.pyrmont.util.GenerateKey;
import java.io.File;
import org.apache.catalina.connector.http.HttpConnector;

public final class Bootstrap {

	public static final String CLASS_PATH = System.getProperty("user.dir") + File.separator + "classes"
			+ File.separator;
//	public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
	public static final String ENCRTPT_FLAG = "flag.txt";
//	public static final String KEY_FILE_NAME = "key.strore";
	public static final String KEY_FILE_NAME = "key.png";

	public static void main(String[] args) {
		
		EncryptClass encrypt =new EncryptClass();
		HttpConnector connector = new HttpConnector();
		SimpleContainer container = new SimpleContainer();
		connector.setContainer(container);
		try {
			encrypt.start();
			connector.initialize();
			connector.start();
			// make the application wait until we press any key.
			System.in.read();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}