package com.advant.utils;

import java.io.IOException;
import java.net.InetAddress;

public class HTMLUtils {

	public static boolean hasInternetConnection() {
		try {
			InetAddress[] addresses = InetAddress.getAllByName("www.google.com");
			for (InetAddress address : addresses) {
				if (address.isReachable(10000)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.out.println("connection failed!");
			return false;
		}
		return false;
	}
	
		
}
