package com.advant.main;

import java.util.ArrayList;

import com.advant.model.Hotel;
import com.advant.service.HtmlService;
import com.advant.utils.HTMLUtils;
import com.advant.utils.StaticVars;

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
		ArrayList<Hotel> hotels = HtmlService.getHotels();

		for (Hotel hotel : hotels) {
			System.out.println(hotel);
		}
		
		for(Hotel hotel:hotels) {
			show(hotel);
		}

		System.out.println("closed");

		getTime(startTime, System.nanoTime());
	}

	private static void show(Hotel hotel) {
		int fPrice = hotel.getfPrice();
		int bPrice = hotel.getbPrice();
		System.out.print(fPrice + " : " + bPrice + " ");
		if (fPrice == bPrice)
			System.out.println("=");
		if (fPrice > bPrice) {
			System.out.println("+  " + StaticVars.SITE_CREATE + hotel.getId());
		}
		if (fPrice < bPrice)
			System.out.println("-");

	}

}