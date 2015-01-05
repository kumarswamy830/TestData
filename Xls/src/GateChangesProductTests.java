package com.disney.test.gatechanges;

import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.disney.api.profile.JodUsers;
import com.disney.selenium.common.Constants;
import com.disney.selenium.common.Global;
import com.disney.selenium.common.JSONParser;
import com.disney.selenium.common.Log;
import com.disney.selenium.common.Utils;
import com.disney.selenium.common.XPath;
import com.disney.selenium.common.enums.Environment;
import com.disney.selenium.common.enums.WdwUrl;
import com.disney.selenium.common.exception.TestCaseFailedException;
import com.disney.selenium.common.exception.TicketQuantityInvalidException;
import com.disney.selenium.common.exception.automation.AutomationInitializationException;
import com.disney.selenium.engine.Engine;
import com.disney.selenium.engine.account.CreateAccount;
import com.disney.selenium.engine.cart.DetailedCartPage;
import com.disney.selenium.engine.checkout.Delivery;
import com.disney.selenium.engine.checkout.OrderConfirmation;
import com.disney.selenium.engine.checkout.PaymentPage;
import com.disney.selenium.engine.checkout.ReviewOrder;
import com.disney.selenium.engine.tickets.AnnualPasses;
import com.disney.selenium.engine.tickets.MYWTickets;
import com.disney.selenium.engine.visacard.VisaCard;
import com.disney.utils.Excel_Reader;
import com.disney.utils.Randomness;


public class GateChangesProductTests {

	private JSONArray json_guest_info;
	private JSONObject registration;
	private JSONObject json_myw_tickets;
	private JSONObject json_delivery;
	private JSONObject json_payment;
	private JSONObject json_payment_detail;
	private JSONObject json_review_order;
	private int adult_quantity;
	private int child_quantity;
	private int single_day;
	private int multi_day;
	private int ticket_options;
	private int day;
	private int no_of_tickets;
	private double base_Price;
	private double tax_Price;
	private double total_Price;
	private String base_price;
	private String tax_price;
	private String total_price;
	private String category;
	private String token;
	private String auth;
	private String affiliation;
	private String disclaimers;
	private String restrictions;
	private String selection_Copy;
	private String cart_Copy;
	private String url;
	private String selection_Factor;

	Excel_Reader xls = new Excel_Reader(System.getProperty("user.dir")
			+ "\\Excel_gatechanges\\TestData.xlsx");

