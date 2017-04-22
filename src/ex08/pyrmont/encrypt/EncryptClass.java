package ex08.pyrmont.encrypt;

import java.io.File;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;

import ex04.pyrmont.startup.Bootstrap;
import ex04.pyrmont.util.FileUtil;

public class EncryptClass {

	private String keyFileName;
//	private String[] args = {"key","ModernServlet.class","PrimitiveServlet.class"};
	private String algorithm = "DES";

	public EncryptClass() {
		keyFileName = Bootstrap.CLASS_PATH +Bootstrap.KEY_FILE_NAME;
	}

	public void encrypt() throws Exception {

		/* 生成密钥 */
		// DES算法要求有一个可信任的随机数源
		SecureRandom secureRandom = new SecureRandom();
		byte rawKey[] = FileUtil.fileReadToByteArray(keyFileName);
		DESKeySpec desKeySpec = new DESKeySpec(rawKey);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		/* 创建用于实际加密的Cipher对象 */
		Cipher cipher = Cipher.getInstance(algorithm);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, secureRandom);

		File file = new File(Bootstrap.CLASS_PATH);
		File[] fileArr = file.listFiles();
		int len = fileArr.length ;
		for( int i = 0 ; i < len ; i++ ){
			File fs = fileArr[i];
			if( fs.getName().endsWith(".class")) {
				String[] arr  = fs.getName().split("/");
				String fileName = arr[arr.length-1];
				/* 读入类文件 */
				byte classData[] = FileUtil.fileReadToByteArray(Bootstrap.CLASS_PATH+fileName);
				// 获取数据并加密,正式执行加密操作
				byte encryptedClassData[] = cipher.doFinal(classData);
				/* 保存加密后的文件 */
				FileUtil.byteArrayWriteToFile(Bootstrap.CLASS_PATH+fileName, encryptedClassData);
				System.out.println("***Encrypted " + fileName + " ***");
			}
		}
		/* 加密命令行中指定的每一类 */
//		for (int i = 1; i < args.length; i++) {
//			String fileName = Bootstrap.CLASS_PATH+ args[i];
//			/* 读入类文件 */
//			byte classData[] = FileUtil.fileReadToByteArray(fileName);
////			System.out.println(new GenerateKey().byteToString(classData));
//			// 获取数据并加密,正式执行加密操作
//			byte encryptedClassData[] = cipher.doFinal(classData);
//			/* 保存加密后的文件 */
//			FileUtil.byteArrayWriteToFile(fileName, encryptedClassData);
//			System.out.println("***Encrypted " + fileName + " ***");
//		}
		System.out.println("***Encrypt Successes***");
	}
}
