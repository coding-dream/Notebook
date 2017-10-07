package application.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class PBECoder {

	private static final String ALGORITHM = "PBEWITHMD5andDES";

	private static final int iterationCount = 100;// 迭代次数

	private static Key getKey(String password) {
		try {
			// 秘钥材料
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			// 秘钥工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			// 生成秘钥
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			return secretKey;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] encript(byte[] data, String password, byte[] salt) {
		try {
			Key key = getKey(password);
			PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterationCount);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] decrypt(byte[] data,String password,byte[] salt){
		try {
			Key key = getKey(password);
			PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterationCount);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {

		String str = "hello world";
		String password = "abcd1234";

		byte[] salt = "12345678".getBytes();// 8 byte
//		byte[] salt = new SecureRandom().getSeed(8);
		System.out.println(Base64.encodeBase64String(salt));
		byte[] encriptDatas = encript(str.getBytes(), password, salt);

		byte[] decrptDatas = decrypt(encriptDatas, password, salt);
		System.out.println(new String(decrptDatas));
	}
}