	public boolean testGateChangesProductTests(WebDriver driver,
			Hashtable<String, String> data) throws Exception {

		setUpJSON();

		// Initialize the Engine object
		Engine engine = new Engine(driver, this.getClass().getName());

		// Set environment: latest, stage, sl, etc and light/dark
		engine.homePageEngine.homePage.configureEnvironment(engine);

		selection_Factor = data.get("Selection Factor").trim();
		token = "TicketPassport - Tickets";
		auth = data.get("Auth").trim();
		url = data.get("URL").trim();
		category = data.get("Category");
		affiliation = data.get("Affiliation");

		// set the test and target off
		engine.debugEngine.debug.setAnalyticsCookie();
		// engine.debugEngine.releaseToggles.clickReleaseToggle();
		// engine.debugEngine.featureToggles.setEnableCheckoutGuestPocToggle();
		

		// Get Session Data
		Map<String, String> session_data = engine.debugEngine.debug
				.getSessionData();
		Log.log(driver).info("ConversationId: " + session_data.get("conv_id"));
		Log.log(driver).info(
				"Build Information: " + session_data.get("build_number"));

		// //////////////////////////////////////////////////////////////////////////
		// MYW Tickets Page
		// //////////////////////////////////////////////////////////////////////////
		// Go to tickets page
		Utils.takeScreenShot(driver, data.get("Code"), "Home_page");
		MYWTickets mywTickets = null;
		int AffiliationMap = MappingAfiiliation(affiliation);
		
		if (auth.contentEquals("URL Passport")) {
			// Going to the passport page.
			engine.passportEngine.passportPage.getPassportPage();
			// Generating token to go to a store
			// By the moment this value only works for TicketPassport - Tickets
			engine.passportEngine.passportPage.generateToken(token, url,
					category);
			Utils.takeScreenShot(driver, data.get("Code"), "Agent_TicketsPage");
			engine.ticketEngine.mywTickets.selectAffiliation(AffiliationMap);
			 mywTickets = engine.ticketEngine.mywTickets;
		}
		else
		{
			mywTickets = selectAffiliationFlow(affiliation, engine, driver, AffiliationMap);
			Utils.takeScreenShot(driver, data.get("Code"), "Tickets_page");
		}
		// To-Do:
		// Else should continue normal flow Eg. wdw-stage.disney.go.com/tickets
		
		
		//Generating Random no.of tickets		
		no_of_tickets=Integer.parseInt(Randomness.randomNumber(1));
		
		if(no_of_tickets==0){
			
			no_of_tickets=no_of_tickets+3;
		}
		
		Log.log(driver).info("no_of_tickets: "+no_of_tickets);
		
		adult_quantity = Integer.parseInt(data.get("Adult_Quantity"));
		
		if(adult_quantity>0){
			
			adult_quantity=adult_quantity*no_of_tickets;
		}
		
		child_quantity = Integer.parseInt(data.get("Child_Quantity"));
		
		if(child_quantity>0){
			
			child_quantity=child_quantity*no_of_tickets;
		}
		
		//uncomment this block once you start using the other spreadsheet
		

		if (category.contains("Theme Park Ticket")) {
			selectTicketDays (selection_Factor, driver,	 mywTickets);
			//themeParkTicket(driver, data, mywTickets);
		}

		if (category.contains("Annual Pass")) {
			annualPass(driver, data, mywTickets);
		}

		// Select the add to cart option
		// DetailedCartPage detailedCartPage = mywTickets.addToCart();
		XPath.waitForVisibleElement(driver, ".//*[@id='addToCart']",
				Constants.GLOBAL_DRIVER_TIMEOUT).click();
		Utils.waitForDomComplete(driver);
		Utils.takeScreenShot(driver, data.get("Code"), "Cart_page");
		DetailedCartPage detailedCartPage = Global.getGlobalObject(driver).cartEngine.detailedCartPage;

		// Cart_copy validation
		if (category.contains("Annual Pass")) {
		validateCartCopy(driver, data,
				"//tr[@class='productRowHeading'][1]//th");
		}
		
		if (category.contains("Theme Park Ticket")) {
			validateCartCopy(driver, data,
					"//tr[@class='productRowDescription'][1]//th");
			}
		// //////////////////////////////////////////////////////////////////////////
		// Detailed Cart Page
		// //////////////////////////////////////////////////////////////////////////

		// String numDays = detailedCartPage.getTicketDays();
		//
		// if (numDays.contains(day + "-Day")) {
		// System.out.println("\n*****   Tickets - " + day
		// + " Day ticket verified in Cart   *****\n");
		//
		// } else {
		// System.out
		// .println("\n*****  Tickets - "
		// + day
		// + " Day ticket FAILED, some other value was selected.   ******\n ");
		//
		// }

		// //////////////////////////////////////////////////////////////////////////
		// Checkout
		// //////////////////////////////////////////////////////////////////////////

		// Cast to DetailedCartPage because you are not currently signed in
		// detailedCartPage = (DetailedCartPage)
		// detailedCartPage.clickCheckout();
		Log.log(driver).info("Click CheckOut");
		XPath.waitAndClick(driver, "//*[@id='checkOutLoginButton']");
		Log.log(driver).info("Checkout clicked");
		

		if (!auth.contentEquals("URL Passport")) {
			
			Utils.takeScreenShot(driver, data.get("Code"), "Sign_Dialog");
			CreateAccount createAccount = new CreateAccount(driver);

			createAccount = detailedCartPage.createAccount();
			createAccount.enterSignInInformation(session_data.get("conv_id")
					+ "@mailinator.com", registration.getString("password"));

			createAccount.enterAboutMeInformation((registration
					.getJSONObject("guest_info")).getString("title"),
					(registration.getJSONObject("guest_info"))
							.getString("first_name"), (registration
							.getJSONObject("guest_info"))
							.getString("middle_initial"), (registration
							.getJSONObject("guest_info"))
							.getString("last_name"), (registration
							.getJSONObject("guest_info")).getInt("suffix"),
					(registration.getJSONObject("birth_date")).getInt("month"),
					(registration.getJSONObject("birth_date")).getInt("day"),
					(registration.getJSONObject("birth_date"))
							.getString("year"));

			createAccount
					.enterContactInformation((registration
							.getJSONObject("contact"))
							.getString("contact_country"), (registration
							.getJSONObject("contact"))
							.getString("contact_address1"), (registration
							.getJSONObject("contact"))
							.getString("contact_address2"), (registration
							.getJSONObject("contact"))
							.getString("contact_city"), (registration
							.getJSONObject("contact"))
							.getString("contact_state"), (registration
							.getJSONObject("contact")).getString("contact_zip"));

			createAccount.enterSecurityQuestions((registration
					.getJSONObject("security_questions"))
					.getString("question1"), (registration
					.getJSONObject("security_questions")).getString("answer1"),
					(registration.getJSONObject("security_questions"))
							.getString("question2"), (registration
							.getJSONObject("security_questions"))
							.getString("answer2"));

			createAccount.selectAgreeToTerms();
			// createAccount.doneFirstStep();
			createAccount.selectAgreeToTermsSecondStep();
			Utils.takeScreenShot(driver, data.get("Code"), "AccountCreation_page");
			createAccount.doneSecondStepDetailCart();

//			for (int i = 0; i < json_guest_info.length(); i++) {
//				engine.checkoutEngine.guestInformation.selectSingleGuest();
//			}
			Utils.takeScreenShot(driver, data.get("Code"), "Guest_page");
			for (int i = 1; i < no_of_tickets; i++) {
				engine.checkoutEngine.guestInformation.selectGuest();
			}
		}

		// //////////////////////////////////////////////////////////////////////////
		// Delivery
		// //////////////////////////////////////////////////////////////////////////
		// Delivery delivery =
		// engine.checkoutEngine.delivery.clickNextTickets();
		Utils.sleep(2000);
		XPath.waitForVisibleElement(driver, ".//*[@id='done']/span/span",
				Constants.GLOBAL_DRIVER_TIMEOUT).click();
		Utils.waitForDomComplete(driver);
		
		Delivery delivery = Global.getGlobalObject(driver).checkoutEngine.delivery;

		// engine.checkoutEngine.delivery.clickContinueTickets();
		Utils.sleep(2000);

		if (driver.findElements(By.id("ui-dialog-title-contents")).size() != 0) {
			Log.log(driver)
					.info("A survey modal is displayed. Pressing the \"No thanks\" button");
			driver.findElement(
					By.xpath("//span[text()='Continue']"))
					.click();

		} else {
			Log.log(driver).info("The survey modal is NOT displayed");
		}
		Utils.waitForDomComplete(driver);
		Utils.sleep(5000);
		
		// /////////////////////////////////------------------------------------///////////////////////////
		if (!auth.contentEquals("URL Passport")) {
			engine.checkoutEngine.delivery.selectDeliveryMethod(JSONParser
					.getInt(json_delivery, "delivery_method"));

			// Anything other than "Will Call" needs filling out personal info
			if ((JSONParser.getInt(json_delivery, "delivery_method")) > 1) {

				// Enter name
				engine.checkoutEngine.delivery.enterName(
						JSONParser.getInt(json_delivery, "title"),
						JSONParser.getString(json_delivery, "first_name"),
						JSONParser.getString(json_delivery, "middle_initial"),
						JSONParser.getString(json_delivery, "last_name"),
						JSONParser.getInt(json_delivery, "suffix"));

				// Enter shipping address if needed
				if (JSONParser.getInt(json_delivery, "delivery_method") > 2) {
					engine.checkoutEngine.delivery.selectShippingAddress(
							JSONParser.getString(json_delivery,
									"shipping_country"), JSONParser.getString(
									json_delivery, "shipping_address1"),
							JSONParser.getString(json_delivery,
									"shipping_address2"), JSONParser.getString(
									json_delivery, "shipping_city"),
							JSONParser.getString(json_delivery,
									"shipping_state"), JSONParser.getString(
									json_delivery, "shipping_zip"));
				}

				engine.checkoutEngine.delivery.enterPhone(JSONParser.getString(
						json_delivery, "shipping_phone"));
				Utils.takeScreenShot(driver, data.get("Code"), "Delivery_page");

				if (delivery == null) {
					throw new AutomationInitializationException(
							"Shipping form not visible", driver);
				}
			}
		}

		// Need to cast to payment page as it returns a generic Checkout object
		// PaymentPage paymentPage = (PaymentPage) delivery.clickNext();
		if (!auth.contentEquals("URL Passport")) {
			XPath.waitForVisibleElement(driver,
			// ".//*[@id='nextPaymentCheckout']",
					".//*[@id='done']", Constants.GLOBAL_DRIVER_TIMEOUT)
					.click();
			
		} else {
			delivery.clickNext();
			
		}

		// Added for CI to help move through to review page.
		// Utils.sleep(10000);

		Utils.waitForDomComplete(driver);
		Utils.takeScreenShot(driver, data.get("Code"), "Payment_page");
		PaymentPage paymentPage = Global.getGlobalObject(driver).checkoutEngine.paymentPage;
		// //////////////////////////////////////////////////////////////////////////
		// Payment
		// //////////////////////////////////////////////////////////////////////////
		/*
		 * // Select payment type and fill out info paymentPage =
		 * paymentPage.selectPaymentMethod( JSONParser.getInt(json_payment,
		 * "payment_method"), JSONParser.getString(json_payment,
		 * "credit_card_type"), JSONParser.getBoolean(json_payment,
		 * "save_card"), json_payment_detail);
		 * 
		 * Utils.sleep(500);
		 * 
		 * paymentPage.clickChatOff();
		 */
		doPayment(driver, paymentPage);

		// Go to next page
		// ReviewOrder reviewOrder = (ReviewOrder) paymentPage.clickNext();
		XPath.waitForVisibleElement(driver, ".//*[@id='nextPaymentCheckout']",
				Constants.GLOBAL_DRIVER_TIMEOUT).click();
		Utils.takeScreenShot(driver, data.get("Code"), "Review_page");
		ReviewOrder reviewOrder = Global.getGlobalObject(driver).checkoutEngine.reviewOrder;
		// Added to slow down because of payment error

		Utils.sleep(1000);
		// //////////////////////////////////////////////////////////////////////////
		// Review Order
		// //////////////////////////////////////////////////////////////////////////

		// Enter confirmation email address
		if (auth.contentEquals("URL Passport")) {
			reviewOrder = reviewOrder.enterConfirmationEmailwithAgent(
					JSONParser.getString(json_review_order, "confirm_email"),
					JSONParser.getString(json_review_order, "agent_email"));
		} else {
			reviewOrder = reviewOrder.enterConfirmationEmail(JSONParser
					.getString(json_review_order, "confirm_email"));
		}

		// Select agree to terms and conditions
		reviewOrder = reviewOrder.selectAgreeToTerms();
		// Purchase
		OrderConfirmation orderConfirmation;

		// Do NOT purchase in SL
		if (!Environment.isSoftLaunch()) {
			// orderConfirmation = reviewOrder.purchase();
			XPath.waitForVisibleElement(driver, ".//*[@id='done']",
					Constants.GLOBAL_DRIVER_TIMEOUT).click();
			orderConfirmation = Global.getGlobalObject(driver).checkoutEngine.orderConfirmation;
			Utils.takeScreenShot(driver, data.get("Code"), "Order_Confirmation_page");
			if (orderConfirmation != null) {
				Log.log(driver).info("Successfully Purchased!");
				Log.log(driver).info(
						"Order confirmation page screenshot: "
								+ Utils.captureScreenshotPass(driver));
				Utils.sleep(1000);
				return true;
			}

			Utils.sleep(1000);
			return false;
		}
		return true;
	}

