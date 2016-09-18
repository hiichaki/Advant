package com.advant.utils;

public class StringUtils {

	public static int getNumber(String text) {
		return Integer.parseInt(text.replaceAll("\\D+",""));
	}
	
}
