package ex08.pyrmont.util;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.SecretKey;

import com.sun.net.ssl.internal.www.protocol.https.BASE64Encoder;
import ex04.pyrmont.startup.Bootstrap;

import javax.crypto.KeyGenerator;

public class GenerateKey {
	
	private String keyFileName;
	private String algorithm;
	
	public GenerateKey (String keyName) {
		this.keyFileName = Bootstrap.CLASS_PATH + keyName;
		this.algorithm = "DES";
	}
	
	public void makeKey() throws NoSuchAlgorithmException, IOException  
	{
		/* 生成密钥 */
		SecureRandom secureRandom = new SecureRandom();
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
		keyGenerator.init(secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		 
		/* 将密钥数据保存到文件 */
		FileUtil.byteArrayWriteToFile(keyFileName, secretKey.getEncoded());
		System.out.println("***The key create succesed ***");
	}
	
	public String byteToString (byte[] byte2String) {
		
		BASE64Encoder enc = new BASE64Encoder();
		String encString = enc.encode(byte2String);
		return encString;
	}
	
//	public byte[] stringToByte (String string2Byte) {
//		
//		BASE64Decoder dec = new  BASE64Decoder();
//		byte[] str2Byte = null;
//		try{
//			str2Byte = dec.decodeBuffer(string2Byte);
//		}catch (IOException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return str2Byte;
//	}
	
}