	private void doPayment(WebDriver driver, PaymentPage paymentPage)
			throws Exception {
		Utils.waitForDomInteractive(driver);
		// Select payment type and fill out info
		paymentPage = paymentPage.selectPaymentMethod(
				JSONParser.getInt(json_payment, "payment_method"),
				JSONParser.getString(json_payment, "credit_card_type"),
				JSONParser.getBoolean(json_payment, "save_card"),
				json_payment_detail);

		Utils.sleep(500);

		Utils.sleep(1000);

		if (driver.findElements(By.id("need_close")).size() != 0) {
			Log.log(driver).info(
					"A Chat modal is displayed. Pressing the \"Close\" button");
			driver.findElement(By.xpath("//*[@id='need_close']")).click();

		} else {
			Log.log(driver).info("The Chat modal is NOT displayed");
		}
	}

	private void validateCartCopy(WebDriver driver,
			Hashtable<String, String> data, String Xpath) throws Exception {

		// Cart copy validation
		cart_Copy = data.get("Cart_Copy");
		WebElement cartCopy = XPath.waitForVisibleElement(driver, Xpath,
				Constants.GLOBAL_DRIVER_TIMEOUT);
		Log.log(driver).info("Cart_Copy text: " + cartCopy.getText());

		String Cart_Copy = cartCopy.getText().trim();

		if (Cart_Copy.contains(cart_Copy)) {

			Log.log(driver).info("Cart_Copy text is  Correct ");

		} else {
			throw new TestCaseFailedException(
					" Cart_Copy text is not Correct ", driver);
		}
	}

