package com.albo.util;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class CryptoUtil {

	private static final String salt = "feae79064ffd6dd2";

	public static String encrypt(String plain, String password) {
		TextEncryptor textEncryptor = Encryptors.queryableText(password, salt);
		return textEncryptor.encrypt(plain);
	}

	public static String decrypt(String encrypted, String password) {
		TextEncryptor textEncryptor = Encryptors.queryableText(password, salt);
		return textEncryptor.decrypt(encrypted);
	}

}
