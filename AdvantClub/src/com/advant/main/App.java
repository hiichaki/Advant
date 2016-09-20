package com.advant.main;

import java.io.IOException;

import com.advant.model.Hotel;
import com.advant.service.HtmlService;
import com.advant.utils.HTMLUtils;

public class App {

	private static void removeWarnings() {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
	}

	private static void getTime(long startTime, long endTime) {
		long duration = ((endTime - startTime) / 1000000000);
		System.out.println(duration + "s");
	}

	public static void main(String args[]) {
		if (HTMLUtils.hasInternetConnection()) {
			start();
		}
	}
	
	private static void start() {
		long startTime = System.nanoTime();

		removeWarnings();

		System.out.println("start");

		try {
			for(Hotel hotel: HtmlService.getHotels()) {
				System.out.println(hotel);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("closed");

		getTime(startTime, System.nanoTime());
	}

}