	private void validateBasePrice(WebDriver driver,
			Hashtable<String, String> data, String Xpath)
			throws Exception {
		// Base price validation
		base_Price=Float.parseFloat(data.get("Base"));
		
		base_Price=base_Price*no_of_tickets;
		
		base_price = "$" + base_Price +"0";
		
		
		WebElement basePrice = XPath.waitForVisibleElement(driver, Xpath,
				Constants.GLOBAL_DRIVER_TIMEOUT);
		Log.log(driver).info("Base Amount Ticket amount: " + basePrice.getText());

		String Base_Price = basePrice.getText().trim().replaceAll(",", "");

		if (Base_Price.equals(base_price)) {

			Log.log(driver).info("The Base Amount is  Correct");

		} else {

			throw new TestCaseFailedException(
					"The Base Ticket Amount is not Correct", driver);
		}
	}

	private void validateTaxPrice(WebDriver driver,
			Hashtable<String, String> data, String Xpath) throws Exception {
		// Tax price validation
		tax_Price=Double.parseDouble(data.get("Tax"));
		
		tax_Price=tax_Price*no_of_tickets;
		
		tax_price = "$" + tax_Price;
		
		WebElement taxprice = XPath.waitForVisibleElement(driver, Xpath,
				Constants.GLOBAL_DRIVER_TIMEOUT);
		Log.log(driver).info("Tax on ticket amount: " + taxprice.getText());

		String Tax_Price = taxprice.getText().trim();

		if (Tax_Price.equals(tax_price)) {

			Log.log(driver).info("Tax on ticket amount is  Correct ");

		} else {

			throw new TestCaseFailedException(
					" Tax on ticket amount is not Correct", driver);
		}
	}

