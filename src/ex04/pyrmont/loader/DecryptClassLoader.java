package ex04.pyrmont.loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.imageio.ImageIO;

import ex04.pyrmont.QRCode.MyQRCodeImage;
import ex04.pyrmont.startup.Bootstrap;
import ex04.pyrmont.util.FileUtil;
import jp.sourceforge.qrcode.QRCodeDecoder;

public class DecryptClassLoader extends ClassLoader {

	private Cipher cipher;
	private String javaClassName;
	private String keyFileName;
	private String algorithm = "DES";

	/**
	 * 构造函数：设置解密所需要的对象
	 * 
	 */
	public DecryptClassLoader(String servletName, String keyFileName) {
		this.javaClassName = Bootstrap.CLASS_PATH + servletName;
		this.keyFileName = Bootstrap.CLASS_PATH + keyFileName;
	}

	public Class decrypt() {

		Class clasz = null;
		try {
			/* 读取密匙 */
			System.err.println("***DecryptClassLoader: reading key***");
			File file = new File(keyFileName);
			BufferedImage bufferedImage = ImageIO.read(file);
			QRCodeDecoder codeDecoder = new QRCodeDecoder();
			byte[] rawKey = codeDecoder.decode(new MyQRCodeImage(bufferedImage));
			// byte rawKey[] = FileUtil.fileReadToByteArray(keyFilename);
			DESKeySpec dks = new DESKeySpec(rawKey);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey secretKey = keyFactory.generateSecret(dks);

			/* 创建解密的ClassLoader */
			SecureRandom secureRandom = new SecureRandom();
			System.err.println("***DecryptClassLoader: creating cipher***");
			// Cipher对象实际完成解密操作
			cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, secureRandom);

			/* 创建应用主类的一个实例，通过ClassLoader装入它 */
			System.err.println("***DecryptClassLoader: loading " + javaClassName + "***");

			clasz = loadClass(javaClassName, false);

		} catch (IOException e) {
			System.out.println(e.toString());
		} catch (InvalidKeyException e) {
			System.out.println(e.toString());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.toString());
		} catch (InvalidKeySpecException e) {
			System.out.println(e.toString());
		} catch (NoSuchPaddingException e) {
			System.out.println(e.toString());
		} catch (ClassNotFoundException e) {
			// System.out.println(e.toString());
		}

		return clasz;
	}

	public Class loadClass(String javaClassName, boolean resolve) throws ClassNotFoundException {

		String[] arr = javaClassName.split("/");
		String className = arr[arr.length - 1];
		
		try {
			/* 要创建的Class对象 */
			Class clasz = null;

			/* 如果类已经在系统缓冲之中，不必再次装入它 */
			clasz = findLoadedClass(javaClassName);

			if (clasz != null)
				return clasz;

			try {
				/* 读取经过加密的类文件 */
				byte classData[] = FileUtil.fileReadToByteArray(javaClassName + ".class");
				if (classData != null) {
					System.err.println("***DecryptClassLoader: decode begin***");
					/* 解密 */
					byte decryptedClassData[] = cipher.doFinal(classData);
					/* 再把它转换成一个类 */
					clasz = defineClass(className, decryptedClassData, 0, decryptedClassData.length);
					System.err.println("***DecryptClassLoader: decrypting class " + className + "***");
				}
			} catch (FileNotFoundException fnfe) {

			}

			// if (clasz == null) {
			// clasz =
			// ClassLoader.getSystemClassLoader().loadClass(javaClassName);
			// }

			/* 如果上面没有成功，尝试用默认的ClassLoader装入它 */
			if (clasz == null)
				clasz = findSystemClass(javaClassName);
			// clasz = super.loadClass(javaClassName, resolve);

			/* 如有必要，则装入相关的类 */
			if (resolve && clasz != null)
				resolveClass(clasz);

			return clasz;

		} catch (IOException ie) {
			throw new ClassNotFoundException(ie.toString());
		} catch (GeneralSecurityException gse) {
			throw new ClassNotFoundException(gse.toString());
		}
	}
}
