package ex04.pyrmont.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import ex04.pyrmont.startup.Bootstrap;

public class KeyUtils {

	private static final char[] keyLeft = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '+', '/' };

	private static final char[] keyRight = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '-', '_' };
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		/* 生成密钥 */
		SecureRandom secureRandom = new SecureRandom();
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
		keyGenerator.init(secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] keyByte = secretKey.getEncoded();
		int position = secretKey.getEncoded().length;
		
		File fileLeft = new File(Bootstrap.CLASS_PATH+"keyLeft.keystore");
		File fileRight = new File(Bootstrap.CLASS_PATH+"keyRight.keystore");
		FileOutputStream fosL;
		FileOutputStream fosR;
		try {
			fosL = new  FileOutputStream(fileLeft);
			fosR = new  FileOutputStream(fileRight);
			for (int i = 0; i < position; i++) {
				if (i < position/2 ) {
					if ( keyByte[i] == keyLeft[i] ) {
						
					}
//					fosL.write(Base64.getEncoder().encode(keyByte[i]));
				} else {
					fosR.write(keyByte[i]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