	private void validateTotalPrice(WebDriver driver,
			Hashtable<String, String> data, String Xpath) throws Exception {
		// Total price validation
		total_Price=Double.parseDouble(data.get("Total"));
		total_Price=total_Price*no_of_tickets;
		total_price = "$" + total_Price + " USD";
		WebElement totalprice = XPath.waitForVisibleElement(driver, Xpath,
				Constants.GLOBAL_DRIVER_TIMEOUT);
		Log.log(driver).info("Total  ticket amount: " + totalprice.getText());

		String Total_Price = totalprice.getText().trim().replaceAll(",", "");

		if (Total_Price.equals(total_price)) {

			Log.log(driver).info("Total ticket amount is  Correct ");

		} else {
			throw new TestCaseFailedException(
					" Total ticket amount is not Correct ", driver);
		}
	}

	private void validateDisclaimers(WebDriver driver,
			Hashtable<String, String> data, String Xpath) throws Exception {
		// Disclaimers validation
		disclaimers = data.get("Disclaimers").trim().replaceAll(" ", "")
				.replaceAll(",", "").replaceAll("'", "").replaceAll(":", "");

		WebElement disclaimer = XPath.waitForVisibleElement(driver, Xpath,
				Constants.GLOBAL_DRIVER_TIMEOUT);
		Log.log(driver).info("Disclaimers text: " + disclaimer.getText());

		String Disclaimers = disclaimer.getText().trim().replaceAll(" ", "")
				.replaceAll(",", "").replaceAll("'", "").replaceAll(":", "");

		if (Disclaimers.contains(disclaimers)) {

			Log.log(driver).info("Disclaimers text is  Correct ");

		} else {
			throw new TestCaseFailedException(
					" Disclaimers text is not Correct ", driver);
		}
	}

