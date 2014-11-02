package com.example.test8;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Transform_md5 {
	
	public static String transform_Md5(String str){
		byte[] enclyptedHash=null;  
		// MD5で暗号化したByte型配列を取得する  
		MessageDigest md5;  
		try {  
			md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes());
			enclyptedHash = md5.digest();  

			// 暗号化されたByte型配列を、16進数表記文字列に変換する  
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();  
		}
		return bytesToHexString(enclyptedHash);  
	}

	private static  String bytesToHexString(byte[] fromByte) {  

		StringBuilder hexStrBuilder = new StringBuilder();  
		for (int i = 0; i < fromByte.length; i++) {  

			// 16進数表記で1桁数値だった場合、2桁目を0で埋める  
			if ((fromByte[i] & 0xff) < 0x10) {  
				hexStrBuilder.append("0");  
			}  
			hexStrBuilder.append(Integer.toHexString(0xff & fromByte[i]));  
		}  

		return hexStrBuilder.toString();  
	}  
}
