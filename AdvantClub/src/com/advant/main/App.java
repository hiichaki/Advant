package com.advant.main;

import com.advant.service.HtmlService;


public class App {
	
	private static void removeWarnings() {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
	}
	
	private static void getTime(long startTime, long endTime) {
		long duration = ((endTime - startTime) / 1000000000);
		System.out.println(duration + "s");
	}
	
	public static void main(String args[]) throws Exception {
		long startTime = System.nanoTime();
		
		removeWarnings();
		
		System.out.println("start");
		
		HtmlService.getHotels();

		System.out.println("closed");
		
		getTime(startTime, System.nanoTime());
		
	}

}