	private void validateRestrictions(WebDriver driver,
			Hashtable<String, String> data, String Xpath) throws Exception {
		// Restrictions validation
		restrictions = data.get("Restrictions").trim();
		WebElement restriction = XPath.waitForVisibleElement(driver, Xpath,
				Constants.GLOBAL_DRIVER_TIMEOUT);
		Log.log(driver).info("Restrictions text: " + restriction.getText());

		String Restrictions = restriction.getText().trim();

		if (restrictions.equals(Restrictions)) {

			Log.log(driver).info("Restrictions text is  Correct ");

		} else {
			throw new TestCaseFailedException(
					" Restrictions text is not Correct ", driver);
		}
	}

	private void validateSelectionCopy(WebDriver driver,
			Hashtable<String, String> data, String Xpath) throws Exception {
		// Selection_Copy validation
		selection_Copy = data.get("Selection_Copy").trim();
		WebElement selectionCopy = XPath.waitForVisibleElement(driver, Xpath,
				Constants.GLOBAL_DRIVER_TIMEOUT);
		Log.log(driver).info("Selection_Copy text: " + selectionCopy.getText());

		String Selection_Copy = selectionCopy.getText().trim();

		if (selection_Copy.equals(Selection_Copy)) {

			Log.log(driver).info("Selection_Copy text is  Correct ");

		} else {
			throw new TestCaseFailedException(
					" Selection_Copy text is not Correct ", driver);
		}
	}

