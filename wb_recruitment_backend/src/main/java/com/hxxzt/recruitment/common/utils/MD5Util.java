package com.hxxzt.recruitment.common.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;


/**
 * md5 加密工具类，密码加密使用用户名加明文密码 加密成md5
 */
public class MD5Util {

	protected MD5Util(){

	}

	private static final String ALGORITH_NAME = "md5";

	private static final int HASH_ITERATIONS = 2;

	public static String encrypt(String password) {
		return new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(password), HASH_ITERATIONS).toHex();
	}

	public static String encrypt(String username, String password) {
		return new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(username.toLowerCase() + password),
				HASH_ITERATIONS).toHex();
	}

	public static void main(String[] args) {
		System.out.println(encrypt("admin","111111"));
	}

}
