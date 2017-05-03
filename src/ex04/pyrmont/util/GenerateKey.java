package ex04.pyrmont.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.imageio.ImageIO;

import com.sun.net.ssl.internal.www.protocol.https.BASE64Encoder;
import com.swetake.util.Qrcode;

import ex04.pyrmont.startup.Bootstrap;

import javax.crypto.KeyGenerator;

public class GenerateKey {

	private String keyFileName;
	private String algorithm;

	public GenerateKey(String keyName) {
		this.keyFileName = Bootstrap.CLASS_PATH + keyName;
		this.algorithm = "DES";
	}

	public byte[] makeKey() throws NoSuchAlgorithmException, IOException {
		
		/* 生成密钥 */
		SecureRandom secureRandom = new SecureRandom();
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
		keyGenerator.init(secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();

		/* 将密钥数据保存到文件 */
		// FileUtil.byteArrayWriteToFile(keyFileName, secretKey.getEncoded());
		// System.out.println("***The key create succesed ***");
		return secretKey.getEncoded();
	}

	public void CreateQRcode(byte[] keyByte) throws IOException {
		// public void CreateQRcode() throws IOException {

		Qrcode x = new Qrcode();
		x.setQrcodeErrorCorrect('M'); // 纠错等级
		x.setQrcodeEncodeMode('B'); // N代表数字，A代表a-Z，B代表其他字符
		x.setQrcodeVersion(7); // 版本

		int width = 67 + 12 * (7 - 1);
		int height = 67 + 12 * (7 - 1);
		BufferedImage bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D gs = bufferImage.createGraphics();

		gs.setBackground(Color.WHITE);
		gs.setColor(Color.BLACK);
		gs.clearRect(0, 0, width, height);

		int pixoff = 2; // 偏移量
		int len = keyByte.length;
		if (len > 0 && len < 120) {
			boolean[][] s = x.calQrcode(keyByte);

			for (int i = 0; i < s.length; i++) {
				for (int j = 0; j < s.length; j++) {
					if (s[i][j]) {
						gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
					}
				}
			}
		}
		gs.dispose();
		bufferImage.flush();

		ImageIO.write(bufferImage, "png", new File(keyFileName));
		System.out.println("***The key create succesed ***");

	}

	public String byteToString(byte[] byte2String) {

		BASE64Encoder enc = new BASE64Encoder();
		String encString = enc.encode(byte2String);
		return encString;
	}

	// public byte[] stringToByte (String string2Byte) {
	//
	// BASE64Decoder dec = new BASE64Decoder();
	// byte[] str2Byte = null;
	// try{
	// str2Byte = dec.decodeBuffer(string2Byte);
	// }catch (IOException e) {
	// e.printStackTrace();
	// }
	// return str2Byte;
	// }

}