	private void themeParkTicket(WebDriver driver,
			Hashtable<String, String> data, MYWTickets mywTickets)
			throws Exception {

		// Set adult quantity
		// Need to cast MYWTickets because returned object is a generic
		// Tickets
		// object
		//adult_quantity = Integer.parseInt(data.get("Adult_Quantity"));
		mywTickets = (MYWTickets) mywTickets
				.selectAdultQuantity(adult_quantity);
		// Check for errors
		if (mywTickets == null) {
			throw new TicketQuantityInvalidException(
					"Invalid adult ticket quantity", driver);
		}

		// Set child quantity
		// Need to cast MYWTickets because returned object is a generic
		// Tickets
		// object
		//child_quantity = Integer.parseInt(data.get("Child_Quantity"));
		mywTickets = (MYWTickets) mywTickets
				.selectChildQuantity(child_quantity);
		// Check for errors
		if (mywTickets == null) {
			throw new TicketQuantityInvalidException(
					"Invalid child ticket quantity", driver);
		}

		// Set day to 1
		// Need to cast MYWTickets because returned object is a generic
		// Tickets
		// object
		single_day = Integer.parseInt(data.get("SingleDay"));
		if (single_day > 0) {
			mywTickets = mywTickets.selectDays(single_day);
			switch (single_day) {
			case 1:
				Log.log(driver).info("Selected 1-Day Non-Magic Kingdom park");
				break;
			case 2:
				Log.log(driver).info("Selected 1- Day Magic Kingdom park");
				break;
			default:
				Log.log(driver).info("Not Selected 1 day Ticket ");
				;
				break;
			}
			day = single_day;
		}
		// Set day to 2-10
		// Need to cast MYWTickets because returned object is a generic
		// Tickets
		// object
		multi_day = Integer.parseInt(data.get("MultiDay"));
		if (multi_day > 0) {

			if (multi_day > 5) {
				mywTickets.clickShowMore();
			}
			mywTickets = mywTickets.selectMultiDays(multi_day);
			day = multi_day;
			Log.log(driver).info(
					"Selected " + data.get("MultiDay") + " Day resort");
		}
		// Select ticket option
		// Need to cast MYWTickets because returned object is a generic
		// Tickets
		// object
		ticket_options = Integer.parseInt(data.get("TicketOptions"));
		mywTickets = mywTickets.selectTicketOptions(ticket_options);
		switch (ticket_options) {
		case 1:
			Log.log(driver).info("Selected Base Ticket only");
			break;
		case 2:
			Log.log(driver).info("Selected Park Hopper Option");
			break;
		case 3:
			Log.log(driver).info("Selected Water Park Fun & More Option");
			break;
		case 4:
			Log.log(driver).info(
					"Selected Hopper Option and Water Park Fun & More Options");
			break;

		default:
			Log.log(driver).info("Selected Base Ticket only");
			;
			break;
		}

		// Base price validation
		validateBasePrice(driver, data, "//*[@id='themepark']/div/div[2]/div");

		// Tax price validation
		validateTaxPrice(driver, data,
				"//*[@id='ticketBuilderTaxContainer']/div[2]");

		// Total price validation
		validateTotalPrice(driver, data,
				"//*[@id='ticketBuilderSubTotalContainer']/div[2]");

		// Disclaimers validation
		validateDisclaimers(driver, data,
				"//div[@class='disclaimerBlock'][1]/div[2]");

		// Restrictions validation
		validateRestrictions(driver, data,
				"//div[@class='disclaimerBlock'][2]/div[2]");

		// Selection_Copy validation
		validateSelectionCopy(driver, data,
				"//*[@id='themepark']//h4[@class='clearfix']");
		
		Utils.takeScreenShot(driver, data.get("Code"), "Ticket_Page_After_Selection");

	}

	private void annualPass(WebDriver driver, Hashtable<String, String> data,
			MYWTickets mywTickets) throws Exception {

		// Click Tickets Annual Passes tab
		AnnualPasses annualPasses = (AnnualPasses) mywTickets
				.switchTab(Constants.TAB_TICKET_ANNUAL_PASSES);

		annualPasses.selectAnnualPass(Constants.ANNUAL_PASS);

		// Set adult quantity
		// Need to cast MYWTickets because returned object is a generic
		// Tickets
		// object
		//adult_quantity = Integer.parseInt(data.get("Adult_Quantity"));
		mywTickets = (MYWTickets) mywTickets
				.selectAdultQuantity(adult_quantity);
		// Check for errors
		if (mywTickets == null) {
			throw new TicketQuantityInvalidException(
					"Invalid adult ticket quantity", driver);
		}

		// Set child quantity
		// Need to cast MYWTickets because returned object is a generic
		// Tickets
		// object
		//child_quantity = Integer.parseInt(data.get("Child_Quantity"));
		mywTickets = (MYWTickets) mywTickets
				.selectChildQuantity(child_quantity);
		// Check for errors
		if (mywTickets == null) {
			throw new TicketQuantityInvalidException(
					"Invalid child ticket quantity", driver);
		}

		// Base price validation
		validateBasePrice(driver, data, "//*[@id='annualpass']/div/div[2]/div");

		// Tax price validation
		validateTaxPrice(driver, data,
				"//*[@id='ticketBuilderTaxContainer']/div[2]");

		// Total price validation
		validateTotalPrice(driver, data,
				"//*[@id='ticketBuilderSubTotalContainer']/div[2]");

		// Disclaimers validation
		validateDisclaimers(driver, data,
				"//*[@id='ticketsDisclaimers']/div/p");

		// Restrictions validation
//		validateRestrictions(driver, data,
//				"//div[@class='disclaimerBlock'][2]/div[2]");

		// Selection_Copy validation
		validateSelectionCopy(driver, data,
				"//*[@id='annualpass']/div/h4");

	}

