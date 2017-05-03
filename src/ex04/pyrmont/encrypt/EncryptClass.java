package ex04.pyrmont.encrypt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;
import javax.imageio.ImageIO;

import ex04.pyrmont.QRCode.MyQRCodeImage;
import ex04.pyrmont.startup.Bootstrap;
import ex04.pyrmont.util.FileUtil;
import ex04.pyrmont.util.GenerateKey;
import jp.sourceforge.qrcode.QRCodeDecoder;

public class EncryptClass extends Thread {

	private String keyFileName;
	private String algorithm = "DES";

	public EncryptClass() {
		keyFileName = Bootstrap.CLASS_PATH + Bootstrap.KEY_FILE_NAME;
	}

	public void createKey() {

		try {
			
			GenerateKey genKey = null;
			File file = new File(keyFileName);
			
			if (!file.exists()) {
				genKey = new GenerateKey(Bootstrap.KEY_FILE_NAME);
				byte[] keyByte = genKey.makeKey();
				genKey.CreateQRcode(keyByte);
			} else {
				System.out.println("***The key file is exists***");
			}
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	public void startEncrypt() {

		createKey();	// 开始创建密钥
		String flag = FileUtil.readTxtFile(Bootstrap.CLASS_PATH + Bootstrap.ENCRTPT_FLAG);
		
		if ("false".equals(flag)) {
			Date a = new Date();
			encryptClass();
			FileUtil.writeTxtFile(Bootstrap.CLASS_PATH + Bootstrap.ENCRTPT_FLAG, null);
			Date b = new Date();
			System.out.println("***The encrypt cost time is : " + (b.getTime() - a.getTime()) +"毫秒***");
			System.out.println("***This servlets encrypt success***");
		} else if ("true".equals(flag)) {
			System.out.println("***The class file has encrypted***");
		}
	}

	public void encryptClass() {

		Cipher cipher = null;
		try {
			SecureRandom secureRandom = new SecureRandom();
			File keyFile = new File(keyFileName);
			BufferedImage bufferedImage = ImageIO.read(keyFile);
			QRCodeDecoder codeDecoder = new QRCodeDecoder();
			byte[] rawKey = codeDecoder.decode(new MyQRCodeImage(bufferedImage));
//			byte[] rawKey = FileUtil.fileReadToByteArray(keyFileName);
			DESKeySpec desKeySpec = new DESKeySpec(rawKey);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			/* 创建用于实际加密的Cipher对象 */
			cipher = Cipher.getInstance(algorithm);
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, secureRandom);

			File file = new File(Bootstrap.CLASS_PATH);
			File[] fileArr = file.listFiles();
			int len = fileArr.length;
			for (int i = 0; i < len; i++) {
				File fs = fileArr[i];
				if (fs.getName().endsWith(".class")) {
					String[] arr = fs.getName().split("/");
					String fileName = arr[arr.length - 1];
					/* 读入类文件 */
					byte classData[] = FileUtil.fileReadToByteArray(Bootstrap.CLASS_PATH + fileName);
					// 获取数据并加密,正式执行加密操作
					byte[] encryptedClassData = cipher.doFinal(classData);
					/* 保存加密后的文件 */
					FileUtil.byteArrayWriteToFile(Bootstrap.CLASS_PATH + fileName, encryptedClassData);
					System.out.println("***Encrypted " + fileName + " ***");
				}
			}
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
		} catch (IllegalBlockSizeException e) {
			System.out.println(e.toString());
		} catch (BadPaddingException e) {
			System.out.println(e.toString());
		}
		System.out.println("***Encrypt Successes***");
	}

	public void run() {
		startEncrypt();
	}
}
