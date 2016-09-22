package com.advant.model;

public class Hotel {

	private int fPrice;
	private int bPrice;
	private String country;
	private String town;
	private String townFrom;
	private int stars;
	private String hotelRoom;
	private String food;
	private int nights;
	private String fromToDate;
	private String link;
	
	public Hotel(int fPrice, int bPrice, String country, String town, String townFrom, int stars, String hotelRoom,
			String food, int nights, String fromToDate, String link) {
		this.fPrice = fPrice;
		this.bPrice = bPrice;
		this.country = country;
		this.town = town;
		this.townFrom = townFrom;
		this.stars = stars;
		this.hotelRoom = hotelRoom;
		this.food = food;
		this.nights = nights;
		this.fromToDate = fromToDate;
		this.link = link;
	}

	@Override
	public String toString() {
		return "Hotel [fPrice=" + fPrice + ", bPrice=" + bPrice + ", country=" + country + ", town=" + town
				+ ", townFrom=" + townFrom + ", stars=" + stars + ", hotelRoom=" + hotelRoom + ", food=" + food
				+ ", nights=" + nights + ", fromToDate=" + fromToDate + ", link=" + link + "]";
	}

	public String getId() {
		return link.substring(link.indexOf("tours/")+6,link.lastIndexOf("/"));
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getFromToDate() {
		return fromToDate;
	}

	public void setFromToDate(String fromToDate) {
		this.fromToDate = fromToDate;
	}
	
	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public String getHotelRoom() {
		return hotelRoom;
	}

	public void setHotelRoom(String hotelRoom) {
		this.hotelRoom = hotelRoom;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public int getNights() {
		return nights;
	}

	public void setNights(int nights) {
		this.nights = nights;
	}

	public int getfPrice() {
		return fPrice;
	}

	public void setfPrice(int fPrice) {
		this.fPrice = fPrice;
	}

	public int getbPrice() {
		return bPrice;
	}

	public void setbPrice(int bPrice) {
		this.bPrice = bPrice;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getTownFrom() {
		return townFrom;
	}

	public void setTownFrom(String townFrom) {
		this.townFrom = townFrom;
	}

	// 1956
	//
}