	private MYWTickets selectAffiliationFlow (String affiliation, Engine engine, WebDriver driver, int AffiliationMap) throws Exception
	{
		
		MYWTickets mywTickets = null;
		switch (affiliation)
		{
		case "All Guests" : mywTickets = engine.globalNavigationBarEngine.parksAndTickets.goToTicketsPage();
							break;
		case "FL-resident" : mywTickets = engine.globalNavigationBarEngine.parksAndTickets.goToTicketsPageFlResident();
							engine.ticketEngine.mywTickets.selectAffiliation(AffiliationMap);
							break;
		}
		return mywTickets;
	}
	
	private int MappingAfiiliation (String affiliation) throws Exception
	{
		
		int MappedAffiliation = 0;
		switch (affiliation)
		{
		case "All Guests" : MappedAffiliation = 0;
							break;
		case "FL-resident" : MappedAffiliation = 1;
							break;
		}
		return MappedAffiliation;
	}
	
	private void selectTicketDays (String selection_Factor, WebDriver driver, MYWTickets mywTickets) throws Exception
	{
		int Ticket_Days;
		int Ticket_Option;
		String park_Select = null;
		String ticket_Option1 = null;
		String ticket_Option2 = null;
		
		String Regex = "(\\d+)\\sDay\\s*(\\w*)\\s*\\w*;\\s*(\\w+)\\W*(\\w*)\\W*\\w*";
		Pattern p = Pattern.compile(Regex);
		Matcher m = p.matcher(selection_Factor);
		if (m.find())
		{
			Ticket_Days = Integer.parseInt(m.group(1));
			park_Select = m.group(2);
			ticket_Option1 = m.group(3);
			ticket_Option2 = m.group(4);
			
			// number of days
			if (Ticket_Days == 1)
			{
				if(park_Select.equals("Non"))
				{
					mywTickets = mywTickets.selectDays(Ticket_Days+1);
					Log.log(driver).info("1 day NON MK");
				} 	
				else
				{
					mywTickets = mywTickets.selectDays(Ticket_Days);
					Log.log(driver).info("1 day MK");
				}	
					
			}
			else
			{
				if (Ticket_Days > 5) {
					mywTickets.clickShowMore();
				}
				mywTickets = mywTickets.selectMultiDays(Ticket_Days);
			}
			
			//ticket options
			switch (ticket_Option1.substring(0, 1))
			{
			case "P" : Ticket_Option = 2;
						if (ticket_Option2.substring(0,1).equals("W"))
						{
							Ticket_Option = 4;
						}
						mywTickets = mywTickets.selectTicketOptions(Ticket_Option);
					  break;
					  
			case "W" : 	Ticket_Option = 3;
						mywTickets = mywTickets.selectTicketOptions(Ticket_Option);
						break;
			case "N" : 	Ticket_Option = 1;
						mywTickets = mywTickets.selectTicketOptions(Ticket_Option);
						break;
			default : 	Log.log(driver).info("No such an option");
						break;
			}
			
		}
		else
		{
			Log.log(driver).info("No matches found");
		}
	}
	
	private void setUpJSON() throws Exception {
		JSONObject myjson = new JSONObject(
				JSONParser.parse("gate_changes/GateChangesProductTests.json"));

		json_myw_tickets = myjson.getJSONObject("myw_tickets");
		json_guest_info = myjson.getJSONArray("guest_info");
		json_delivery = myjson.getJSONObject("delivery");
		json_review_order = myjson.getJSONObject("review_order");
		registration = myjson.getJSONObject("registration");

		// "payment" has only payment method, credit card type and save card
		json_payment = myjson.getJSONObject("payment");

		// Read credit card detail info from global json file
		JSONObject json_global_payment = new JSONObject(
				JSONParser.parse("payment.json"));
		json_payment_detail = json_global_payment.getJSONObject("payment");
	}

}
