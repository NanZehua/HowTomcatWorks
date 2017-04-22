package ex04.pyrmont.loader;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import ex04.pyrmont.startup.Bootstrap;
import ex04.pyrmont.util.FileUtil;

public class StartClassLoader extends ClassLoader {

	private Cipher cipher;
	/**
	 * 构造函数：设置解密所需要的对象
	 * @throws IOException 
	 * @throws InvalidKeyException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * 
	 */
	public StartClassLoader() throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
		System.err.println("***DecryptClassLoader: reading key***");
//		byte rawKey[] = FileUtil.fileReadToByteArray(keyFilename);
//		DESKeySpec dks = new DESKeySpec(rawKey);
//		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
//		SecretKey secretKey = keyFactory.generateSecret(dks);
		
		SecureRandom secureRandom = new SecureRandom();
		/* 创建解密的ClassLoader */
		System.err.println("***DecryptClassLoader: creating cipher***");
		// Cipher对象实际完成解密操作
		cipher = Cipher.getInstance("DES");
//		cipher.init(Cipher.DECRYPT_MODE, secretKey, secureRandom);
	}

	public Class decrypt(String javaClassName, String keyFileName) throws Exception {
		
		String algorithm = "DES";
		javaClassName = Bootstrap.CLASS_PATH+javaClassName;
		keyFileName = Bootstrap.CLASS_PATH+keyFileName;
		/* 创建应用主类的一个实例，通过ClassLoader装入它 */
		System.err.println("***DecryptClassLoader: loading " + javaClassName + "***");
		Class clasz = loadClass(javaClassName, false);

		return clasz;
	}

	public Class loadClass(String javaClassName, boolean resolve) throws ClassNotFoundException {
		try {
			
			String[] arr = javaClassName.split("/");
			String className = arr[arr.length - 1];
			
			/* 要创建的Class对象 */
			Class clasz = null;

			/* 如果类已经在系统缓冲之中，不必再次装入它 */
			clasz = findLoadedClass(javaClassName);

			if (clasz != null){
				return clasz;
			}
			try {
				/* 读取经过加密的类文件 */
				byte classData[] = FileUtil.fileReadToByteArray(javaClassName + ".class");
				if (classData != null) {
					System.out.println("***DecryptClassLoader: decode begin***");
					/* 解密 */
					byte decryptedClassData[] = cipher.doFinal(classData);
//					System.out.println(new GenerateKey().byteToString(decryptedClassData));
					/* 再把它转换成一个类 */
					clasz = defineClass(className, decryptedClassData, 0, decryptedClassData.length);
					System.err.println("***DecryptClassLoader: decrypting class " + className + "***");
				}
			} catch (FileNotFoundException fnfe) {
//				System.out.println(fnfe.toString());
			}

			if(clasz == null) {
				clasz = ClassLoader.getSystemClassLoader().loadClass(javaClassName);
			}
			
			/* 如果上面没有成功，尝试用默认的ClassLoader装入它 */
			if (clasz == null) {
				clasz = findSystemClass(javaClassName);
//				clasz = super.loadClass(javaClassName, resolve);
			}
			/* 如有必要，则装入相关的类 */
			if (resolve && clasz != null) {
				resolveClass(clasz);
			}
			return clasz;

		} catch (Exception ie) {
			throw new ClassNotFoundException(ie.toString());
		} 
	}
}
