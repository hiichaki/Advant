package com.advant.service;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.advant.model.Hotel;
import com.advant.utils.StaticVars;
import com.advant.utils.StringUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHeading4;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class HtmlService {

	private static final WebClient WEB_CLIENT = new WebClient(BrowserVersion.CHROME);
	private static String REQUEST_URL = null;

	public static String getRequestURL() {
		return REQUEST_URL;
	}

	private static void submitForm() {
		try {
			WEB_CLIENT.getOptions().setJavaScriptEnabled(true);
			WEB_CLIENT.getOptions().setDoNotTrackEnabled(true);

			final HtmlPage page1 = WEB_CLIENT.getPage(StaticVars.SITE);

			final HtmlForm form = page1.getHtmlElementById("sky-form");
			final HtmlSubmitInput button = form.getInputByValue("Войти");

			final HtmlTextInput loginField = form.getInputByName("login_or_email");
			final HtmlPasswordInput passField = form.getInputByName("password");

			loginField.setValueAttribute(StaticVars.LOGIN);
			passField.setValueAttribute(StaticVars.PASS);

			button.click();

			System.out.println("login");

		} catch (Exception e) {
			try {
				throw (new Throwable("fail to submit"));
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static HtmlPage getPageByRequest() throws FailingHttpStatusCodeException, IOException {
		WebRequest requestSettings = new WebRequest(new URL(StaticVars.SITE_SEARCH), HttpMethod.GET);

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// get current date time with Date()
		Date date = new Date();
		System.out.println(dateFormat.format(date));

		requestSettings.setRequestParameters(new ArrayList());
		requestSettings.getRequestParameters().add(new NameValuePair("country", "318"));
		requestSettings.getRequestParameters().add(new NameValuePair("date_from", "22.09.2016"));
		requestSettings.getRequestParameters().add(new NameValuePair("date_till", "15.10.2016"));
		requestSettings.getRequestParameters().add(new NameValuePair("night_from", "7"));
		requestSettings.getRequestParameters().add(new NameValuePair("night_till", "30"));
		requestSettings.getRequestParameters().add(new NameValuePair("adult_amount", "2"));
		requestSettings.getRequestParameters().add(new NameValuePair("child_amount", "0"));
		requestSettings.getRequestParameters().add(new NameValuePair("child1_age", "4"));
		requestSettings.getRequestParameters().add(new NameValuePair("child2_age", "0"));
		requestSettings.getRequestParameters().add(new NameValuePair("child3_age", "0"));
		requestSettings.getRequestParameters().add(new NameValuePair("hotel_ratings", "78"));
		requestSettings.getRequestParameters().add(new NameValuePair("hotel_ratings", "4"));
		requestSettings.getRequestParameters().add(new NameValuePair("meal_types", "512"));
		requestSettings.getRequestParameters().add(new NameValuePair("price_from", "0"));
		requestSettings.getRequestParameters().add(new NameValuePair("price_till", "1000"));

		StringBuilder request = new StringBuilder();
		request.append(StaticVars.SITE_SEARCH + "?");
		for (NameValuePair param : requestSettings.getRequestParameters()) {
			request.append(param + "&");
		}
		request.deleteCharAt(request.length() - 1);
		REQUEST_URL = request.toString();
		return WEB_CLIENT.getPage(requestSettings);
	}

	@SuppressWarnings("unchecked")
	private static HtmlPage getFirstPage() {
		try {
			HtmlPage page = getPageByRequest();
			HtmlButton searchBtn = (HtmlButton) page.getElementsByTagName("button").get(0);
			HtmlPage newPage = searchBtn.click();
			System.out.println("clicked search");
			ArrayList<HtmlAnchor> hrefList = (ArrayList<HtmlAnchor>) newPage.getByXPath(StaticVars.F_PRICE_HREF);
			while (hrefList.isEmpty()) {
				hrefList = (ArrayList<HtmlAnchor>) newPage.getByXPath(StaticVars.F_PRICE_HREF);
			}
			return newPage;
		} catch (FailingHttpStatusCodeException | IOException e) {
			System.out.println("failed to click searchBtn");
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private static void clickMore(HtmlPage newPage) {
		HtmlButton moreBtn = (HtmlButton) newPage.getElementsByTagName("button").get(1);
		try {
			moreBtn.click();
			System.out.println("clicked moreBtn");
			sleep();
		} catch (IOException e) {
			System.out.println("failed to click moreBtn");
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, ArrayList<?>> getPrices(HtmlPage newPage) throws IOException {
		ArrayList<Integer> fPrice = new ArrayList<Integer>();
		ArrayList<Integer> bPrice = new ArrayList<Integer>();
		ArrayList<String> links = new ArrayList<String>();
		ArrayList<HtmlAnchor> hrefList = (ArrayList<HtmlAnchor>) newPage.getByXPath(StaticVars.F_PRICE_HREF);
		System.out.println("start anchoring");
		int i = 0;
		for (HtmlAnchor href : hrefList) {
			links.add(href.asXml().substring(href.asXml().indexOf("\"") + 1, href.asXml().indexOf("\"", 20)));
			fPrice.add(StringUtils.getNumber(href.asText()));
			HtmlPage tmpHref = href.click();

			ArrayList<HtmlSpan> spanList = (ArrayList<HtmlSpan>) tmpHref.getByXPath(StaticVars.B_PRICE_SPAN);
			while (spanList.isEmpty()) {
				spanList = (ArrayList<HtmlSpan>) tmpHref.getByXPath(StaticVars.B_PRICE_SPAN);
			}
			bPrice.add(StringUtils.getNumber(spanList.get(0).asText()));
			System.out.println(++i + "/" + hrefList.size());
		}
		System.out.println("done anchoring");

		HashMap<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		map.put("fPrice", fPrice);
		map.put("bPrice", bPrice);
		map.put("links", links);
		return map;

	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, ArrayList<String>> getName(HtmlPage newPage) {
		ArrayList<HtmlHeading4> hrefList = (ArrayList<HtmlHeading4>) newPage.getByXPath(StaticVars.HOTEL_NAME_HREF);
		ArrayList<String> countryList = new ArrayList<String>();
		ArrayList<String> townList = new ArrayList<String>();
		ArrayList<String> townFromList = new ArrayList<String>();
		for (HtmlHeading4 href : hrefList) {
			String text = href.getElementsByTagName("span").get(0).asText();
			int coma = text.indexOf(',');
			int nl = text.indexOf("\n");
			String country = text.substring(0, coma);
			String town = text.substring(coma + 1, nl - 1);
			String townFrom = text.substring(nl);
			countryList.add(country);
			townList.add(StringUtils.getOnlyWords(town));
			townFromList.add(StringUtils.getOnlyWords(townFrom));

		}

		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		map.put("country", countryList);
		map.put("town", townList);
		map.put("townFrom", townFromList);

		return map;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, ArrayList<?>> getInfo(HtmlPage newPage) {
		ArrayList<HtmlDivision> list = (ArrayList<HtmlDivision>) newPage.getByXPath(StaticVars.HOTEL_INF_MAIN);
		ArrayList<Integer> starsList = new ArrayList<Integer>();
		ArrayList<String> hotelRoomList = new ArrayList<String>();
		ArrayList<String> foodList = new ArrayList<String>();
		ArrayList<Integer> nightsList = new ArrayList<Integer>();

		ArrayList<HtmlDivision> listDate = (ArrayList<HtmlDivision>) newPage
				.getByXPath("//div[@class='hotel-inf-descr hotel-inf-descr_date']");
		ArrayList<String> fromToDateList = new ArrayList<String>();

		for (int i = 0; i < listDate.size(); ++i) {
			fromToDateList.add(listDate.get(i).asText());
		}

		for (int i = 0; i < list.size() - 4; i += 4) {
			starsList.add(StringUtils.getNumber(list.get(i).asText()));
			hotelRoomList.add(list.get(i + 1).asText());
			foodList.add(list.get(i + 2).asText());
			nightsList.add(StringUtils.getNumber(list.get(i + 3).asText()));
		}

		HashMap<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		map.put("stars", starsList);
		map.put("hotelRoom", hotelRoomList);
		map.put("food", foodList);
		map.put("nights", nightsList);
		map.put("fromToDate", fromToDateList);

		return map;

	}

	public static ArrayList<Hotel> getHotels() {
		submitForm();

		ArrayList<Hotel> hotelList = new ArrayList<Hotel>();
		HtmlPage newPage = getFirstPage();

		clickMore(newPage);
		getInfo(newPage);

		HashMap<String, ArrayList<String>> nameMap = getName(newPage);
		HashMap<String, ArrayList<?>> priceMap;
		try {
			priceMap = getPrices(newPage);
			HashMap<String, ArrayList<?>> mainInfoMap = getInfo(newPage);

			for (int i = 0; i < nameMap.get("country").size(); ++i) {
				int fPrice = (int) priceMap.get("fPrice").get(i);
				int bPrice = (int) priceMap.get("bPrice").get(i);
				String link = (String) priceMap.get("links").get(i);
				String country = nameMap.get("country").get(i);
				String town = nameMap.get("town").get(i);
				String townFrom = nameMap.get("townFrom").get(i);
				int stars = (int) mainInfoMap.get("stars").get(i);
				String hotelRoom = (String) mainInfoMap.get("hotelRoom").get(i);
				String food = (String) mainInfoMap.get("food").get(i);
				int nights = (int) mainInfoMap.get("nights").get(i);
				String fromToDate = (String) mainInfoMap.get("fromToDate").get(i);

				hotelList.add(new Hotel(fPrice, bPrice, country, town, townFrom, stars, hotelRoom, food, nights,
						fromToDate, link));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WEB_CLIENT.close();

		return hotelList;
	}

	private static void sleep() {
		System.out.println("sleep");
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
			System.out.println("failed to sleep");
			e.printStackTrace();
		}
		System.out.println("wake");
	}
}
