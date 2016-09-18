package com.advant.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.advant.utils.StaticVars;
import com.advant.utils.StringUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class HtmlService {

	private static final WebClient webClient = new WebClient(BrowserVersion.CHROME);

	private static void submitForm() {
		try {
			webClient.getOptions().setJavaScriptEnabled(true);

			final HtmlPage page1 = webClient.getPage(StaticVars.SITE);

			final HtmlForm form = page1.getHtmlElementById("sky-form");
			final HtmlSubmitInput button = form.getInputByValue("Войти");

			final HtmlTextInput loginField = form.getInputByName("login_or_email");
			final HtmlPasswordInput passField = form.getInputByName("password");

			loginField.setValueAttribute(StaticVars.LOGIN);
			passField.setValueAttribute(StaticVars.PASS);

			button.click();

			System.out.println("login");

		} catch (Exception e) {
			System.out.println("fail to submit");
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static HtmlPage getPageByRequest() throws FailingHttpStatusCodeException, IOException {
		WebRequest requestSettings = new WebRequest(new URL("https://advant.club/ua/search/"), HttpMethod.GET);

		requestSettings.setRequestParameters(new ArrayList());
		requestSettings.getRequestParameters().add(new NameValuePair("country", "318"));
		requestSettings.getRequestParameters().add(new NameValuePair("date_from", "18.09.2016"));
		requestSettings.getRequestParameters().add(new NameValuePair("date_till", "30.09.2016"));
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

		return webClient.getPage(requestSettings);
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<HtmlAnchor> getFirstPage() {
		try {
			HtmlPage page = getPageByRequest();
			HtmlButton searchBtn = (HtmlButton) page.getElementsByTagName("button").get(0);
			HtmlPage newPage = searchBtn.click();
			System.out.println("clicked search");
			ArrayList<HtmlAnchor> hrefList = (ArrayList<HtmlAnchor>) newPage.getByXPath(StaticVars.F_PRICE_HREF);
			while(hrefList.isEmpty()) {
				hrefList = (ArrayList<HtmlAnchor>) newPage.getByXPath(StaticVars.F_PRICE_HREF);
			}
			return hrefList;
		} catch (FailingHttpStatusCodeException | IOException e) {
			System.out.println("failed to click searchBtn");
			e.printStackTrace();
		}
		return null;
	}

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
	private static void getPrices(ArrayList<HtmlAnchor> hrefList) throws IOException {
		ArrayList<Integer> fPrice = new ArrayList<Integer>();
		ArrayList<Integer> bPrice = new ArrayList<Integer>();
//		ArrayList<HtmlAnchor> hrefList = (ArrayList<HtmlAnchor>) newPage.getByXPath(StaticVars.F_PRICE_HREF);
		System.out.println("start anchoring");
		int i = 0;
		for (HtmlAnchor href : hrefList) {
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

		show(fPrice, bPrice);

	}

	private static void show(ArrayList<Integer> fPrice, ArrayList<Integer> bPrice) {
		for (int i = 0; i < fPrice.size() - 1; ++i) {
			int a = fPrice.get(i);
			int b = bPrice.get(i);
			System.out.print(a + " : " + b + " ");
			if (a == b)
				System.out.println("=");
			if (a > b)
				System.out.println("+");
			if (a < b)
				System.out.println("-");
		}
		System.out.println("size:" + fPrice.size());
	}

	public static void getHotels() throws IOException {
		submitForm();
		

//		clickMore(newPage);

		getPrices(getFirstPage());

		webClient.close();
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
