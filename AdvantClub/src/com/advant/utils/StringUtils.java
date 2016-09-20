package com.advant.utils;

public class StringUtils {

	public static int getNumber(String text) {
		return Integer.parseInt(text.replaceAll("\\D+",""));
	}
	
	public static String getOnlyWords(String text) {
		return text.replaceAll("\\w+", "").replace("\n", "").replace(" ", "");
	}
	
}
